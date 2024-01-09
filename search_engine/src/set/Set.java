package set;

import java.util.ArrayList;
import java.util.Iterator;

public class Set<T> implements Iterable<T> {
    private boolean visit = false;

    public boolean isVisit() {
        return visit;
    }

    public void setVisit(boolean visit) {
        this.visit = visit;
    }

    private ArrayList<T> set;

    public Set() {
        set = new ArrayList<>();
    }

    public boolean add(T item) {
        if (!set.contains(item)) {
            set.add(item);
            return true;
        }
        return false;
    }

    public boolean remove(T item) {
        return set.remove(item);
    }

    public boolean contains(T item) {
        return set.contains(item);
    }

    public int size() {
        return set.size();
    }

    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set<?> set1 = (Set<?>) o;
        return set.equals(set1.set);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            result.append(set.get(i).toString()).append(" ");
        }
        return result.toString();
    }

    public ArrayList<T> getSet() {
        return set;
    }

    public void setSet(ArrayList<T> set) {
        this.set = set;
    }
}
