package utd.multicore.ds.linkedlist;

import utd.multicore.ds.utils.Node;

public class FineGrainedConcurrentLinkedList<T extends Comparable<T>> extends LinkedList<T> {
    private final Node<T> head = new Node<>(null);

    public void add(T item) {
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
                    this.size++;
                    pred.next = new Node<>(item, curr);
                }
            } finally {
                this.numAdds++;
                if (curr != null) curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public void remove(T item) {
        Node<T> pred = head;
        Node<T> curr;
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
                    this.size--;
                    pred.next = curr.next;
                }
            } finally {
                this.numDeletes++;
                if (curr != null) curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public void search(T item) {
        Node<T> pred = head;
        Node<T> curr;
        pred.lock.lock();
        try {
            curr = pred.next;
            if (curr != null) curr.lock.lock();
            try {
                while (curr != null) {
                    if (curr.item.equals(item)) return;
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    if (curr != null) curr.lock.lock();
                }
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
