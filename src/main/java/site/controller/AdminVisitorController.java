package site.controller;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import site.facade.AdminService;
import site.facade.MailService;
import site.facade.UserServiceJPro;
import site.model.CSVFileModel;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorJPro;
import site.model.VisitorStatus;
import site.model.VisitorType;

/**
 * @author Mitia
 */
@Controller()
@RequestMapping(value = "/admin/visitor")
public class AdminVisitorController {

    public static final String VISITORS_JSP = "/admin/visitor/view.jsp";
    public static final String VISITOR_EDIT_JSP = "/admin/visitor/edit.jsp";
    public static final String VISITOR_UPLOAD_JSP = "/admin/visitor/upload.jsp";
    public static final String VISITOR_EDIT_SEND = "/admin/visitor/send.jsp";

    private static final String EMAIL = "Email";
    private static final String EMAIL_ADDRESS = "Email Address";

    private static final String NAMES = "Names";

    private static final String IS_COMPANY = "Is Company";

    private static final String COMPANY = "Company";

    private static final String LAST_NAME = "Last Name";

    private static final String FIRST_NAME = "First Name";

    private static final String REGISTRANT = "Registrant";

    private static final String ADDRESS = "Address";

    private static final String VAT_NUMBER = "VAT Number";

    private static final String MATERIAL_RESPONSIBLE_PERSON = "Material Responsible Person";

    private static final String COMPANY_E_MAIL = "Company E-Mail";

    private static final String[] JPRIME_FIELDS_LIST = {
        NAMES, EMAIL, COMPANY, IS_COMPANY, REGISTRANT, ADDRESS, VAT_NUMBER, MATERIAL_RESPONSIBLE_PERSON,
        COMPANY_E_MAIL
    };

    private static final String[] JPRO_FIELDS_LIST = { // These are the fields from Eventbrite CSV export
                                                       "Event Name", "Order ID", "Date", "Venue Name",
                                                       "Venue ID", "Gross Revenue (USD)",
                                                       "Ticket Revenue (USD)", "Eventbrite Fees (USD)",
                                                       "Eventbrite Payment Processing (USD)", "Tickets",
                                                       "Type", "Status", "Distribution Partner",
                                                       "Delivery Method", "Transaction ID", FIRST_NAME,
                                                       LAST_NAME, EMAIL_ADDRESS
    };

    private Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @Autowired
    @Qualifier(MailService.NAME)
    @Lazy
    private MailService mailFacade;

    @Autowired
    @Qualifier(UserServiceJPro.NAME)
    private UserServiceJPro userServiceJPro;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewVisitors(Model model) {

        List<Visitor> visitors = adminFacade.findAllNewestVisitors();
        long payedCount = visitors.stream().filter(v->v.getStatus()==VisitorStatus.PAYED).count();
        long requestingCount = visitors.stream().filter(v->v.getStatus()==VisitorStatus.REQUESTING).count();
        long sponsoredCount = visitors.stream().filter(v->v.getStatus()==VisitorStatus.Sponsored).count();
        model.addAttribute("visitors", visitors);
        model.addAttribute("payedCount", payedCount);
        model.addAttribute("requestingCount", requestingCount);
        model.addAttribute("sponsoredCount", sponsoredCount);
        return VISITORS_JSP;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid final Visitor visitor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VISITOR_EDIT_JSP;
        }
        //may be its better to use a set of registrants for sponsors?

        String redirectUrl = "redirect:/admin/visitor/view";
        Registrant registrant = new Registrant();

        if (visitor.getRegistrant().getId() == null) {
            // This means that we came here from the visitors admin panel
            registrant.setName(visitor.getName());
            registrant.setEmail(visitor.getEmail());
            registrant = adminFacade.saveRegistrant(registrant);
        } else {
            // This means that we came here from the registrant admin panel
            registrant = adminFacade.findOneRegistrant(visitor.getRegistrant().getId());
            redirectUrl = "redirect:/admin/registrant/edit/" + visitor.getRegistrant().getId();
        }
        visitor.setRegistrant(registrant);
        adminFacade.saveVisitor(visitor);
        return redirectUrl;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getNewVisitorForm(Model model) {
        model.addAttribute("visitor", new Visitor());
        model.addAttribute("statuses", VisitorStatus.values());
        return VISITOR_EDIT_JSP;
    }

    @GetMapping(value = "/upload")
    public String getUploadVisitorModel(Model model) {
        model.addAttribute("fileModel", new CSVFileModel());
        model.addAttribute("visitorTypes", VisitorType.values());
        model.addAttribute("visitorStatuses", VisitorStatus.values());
        return VISITOR_UPLOAD_JSP;
    }

    @PostMapping(value = "/upload")
    public String uploadCSVFile(@Validated CSVFileModel fileModel, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return getUploadVisitorModel(model);
        }

        MultipartFile csvFile = fileModel.getCsvFile();
        if (csvFile.isEmpty()) {
            return getUploadVisitorModel(model);
        }

