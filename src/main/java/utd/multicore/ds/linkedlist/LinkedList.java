package utd.multicore.ds.linkedlist;

import utd.multicore.ds.DataStructure;
import utd.multicore.ds.utils.Node;

import java.util.Random;

public abstract class LinkedList<T extends Comparable<T>> extends DataStructure<T> {
    public void push(T k) {
    }

    public void pop() {
    }

    public void warmup(Class<T> clazz, int bound) {
        Random random = new Random();
        T value;

        for (int i = 0; i < bound / 2; i++) {
            if (clazz == Integer.class) {
                value = clazz.cast(random.nextInt(bound));
                this.add(value);
                if(this.size != i + 1) i--;
            }
        }
        this.numAdds = 0;
    }

    String toString(Node<T> head) {
        int size = 0;
        Node<T> current = head.next;
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            size++;
            sb.append(current.item).append(", ");
            current = current.next;
        }
        return "[" + size + "] " + sb;
    }
}
