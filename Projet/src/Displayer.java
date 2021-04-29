import java.util.Collection;

public abstract class Displayer {

    protected String outputDir;
    protected String fileName;

    public Displayer(String outputDir, String fileName) {
        this.outputDir = outputDir;
        this.fileName = fileName;
    }

    public abstract void display(Collection<People> _allpeople, String _fileName, String _courseName, String _startTime, String _endTime);
}