        Map<String, Registrant> registrantsMap = new HashMap<>();
        String[] fieldsList;
        switch (fileModel.getVisitorType()) {
            case JPRIME:
                fieldsList = JPRIME_FIELDS_LIST;
                if (fileModel.isEmptyVisitorsBeforeUpload()) {
                    //userServiceJPro.clearVisitors();
                }
                registrantsMap = StreamSupport.stream(adminFacade.findAllRegistrants().spliterator(), false)
                    .collect(Collectors.toMap(
                        r -> StringUtils.isEmpty(r.getVatNumber()) ? r.getName() : r.getVatNumber(),
                        Function.identity(), (a, b) -> a));

                break;
            case JPROFESSIONALS:
                fieldsList = JPRO_FIELDS_LIST;
                if (fileModel.isEmptyVisitorsBeforeUpload()) {
                    userServiceJPro.clearVisitors();
                }
                break;
            default:
                return getUploadVisitorModel(model);
        }

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            try (CsvMapReader csvReader = new CsvMapReader(reader, CsvPreference.STANDARD_PREFERENCE)) {
                Map<String, String> csvLine = csvReader.read(fieldsList);
                if (csvLine == null) {
                    return getUploadVisitorModel(model);
                }

                if (csvLine.entrySet().stream().anyMatch(e-> !e.getKey().equalsIgnoreCase(e.getValue()))) {
                    fieldsList = Arrays.stream(fieldsList).map(csvLine::get).collect(Collectors.toList()).toArray(new String[]{});
                }

                Registrant lastRegistrant = null;
                // Ignore header line
                csvLine = csvReader.read(fieldsList);
                do {
                    if (fileModel.getVisitorType() == VisitorType.JPRIME) {
                        lastRegistrant = processJPrimeVisitor(csvLine, registrantsMap, fileModel.getVisitorStatus(), lastRegistrant);
                    } else {
                        processJProVisitor(csvLine);
                    }
                    csvLine = csvReader.read(fieldsList);
                } while (csvLine != null);
            }
        } catch (IOException e) {
        }

        return getUploadVisitorModel(model);
    }

    private void processJProVisitor(Map<String, String> csvLine) {
        VisitorJPro visitorJPro = new VisitorJPro(csvLine.get(FIRST_NAME) + " " + csvLine.get(LAST_NAME),
            csvLine.get(EMAIL_ADDRESS), "");
        userServiceJPro.save(visitorJPro);
    }

    private Registrant processJPrimeVisitor(Map<String, String> csvLine, Map<String, Registrant> registrantsMap,
        VisitorStatus visitorStatus, Registrant lastRegistrant) {
        Registrant registrant;
        String companyName = csvLine.get(COMPANY);
        String name = csvLine.get(NAMES);
        String email = csvLine.get(EMAIL);
        if (csvLine.containsKey(IS_COMPANY)) {
            String registrantName = csvLine.get(REGISTRANT);
            String address = csvLine.get(ADDRESS);
            String vatNumber = csvLine.get(VAT_NUMBER);
            String mol = csvLine.get(MATERIAL_RESPONSIBLE_PERSON);
            String companyEmail = csvLine.get(COMPANY_E_MAIL);
            String registrantKey = StringUtils.isEmpty(vatNumber) ? registrantName : vatNumber;
            if (lastRegistrant != null && (StringUtils.isEmpty(registrantKey) || registrantKey.equals(StringUtils.isEmpty(lastRegistrant.getVatNumber()) ? lastRegistrant.getName() : lastRegistrant.getVatNumber()))) {
                registrant = lastRegistrant;
            } else {
                registrant =
                    registrantsMap.computeIfAbsent(registrantKey,
                    k -> new Registrant(true, registrantName, address, vatNumber, mol, companyEmail));
            }
        } else {
            registrant = new Registrant(name, email);
        }
        Visitor visitor = new Visitor(registrant, name, email, companyName);
        visitor.setStatus(visitorStatus);
        if (registrant.getId() == null) {
            adminFacade.saveRegistrant(registrant);
        }
        adminFacade.saveVisitor(visitor);

        return registrant;
    }

    @RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
    public String getEditVisitorForm(@PathVariable("itemId") Long itemId, Model model) {
        Visitor visitor = adminFacade.findOneVisitor(itemId);
        model.addAttribute("statuses", VisitorStatus.values());
        model.addAttribute("visitor", visitor);
        return VISITOR_EDIT_JSP;
    }

    @RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
    public String remove(@PathVariable("itemId") Long itemId) {
        adminFacade.deleteVisitor(itemId);
        return "redirect:/admin/visitor/view";
    }

    private CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
            // new UniqueHashCode(), // customerNo (must be unique)
            new Optional(), // id
            new Optional(), // name
            new Optional(), // email
            new Optional(), // company
            new Optional(), // status
//                new NotNull(), // mailingAddress
//                new Optional(new FmtBool("Y", "N")), // married
//                new Optional(), // numberOfKids
//                new NotNull(), // favouriteQuote
//                new NotNull(), // email
//                new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
        };

        return processors;
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]>  exportVisitors() throws IOException{
        Iterable<Visitor> visitors = adminFacade.findAllVisitors();
    	final String[] header = new String[] { "id", "name", "email", "company", "status"};

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ICsvBeanWriter beanWriter = new CsvBeanWriter(
            new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)),
            CsvPreference.STANDARD_PREFERENCE)) {
            beanWriter.writeHeader(header);
            for (Visitor visitor : visitors) {
                beanWriter.write(visitor, header, getProcessors());
            }
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(out.toByteArray());
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String send() {
        return VISITOR_EDIT_SEND;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public String  send(@RequestParam String subject, @RequestParam String content) throws IOException{
        Iterable<Visitor> visitors = adminFacade.findAllVisitors();

         for(Visitor visitor : visitors ){
             if (StringUtils.isEmpty(visitor.getEmail())) {
                 continue;
             }

             try {
                 mailFacade.sendEmail(visitor.getEmail(), subject, content);
             } catch (Throwable t) {
                 log.error("issue when sending email to " + visitor.getEmail(), t);
             }
         }
        return "Done ... all emails should be send but check the log for exceptions";
    }

}
