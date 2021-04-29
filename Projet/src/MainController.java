import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController {
    public Label HelloWorld;

    public void sayHelloWorld(ActionEvent actionEvent) {

        boolean showName = true;
        boolean showId = false;
        boolean showTime = true;


        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        // process the file, and limit periods to given time interval
        var teamsProcessor = new TEAMSProcessor(selectedFile,"19/01/2021 à 10:15:00", "19/01/2021 à 11:45:00");
        teamsProcessor.setSorter(new SorterDuration());
        teamsProcessor.sort();
        teamsProcessor.setDisplayer(new DisplayerHTML());

        DataExtractor dataExtractor = null;

        if(showName){
            dataExtractor = new NameExtractor(dataExtractor);
        }
        if(showId){
            dataExtractor = new IdExtractor(dataExtractor);
        }
        if(showTime){
            dataExtractor = new TimeExtractor(dataExtractor);
        }

        People.dataExtractor = dataExtractor;

        teamsProcessor.display();

    }
}
