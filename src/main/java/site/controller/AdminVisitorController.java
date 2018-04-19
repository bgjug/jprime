package site.controller;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import site.facade.AdminService;
import site.facade.MailService;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorStatus;

/**
 * @author Mitia
 */
@Controller()
@RequestMapping(value = "/admin/visitor")
public class AdminVisitorController {

    public static final String VISITORS_JSP = "/admin/visitor/view.jsp";
    public static final String VISITOR_EDIT_JSP = "/admin/visitor/edit.jsp";
    public static final String VISITOR_EDIT_SEND = "/admin/visitor/send.jsp";

    private Logger log = LogManager.getLogger(this.getClass());
    
    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;
    
    @Autowired
    @Qualifier(MailService.NAME)
    @Lazy
    private MailService mailFacade;

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
    	 final String[] header = new String[] { "name", "email", "company", "status"};
    	 
    	 ICsvBeanWriter beanWriter = null;
    	 ByteArrayOutputStream out = new ByteArrayOutputStream();
         try {
			beanWriter = new CsvBeanWriter(new BufferedWriter(new OutputStreamWriter(out)),
                     CsvPreference.STANDARD_PREFERENCE);
        	 beanWriter.writeHeader(header);
         for(Visitor visitor : visitors ){
        	  beanWriter.write(visitor, header, getProcessors());
			}
		} finally {
			if (beanWriter != null) {
				beanWriter.close();
			}
		}
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(out.toByteArray());
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
        	 if(!StringUtils.isEmpty(visitor))
				try {
					mailFacade.sendEmail(visitor.getEmail(), subject, content);
				} catch (Throwable t) {
					log.error("issue when sending email to" + visitor.getEmail(), t);
				}
		}
        return "Done ... all emails should be send but check the log for exceptions";
	}
    
}
