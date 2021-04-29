import java.util.HashMap;

public class ExtractorId extends Extractor {

    public ExtractorId(Extractor newExtractor) {
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
        map.put("id", people.get_id());
        return map;
    }
    
}
