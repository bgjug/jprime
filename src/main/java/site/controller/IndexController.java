package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.config.Globals;
import site.facade.UserService;
import site.model.Partner;
import site.model.Sponsor;
import site.model.SponsorPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    static final String PAGE_INDEX = "index.jsp";

    static final int PARTNERS_CHUNK_SIZE = 6;

    private static final String SOLD_OUT_STYLE = "opacity: 0.5;";

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
        List<Partner> officialSupporterPartners = userFacade.findAllActiveOfficalSupportingPartners();
        Collections.shuffle(officialSupporterPartners);
        List<List<Partner>> officialSupporterPartnersChunks = getPartnerChunks(officialSupporterPartners);
        model.addAttribute("officialSupporterPartnersChunks", officialSupporterPartnersChunks);

        List<Partner> mediaPartners = userFacade.findAllActiveMediaPartners();
        Collections.shuffle(mediaPartners);
        List<List<Partner>> mediaPartnersChunks = getPartnerChunks(mediaPartners);
        model.addAttribute("mediaPartnersChunks", mediaPartnersChunks);

        List<Partner> eventPartners = userFacade.findAllActiveEventPartners();
        Collections.shuffle(eventPartners);
        List<List<Partner>> eventPartnerChunks = getPartnerChunks(eventPartners);
        model.addAttribute("eventPartnerChunks", eventPartnerChunks);

        if (Globals.CURRENT_BRANCH.getCfpCloseDate().isAfterNow() && Globals.CURRENT_BRANCH.getCfpOpenDate()
            .isBeforeNow()) {
            model.addAttribute("early_sold_out", "");
            model.addAttribute("regular_sold_out", SOLD_OUT_STYLE);
        } else {
            model.addAttribute("early_sold_out", SOLD_OUT_STYLE);
            model.addAttribute("regular_sold_out", "");
        }

        Map<String, String> soldOutPackages = Arrays.stream(SponsorPackage.values())
            .map(sp -> Pair.of(sp, Globals.CURRENT_BRANCH.isSoldOut(sp) ? SOLD_OUT_STYLE : ""))
            .collect(Collectors.toMap(p -> p.getFirst().name(), Pair::getSecond));

        model.addAttribute("sold_out_sponsor_packages", soldOutPackages);

        return PAGE_INDEX;
    }

    private List<List<Partner>> getPartnerChunks(List<Partner> partners) {
        List<List<Partner>> partnerChunks = new LinkedList<>();
        int partnersCount = 0;
        List<Partner> currentChunk = new LinkedList<>();
        for (Partner partner : partners) {
            currentChunk.add(partner);
            partnersCount++;

            if (partnersCount == PARTNERS_CHUNK_SIZE) {
                partnerChunks.add(currentChunk);
                partnersCount = 0;
                currentChunk = new LinkedList<>();
            }
        }

        if (partnersCount > 0) {
            partnerChunks.add(currentChunk);
        }

        return partnerChunks;
    }

    private List<Sponsor> shuffleAndGet(Map<SponsorPackage, List<Sponsor>> allSponsors,
        SponsorPackage sponsorPackage) {
        List<Sponsor> silverSponsors = allSponsors.getOrDefault(sponsorPackage, new ArrayList<>());
        Collections.shuffle(silverSponsors);
        return silverSponsors;
    }

}
