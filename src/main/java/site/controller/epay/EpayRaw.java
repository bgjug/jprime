package site.controller.epay;

/**
 * Carries the ENCODED and CHECKSUM fields, that epay can read
 * @author Mihail Stoynov
 */
public class EpayRaw {
    private String encoded;
    private String checksum;
    private String epayUrl;

    public EpayRaw() {}

    public EpayRaw(String checksum, String encoded, String epayUrl) {
        this.encoded = encoded;
        this.checksum = checksum;
        this.epayUrl = epayUrl;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getEpayUrl() {
        return epayUrl;
    }

    public void setEpayUrl(String epayUrl) {
        this.epayUrl = epayUrl;
    }

    @Override
    public String toString() {
        return "EpayRaw{" +
                "encoded='" + encoded + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }
}
