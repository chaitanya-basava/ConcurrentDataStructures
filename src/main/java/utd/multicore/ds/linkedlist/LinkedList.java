package utd.multicore.ds.linkedlist;

import utd.multicore.ds.DataStructure;
import utd.multicore.ds.utils.Node;

public abstract class LinkedList<T extends Comparable<T>> extends DataStructure<T> {
    public void push(T k) {
    }

    public T pop() {
        return null;
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
