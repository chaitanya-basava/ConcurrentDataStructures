package utd.multicore.ds;

import java.util.Random;

public abstract class DataStructure<T extends Comparable<T>> {
    protected int size = 0;
    protected int numAdds = 0;
    protected int numDeletes = 0;
    protected int numSearches = 0;

    public abstract boolean search(T k);
    public abstract boolean add(T k);
    public abstract boolean remove(T k);
    public abstract void push(T k);
    public abstract T pop();

    public void warmup(Class<T> clazz, int bound) {
        Random random = new Random();
        T value;

        for (int i = 0; i < bound / 2; i++) {
            if (clazz == Integer.class) {
                value = clazz.cast(random.nextInt(bound));
                if(!this.add(value)) i--;
            }
        }
        this.numAdds = 0;
    }

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
