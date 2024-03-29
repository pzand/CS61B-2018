package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 *
 * @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /**
     * Computes the hash function of the given key. Consists of
     * computing the hashcode, followed by modding by the number of buckets.
     * To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int hash = hash(key) % buckets.length;
        return buckets[hash].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int hash = hash(key) % buckets.length;
        int sizeOfBucket = buckets[hash].size();
        buckets[hash].put(key, value);
        size += buckets[hash].size() - sizeOfBucket;

        if (loadFactor() > MAX_LF) {
            reSize();
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
    }

    private void reSize() {
        ArrayMap<K, V>[] oldBuckets = buckets;
        buckets = new ArrayMap[buckets.length * 2];
        for (int i = 0; i < buckets.length; i += 1) {
            buckets[i] = new ArrayMap<>();
        }


        for (ArrayMap<K, V> arr : oldBuckets) {
            Set<K> keySet = arr.keySet();

            for (K key : keySet) {
                int hash = hash(key) % buckets.length;
                buckets[hash].put(key, arr.get(key));
            }
        }
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (ArrayMap<K, V> arr : buckets) {
            for (K key : arr) {
                set.add(key);
            }
        }
        return set;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int hash = hash(key) % buckets.length;
        V value = buckets[hash].remove(key);
        if (value != null) {
            this.size--;
        }
        return value;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int hash = hash(key) % buckets.length;
        V val = buckets[hash].remove(key, value);
        if (val != null) {
            this.size--;
        }
        return val;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
//
//    private class iteratorHelper implements Iterator<K> {
//        private int index;
//        private Iterator<K> iter;
//
//        public iteratorHelper() {
//            for (int i = 0;i < buckets.length;i++) {
//                if (buckets[i].size() > 0){
//                    index = i;
//                    iter = iterator();
//                    break;
//                }
//            }
//        }
//
//        @Override
//        public boolean hasNext() {
//            if ()
//        }
//
//        @Override
//        public K next() {
//
//        }
//    }
}
