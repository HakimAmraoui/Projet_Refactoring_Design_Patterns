import javafx.util.converter.LocalDateTimeStringConverter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class TEAMSDateTimeConverter {

    public static LocalDateTime StringToLocalDateTime(String instant) {
        
        String pattern = "dd/MM/yyyy à HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        var _converter = new LocalDateTimeStringConverter(formatter, formatter);
        return _converter.fromString(instant);
    }

}
