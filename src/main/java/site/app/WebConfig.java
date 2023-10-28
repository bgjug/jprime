package site.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    static class JodaDateTimeConverter implements Converter<LocalDateTime, String> {

        @Override
        public String convert(LocalDateTime dateTime) {
            return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm"));
        }
    }
 
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new JodaDateTimeConverter());
    }
}