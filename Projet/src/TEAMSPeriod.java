import javafx.util.converter.LocalDateTimeStringConverter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TEAMSPeriod {

    private LocalDateTime _start = null;
    private LocalDateTime _end = null;
    private LocalDateTimeStringConverter _converter = null;

    public TEAMSPeriod(String _start) {
        String pattern = "dd/MM/yyyy à HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        this._converter = new LocalDateTimeStringConverter(formatter, formatter);
        this.startAt(_start);
    }

    public void startAt(String instant) {
        this._start = this._converter.fromString(instant);
    }

    public void stopAt(String _end) {
        this._end = this._converter.fromString(_end);
    }

    public double getDurationInMinutes() {
        Duration duration = Duration.between(this._start, this._end);
        return Math.abs(duration.toSeconds()/60.);
    }

    public boolean isEnded() {
        return (this._end != null);
    }

    public LocalDateTime get_start() {
        return _start;
    }

    public LocalDateTime get_end() {
        return _end;
    }

    @Override
    public String toString() {
        return "TEAMSPeriod{" +
                "_start=" + _start +
                ", _end=" + _end +
                '}';
    }
}
