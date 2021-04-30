import javafx.scene.control.RadioButton;

public class DisplayerFactory {
    public static Displayer makeDisplayer(RadioButton buttonHTML, RadioButton buttonExcel, String sDirectory, String inputFileName) {
        if (buttonExcel.isSelected())
            return new DisplayerExcel(sDirectory, inputFileName);
        else
            return new DisplayerHTML(sDirectory, inputFileName);
    }
}
