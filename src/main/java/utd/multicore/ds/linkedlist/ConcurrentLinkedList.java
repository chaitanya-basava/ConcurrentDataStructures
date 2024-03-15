package utd.multicore.ds.linkedlist;

import utd.multicore.ds.utils.Node;

import java.util.concurrent.locks.ReentrantReadWriteLock;

// lock the entire LL based on read or write op
public class ConcurrentLinkedList<T extends Comparable<T>> implements LinkedList<T> {
    private final Node<T> head = new Node<>(null);
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public boolean search(T k) {
        readLock.lock();
        try {
            Node<T> curr = head.next;
            while (curr != null) {
                if (curr.item.equals(k)) return true;
                curr = curr.next;
            }
            return false;
        } finally {
            readLock.unlock();
        }
    }

    public boolean add(T k) {
        writeLock.lock();
        try {
            Node<T> prev = head;
            Node<T> curr = head.next;
            while (curr != null && curr.item.compareTo(k) < 0) {
                prev = curr;
                curr = curr.next;
            }
            if (curr != null && curr.item.equals(k)) return false;
            prev.next = new Node<>(k, curr);
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean remove(T k) {
        writeLock.lock();
        try {
            Node<T> prev = head;
            Node<T> curr = head.next;
            while (curr != null && curr.item.compareTo(k) < 0) {
                prev = curr;
                curr = curr.next;
            }
            if (curr == null || !curr.item.equals(k)) return false;
            prev.next = curr.next;
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String toString() {
        return LinkedList.super.toString(head);
    }
}

