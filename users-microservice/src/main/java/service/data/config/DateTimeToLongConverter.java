package data.config;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;

public class DateTimeToLongConverter implements Converter<DateTime, Long> {

    @Override
    public Long convert(DateTime source) {
        return source.toDateTimeISO().getMillis();
    }

}
