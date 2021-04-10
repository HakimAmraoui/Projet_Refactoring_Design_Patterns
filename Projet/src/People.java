import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;

public class People implements Comparable<People> {

    private final String _name;
    private final String _id;
    private String _start;
    private String _stop;
    private LinkedList<TEAMSPeriod> _periodList;

    public People(String _name) {
        this._name = _name;
        this._id = StudentIDServer.getId(this._name);
        this._periodList = new LinkedList<>();
    }

    public String get_id() {
        return _id;
    }

    public void addPeriod(String action, String instant) {

        if ( action.charAt(0) == 'R' ) {
            TEAMSPeriod period = new TEAMSPeriod(instant);
            this._periodList.add(period);
        } else
            if ( action.charAt(0) == 'A' ) {
                this._periodList.getLast().stopAt(instant);
            } else {
                System.out.println(this._name + " --> erreur : action inconnue ["+action+"] ");
            }
    }

    public void forceEndTimeAt(String instant) {
        this._stop = instant;
        // delete all periods started after ending time
        var localPeriodEndingTime = TEAMSDateTimeConverter.StringToLocalDateTime(this._stop);
        boolean STOP = false;
        while (!STOP) {
            if (this._periodList.size()>0 && this._periodList.getLast().get_start().isAfter(localPeriodEndingTime))
                this._periodList.removeLast();
            else
                STOP = true;
        }

        // no ending time, so fix it
        if (this._periodList.size()>0 && this._periodList.getLast().get_end()==null)
            this._periodList.getLast().stopAt(this._stop);
        else
        // ending after the official end of the course, so limit it
        if (this._periodList.size()>0 && this._periodList.getLast().get_end().isAfter(localPeriodEndingTime))
            this._periodList.getLast().stopAt(this._stop);
    }

    public void forceStartTimeAt(String instant) {
        this._start = instant;
        // delete all periods ended before starting time
        var periods = this._periodList.iterator();
        boolean STOP = false;
        var localPeriodStartingTime = TEAMSDateTimeConverter.StringToLocalDateTime(this._start);
        while ( periods.hasNext() && !STOP) {
            var period = periods.next();
            if (period.get_end().isBefore(localPeriodStartingTime))
                periods.remove();
            else
                STOP = true;
        }
        // adjust starting time of period if before official course starting time
        if (!this._periodList.isEmpty() && this._periodList.getFirst().get_start().isBefore(localPeriodStartingTime))
            this._periodList.getFirst().startAt(this._start);

    }

    public long getTotalAttendanceDuration() {
        double totalDuration = 0;
        for (TEAMSPeriod period : this._periodList) {
            totalDuration += period.getDurationInMinutes();
        }
        return Math.round(totalDuration);
    }

    public boolean isClosed() {
        return this._periodList.getLast().isEnded();
    }

    public String getName() {
        return this._name;
    }

    public String getHTMLCode() {

        if ( this.isOutOfPeriod() ) return ("");

        // duration max, in order to compute images width in percent
        LocalDateTime startTime = TEAMSDateTimeConverter.StringToLocalDateTime(this._start);
        LocalDateTime endTime = TEAMSDateTimeConverter.StringToLocalDateTime(this._stop);
        Duration delayMax = Duration.between(startTime, endTime);
        double durationMaxMinutes = Math.abs(delayMax.toSeconds()/60.);

        String html="";
		html += "<div class=\"datapeople\"> \n";
		html += "<div class=\"name\"> " + this.getName() + " </div> \n";
		html +=	"<div class=\"timebar\">";

        double totalDuration = 0;
        LocalDateTime refTime = TEAMSDateTimeConverter.StringToLocalDateTime(this._start);
        for (TEAMSPeriod period : this._periodList) {

            LocalDateTime begin = period.get_start();
            LocalDateTime end = period.get_end();
            double duration = period.getDurationInMinutes();
            totalDuration += duration;
            // begin > reftime : white bar
            Duration delay = Duration.between(refTime, begin);
            double delayMinutes = Math.abs(delay.toSeconds()/60.);
            if (delayMinutes>0.0) {
                html += "<img src=\"off.png\" ";
                html += "width=\"" + (100.*delayMinutes/durationMaxMinutes) + "%\" ";
                html += "height=\"20\" title=\"absent(e) de " + refTime.toString();
                html += " à " + begin.toString() + " \"> \n";
            }
            // green bar for the current period
            html += "<img src=\"on.png\" ";
            html += "width=\"" + (100.*duration/durationMaxMinutes) + "%\" ";
            html += "height=\"20\" title=\"connecté(e) de " + begin.toString();
            html += " à " + end.toString()+ "\"> \n";
            refTime = end;
        }
        // last period aligned on end time ?
        //LocalDateTime endTime = TEAMSDateTimeConverter.StringToLocalDateTime(this._stop);
        Duration delay = Duration.between(refTime, endTime);
        double delayMinutes = Math.abs(delay.toSeconds()/60.);
        if (delayMinutes>0.0) {
            html += "<img src=\"off.png\" ";
            html += "width=\"" + (100.*delayMinutes/durationMaxMinutes) + "%\" ";
            html += "height=\"20\" title=\"absent(e) de " + refTime.toString();
            html += " à " + endTime.toString() + " \"> \n";
        }
		html += "</div> \n"; // end of div timebar
        html +=	"<div class=\"duration\"> " + (long)Math.round(totalDuration) + " </div> \n";
        html +=	"<div class=\"percentd\"> " + (long)Math.round(100.*totalDuration/durationMaxMinutes) + "% </div> \n";
        html += "</div>\n"; // end of div datapeople
        return html;
    }

    @Override
    public String toString() {
        return "People{" +
                "_name='" + _name + '\'' +
                ", _id='" + _id + '\'' +
                ", _periodList=" + _periodList +
                ", _start='" + _start + '\'' +
                ", _stop='" + _stop + '\'' +
                '}';
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return ( this._periodList.getFirst().get_start().format(formatter.withLocale(Locale.FRANCE)) );
    }

    @Override
    public int compareTo(People o) {
        return (int)(this.getTotalAttendanceDuration()-o.getTotalAttendanceDuration());
    }

    public boolean isOutOfPeriod() {
        return this._periodList.isEmpty();
    }

}
