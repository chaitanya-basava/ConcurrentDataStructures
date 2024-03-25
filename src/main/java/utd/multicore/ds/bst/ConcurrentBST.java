package utd.multicore.ds.bst;

import utd.multicore.ds.DataStructure;
import utd.multicore.ds.utils.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentBST<T extends Comparable<T>> extends DataStructure<T> {
    private volatile TreeNode<T> root = null;
    private final AtomicInteger size = new AtomicInteger(0);

    @Override
    public void push(T k) {
    }

    @Override
    public T pop() {
        return null;
    }

    @Override
    public boolean search(T k) {
        if(root == null) {
            return false;
        }
        TreeNode<T> current = root;
        current.lock.lock();
        try {
            while (current != null) {
                int cmp = k.compareTo(current.item);
                if(cmp == 0) {
                    return true;
                }
                TreeNode<T> next = (cmp < 0) ? current.left : current.right;
                if(next != null) {
                    next.lock.lock();
                }
                current.lock.unlock();
                current = next;
            }
        } finally {
            this.numSearches++;
            if(current != null) current.lock.unlock();
        }
        return false;
    }

    @Override
    public boolean add(T k) {
        if(root == null) {
            synchronized (this) {
                if(root == null) {
                    root = new TreeNode<>(k);
                    this.size.incrementAndGet();
                    return true;
                }
            }
        }
        TreeNode<T> parent = null;
        TreeNode<T> current = root;
        current.lock.lock();
        try {
            while (current != null) {
                parent = current;
                int cmp = k.compareTo(current.item);
                if(cmp == 0) return false; // Key already exists.
                if(cmp < 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
                if(current != null) {
                    current.lock.lock();
                    parent.lock.unlock();
                }
            }
            TreeNode<T> newNode = new TreeNode<>(k);
            if(k.compareTo(parent.item) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
            this.size.incrementAndGet();
        } finally {
            this.numAdds++;
            parent.lock.unlock();
        }
        return true;
    }

    @Override
    public boolean remove(T k) {
        TreeNode<T> current = root;
        TreeNode<T> parent = null;
        current.lock.lock();
        try {
            while (current != null && !current.item.equals(k)) {
                if(parent != null) parent.lock.unlock();
                parent = current;
                current = (k.compareTo(current.item) < 0) ? current.left : current.right;
                if(current != null) current.lock.lock();
            }
            if(current == null) return false; // Key not found

            // Node to be deleted is found
            if(current.left == null || current.right == null) {
                // Node with only one child or no child
                TreeNode<T> child = (current.left != null) ? current.left : current.right;
                if(parent == null) {
                    root = child;
                } else if(parent.left == current) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
            } else {
                // Node with two children
                // Find successor
                TreeNode<T> successor = current.right;
                TreeNode<T> successorParent = current;
                successor.lock.lock();
                try {
                    while (successor.left != null) {
                        if(successorParent != current) successorParent.lock.unlock();
                        successorParent = successor;
                        successor = successor.left;
                        successor.lock.lock();
                    }
                    current.item = successor.item;
                    if(successorParent.left == successor) {
                        successorParent.left = successor.right;
                    } else {
                        successorParent.right = successor.right;
                    }
                } finally {
                    successor.lock.unlock();
                    if(successorParent != current) {
                        successorParent.lock.unlock();
                    }
                }
            }
            this.size.decrementAndGet();
            return true;
        } finally {
            this.numDeletes++;
            if(current != null) current.lock.unlock();
            if(parent != null && parent != current) parent.lock.unlock();
        }
    }

    @Override
    public String toString() {
        List<T> values = new ArrayList<>();
        collectBST(root, values);
        return "[" + values.size() + "] " + values;
    }

    private void collectBST(TreeNode<T> node, List<T> values) {
        if(node == null) return;
        collectBST(node.left, values);
        values.add(node.item);
        collectBST(node.right, values);
    }

    @Override
    public int getSize() {
        return this.size.get();
    }
}
