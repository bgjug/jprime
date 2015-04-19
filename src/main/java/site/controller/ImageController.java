package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.facade.AdminFacade;

/**
 * @author Ivan St. Ivanov
 */
@Controller
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    @Qualifier(AdminFacade.NAME)
    private AdminFacade adminFacade;

    @RequestMapping(value = "/sponsor/{itemId}")
    @ResponseBody
    public byte[] getSponsorLogo(@PathVariable("itemId") Long itemId)  {
        return adminFacade.findOneSponsor(itemId).getLogo();
    }

    @RequestMapping(value = "/speaker/{itemId}")
    @ResponseBody
    public byte[] getSpeakerPhoto(@PathVariable("itemId") Long itemId)  {
        return adminFacade.findOneSpeaker(itemId).getPicture();
    }

}
