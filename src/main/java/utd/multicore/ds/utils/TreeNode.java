package utd.multicore.ds.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TreeNode<T> {
    public final T item;
    public TreeNode<T> left, right;
    public final Lock lock = new ReentrantLock();

    public TreeNode(T item) {
        this.item = item;
        this.left = null;
        this.right = null;
    }

    public TreeNode(T item, TreeNode<T> left, TreeNode<T> right) {
        this(item);
        this.left = left;
        this.right = right;
    }
}
