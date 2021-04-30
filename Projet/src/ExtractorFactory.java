import javafx.scene.control.CheckBox;

public class ExtractorFactory {
    public static Extractor makeExtractor(CheckBox checkboxId, CheckBox checkboxName, CheckBox checkboxTime) {
        Extractor extractor = null;

        if (!checkboxId.isSelected()) {
            extractor = new ExtractorId(extractor);
        }
        if (!checkboxName.isSelected()) {
            extractor = new ExtractorName(extractor);
        }
        if (!checkboxTime.isSelected()) {
            extractor = new ExtractorTime(extractor);
        }

        return extractor;
    }
}
