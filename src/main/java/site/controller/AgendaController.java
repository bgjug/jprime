package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import site.config.Globals;
import site.facade.UserService;
import site.model.Session;

import java.util.List;

@Controller
public class AgendaController {

	private static final Logger logger = LogManager.getLogger(AgendaController.class);

	@Autowired
	@Qualifier(UserService.NAME)
	private UserService userFacade;

	@Autowired
	private UserService userService;

	@Value("${agenda.published:false}")
	private boolean agenda;

	//read a single agenda post
    @RequestMapping("/agenda/{id}")
    public String getById(@PathVariable("id") final long id, Model model) {
	   model.addAttribute("tags", userFacade.findAllTags());
		model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());
    	Session talk = userService.findSessionTalk(id);
    	if (talk == null) {
			logger.error(String.format("Invalid session id (%1$d)", id));
    		return "/404.jsp";
		}
    	model.addAttribute("talk", talk);
        return "/talk.jsp";
    }
    
    @RequestMapping("/agenda")
	public String getAgenda(Model model) {
	   model.addAttribute("tags", userFacade.findAllTags());

    	List<Session> alpha = userService.findSessionTalksAndBreaksByHallName("Hall A");
    	List<Session> beta = userService.findSessionTalksAndBreaksByHallName("Hall B");
	   List<Session> workshops = userService.findSessionTalksAndBreaksByHallName("Workshops");

    	model.addAttribute("alpha", alpha);
    	model.addAttribute("beta", beta);
	   model.addAttribute("workshops", workshops);

		DateTime startDate = Globals.CURRENT_BRANCH.getStartDate();
		model.addAttribute("firstDayDate", startDate);
		model.addAttribute("secondDayDate", startDate.plusDays(1));

		model.addAttribute("agenda", agenda);

    	//we have 2 options here
    	//option one iterate on all alpha on the view and with status=i and use i.count in beta to get the beta talk with the same position as in alpha.
    	//pros - easy, cons - not good when we have a missing slots on beta or when slots dont match !

    		//option two is to create a structure with slots (indexes or timings 09:00-09:40 and so on .. and to put on each slot the one(when break) or two (when talks for alpha and beta)
    	//then to just iterate over this indexes or timings on the frontend taking the content
    	//pros have to make the mapping here, cons - well this implementation costs more time

		 //implementing variant 1 since we are in a rush + we do

        return "/talks.jsp";
    }
}
