import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TEAMSProcessor {

    private Collection<People> _allpeople = null;
    private String _fileName;
    private String _courseName;
    private String _startTime;
    private String _endTime;
    private Displayer displayer;
    private Sorter sorter;

    public TEAMSProcessor(File _file, String courseName, String _start, String _stop) {
        this._courseName = courseName;

        /*
         csv file to read
         start time of the course
         end time of the source
        */
        this._startTime = _start;
        this._endTime = _stop;

        // load CSV file
        this._fileName = _file.getName();
        var teamsFile = new TEAMSAttendanceList(_file);

        // filter to extract data for each people
        var lines = teamsFile.get_attlist();
        if (lines != null) {
            // convert lines in data structure with people & periods
            var filter = new TEAMSAttendanceListAnalyzer(lines);
            // cut periods before start time and after end time
            filter.setStartAndStop(_start, _stop);
            // init the people collection
            this._allpeople = new ArrayList<>(filter.get_peopleList().values());
        }
    }

    public void sort() {
        List<People> peopleToSort = (List<People>) this._allpeople;
        sorter.sort(peopleToSort);
        this._allpeople = peopleToSort;
    }

    public Collection<People> get_allpeople() {
        return _allpeople;
    }

    public String display() {
        return displayer.display(_allpeople, _fileName, _courseName, _startTime, _endTime);
    }

    public void setDisplayer(Displayer displayer) {
        this.displayer = displayer;
    }

    public void setSorter(Sorter sorter) {
        this.sorter = sorter;
    }
}
