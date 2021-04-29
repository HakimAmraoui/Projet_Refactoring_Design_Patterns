import java.util.HashMap;

public class ExtractorName extends Extractor {
    public ExtractorName(Extractor newExtractor) {
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
        map.put("name", people.getName());
        return map;
    }
}
