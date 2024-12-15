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

import site.config.Globals;
import site.facade.UserService;
import site.model.Speaker;

@Controller
public class SpeakerController {
    private static final Logger logger = LogManager.getLogger(SpeakerController.class);

   @Autowired
   private UserService userService;

   @GetMapping("/speakers")
   public String speakers(Pageable pageable, Model model) {
      List<Speaker> acceptedSpeakers = userService.findAcceptedSpeakers();
      Page<Speaker> speakers = new PageImpl<>(acceptedSpeakers, pageable, acceptedSpeakers.size());
      model.addAttribute("speakers", speakers);

      model.addAttribute("tags", userService.findAllTags());

      return "speakers";
   }

   //read a single blog
   @GetMapping("/speaker/{id}")
   public String getById(@PathVariable final long id, Model model) {
      Speaker speaker = userService.findSpeaker(id);
      model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());
        if (speaker == null) {
            logger.error(String.format("Invalid speaker id (%1$d)", id));
            return "404";
        }
      if(speaker.getAccepted()) {
         model.addAttribute("speaker", speaker);
      }

      model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());
      model.addAttribute("tags", userService.findAllTags());
      return "speaker";
   }
}
