package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.facade.UserFacade;
import site.model.Article;
import site.model.Sponsor;
import site.model.SponsorPackage;
import site.model.Tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
	
	static final String PAGE_INDEX = "index.jsp";

    @Autowired
    @Qualifier(UserFacade.NAME)
    private UserFacade userFacade;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {

        Map<SponsorPackage, List<Sponsor>> allSponsors = userFacade.findAllSponsors();
        model.addAttribute("platinumSponsors", allSponsors.getOrDefault(SponsorPackage.PLATINUM,
                new ArrayList<>()));
        model.addAttribute("goldSponsors", allSponsors.getOrDefault(SponsorPackage.GOLD,
                new ArrayList<>()));
        model.addAttribute("silverSponsors", allSponsors.getOrDefault(SponsorPackage.SILVER,
                new ArrayList<>()));
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("featuredSpeakers", userFacade.findFeaturedSpeakers());
		return PAGE_INDEX;
	}

}
