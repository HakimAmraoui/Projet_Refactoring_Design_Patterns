import java.util.HashMap;
import java.util.List;

public abstract class Extractor {
    protected Extractor extractor;
    
    public Extractor(Extractor newExtractor) {
        extractor = newExtractor;
    }

    public abstract List<String> getData();
}