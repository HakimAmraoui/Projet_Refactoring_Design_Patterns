import javafx.scene.control.RadioButton;

public class SorterFactory {
    public static Sorter makeSorter(RadioButton buttonId, RadioButton buttonName, RadioButton buttonDuration) {
        if (buttonId.isSelected())
            return new SorterId();
        else if (buttonName.isSelected())
            return new SorterName();
        else
            return new SorterDuration();
    }
}
