package site.controller.util;

import site.controller.TicketsController;
import site.model.JprimeException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * This class does the epay Hmac magic
 * For more info: https://www.epay.bg/v3main/img/front/tech_wire.pdf (although this link is hard to read).
 *
 * demo.epay.bg: epay.client:12345678
 * demo.epay.bg: epay.merchant:12345678 (D990264495, KQUND3A923RX7PEW3WI9CAEK4YGWBEVIO2ASJELWUUM24R0SNGFFW02CA7GC0BMY)

 * @author Mihail Stoynov
 */
public class EpayUtil {

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();//for ...AsHex()
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();//for ENCODED

    private static final String EPAY_KEY;
    private static final Mac HMAC;//for HMAC
    private static final String EPAY_KIN;
    public static final String EPAY_URL;

    /** Mihail: if the <code>epay.key</code> param is supplied, we switch to real epay transactions. If not, we use demo. */
    static {
        if (System.getProperty("epay.key") != null) {
            EPAY_KEY = System.getProperty("epay.key");
            EPAY_KIN = "8323189568";
            EPAY_URL = "https://www.epay.bg/";

        } else {
            EPAY_KEY = "KQUND3A923RX7PEW3WI9CAEK4YGWBEVIO2ASJELWUUM24R0SNGFFW02CA7GC0BMY";//demo key
            EPAY_KIN = "D990264495";
            EPAY_URL = "https://demo.epay.bg/";
        }
        HMAC = prepareHmac(EPAY_KEY);
    }

    /** used only by the static initializer */
    private static Mac prepareHmac(String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(),"HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            return mac;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new JprimeException(e);
        }
    }

    /** Get the SHA1 HMAC hash. The actual magic is here */
    private static byte[] getHmacAsByteArray(String input) {
        return HMAC.doFinal(input.getBytes());
    }


    /** Used by the controller */
    private static String getHmacAsHex(String input) {
        byte[] result = getHmacAsByteArray(input);

        return bytesToHex(result);
    }

    /** Used by the controller */
    public static String getEpayCHECKSUM(String input) {
        return getHmacAsHex(input);
    }

    /** Not needed, left for debugging purposes. Epay uses hex, not base64 (but they don't say that, assholes). */
    private static String getHmacAsBase64(String input) {
        byte[] result = getHmacAsByteArray(input);
        return Base64.getEncoder().encodeToString(result);
    }

    public static String getEpayENCODED(int numberOfTickets, long facNo) {
        String description = numberOfTickets == 1 ? "One jPrime.io ticket" : numberOfTickets+" jPrime.io tickets";
        String stuff = "" +
                "MIN="+ EpayUtil.EPAY_KIN +"\r\n" +
                "EMAIL=mihail@sty.bz\r\n" +
                "INVOICE="+facNo+"\r\n" +
                "AMOUNT="+(numberOfTickets*100)+"\r\n" +
                "CURRENCY=BGN\r\n" +
                "EXP_TIME=01.08.2020\r\n" +
                "DESCR="+description+"\r\n" +
                "ENCODING=utf-8";
        return BASE64_ENCODER.encodeToString(stuff.getBytes());
    }

    /** byte[] to hex representation, stolen from stackoverflow */
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /** some testing goes here */
    @Deprecated
    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
        String decoded = "MIN=D990264495\r\n" +
                "EMAIL=mihail@sty.bz\r\n" +
                "INVOICE=123456\r\n" +
                "AMOUNT=200\r\n" +
                "CURRENCY=BGN\r\n" +
                "EXP_TIME=01.08.2020\r\n" +
                "DESCR=jPrime ticket\r\n" +
                "ENCODING=utf-8";
        String encoded = "TUlOPUQ5OTAyNjQ0OTUNCkVNQUlMPW1paGFpbEBzdHkuYnoNCklOVk9JQ0U9MTIzNDU2DQpBTU9VTlQ9MjAwDQpDVVJSRU5DWT1CR04NCkVYUF9USU1FPTAxLjA4LjIwMjANCkRFU0NSPVRlc3QgKG9wdGlvbmFsIGRlc2NyaXB0aW9uIDEwMCBzeW1ib2xzKQ0KRU5DT0RJTkc9dXRmLTg=";
        byte[] result = getHmacAsByteArray(encoded);

//        System.out.println(bytesToHex(input)));
        System.out.println(bytesToHex(result));
        System.out.println(getHmacAsBase64(encoded));
        System.out.println(getHmacAsBase64(decoded));

//        System.out.println("["+System.getProperty("epay.key"));
    }
}
