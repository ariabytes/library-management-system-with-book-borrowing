package utils;

import java.util.LinkedList;
import java.util.Iterator;

public class HashMap_Imp<K, V> implements HashMap_Int<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<Entry<K, V>>[] buckets;
    private int capacity = 16;
    private int size = 0;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public HashMap_Imp() {
        buckets = new LinkedList[capacity];
    }

    private int getIndex(K key) {
        if (key == null) return 0; // Handle null keys
        return Math.abs(key.hashCode() % capacity);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }
        
        // Check if key already exists
        for (Entry<K, V> entry : buckets[index]) {
            if ((key == null && entry.key == null) || 
                (key != null && key.equals(entry.key))) {
                entry.value = value; // update existing
                return;
            }
        }
        
        // Add new entry
        buckets[index].add(new Entry<>(key, value));
        size++;
        
        // Resize if load factor exceeded
        if (size > capacity * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V get(K key) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            for (Entry<K, V> entry : buckets[index]) {
                if ((key == null && entry.key == null) || 
                    (key != null && key.equals(entry.key))) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            for (Entry<K, V> entry : buckets[index]) {
                if ((key == null && entry.key == null) || 
                    (key != null && key.equals(entry.key))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V remove(K key) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            Iterator<Entry<K, V>> iterator = buckets[index].iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if ((key == null && entry.key == null) || 
                    (key != null && key.equals(entry.key))) {
                    V value = entry.value;
                    iterator.remove(); // Safe removal during iteration
                    size--;
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedList<Entry<K, V>>[] oldBuckets = buckets;
        capacity *= 2;
        buckets = new LinkedList[capacity];
        size = 0;
        
        // Rehash all existing entries
        for (LinkedList<Entry<K, V>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }
}