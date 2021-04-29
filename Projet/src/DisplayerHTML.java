import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DisplayerHTML extends Displayer {

    @Override
    public String display(Collection<People> _allpeople, String _fileName, String _startTime, String _endTime) {
        File htmlTemplateFile = new File("ressources/templateHTML.html");
        String htmlString = null;
        try {
            htmlString = Files.readString(htmlTemplateFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String date = LocalDate.now().toString();
        String courseName = "CM Bases de données et programmation Web"; // Temporary
        Integer numberOfStudents = _allpeople.size();

        htmlString = htmlString.replace("$date", date);
        htmlString = htmlString.replace("$startTime", _startTime);
        htmlString = htmlString.replace("$endTime", _endTime);
        htmlString = htmlString.replace("$courseName", courseName);
        htmlString = htmlString.replace("$fileName", _fileName);
        htmlString = htmlString.replace("$numberOfStudents", numberOfStudents.toString());

        LocalDateTime startTime = TEAMSDateTimeConverter.StringToLocalDateTime(_startTime);
        LocalDateTime endTime = TEAMSDateTimeConverter.StringToLocalDateTime(_endTime);
        double durationMaxMinutes = Math.abs(Duration.between(startTime, endTime).toSeconds()/60.);
        Iterator<People> pIterator = _allpeople.iterator();
        String blockPeople = "";
        while (pIterator.hasNext()) {
            People people = pIterator.next();

            if (people.isOutOfPeriod()) {
                continue;
            }
            
            File dataPeopleTemplateFile = new File("ressources/templateDataPeople.html");
            String dataPeopleString = null;
            try {
                dataPeopleString = Files.readString(dataPeopleTemplateFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            HashMap<String, String> data = People.getExtractor().getData(people);
            String dataPeople = "";

            if (data.containsKey("id")) {
                String id = data.get("id");
                dataPeople += "\t\t\t\t<div class=\"id\">\n";
                dataPeople += ("\t\t\t\t\t" + id + "\n");
                dataPeople += "\t\t\t\t</div>\n";
            }

            if (data.containsKey("name")) {
                String name = data.get("name");
                dataPeople += "\t\t\t\t<div class=\"name\">\n";
                dataPeople += ("\t\t\t\t\t" + name + "\n");
                dataPeople += "\t\t\t\t</div>\n";
            }

            if (data.containsKey("time")) {
                dataPeople += "\t\t\t\t<div class=\"timebar\">\n";
                dataPeople += (timBar(people));
                dataPeople += "\t\t\t\t</div>\n";

                Long duration = people.getTotalAttendanceDuration();
                dataPeople += "\t\t\t\t<div class=\"duration\">\n";
                dataPeople += ("\t\t\t\t\t" + duration.toString() + "\n");
                dataPeople += "\t\t\t\t</div>\n";

                int percentage = (int)(duration/durationMaxMinutes*100);
                dataPeople += "\t\t\t\t<div class=\"percentd\">\n";
                dataPeople += ("\t\t\t\t\t" + percentage + "%\n");
                dataPeople += "\t\t\t\t</div>\n";
            }

            if (dataPeople.isBlank()) {
                continue;
            }

            dataPeopleString = dataPeopleString.replace("$dataPeople", dataPeople);

            blockPeople += dataPeopleString;
        }
        htmlString = htmlString.replace("$blockPeople", blockPeople);

        String fileName = _fileName.replace(".csv", ".html");
        String filePath = "output/";
        File htmlOutputFile = new File(filePath + fileName);
        try {
            Files.createDirectories(Paths.get(filePath));
            htmlOutputFile.createNewFile();
            FileWriter fw = new FileWriter(htmlOutputFile, false);
            fw.write(htmlString);
            fw.close();
            Desktop.getDesktop().browse(htmlOutputFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlString;
    }

    private String timBar(People people) {
        String html = "";
        // duration max, in order to compute images width in percent
        LocalDateTime startTime = TEAMSDateTimeConverter.StringToLocalDateTime(people.get_start());
        LocalDateTime endTime = TEAMSDateTimeConverter.StringToLocalDateTime(people.get_stop());
        double durationMaxMinutes = Math.abs(Duration.between(startTime, endTime).toSeconds()/60.);
        LocalDateTime refTime = TEAMSDateTimeConverter.StringToLocalDateTime(people.get_start());
        String imgTag;
        Iterator<TEAMSPeriod> tIterator = people.get_periodList().iterator();
        while (tIterator.hasNext()) {
            TEAMSPeriod period = tIterator.next();
            LocalDateTime begin = period.get_start();
            LocalDateTime end = period.get_end();
            double duration = period.getDurationInMinutes();
            // begin > reftime : white bar
            double delayMinutes = Math.abs(Duration.between(refTime, begin).toSeconds()/60.);
            if (delayMinutes>0.0) {
                imgTag = "\t\t\t\t\t<img src=\"off.png\" width=\"$width%\" height=\"20\" title=\"absent(e) de $refTime à $begin\">\n";
                imgTag = imgTag.replace("$width", "" + (100.*delayMinutes/durationMaxMinutes));
                imgTag = imgTag.replace("$refTime", refTime.toString());
                imgTag = imgTag.replace("$begin", begin.toString());
                html += imgTag;
            }
            // green bar for the current period
            imgTag = "\t\t\t\t\t<img src=\"on.png\" width=\"$width%\" height=\"20\" title=\"connecté(e) de $refTime à $begin\">\n";
            imgTag = imgTag.replace("$width", "" + (100.*duration/durationMaxMinutes));
            imgTag = imgTag.replace("$refTime", begin.toString());
            imgTag = imgTag.replace("$begin", end.toString());
            html += imgTag;
            refTime = end;
        }
        // last period aligned on end time ?
        Duration delay = Duration.between(refTime, endTime);
        double delayMinutes = Math.abs(delay.toSeconds()/60.);
        if (delayMinutes>0.0) {
            imgTag = "\t\t\t\t\t<img src=\"off.png\" width=\"$width%\" height=\"20\" title=\"absent(e) de $refTime à $begin\">\n";
            imgTag = imgTag.replace("$width", "" + (100.*delayMinutes/durationMaxMinutes));
            imgTag = imgTag.replace("$refTime", refTime.toString());
            imgTag = imgTag.replace("$begin", endTime.toString());
            html += imgTag;
        }
        return html;
    }
}
