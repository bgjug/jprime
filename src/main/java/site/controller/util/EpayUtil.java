package site.controller.util;

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
        HMAC = _prepareHmac(EPAY_KEY);
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

    /** Used by the controller */
    private static String getChecksum(String encoded) {
        byte[] encrypted = HMAC.doFinal(encoded.getBytes());
        String checksum = bytesToHex(encrypted);
        return checksum;
    }

//    /** Used by the controller */
//    private static String getEpayCHECKSUM(String encoded) {
//        return getChecksum(encoded);
//    }

//    /** Not needed, left for debugging purposes. Epay uses hex, not base64 (but they don't say that, assholes). */
//    private static String getHmacAsBase64(String input) {
//        byte[] result = HMAC.doFinal(input.getBytes());
//        return BASE64_ENCODER.encodeToString(result);
//    }

    public static EpayRaw encrypt(int numberOfTickets, long facNo) {
        String description = numberOfTickets == 1 ? "One jPrime.io ticket" : numberOfTickets+" jPrime.io tickets";
        String message = "" +
                "MIN="+ EpayUtil.EPAY_KIN +"\r\n" +
                "EMAIL=mihail@sty.bz\r\n" +
                "INVOICE="+facNo+"\r\n" +
                "AMOUNT="+(numberOfTickets*100)+"\r\n" +
                "CURRENCY=BGN\r\n" +
                "EXP_TIME=01.08.2020\r\n" +
                "DESCR="+description+"\r\n" +
                "ENCODING=utf-8";
        String encoded = BASE64_ENCODER.encodeToString(message.getBytes());
        String checksum = getChecksum(encoded);
        return new EpayRaw(checksum, encoded);
    }









    /////Methods for checking payment
    private static String base64Decode(String base64) {
        try {
            return new String(BASE64_DECODER.decode(base64), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new JprimeException("while decoding base64", e);
        }
    }


    public static EpayResponse decrypt(EpayRaw epayRaw) {
        EpayResponse epayResponse = new EpayResponse();
        epayResponse.setEncoded(epayRaw.getEncoded());
        epayResponse.setChecksum(epayRaw.getChecksum());

        {//isValid
            String computedChecksum = getChecksum(epayRaw.getEncoded());
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


    /** some testing goes here */
    @Deprecated
    public static void main(String[] args) throws Throwable {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        System.out.println(simpleDateFormat.parse("20150419024853"));

        EpayRaw[] raws = {
                new EpayRaw("1b00c25445a469109c90707a320fbf8e1bb46ad7", "SU5WT0lDRT0xMDAwMDA2OlNUQVRVUz1QQUlEOlBBWV9USU1FPTIwMTUwNDE5MDIyMTExOlNUQU49MDI2NDU5OkJDT0RFPTAyNjQ1OQo="),
                new EpayRaw("912cfa49a7b224d6e135b1877287752634228404", "SU5WT0lDRT0xMDAwMDA3OlNUQVRVUz1QQUlEOlBBWV9USU1FPTIwMTUwNDE5MDI0ODUzOlNUQU49MDI2NDYwOkJDT0RFPTAyNjQ2MAo="),
//                new EpayRaw("", ""),
                new EpayRaw("1e15557afe359f68b7ad6808790c8349a4e7f8f9", "SU5WT0lDRT0xMDAwMDA0OlNUQVRVUz1QQUlEOlBBWV9USU1FPTIwMTUwNDE5MDIwODQwOlNUQU49MDI2NDU4OkJDT0RFPTAyNjQ1OAo="),
                new EpayRaw("4870abcf604b1d23430fa845d69d14802683941d", "SU5WT0lDRT0xMDAwMDA3OlNUQVRVUz1QQUlEOlBBWV9USU1FPTIwMTUwNDE5MDI0ODUzOlNUQU49MDI2NDYwOkJDT0RFPTAyNjQ2MApJTlZPSUNFPTEwMDAwMDQ6U1RBVFVTPVBBSUQ6UEFZX1RJTUU9MjAxNTA0MTkwMjA4NDA6U1RBTj0wMjY0NTg6QkNPREU9MDI2NDU4Cg=="),
                new EpayRaw("af847695d723b99635dce7eed2bf742c3e372667", "SU5WT0lDRT0xMDAwMDAzOlNUQVRVUz1QQUlEOlBBWV9USU1FPTIwMTUwNDE4MjE0NDQzOlNUQU49MDI2NDU1OkJDT0RFPTAyNjQ1NQo="),
                new EpayRaw("2188d3bc0d771dde5667a6c176761ad29628efcd", "SU5WT0lDRT0xMDAwMDAyOlNUQVRVUz1QQUlEOlBBWV9USU1FPTIwMTUwNDE4MTcwMTQ3OlNUQU49MDI2NDU0OkJDT0RFPTAyNjQ1NAo="),
                new EpayRaw("e5060a24086c9a25a928c6b803ba22ea81dfbe25", "SU5WT0lDRT0xMjM0NTY6U1RBVFVTPVBBSUQ6UEFZX1RJTUU9MjAxNTA0MTQyMjIyMDc6U1RBTj0wMjYzNjA6QkNPREU9MDI2MzYwCg=="),
//                new EpayRaw("", ""),
//                new EpayRaw("", ""),
//                new EpayRaw("", ""),
//                new EpayRaw("", ""),
//                new EpayRaw("", ""),
//                new EpayRaw("", ""),
                new EpayRaw("e5060a24086c9a25a928c6b803ba22ea81dfbe25__", "SU5WT0lDRT0xMjM0NTY6U1RBVFVTPVBBSUQ6UEFZX1RJTUU9MjAxNTA0MTQyMjIyMDc6U1RBTj0wMjYzNjA6QkNPREU9MDI2MzYwCg=="),
                new EpayRaw("e5060a24086c9a25a928c6b803ba22ea81dfbe25", "SU5WT0lDRT0xMjM0NTY6U1RBVFVTPVBBSUQ6UEFZX1RJTUU9MjAxNTA0MTQyMjIyMDc6U1RBTj0wMjYzNjA6QkNPREU9MDI2MzYwCg==fff"),
//                new EpayRaw("", ""),
        };

        for (EpayRaw raw : raws) {
            EpayResponse epayResponse = EpayUtil.decrypt(raw);
            System.out.println(epayResponse);
        }

//
//        {//message from epay
//            String encoded = "SU5WT0lDRT0xMDAwMDA3OlNUQVRVUz1QQUlEOlBBWV9USU1FPTIwMTUwNDE5MDI0ODUzOlNUQU49MDI2NDYwOkJDT0RFPTAyNjQ2MAo=";
//            String decoded = base64Decode(encoded);
//            String checksum = "912cfa49a7b224d6e135b1877287752634228404";
//            String computedChecksum = getChecksum(encoded);
//            boolean isValidMessage;
//            if(checksum.equals(computedChecksum)) {
//                isValidMessage = true;
//                System.out.println("Validated message from EPAY: "+decoded);
//            } else {
//                isValidMessage = false;
//                System.out.println("INVALID message from EPAY: "+decoded);
//            }
//            System.out.println(checksum);
//            System.out.println(computedChecksum);
//            System.out.println(isValidMessage);
//        }
    }
    {//testing epay

//        String decoded = "MIN=D990264495\r\n" +
//                "EMAIL=mihail@sty.bz\r\n" +
//                "INVOICE=123456\r\n" +
//                "AMOUNT=200\r\n" +
//                "CURRENCY=BGN\r\n" +
//                "EXP_TIME=01.08.2020\r\n" +
//                "DESCR=jPrime ticket\r\n" +
//                "ENCODING=utf-8";
//        String encoded = "TUlOPUQ5OTAyNjQ0OTUNCkVNQUlMPW1paGFpbEBzdHkuYnoNCklOVk9JQ0U9MTIzNDU2DQpBTU9VTlQ9MjAwDQpDVVJSRU5DWT1CR04NCkVYUF9USU1FPTAxLjA4LjIwMjANCkRFU0NSPVRlc3QgKG9wdGlvbmFsIGRlc2NyaXB0aW9uIDEwMCBzeW1ib2xzKQ0KRU5DT0RJTkc9dXRmLTg=";
//        byte[] result = getHmacAsByteArray(encoded);
//
////        System.out.println(bytesToHex(encoded)));
//        System.out.println(bytesToHex(result));
//        System.out.println(getHmacAsBase64(encoded));
//        System.out.println(getHmacAsBase64(decoded));
//
////        System.out.println("["+System.getProperty("epay.key"));
//    }
    }
}

