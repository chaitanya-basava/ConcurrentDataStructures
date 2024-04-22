package utd.multicore.ds.bst;

import utd.multicore.ds.DataStructure;
import utd.multicore.ds.utils.LazyTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LazyLockingExternalBST<T extends Comparable<T>> extends DataStructure<T> {
    private class Window {
        public final LazyTreeNode<T> grandParent, parent, current;

        private Window(LazyTreeNode<T> grandParent, LazyTreeNode<T> parent, LazyTreeNode<T> current) {
            this.grandParent = grandParent;
            this.parent = parent;
            this.current = current;
        }
    }

    private final AtomicInteger size = new AtomicInteger(0);
    public LazyTreeNode<T> grandParent = null, parent = null, current = null;

    private Window findNodeAndParent(T key) {
        LazyTreeNode<T> current = this.current;
        LazyTreeNode<T> parent = this.parent;
        LazyTreeNode<T> grandParent = this.grandParent;
        while(!current.isLeaf) {
            grandParent = parent;
            parent = current;
            current = (key.compareTo(current.item)) < 0 ? current.left : current.right;
        }
        return new Window(grandParent, parent, current);
    }

    @Override
    public boolean search(T k) {
        Window findResult = findNodeAndParent(k);
        return !findResult.current.marked && findResult.current.item .compareTo(k) == 0;
    }

    @Override
    public boolean add(T k) {
        Window findResult = findNodeAndParent(k);

        if(findResult.current.item.compareTo(k) == 0) {
            return false;
        }

        boolean isLeft = k.compareTo(findResult.current.item) < 0;

        if(isLeft) {
            findResult.current.isLeaf = false;
            findResult.current.left = new LazyTreeNode<>(k);
            findResult.current.right = new LazyTreeNode<>(findResult.current.item);
        } else {
            LazyTreeNode<T> newChild = new LazyTreeNode<>(k, findResult.current, new LazyTreeNode<>(k));
            if(findResult.parent.left == findResult.current) {
                findResult.parent.left = newChild;
            } else {
                findResult.parent.right = newChild;
            }
        }
        this.size.incrementAndGet();

        return true;
    }

    @Override
    public boolean remove(T k) {
        Window findResult = findNodeAndParent(k);

        if(findResult.current.item.compareTo(k) != 0) return false;

        if(findResult.parent.right == findResult.current) {
            if(findResult.grandParent.left == findResult.parent) {
                findResult.grandParent.left = findResult.parent.left;
            } else {
                findResult.grandParent.right = findResult.parent.left;
            }
        } else {
            if(findResult.grandParent.left == findResult.parent) {
                findResult.grandParent.left = findResult.parent.right;
            } else {
                findResult.grandParent.right = findResult.parent.right;
            }
        }
        findResult.parent.left = null;
        findResult.parent.right = null;
        this.size.decrementAndGet();

        return true;
    }

    @Override
    public String toString() {
        List<String> values = new ArrayList<>();
        collectBST(grandParent, values);
        return "[" + (values.size() - 3) + "] " + values;
    }

    private void collectBST(LazyTreeNode<T> node, List<String> values) {
        if(node == null) return;
        collectBST(node.left, values);
        if(node.left == null && node.right == null) values.add(node.item + "(" + node.isLeaf + ")");
        collectBST(node.right, values);
    }

    @Override
    public int getSize() {
        return this.size.get();
    }

    @Override
    public void push(T k) {

    }

    @Override
    public T pop() {
        return null;
    }
}
