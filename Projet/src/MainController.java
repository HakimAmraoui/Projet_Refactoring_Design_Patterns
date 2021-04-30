import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class MainController {

    @FXML
    private StackPane rectDrag;

    @FXML
    private Text labelDragAndDrop;

    @FXML
    private Label labelTitle, labelDate, labelTimeMin, labelTimeMax, labelSelectedFolder;

    @FXML
    private TextField textFieldCourseName, textFieldTimeStart, textFieldTimeEnd, textFieldOutputFileName;

    @FXML
    private RadioButton buttonHTML, buttonExcel, buttonId, buttonName, buttonDuration;

    @FXML
    private CheckBox checkboxId, checkboxName, checkboxTime;

    private File sFile;

    private LocalDateTime timeMinDateTime, timeMaxDateTime;

    private String date, timeMin, timeMax;

    private String sDirectory = "output/";

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void dragAndDrop() {
        rectDrag.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        rectDrag.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            Boolean success = false;
            String fileName;
            if (db.hasFiles()) {
                fileName = db.getFiles().get(0).getName();
                if (fileName.endsWith(".csv")) {
                    sFile = db.getFiles().get(0);
                    var teamsFile = new TEAMSAttendanceList(sFile);
                    var lines = teamsFile.get_attlist();
                    if (lines != null) {
                        var filter = new TEAMSAttendanceListAnalyzer(lines);
                        ArrayList<People> peopleList = new ArrayList<>(filter.get_peopleList().values());
                        timeMinDateTime = peopleList.get(0).get_periodList().getFirst().get_start();
                        timeMaxDateTime = peopleList.get(0).get_periodList().getFirst().get_end();
                        Iterator<People> peopleIterator = peopleList.iterator();
                        while (peopleIterator.hasNext()) {
                            People people = peopleIterator.next();
                            Iterator<TEAMSPeriod> periodIterator = people.get_periodList().iterator();
                            while (periodIterator.hasNext()) {
                                TEAMSPeriod period = periodIterator.next();
                                LocalDateTime start = period.get_start();
                                if (start != null && start.isBefore(timeMinDateTime)) {
                                    timeMinDateTime = start;
                                }
                                LocalDateTime end = period.get_end();
                                if (end != null && end.isAfter(timeMaxDateTime)) {
                                    timeMaxDateTime = end;
                                }
                            }
                        }
                        if (!timeMinDateTime.toLocalDate().isEqual(timeMaxDateTime.toLocalDate())) {
                            alertError("Chevauchement des dates",
                                    "Il existe un chevauchement de dates dans ce fichier!");
                        } else {
                            labelDragAndDrop.setText("Fichier déposé");

                            labelTitle.setText(sFile.getName());

                            date = timeMinDateTime.format(dateFormatter);
                            labelDate.setText(date);

                            timeMin = timeMinDateTime.format(timeFormatter);
                            timeMax = timeMaxDateTime.format(timeFormatter);
                            labelTimeMin.setText(timeMin);
                            labelTimeMax.setText(timeMax);

                            textFieldCourseName.setText(fileName.replace(".csv", ""));
                            textFieldTimeStart.setText(timeMin);
                            textFieldTimeEnd.setText(timeMax);
                            textFieldOutputFileName.setText(fileName.replace(".csv", ""));

                            success = true;
                        }
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void setOutputDirectory() {
        DirectoryChooser dc = new DirectoryChooser();
        File f = dc.showDialog(null);
        String s = f.getAbsolutePath();
        s = s.replace("\\", "/");
        labelSelectedFolder.setText(s);
        sDirectory = s;
    }

    public void processData() {
        if (sFile == null) {
            alertError("Aucun fichier sélectionné", "Vous devez sélectionner un fichier à analyser.");
        }

        String courseName = textFieldCourseName.getText();
        if (courseName.isBlank())
            alertError("Libellé vide", "Le libellé ne peut pas être vide.");

        Boolean invalidTimeInput = false;
        String timeRegex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$";
        String fullTimeRegex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
        String inputTimeMin = textFieldTimeStart.getText();
        Boolean boolTimeMin = !inputTimeMin.matches(timeRegex);
        Boolean boolFullTimeMin = !inputTimeMin.matches(fullTimeRegex);
        String inputTimeMax = textFieldTimeEnd.getText();
        Boolean boolTimeMax = !inputTimeMax.matches(timeRegex);
        Boolean boolFullTimeMax = !inputTimeMax.matches(fullTimeRegex);
        if (boolFullTimeMin && !boolTimeMin)
            inputTimeMin += ":00";
        if (boolFullTimeMax && !boolTimeMax)
            inputTimeMax += ":00";
        if (boolTimeMin || boolTimeMax)
            invalidTimeInput = true;
        LocalTime inputDateTimeMin = null;
        LocalTime inputDateTimeMax = null;
        if (!invalidTimeInput) {
            inputDateTimeMin = LocalTime.parse(inputTimeMin, timeFormatter);
            inputDateTimeMax = LocalTime.parse(inputTimeMax, timeFormatter);
            if (inputDateTimeMax.isBefore(inputDateTimeMin))
                invalidTimeInput = true;
        }
        if (invalidTimeInput)
            alertError("Heures invalides", "Les heures que vous avez saisies ne sont pas valides!");

        String inputFileName = textFieldOutputFileName.getText();
        if (inputFileName.isBlank()) {
            inputFileName = sFile.getName().replace(".csv", "");
        }

        String from = date + " à " + inputTimeMin;
        String to = date + " à " + inputTimeMax;
        var teamsProcessor = new TEAMSProcessor(sFile, courseName, from, to);

        Sorter sorter = SorterFactory.makeSorter(buttonId, buttonName, buttonDuration);
        TEAMSProcessor.setSorter(sorter);
        teamsProcessor.sort();

        Displayer displayer = DisplayerFactory.makeDisplayer(buttonHTML, buttonExcel, sDirectory, inputFileName);
        TEAMSProcessor.setDisplayer(displayer);

        Extractor extractor = ExtractorFactory.makeExtractor(checkboxId, checkboxName, checkboxTime);
        TEAMSProcessor.setExtractor(extractor);

        teamsProcessor.display();
    }

    private void alertError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
