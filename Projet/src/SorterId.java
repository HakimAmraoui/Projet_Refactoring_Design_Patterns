import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SorterId extends Sorter {

    @Override
    public void sort(List<People> peopleToSort) {
        Collections.sort(peopleToSort, (p1, p2) -> p1.get_id().compareToIgnoreCase(p2.get_id()));
    };
}
