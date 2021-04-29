import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtractorId extends Extractor {

    public ExtractorId(Extractor newExtractor) {
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
        data.add("id");
        return data;
    }
    
}
