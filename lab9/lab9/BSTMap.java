package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    public Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */
    private V storeDeleteNode; /* saving the node of delete*/

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }

        int compareResult = key.compareTo(p.key);
        if (compareResult < 0) {
            return getHelper(key, p.left);
        } else if (compareResult > 0) {
            return getHelper(key, p.right);
        }
        return p.value;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }

        return getHelper(key, root);
    }


    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            this.size++;
            return new Node(key, value);
        }

        int compareResult = key.compareTo(p.key);
        if (compareResult < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (compareResult > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }

        return p;
    }

    /**
     * Inserts the key KEY
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            return;
        }

        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keySetHelp(new HashSet<>(), root);
    }

    private Set<K> keySetHelp(Set<K> set, Node p) {
        if (p == null) {
            return null;
        }

        keySetHelp(set, p.left);
        set.add(p.key);
        keySetHelp(set, p.right);
        return set;
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        this.storeDeleteNode = null;

        root = removeHelper(key, null, root);
        return storeDeleteNode;
    }

    /**
     * remove key from the tree if present
     * returns a tree that have been removed the node of needing
     */
    private Node removeHelper(K key, V value, Node p) {
        if (p == null) {
            return null;
        }

        int compareResult = key.compareTo(p.key);
        if (compareResult < 0) {
            p.left = removeHelper(key, value, p.left);
            return p;
        } else if (compareResult > 0) {
            p.right = removeHelper(key, value, p.right);
            return p;
        }

        /* Removes the key-value entry for the specified key only if it is
         *  if it is not, return nodes*/
        if (value != null && !value.equals(p.value)) {
            return p;
        }

        // key equal p.key
        storeDeleteNode = p.value;
        // only have a child tree
        if (p.left == null) {
            return p.right;
        }
        if (p.right == null) {
            return p.left;
        }

        // have two child tree
        Node n = deleteMaxNode(p.left);
        n.left = removeHelper(n.key, null, p.left);
        n.right = p.right;
        return n;
    }

    /* delete the max node */
    private Node deleteMaxNode(Node p) {
        if (p.right == null) {
            return p;
        }
        return deleteMaxNode(p.right);
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            return null;
        }
        this.storeDeleteNode = null;

        root = removeHelper(key, value, root);
        return storeDeleteNode;
    }

    @Override
    public Iterator<K> iterator() {
        return new iteratorHelp();
    }

    // 层次遍历
    private class iteratorHelp implements Iterator<K> {
        LinkedList<Node> list;

        public iteratorHelp() {
            this.list = new LinkedList<>();
            if (root != null) {
                list.add(root);
            }
        }

        @Override
        public boolean hasNext() {
            return list.size() != 0;
        }

        @Override
        public K next() {
            Node node = list.removeFirst();
            if (node.left != null) {
                list.add(node.left);
            }
            if (node.right != null) {
                list.add(node.right);
            }
            return node.key;
        }
    }
}
