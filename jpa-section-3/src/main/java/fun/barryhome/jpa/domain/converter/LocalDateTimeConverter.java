package fun.barryhome.jpa.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created on 2020/3/19 0019 17:11
 *
 * @author Administrator
 * Description:
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Long> {
    @Override
    public Long convertToDatabaseColumn(LocalDateTime localDateTime) {
        if (localDateTime == null){
            return null;
        }

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long aLong) {
        if (null == aLong) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(aLong), ZoneId.systemDefault());
    }
}