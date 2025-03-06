package site.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import site.facade.BranchService;
import site.facade.UserService;
import site.model.Branch;
import site.model.Speaker;

@Controller
public class SpeakerController {

    private static final Logger logger = LogManager.getLogger(SpeakerController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BranchService branchService;

    @GetMapping("/speakers")
    public String speakers(Pageable pageable, Model model) {
        List<Speaker> acceptedSpeakers = userService.findConfirmedSpeakers();
        Page<Speaker> speakers = new PageImpl<>(acceptedSpeakers, pageable, acceptedSpeakers.size());
        model.addAttribute("speakers", speakers);

        model.addAttribute("tags", userService.findAllTags());

        return "speakers";
    }

    //read a single blog
    @GetMapping("/speaker/{id}")
    public String getById(@PathVariable final long id, Model model) {
        Speaker speaker = userService.findSpeaker(id);
        if (speaker == null) {
            logger.error("Invalid speaker id ({})", id);
            return "404";
        }

        if (userService.isSpeakerConfirmed(speaker)) {
            model.addAttribute("speaker", speaker);
        }

        Branch currentBranch = branchService.getCurrentBranch();
        model.addAttribute("jprime_year", currentBranch.getYear());
        model.addAttribute("tags", userService.findAllTags());
        return "speaker";
    }
}
