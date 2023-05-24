package site.facade;

public class Attachment {
    final byte[] data;

    public String type;

    public boolean isInline() {
        return inline;
    }

    public String getCharset() {
        return charset;
    }

    private final boolean inline;

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    final String name;

    final String charset;

    public Attachment(byte[] data, String name, String charset, boolean inline, String type) {
        this.data = data;
        this.name = name;
        this.charset = charset;
        this.inline = inline;
        this.type = type;
    }
}
