package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import site.facade.RegistrantFacade;
import site.facade.UserFacade;
import site.model.Registrant;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

/**
 * demo.epay.bg: epay.client:12345678
 * demo.epay.bg: epay.merchant:12345678 (D990264495, KQUND3A923RX7PEW3WI9CAEK4YGWBEVIO2ASJELWUUM24R0SNGFFW02CA7GC0BMY)
 *
 * @author Mihail Stoynov
 */
@Controller
public class TicketsController {

//    private static final String epayKIN = "8323189568";
    private static final String epayKIN = "D990264495";

    static final String TICKETS_JSP = "/tickets-intro.jsp";

    private static final String epayKey;

    static {
        if (System.getProperty("epay.key") != null) {
            epayKey = System.getProperty("epay.key");
        } else {
            epayKey = "KQUND3A923RX7PEW3WI9CAEK4YGWBEVIO2ASJELWUUM24R0SNGFFW02CA7GC0BMY";//demo key
        }

    }

    @Autowired
    @Qualifier(UserFacade.NAME)
    private UserFacade userFacade;

    @Autowired
    @Qualifier(RegistrantFacade.NAME)
    private RegistrantFacade registrantFacade;

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public String goToTicketsIntroPage(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        return TICKETS_JSP;
    }

    @RequestMapping(value = "/tickets/register", method = RequestMethod.GET)
    public String goToRegisterPage(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("registrant", new Registrant(1));
        return "/tickets-register.jsp";
    }

    @Transactional
    @RequestMapping(value = "/tickets/register", method = RequestMethod.POST)
    public String register(Model model, @Valid final Registrant registrant, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "/tickets-register.jsp";
        }

        registrantFacade.save(registrant);

        model.addAttribute("tags", userFacade.findAllTags());
//        model.addAttribute("registrant", registrant);
        prepareEpay(model, registrant);
        return "/tickets-buy.jsp";
    }

//    @RequestMapping(value = "/tickets/buy", method = RequestMethod.GET)
    public String prepareEpay(Model model, Registrant registrant) {
//        Registrant registrant = model.
        String stuff = "" +
                "MIN="+epayKIN+"\r\n" +
                "EMAIL=mihail@sty.bz\r\n" +
                "INVOICE=123456\r\n" +
                "AMOUNT="+(registrant.getVisitors().size()*100)+"\r\n" +
                "CURRENCY=BGN\r\n" +
                "EXP_TIME=01.08.2020\r\n" +
                "DESCR=Test (optional description 100 symbols)\r\n" +
                "ENCODING=utf-8";
        String encodedStuff = Base64.getEncoder().encodeToString(stuff.getBytes());
        model.addAttribute("ENCODED", encodedStuff);
        model.addAttribute("CHECKSUM", getHmac(stuff));

        model.addAttribute("tags", userFacade.findAllTags());
        return "/tickets/buy";
    }

    private static String getHmac(String encoded) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    epayKey.getBytes(),
                    "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            byte[] result = mac.doFinal(encoded.getBytes());

            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Receiving data from epay.bg
     * @param model
     * @return
     */
    @RequestMapping(value = "/tickets/from.epay", method = RequestMethod.POST)
    public String register(Model model) {
        for(Map.Entry entry: model.asMap().entrySet()) {
            System.out.println("FROM EPAY:"+entry.getKey()+ " " + entry.getValue() );
        }
        return "";
    }


    final protected static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
        String decoded = "MIN=D990264495\r\n" +
                "EMAIL=mihail@sty.bz\r\n" +
                "INVOICE=123456\r\n" +
                "AMOUNT=100\r\n" +
                "CURRENCY=BGN\r\n" +
                "EXP_TIME=01.08.2020\r\n" +
                "DESCR=Test (optional description 100 symbols)\r\n" +
                "ENCODING=utf-8";
//        String encoded = "TUlOPUQ5OTAyNjQ0OTUKRU1BSUw9bWloYWlsQHN0eS5iegpJTlZPSUNFPTEyMzQ1NgpBTU9VTlQ9MTAwCkNVUlJFTkNZPUJHTgpFWFBfVElNRT0wMS4wOC4yMDIwCkRFU0NSPVRlc3QgKG9wdGlvbmFsIGRlc2NyaXB0aW9uIDEwMCBzeW1ib2xzKQpFTkNPRElORz11dGYtOA==";
        SecretKeySpec keySpec = new SecretKeySpec(
                epayKey.getBytes(),
                "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] result = mac.doFinal(decoded.getBytes());

//        System.out.println(bytesToHex(getBytes(encoded)));
        System.out.println(bytesToHex(result));
//        System.out.println(getHmac(encoded));
        System.out.println(getHmac(decoded));

        System.out.println("["+System.getProperty("epay.key"));
    }

}