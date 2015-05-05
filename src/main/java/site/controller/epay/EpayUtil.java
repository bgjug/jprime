package site.controller.epay;

import site.model.JprimeException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    private static final String EPAY_KEY;
    private static final Mac HMAC;//for HMAC
    public static final String EPAY_KIN;
    private static final String EPAY_URL;

    private static final String DEMO_EPAY_KEY;
    private static final Mac DEMO_HMAC;//for HMAC
    private static final String DEMO_EPAY_KIN;
    private static final String DEMO_EPAY_URL;


    /** Mihail:  we init the real and demo epay data. */
    static {
        EPAY_KEY = System.getProperty("epay.key")!=null?System.getProperty("epay.key"):"some fake data";
        EPAY_KIN = "8323189568";
        EPAY_URL = "https://www.epay.bg/v3main/paylogin";
        HMAC = _prepareHmac(EPAY_KEY);

        DEMO_EPAY_KEY = "KQUND3A923RX7PEW3WI9CAEK4YGWBEVIO2ASJELWUUM24R0SNGFFW02CA7GC0BMY";//demo key
        DEMO_EPAY_KIN = "D990264495";
        DEMO_EPAY_URL = "https://demo.epay.bg/";
        DEMO_HMAC = _prepareHmac(DEMO_EPAY_KEY);
    }

    /** used only by the static initializer */
    private static Mac _prepareHmac(String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(),"HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            return mac;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new JprimeException(e);
        }
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

    /**
     * Get the encrypted checksum.
     * @param encoded the encoded text (base64)
     * @param real encrypt for real or for demo site
     * @return
     */
    private static String getChecksum(String encoded, boolean real) {
        byte[] encrypted = (real?HMAC:DEMO_HMAC).doFinal(encoded.getBytes());
        String checksum = bytesToHex(encrypted);
        return checksum;
    }

    /**
     * Epay payload is prepared
     * @param numberOfTickets how many tickets
     * @param facNo
     * @param isReal if payload should be real or demo
     * @param overrideAmount if != 0, then use this amount, for testing purposes
     * @return
     */
    public static EpayRaw encrypt(int numberOfTickets, long facNo, boolean isReal, int overrideAmount) {
        String description = numberOfTickets == 1 ? "One jPrime.io ticket" : numberOfTickets+" jPrime.io tickets";
        String message = "" +
                "MIN="+ (isReal?EPAY_KIN:DEMO_EPAY_KIN) +"\r\n" +
                "EMAIL=mihail@sty.bz\r\n" +
                "INVOICE="+facNo+"\r\n" +
                "AMOUNT="+(overrideAmount==0?(numberOfTickets*100):overrideAmount)+"\r\n" +
                "CURRENCY=BGN\r\n" +
                "EXP_TIME=01.08.2020\r\n" +
                "DESCR="+description+"\r\n" +
                "ENCODING=utf-8";
        String encoded = BASE64_ENCODER.encodeToString(message.getBytes());
        String checksum = getChecksum(encoded, isReal);
        return new EpayRaw(checksum, encoded, isReal?EPAY_URL:DEMO_EPAY_URL);
    }

    /////Methods for checking payment
    private static String base64Decode(String base64) {
        try {
            return new String(BASE64_DECODER.decode(base64), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new JprimeException("while decoding base64", e);
        }
    }

    /**
     * Decrypt the payload from epay coming from the backchannel.
     * @param epayRaw
     * @return
     */
    public static EpayResponse decrypt(EpayRaw epayRaw) {
        EpayResponse epayResponse = new EpayResponse();
        epayResponse.setEncoded(epayRaw.getEncoded());
        epayResponse.setChecksum(epayRaw.getChecksum());

        {//isValid
            //we might get an answer from demo or real site
            boolean isReal = true;
            String computedChecksum = getChecksum(epayRaw.getEncoded(), true);//with real
            boolean isMessageValid = epayRaw.getChecksum().equals(computedChecksum);

            epayResponse.setIsValid(isMessageValid);
        }

        String decoded = base64Decode(epayRaw.getEncoded());
        Map<String, String> parsed = parseDecoded(decoded);
        {//decode
            epayResponse.setInvoiceNumber(Long.parseLong(parsed.get("INVOICE")));
            epayResponse.setBorikaAuthorizationCode(parsed.get("BCODE").trim());
            epayResponse.setTransactionNumberIfPaidByCard(parsed.get("STAN"));
            epayResponse.setStatus(EpayResponse.Status.valueOf(parsed.get("STATUS")));
        }
        {//compute time
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = simpleDateFormat.parse(parsed.get("PAY_TIME"));
                epayResponse.setDate(date);
            } catch (ParseException e) {
                throw new JprimeException("parsing date failed: "+parsed.get("PAY_TIME"), e);
            }
        }

        {//compute answer
            String answer = "INVOICE=" + epayResponse.getInvoiceNumber() + ":STATUS="+(epayResponse.isValid()?"OK":"ERR");
            epayResponse.setEpayAnswer(answer);
        }

        return epayResponse;
    }

    private static Map<String, String> parseDecoded(String decoded) {
        try {
            String[] parts = decoded.split(":");
            Map<String, String> parsed = new HashMap<>(parts.length);
            for (String part : parts) {
                String[] keyValuePair = part.split("=");
                parsed.put(keyValuePair[0], keyValuePair[1]);
            }
            return parsed;
        } catch (Throwable t) {
            throw new JprimeException("parsing failed", t);
        }
    }
}

