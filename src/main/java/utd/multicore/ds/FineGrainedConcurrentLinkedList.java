package utd.multicore.ds;

import utd.multicore.ds.utils.Node;

public class FineGrainedConcurrentLinkedList<T extends Comparable<T>> {
    private final Node<T> head = new Node<>(null);

    public boolean add(T item) {
        Node<T> pred = head, curr;
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
                    pred.next = new Node<>(item, curr);
                    return true;
                }
                return false; // Item already exists
            } finally {
                if (curr != null) curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean remove(T item) {
        Node<T> pred = head, curr;
        pred.lock.lock();
        try {
            curr = pred.next;
            if (curr != null) curr.lock.lock();
            try {
                while (curr != null && curr.item.compareTo(item) < 0) {
                    pred.lock.unlock(); // Unlock predecessor
                    pred = curr;
                    curr = curr.next;
                    if (curr != null) curr.lock.lock();
                }
                if (curr != null && curr.item.equals(item)) {
                    pred.next = curr.next; // Remove current node
                    return true;
                }
                return false; // Item not found
            } finally {
                if (curr != null) curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean search(T item) {
        Node<T> pred = head, curr;
        pred.lock.lock();
        try {
            curr = pred.next;
            if (curr != null) curr.lock.lock();
            try {
                while (curr != null) {
                    if (curr.item.equals(item)) return true; // Item found
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    if (curr != null) curr.lock.lock();
                }
                return false; // Item not found
            } finally {
                if (curr != null) curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }
}
