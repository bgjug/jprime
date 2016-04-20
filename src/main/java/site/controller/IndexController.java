package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import site.facade.UserService;
import site.model.Partner;
import site.model.Sponsor;
import site.model.SponsorPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

	static final String PAGE_INDEX = "index.jsp";

    @Autowired
    @Qualifier(UserService.NAME)
    private UserService userFacade;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {

        Map<SponsorPackage, List<Sponsor>> allSponsors = userFacade.findAllActiveSponsors();
        
        List<Sponsor> platinumSponsors = shuffleAndGet(allSponsors, SponsorPackage.PLATINUM);
		model.addAttribute("platinumSponsors", platinumSponsors);
		List<Sponsor> goldSponsors = shuffleAndGet(allSponsors, SponsorPackage.GOLD);
		model.addAttribute("goldSponsors", goldSponsors);
		List<Sponsor> silverSponsors = shuffleAndGet(allSponsors, SponsorPackage.SILVER);
		model.addAttribute("silverSponsors", silverSponsors);
		
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("featuredSpeakers", userFacade.findFeaturedSpeakers());
        
        List<Partner> mediaPartners = userFacade.findAllPartners();
		model.addAttribute("partners", mediaPartners);
		
		return PAGE_INDEX;
	}

	private List<Sponsor> shuffleAndGet(
			Map<SponsorPackage, List<Sponsor>> allSponsors, SponsorPackage sponsorPackage) {
		List<Sponsor> silverSponsors = allSponsors.getOrDefault(sponsorPackage,
                new ArrayList<>());
        Collections.shuffle(silverSponsors);
		return silverSponsors;
	}

}
