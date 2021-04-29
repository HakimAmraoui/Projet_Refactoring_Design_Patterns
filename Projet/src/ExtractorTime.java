import java.util.HashMap;

public class ExtractorTime extends Extractor {
    public ExtractorTime(Extractor newExtractor) {
        super(newExtractor);
    }

    @Override
    public HashMap<String, String> getData(People people) {
        HashMap<String, String> map;
        if (extractor == null) {
            map = new HashMap<String, String>();
        } else {
            map = extractor.getData(people);
        }
        map.put("time", "true");
        return map;
    }
}
