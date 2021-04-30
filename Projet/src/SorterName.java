import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SorterName extends Sorter {

    @Override
    public void sort(List<People> peopleToSort) {
        Collections.sort(peopleToSort, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
    };
}
