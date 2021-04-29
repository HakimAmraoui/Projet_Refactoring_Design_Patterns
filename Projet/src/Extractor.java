import java.util.HashMap;

public abstract class Extractor {
    protected Extractor extractor;
    
    public Extractor(Extractor newExtractor) {
        extractor = newExtractor;
    }

    public abstract HashMap<String, String> getData(People people);
}