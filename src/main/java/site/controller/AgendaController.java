package site.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import site.facade.BranchService;
import site.facade.UserService;
import site.model.Branch;
import site.model.Session;

@Controller
public class AgendaController {

	private static final Logger logger = LogManager.getLogger(AgendaController.class);

	@Autowired
	private UserService userFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private BranchService branchService;

	//read a single agenda post
    @GetMapping("/agenda/{id}")
    public String getById(@PathVariable final long id, Model model) {
		Branch currentBranch = branchService.getCurrentBranch();
	   model.addAttribute("tags", userFacade.findAllTags());
		model.addAttribute("jprime_year", currentBranch.getYear());
    	Session talk = userService.findSessionTalk(id);
    	if (talk == null) {
			logger.error(String.format("Invalid session id (%1$d)", id));
    		return "404";
		}
    	model.addAttribute("talk", talk);
        return "talk";
    }
    
    @GetMapping("/agenda")
	public String getAgenda(Model model) {
	   model.addAttribute("tags", userFacade.findAllTags());

    	List<Session> alpha = userService.findSessionTalksAndBreaksByHallName("Hall A");
    	List<Session> beta = userService.findSessionTalksAndBreaksByHallName("Hall B");
	   List<Session> workshops = userService.findSessionTalksAndBreaksByHallName("Workshops");

    	model.addAttribute("alpha", alpha);
    	model.addAttribute("beta", beta);
	   model.addAttribute("workshops", workshops);

		Branch currentBranch = branchService.getCurrentBranch();
		LocalDateTime startDate = currentBranch.getStartDate();
		model.addAttribute("firstDayDate", DateUtils.fromLocalDateTime(startDate));
		model.addAttribute("secondDayDate", DateUtils.fromLocalDateTime(startDate.plusDays(1)));

		model.addAttribute("agenda", currentBranch.isAgendaPublished());

    	//we have 2 options here
    	//option one iterate on all alpha on the view and with status=i and use i.count in beta to get the beta talk with the same position as in alpha.
    	//pros - easy, cons - not good when we have a missing slots on beta or when slots dont match !

    		//option two is to create a structure with slots (indexes or timings 09:00-09:40 and so on .. and to put on each slot the one(when break) or two (when talks for alpha and beta)
    	//then to just iterate over this indexes or timings on the frontend taking the content
    	//pros have to make the mapping here, cons - well this implementation costs more time

		 //implementing variant 1 since we are in a rush + we do

        return "talks";
    }
}
