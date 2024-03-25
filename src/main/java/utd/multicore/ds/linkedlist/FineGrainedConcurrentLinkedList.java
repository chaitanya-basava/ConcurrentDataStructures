package utd.multicore.ds.linkedlist;

import utd.multicore.ds.utils.Node;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FineGrainedConcurrentLinkedList<T extends Comparable<T>> extends LinkedList<T> {
    private final Node<T> head = new Node<>(null);
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public boolean add(T item) {
        Node<T> pred = head;
        Node<T> curr;
        pred.lock.lock();
        try {
            curr = pred.next;
            if (curr != null) curr.lock.lock();
            try {
                while (curr != null && curr.item.compareTo(item) < 0) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    if (curr != null) curr.lock.lock();
                }
                if (curr == null || !curr.item.equals(item)) {
                    rwLock.writeLock().lock();
                    this.size++;
                    rwLock.writeLock().unlock();
                    pred.next = new Node<>(item, curr);
                    return true;
                }
                return false;
            } finally {
                this.numAdds++;
                if (curr != null) curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean remove(T item) {
        Node<T> pred = head;
        Node<T> curr;
        pred.lock.lock();
        try {
            curr = pred.next;
            if (curr != null) curr.lock.lock();
            try {
                while (curr != null && curr.item.compareTo(item) < 0) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    if (curr != null) curr.lock.lock();
                }
                if (curr != null && curr.item.equals(item)) {
                    rwLock.writeLock().lock();
                    this.size--;
                    rwLock.writeLock().unlock();
                    pred.next = curr.next;
                    return true;
                }
                return false;
            } finally {
                this.numDeletes++;
                if (curr != null) curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean search(T item) {
        Node<T> pred = head;
        Node<T> curr;
        pred.lock.lock();
        try {
            curr = pred.next;
            if (curr != null) curr.lock.lock();
            try {
                while (curr != null) {
                    if (curr.item.equals(item)) return true;
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    if (curr != null) curr.lock.lock();
                }
                return false;
            } finally {
                this.numSearches++;
                if (curr != null) curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    @Override
    public String toString() {
        return super.toString(head);
    }
}
