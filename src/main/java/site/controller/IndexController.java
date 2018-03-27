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

import java.util.*;

@Controller
public class IndexController {

	static final String PAGE_INDEX = "index.jsp";

    static final int PARTNERS_CHUNK_SIZE = 6;

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
        model.addAttribute("acceptedSpeakers", userFacade.findAcceptedSpeakers());

        // split partners in groups for better display in rows
        List<Partner> partners = userFacade.findAllActiveMediaPartners();
        Collections.shuffle(partners);
        List<List<Partner>> partnerChunks = getPartnerChunks(partners);
        model.addAttribute("partnerChunks", partnerChunks);

        List<Partner> eventPartners = userFacade.findAllActiveEventPartners();
        Collections.shuffle(eventPartners);
        List<List<Partner>> eventPartnerChunks = getPartnerChunks(eventPartners);

        model.addAttribute("eventPartnerChunks", eventPartnerChunks);

		return PAGE_INDEX;
	}

    private List<List<Partner>> getPartnerChunks(List<Partner> partners) {
        List<List<Partner>> partnerChunks = new LinkedList<>();
        int partnersCount = 0;
        List<Partner> currentChunk = new LinkedList<>();
        for(Partner partner : partners) {
            currentChunk.add(partner);
            partnersCount++;

            if(partnersCount == PARTNERS_CHUNK_SIZE) {
                partnerChunks.add(currentChunk);
                partnersCount = 0;
                currentChunk = new LinkedList<>();
            }
        }

        if(partnersCount > 0) {
            partnerChunks.add(currentChunk);
        }

        return partnerChunks;
    }

	private List<Sponsor> shuffleAndGet(
			Map<SponsorPackage, List<Sponsor>> allSponsors, SponsorPackage sponsorPackage) {
		List<Sponsor> silverSponsors = allSponsors.getOrDefault(sponsorPackage,
                new ArrayList<>());
        Collections.shuffle(silverSponsors);
		return silverSponsors;
	}

}
