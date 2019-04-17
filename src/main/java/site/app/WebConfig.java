package site.app;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    class JodaDateTimeConverter implements Converter<DateTime, String> {

        @Override
        public String convert(DateTime dateTime) {
            return dateTime.toString("dd.MM.yyy HH:mm");
        }
    }
 
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new JodaDateTimeConverter());
    }
}