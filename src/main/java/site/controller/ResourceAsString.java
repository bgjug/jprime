package site.controller;

import java.io.IOException;
import java.io.InputStream;

public class ResourceAsString {
    static String resourceAsString(String resource) throws IOException {
        InputStream is = ResourceAsString.class.getResourceAsStream(resource);

        byte[] buffer = new byte[is.available()];
        is.read(buffer);

        return new String(buffer);
    }
}
