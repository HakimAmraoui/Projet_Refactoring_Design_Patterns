import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtractorName extends Extractor {
    public ExtractorName(Extractor newExtractor) {
        super(newExtractor);
    }

    @Override
    public List<String> getData() {
        List<String> data;
        if (extractor == null) {
            data = new ArrayList<String>();
        } else {
            data = extractor.getData();
        }
        data.add("name");
        return data;
    }
}
