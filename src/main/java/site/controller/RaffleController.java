package site.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import site.config.Globals;
import site.facade.AdminService;
import site.facade.UserServiceJPro;
import site.model.VisitorJPro;
import site.model.VisitorStatus;

/**
 * @author Mitia
 */
@Controller()
@RequestMapping(value = "/raffle")
public class RaffleController {

	public static final String RAFFLE_JSP = "raffle";

	private static final Logger log = LogManager.getLogger(RaffleController.class);

	private final AdminService adminService;

	private final UserServiceJPro userServiceJPro;
	
	private final ObjectMapper mapper;

	public RaffleController(AdminService adminService,
		UserServiceJPro userServiceJPro, ObjectMapper mapper) {
		this.adminService = adminService;
		this.userServiceJPro = userServiceJPro;
		this.mapper = mapper;
	}

	@GetMapping(value = "/view")
	public String viewVisitors(Model model) {

		List<RaffleVisitor> visitors = adminService.findAllNewestVisitors(Globals.CURRENT_BRANCH).stream()
												   .filter(v -> v.isPresent() && (v.getStatus() == VisitorStatus.PAYED || v.getStatus() == VisitorStatus.SPONSORED))
												   .map(v -> new RaffleVisitor(v.getName(), v.getCompany()))
												   .collect(Collectors.toList());
		try {
			model.addAttribute("visitors", mapper.writeValueAsString(visitors));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return RAFFLE_JSP;
	}

	@GetMapping(value = "/view/jpro")
	public String viewVisitorsJPro(Model model) {

		List<RaffleVisitor> visitors = userServiceJPro.findAllNewestVisitors().stream()
			.filter(VisitorJPro::isPresent)
			.map(v -> new RaffleVisitor(v.getName(), maskEmail(v.getEmail())))
			.collect(Collectors.toList());
		try {
			model.addAttribute("visitors", mapper.writeValueAsString(visitors));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return RAFFLE_JSP;
	}

	static String maskEmail(String email) {
		int lastDot = email.lastIndexOf(".");
		int atIndex = email.indexOf("@");

		return email.substring(0, atIndex+1) + IntStream.range(atIndex+1, lastDot).mapToObj(i-> "*").collect(
			Collectors.joining()) + email.substring(lastDot);
	}

	static class RaffleVisitor {
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
