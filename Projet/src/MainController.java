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
        // FACTORY
        var teamsProcessor = new TEAMSProcessor(selectedFile,"19/01/2021 à 10:15:00", "19/01/2021 à 11:45:00", new DurationSorter());
        teamsProcessor.setDisplayer(new DisplayerHTML());
        teamsProcessor.setSorter(new IdSorter());
        teamsProcessor.sort();

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
/*
        var allpeople = teamsProcessor.get_allpeople();
        for (People people : allpeople) {
            System.out.println( people );
        }
*/

        System.out.println( teamsProcessor.display() );

    }
}
