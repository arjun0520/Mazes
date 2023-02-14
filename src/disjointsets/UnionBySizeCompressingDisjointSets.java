package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> tracker;
    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        tracker = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        tracker.put(item, pointers.size());
        pointers.add(pointers.size(), -1);
    }

    @Override
    public int findSet(T item) {
        if (!tracker.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int pointerValue = pointers.get(tracker.get(item));
        int index = tracker.get(item);
        while (pointerValue >= 0) {
            index = pointerValue;
            pointerValue = pointers.get(pointerValue);
        }
        pointerValue = (tracker.get(item));
        while (pointerValue != index) {
            pointers.set(pointerValue, index);
            pointerValue = pointers.get(pointerValue);
        }
        return index;
    }

    @Override
    public boolean union(T item1, T item2) {
        int index1 = findSet(item1);
        int index2 = findSet(item2);
        if (index1 != index2) {
            int weight1 = pointers.get(index1);
            int weight2 = pointers.get(index2);
            if (Math.abs(weight1) > Math.abs(weight2)) {
                pointers.set(index2, index1);
                pointers.set(index1, weight1 + weight2);
            } else {
                pointers.set(index1, index2);
                pointers.set(index2, weight1 + weight2);
            }
            return true;
        }
        return false;
    }
}
