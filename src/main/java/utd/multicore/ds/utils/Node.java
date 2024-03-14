package utd.multicore.ds.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node<T> {
    public final T item;
    public Node<T> next;
    public final Lock lock = new ReentrantLock();

    public Node(T item) {
        this.item = item;
        this.next = null;
    }

    public Node(T item, Node<T> next) {
        this(item);
        this.next = next;
    }
}
