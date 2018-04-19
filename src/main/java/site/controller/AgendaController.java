package site.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.facade.UserService;
import site.model.Article;
import site.model.Session;
import site.repository.SessionRepository;

@Controller
public class AgendaController {
	
	@Autowired
	private UserService userService;
	
	//read a single agenda post
    @RequestMapping("/agenda/{id}")
    public String getById(@PathVariable("id") final long id, Model model) {
    	Session talk = userService.findSessionTalk(id);
    	model.addAttribute("talk", talk);
        return "/talk.jsp";
    }
    
    @RequestMapping("/agenda")
	public String getAgenda(Model model) {
    	List<Session> alpha = userService.findSessionTalksAndBreaksByHallName("Hall A");
    	List<Session> beta = userService.findSessionTalksAndBreaksByHallName("Hall B");
	   List<Session> workshops = userService.findSessionTalksAndBreaksByHallName("Workshops");

    	model.addAttribute("alpha", alpha);
    	model.addAttribute("beta", beta);
	   model.addAttribute("workshops", workshops);

		model.addAttribute("firstDayDate", alpha.get(0).getStartTime());
	   model.addAttribute("secondDayDate", alpha.get(0).getStartTime().plusDays(1));

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
