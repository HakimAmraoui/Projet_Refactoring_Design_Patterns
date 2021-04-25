public class IdExtractor extends DataExtractor{

    public IdExtractor(DataExtractor dataExtractor) {
        super(dataExtractor);
    }

    @Override
    public String getHTMLCode(People people) {
        String html="";

        html += "<div class=\"id\"> " + people.get_id() + " </div> \n";

        if(this.dataExtractor != null){
            return html + this.dataExtractor.getHTMLCode(people);
        }

        return html;
    }
}
