package utils;

import java.util.List;

public interface MaxHeapBookADT<E extends Comparable<E>> {

    void buildHeap(List<E> items);   // build heap from a list

    E getMax();                      // return the max element

    boolean isEmpty();               // check if empty
}