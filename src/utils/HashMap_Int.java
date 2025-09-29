package utils;

public interface HashMap_Int<K, V> {
    void put(K key, V value);       // insert or update
    V get(K key);                   // lookup
    boolean containsKey(K key);     // check if key exists
    V remove(K key);                // delete a key
    int size();                     // how many key-value pairs
    boolean isEmpty();              // quick check
}