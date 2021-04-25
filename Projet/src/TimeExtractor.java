import java.time.Duration;
import java.time.LocalDateTime;

public class TimeExtractor extends DataExtractor{

    public TimeExtractor(DataExtractor dataExtractor) {
        super(dataExtractor);
    }

    @Override
    public String getHTMLCode(People people) {
        String html ="";
        // duration max, in order to compute images width in percent
        LocalDateTime startTime = TEAMSDateTimeConverter.StringToLocalDateTime(people.get_start());
        LocalDateTime endTime = TEAMSDateTimeConverter.StringToLocalDateTime(people.get_stop());
        Duration delayMax = Duration.between(startTime, endTime);
        double durationMaxMinutes = Math.abs(delayMax.toSeconds()/60.);

        double totalDuration = 0;
        LocalDateTime refTime = TEAMSDateTimeConverter.StringToLocalDateTime(people.get_start());
        for (TEAMSPeriod period : people.get_periodList()) {

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
        html +=	"<div class=\"duration\"> " + Math.round(totalDuration) + " </div> \n";
        html +=	"<div class=\"percentd\"> " + Math.round(100.*totalDuration/durationMaxMinutes) + "% </div> \n";

        if(this.dataExtractor != null){
            return html + this.dataExtractor.getHTMLCode(people);
        }
        return null;
    }
}
