import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SorterDuration extends Sorter {

    @Override
    public void sort(List<People> peopleToSort) {
        Collections.sort(peopleToSort, (p1, p2) -> (int)(p1.getTotalAttendanceDuration() - p2.getTotalAttendanceDuration()));
    };
}
