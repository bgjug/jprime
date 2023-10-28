package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import site.config.Globals;
import site.controller.invoice.InvoiceData;
import site.facade.UserService;
import site.model.Branch;
import site.model.Partner;
import site.model.Sponsor;
import site.model.SponsorPackage;

import java.time.LocalDateTime;
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

    @GetMapping("/")
    public String index(Model model) {

        Map<SponsorPackage, List<Sponsor>> allSponsors = userFacade.findAllActiveSponsors();

        List<Sponsor> platinumSponsors = shuffleAndGet(allSponsors, SponsorPackage.PLATINUM);
        model.addAttribute("platinumSponsors", platinumSponsors);
        List<Sponsor> goldSponsors = shuffleAndGet(allSponsors, SponsorPackage.GOLD);
        model.addAttribute("goldSponsors", goldSponsors);
        List<Sponsor> goldLiteSponsors = shuffleAndGet(allSponsors, SponsorPackage.GOLD_LITE);
        model.addAttribute("goldLiteSponsors", goldLiteSponsors);
        List<Sponsor> silverSponsors = shuffleAndGet(allSponsors, SponsorPackage.SILVER);
        model.addAttribute("silverSponsors", silverSponsors);

        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("featuredSpeakers", userFacade.findFeaturedSpeakers());

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

        Branch currentBranch = Globals.CURRENT_BRANCH;
        if (currentBranch.getCfpCloseDate().isAfter(LocalDateTime.now()) && currentBranch.getCfpOpenDate().minusDays(30)
            .isBefore(LocalDateTime.now())) {
            model.addAttribute("early_sold_out", "");
            model.addAttribute("regular_sold_out", SOLD_OUT_STYLE);
            model.addAttribute("students_sold_out", "");
        } else {
            model.addAttribute("early_sold_out", SOLD_OUT_STYLE);
            model.addAttribute("regular_sold_out", currentBranch.isSoldOut() ? SOLD_OUT_STYLE : "");
            model.addAttribute("students_sold_out", currentBranch.isSoldOut() ? SOLD_OUT_STYLE : "");
        }

        Map<String, String> soldOutPackages = Arrays.stream(SponsorPackage.values())
            .map(sp -> Pair.of(sp, currentBranch.isSoldOut(sp) ? SOLD_OUT_STYLE : ""))
            .collect(Collectors.toMap(p -> p.getFirst().name(), Pair::getSecond));

        model.addAttribute("sold_out_sponsor_packages", soldOutPackages);

        InvoiceData.TicketPrices prices = InvoiceData.getPrices(currentBranch);

        model.addAttribute("early_bird_ticket_price", String.format("%.2f", prices.getEarlyBirdPrice()));
        model.addAttribute("regular_ticket_price", String.format("%.2f",prices.getRegularPrice()));
        model.addAttribute("student_ticket_price", String.format("%.2f",prices.getStudentPrice()));

        model.addAttribute("cfp_close_date", DateUtils.dateToStringWithMonthAndYear(currentBranch.getCfpCloseDate()));

        // 30th and 31st of May 2023
        LocalDateTime startDate = currentBranch.getStartDate();
        model.addAttribute("conference_dates", String.format("%s and %s", DateUtils.dateToString(startDate),
            DateUtils.dateToStringWithMonthAndYear(startDate.plusDays(1))));
        model.addAttribute("jprime_year", currentBranch.getStartDate().getYear());

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
        List<Sponsor> sponsorsList = allSponsors.getOrDefault(sponsorPackage, new ArrayList<>());
        Collections.shuffle(sponsorsList);
        return sponsorsList;
    }

}
