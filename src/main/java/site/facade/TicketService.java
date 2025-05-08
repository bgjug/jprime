package site.facade;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import jakarta.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import site.controller.invoice.InvoiceLanguage;
import site.controller.ticket.TicketData;
import site.controller.ticket.TicketDetail;
import site.controller.ticket.TicketExporter;
import site.model.Branch;
import site.model.Visitor;
import site.model.VisitorStatus;

@Service
public class TicketService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private TicketExporter ticketExporter;

    @Autowired
    @Lazy
    private MailService mailFacade;

    @Autowired
    private AdminService adminFacade;

    @Autowired
    private BackgroundJobService jobService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Sends tickets to all visitors that have been paid or are sponsored.
     *
     * @return True if tickets are sent using a background job
     */
    public boolean sendTickets() {
        Map<String, List<Visitor>> pendingVisitorsMap =
            StreamSupport.stream(adminFacade.findAllVisitors().spliterator(), false)
                .filter(v -> v.getStatus() != VisitorStatus.REQUESTING)
                .filter(v -> StringUtils.isEmpty(v.getTicket()))
                .filter(v -> StringUtils.isNotBlank(v.anyEmail()))
                // .limit(5) // Remove this before deployment
                .collect(Collectors.groupingBy(Visitor::anyEmail));

        if (pendingVisitorsMap.values().stream().mapToLong(Collection::size).sum() > 3) {
            String jobId = jobService.createBackgroundJob("Sending emails with tickets.");
            jobService.runJob(jobId,
                pendingVisitorsMap.values().stream().flatMap(Collection::stream).toList(), this::sendEmail,
                this::emailErrorLog);
            return true;
        } else {
            pendingVisitorsMap.forEach(this::generateAndSendTicketEmail);
            return false;
        }
    }

    public void sendTicketsForVisitor(Visitor visitor) {
        generateAndSendTicketEmail(visitor.anyEmail(), Collections.singletonList(visitor));
    }

    private String emailErrorLog(Visitor visitor) {
        return visitor.anyEmail();
    }

    private Boolean sendEmail(Visitor visitor) {
        List<Pair<Visitor, Boolean>> result =
            generateAndSendTicketEmail(visitor.anyEmail(), Collections.singletonList(visitor));

        return result.stream().map(Pair::getValue).reduce(true, (a, b) -> a && b);
    }

    /**
     * Generates and sends a ticket email message for each visitor in the list. The provided email is used
     * as a receiver for all ticket messages
     *
     * @param email    the receiver of the email messages
     * @param visitors list of the visitors for which a ticket email message will be generated
     * @return List of pairs of {@link Visitor} and boolean. The boolean indicates success or failure for
     * the operation for that {@link Visitor}
     */
    public List<Pair<Visitor, Boolean>> generateAndSendTicketEmail(String email, List<Visitor> visitors) {
        Branch currentBranch = branchService.getCurrentBranch();
        List<Pair<Visitor, Pair<byte[], byte[]>>> ticketPdfs = visitors.stream().map(visitor -> {
            TicketData ticketData = new TicketData();
            ticketData.setEvent("JPrime " + currentBranch.getYear());
            ticketData.setOrganizer("JPrime Events");
            String ticketNumber =
                StringUtils.isEmpty(visitor.getTicket()) ? UUID.randomUUID().toString() : visitor.getTicket();
            ticketData.addDetail(new TicketDetail(ticketNumber, visitor.getName(), "Visitor"));
            visitor.setTicket(ticketNumber);
            return Pair.of(visitor, Pair.of(ticketExporter.exportTicket(ticketData, InvoiceLanguage.EN),
                ticketExporter.generateTicketQrCode(ticketData)));
        }).toList();

        return ticketPdfs.stream().map(p -> {
            Visitor visitor = p.getKey();
            Pair<byte[], byte[]> binaryPair = p.getValue();
            byte[] ticketPdf = binaryPair.getKey();
            byte[] qrImage = binaryPair.getValue();
            try {
                mailFacade.sendEmail(email, "JPrime " + currentBranch.getYear() + " Conference ticket !",
                    ticketMessage(visitor),
                    new Attachment(ticketPdf, "ticket_" + visitor.getId() + ".pdf", "utf-8", false,
                        "application/pdf"),
                    new Attachment(qrImage, "ticket_qr.png", "utf-8", true, MediaType.IMAGE_PNG_VALUE));
                adminFacade.saveVisitor(visitor);
                return Pair.of(visitor, true);
            } catch (MessagingException e) {
                log.error("Unable to send ticket {} to {}", visitor.getTicket(), email);
                return Pair.of(visitor, false);
            }
        }).toList();
    }

    private String ticketMessage(Visitor visitor) {
        Branch currentBranch = branchService.getCurrentBranch();

        Context context = new Context();
        context.setVariable("branch", currentBranch);
        context.setVariable("visitor", visitor);

        return templateEngine.process("ticket-message", context);
    }
}
