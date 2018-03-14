package site.controller;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import site.facade.AdminService;
import site.facade.MailService;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorStatus;

/**
 * @author Mitia
 */
@Controller()
@RequestMapping(value = "/raffle")
public class RaffleController {

	public static final String RAFFLE_JSP = "/raffle.jsp";

	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	@Qualifier(AdminService.NAME)
	private AdminService adminFacade;
	
	@Autowired
	private ObjectMapper mapper;

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String viewVisitors(Model model) {

		List<RaffleVisitor> visitors = adminFacade.findAllNewestVisitors().stream().filter(v -> v.getStatus() == VisitorStatus.PAYED || v.getStatus() == VisitorStatus.Sponsored).map(v -> new RaffleVisitor(v.getName(), v.getCompany())).collect(Collectors.toList());
		try {
			model.addAttribute("visitors", mapper.writeValueAsString(visitors));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return RAFFLE_JSP;
	}
	
	class RaffleVisitor {
		private String name;
		private String company;
		
		
		public RaffleVisitor(String name, String company) {
			super();
			this.name = name;
			this.company = company;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
	}

}
