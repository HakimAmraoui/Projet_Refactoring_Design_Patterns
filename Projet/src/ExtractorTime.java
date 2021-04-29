import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtractorTime extends Extractor {
    public ExtractorTime(Extractor newExtractor) {
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
        data.add("time");
        return data;
    }
}
