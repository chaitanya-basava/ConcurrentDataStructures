package utd.multicore.ds;

public interface DataStructure<T extends Comparable<T>> {
    boolean search(T k);
    boolean add(T k);
    boolean remove(T k);
    T push(T k);
    T pop();
}
