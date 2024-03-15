package utd.multicore.ds.linkedlist;

import utd.multicore.ds.DataStructure;
import utd.multicore.ds.utils.Node;

public interface LinkedList<T extends Comparable<T>> extends DataStructure<T> {
    default T push(T k) {
        return null;
    }

    default T pop() {
        return null;
    }

    default String toString(Node<T> head) {
        Node<T> current = head.next;
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            sb.append(current.item).append(", ");
            current = current.next;
        }
        return sb.toString();
    }
}
