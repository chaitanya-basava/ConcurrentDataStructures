package utd.multicore.ds;

import utd.multicore.ds.utils.Node;

import java.util.concurrent.locks.ReentrantReadWriteLock;

// lock the entire LL based on read or write op
public class ConcurrentLinkedList<T extends Comparable<T>> {
    private Node<T> head = null;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public boolean search(T k) {
        readLock.lock();
        try {
            Node<T> current = head;
            while (current != null) {
                if (current.item.equals(k)) return true;
                current = current.next;
            }
            return false;
        } finally {
            readLock.unlock();
        }
    }

    public boolean add(T k) {
        writeLock.lock();
        try {
            if (head == null || head.item.compareTo(k) > 0) {
                head = new Node<>(k);
                head.next = null;
                return true;
            }
            Node<T> current = head;
            while (current.next != null && current.next.item.compareTo(k) < 0) current = current.next;
            if (current.next != null && current.next.item.equals(k)) return false; // Key already exists
            current.next = new Node<>(k, current.next);
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean remove(int k) {
        writeLock.lock();
        try {
            if (head == null) return false;
            if (head.item.equals(k)) {
                head = head.next;
                return true;
            }
            Node<T> current = head;
            while (current.next != null && !current.next.item.equals(k)) current = current.next;
            if (current.next == null) return false; // Key not found
            current.next = current.next.next;
            return true;
        } finally {
            writeLock.unlock();
        }
    }
}

