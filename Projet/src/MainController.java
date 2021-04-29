import java.io.File;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

public class MainController {
    // public Label HelloWorld;

    public void sayHelloWorld(ActionEvent actionEvent) {

        boolean showName = false;
        boolean showId = true;
        boolean showTime = true;


        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        // process the file, and limit periods to given time interval
        var teamsProcessor = new TEAMSProcessor(selectedFile,"19/01/2021 à 10:15:00", "19/01/2021 à 11:45:00");
        teamsProcessor.setSorter(new SorterDuration());
        teamsProcessor.sort();
        teamsProcessor.setDisplayer(new DisplayerHTML());

        Extractor extractor = null;

        if(showName){
            extractor = new ExtractorName(extractor);
        }
        if(showId){
            extractor = new ExtractorId(extractor);
        }
        if(showTime){
            extractor = new ExtractorTime(extractor);
        }

        People.setExtractor(extractor);

        teamsProcessor.display();

    }
}
