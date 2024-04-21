package utd.multicore.ds.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LazyTreeNode<T> {
    public T item;
    public LazyTreeNode<T> left, right;
    public final Lock lock = new ReentrantLock();

    public boolean marked = false;
    public boolean isLeaf = true;

    public LazyTreeNode(T item) {
        this.item = item;
        this.left = null;
        this.right = null;
    }

    public LazyTreeNode(T item, LazyTreeNode<T> left, LazyTreeNode<T> right) {
        this(item);
        this.left = left;
        this.right = right;
        this.isLeaf = left == null && right == null;
    }
}
