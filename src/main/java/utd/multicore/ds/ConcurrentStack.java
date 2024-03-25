package utd.multicore.ds;

import utd.multicore.ds.utils.Node;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentStack<T extends Comparable<T>> extends DataStructure<T> {
    private final AtomicReference<Node<T>> top = new AtomicReference<>();
    private final AtomicInteger size = new AtomicInteger(0);

    public void push(T k) {
        Node<T> newHead = new Node<>(k);
        Node<T> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
        this.numAdds++;
        this.size.incrementAndGet();
    }

    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null) {
                this.numDeletes++;
                return null;
            }
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        this.numDeletes++;
        this.size.decrementAndGet();
        return oldHead.item;
    }

    @Override
    public void warmup(Class<T> clazz, int bound) {
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
        int size = 0;
        Node<T> current = top.get();
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            size++;
            sb.append(current.item).append(", ");
            current = current.next;
        }
        return "[" + size + "] " + sb;
    }

    @Override
    public int getSize() {
        return this.size.get();
    }
}
