package utd.multicore.ds;

import utd.multicore.ds.utils.Node;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentStack<T extends Comparable<T>> implements DataStructure<T> {
    private final AtomicReference<Node<T>> top = new AtomicReference<>();

    public T push(T k) {
        Node<T> newHead = new Node<>(k);
        Node<T> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
        return k;
    }

    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }

    public boolean search(T k) {
        return false;
    }

    public boolean add(T k) {
        return false;
    }

    public boolean remove(T k) {
        return false;
    }

    @Override
    public String toString() {
        Node<T> current = top.get();
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            sb.append(current.item).append(", ");
            current = current.next;
        }
        return sb.toString();
    }
}
