package site.facade;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;

import site.controller.invoice.InvoiceData;
import site.controller.invoice.InvoiceDetail;
import site.model.Branch;
import site.model.Registrant;
import site.model.TicketPrice;
import site.model.TicketType;

@Service
public class InvoiceService {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final BranchService branchService;

    public InvoiceService(BranchService branchService) {
        this.branchService = branchService;
    }

    public InvoiceData fromRegistrant(Registrant registrant) {
        InvoiceData result = new InvoiceData();
        result.setClient(registrant.getName());
        result.setClientAddress(registrant.getAddress());
        result.setClientEIK(registrant.getEik());
        if (registrant.getVatNumber() != null) {
            result.setClientVAT(registrant.getVatNumber());
        } else {
            result.setClientVAT("");
        }
        result.setMol(registrant.getMol());
        result.setRegistrantId(registrant.getId());
        result.setPaymentType(
            registrant.getPaymentType() != null ? registrant.getPaymentType().getBulgarianValue() : "");
        result.setInvoiceNumber(registrant.getRealInvoiceNumber() + "");
        result.setInvoiceDate(LocalDate.now().format(FORMATTER));

        String descriptionBg = "jPrime "+ branchService.getCurrentBranch() + " билет за конференция";

        int tickets = registrant.getVisitors().size();
        Branch branch = registrant.getBranch();
        Map<TicketType, TicketPrice> ticketPrices = branchService.getTicketPrices(branch);
        if (registrant.isStudent()) {
            BigDecimal studentPrice = ticketPrices.get(TicketType.STUDENT).getPrice();
            result.addInvoiceDetail(
                new InvoiceDetail(studentPrice, tickets, descriptionBg));
        } else {
            BigDecimal ticketPrice = ticketPrices.get(TicketType.REGULAR).getPrice();

            LocalDateTime registrationDate =
                registrant.getCreatedDate() != null ? registrant.getCreatedDate() : branch.getCfpOpenDate();

            if (registrationDate.isBefore(branch.getCfpCloseDate()) && Duration.between(
                registrationDate, LocalDateTime.now()).abs().getSeconds() <= Duration.of(3, ChronoUnit.DAYS).getSeconds()) {
                ticketPrice = ticketPrices.get(TicketType.EARLY_BIRD).getPrice();
            }
            result.addInvoiceDetail(new InvoiceDetail(ticketPrice, tickets, descriptionBg));
        }

        return result;
    }
}
