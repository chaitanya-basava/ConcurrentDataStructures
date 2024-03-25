package utd.multicore.ds;

public abstract class DataStructure<T extends Comparable<T>> {
    protected int size = 0;
    protected int numAdds = 0;
    protected int numDeletes = 0;
    protected int numSearches = 0;

    public abstract void search(T k);
    public abstract void add(T k);
    public abstract void remove(T k);
    public abstract void push(T k);
    public abstract void pop();

    public int getNumAdds() {
        return numAdds;
    }

    public int getNumDeletes() {
        return numDeletes;
    }

    public int getNumSearches() {
        return numSearches;
    }

    public int getSize() {
        return size;
    }
}
