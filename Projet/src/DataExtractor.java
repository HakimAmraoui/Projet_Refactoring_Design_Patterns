public abstract class DataExtractor {

    DataExtractor dataExtractor;

    public DataExtractor(DataExtractor dataExtractor){
        this.dataExtractor = dataExtractor;
    }

    public abstract String getHTMLCode(People people);
}
