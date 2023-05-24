package site.facade;

public class Attachment {
    final byte[] data;

    final String type;

    final boolean isInline;

    final String name;

    final String charset;

    public Attachment(byte[] data, String name, String charset, boolean isInline, String type) {
        this.data = data;
        this.name = name;
        this.charset = charset;
        this.isInline = isInline;
        this.type = type;
    }
}
