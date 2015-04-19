package site.controller.util;

/**
 * Carries the ENCODED and CHECKSUM fields, that epay can read
 */
public class EpayRaw {
    private String encoded;
    private String checksum;

    public EpayRaw() {}

    public EpayRaw(String checksum, String encoded) {
        this.encoded = encoded;
        this.checksum = checksum;
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

    @Override
    public String toString() {
        return "EpayRequest{" +
                "encoded='" + encoded + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }
}
