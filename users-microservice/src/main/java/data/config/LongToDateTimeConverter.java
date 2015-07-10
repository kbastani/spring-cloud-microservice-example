package data.config;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;

public class LongToDateTimeConverter implements Converter<Long, DateTime> {

    @Override
    public DateTime convert(Long source) {
        return new DateTime(source);
    }

}
