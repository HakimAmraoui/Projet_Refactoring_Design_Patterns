import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SorterId extends Sorter {

    @Override
    public void sort(List<People> peopleToSort) {
        Collections.sort(peopleToSort, new Comparator<People>() {
            @Override
            public int compare(People p1, People p2) {
                return (int)(p1.get_id().compareToIgnoreCase(p2.get_id()));
            }
        });
    };
}
