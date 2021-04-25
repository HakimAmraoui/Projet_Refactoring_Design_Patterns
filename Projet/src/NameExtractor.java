public class NameExtractor extends  DataExtractor{

    public NameExtractor(DataExtractor dataExtractor){
        super(dataExtractor);
    }

    @Override
    public String getHTMLCode(People people) {
        String html="";
        html += "<div class=\"name\"> " + people.getName() + " </div> \n";

        if(this.dataExtractor != null){
            return html + this.dataExtractor.getHTMLCode(people);
        }

        return html;
    }
}
