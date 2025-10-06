package utils;

import java.util.LinkedList;
import java.util.List;

public class MaxHeap_Imp<E extends Comparable<E>> implements MaxHeapBookADT<E> {
    private LinkedList<E> heap;

    public MaxHeap_Imp() {
        heap = new LinkedList<>();
    }

    @Override
    public void buildHeap(List<E> items) {
        heap = new LinkedList<>(items);
        for (int i = (heap.size() / 2) - 1; i >= 0; i--) {
            bubbleDown(i);
        }
    }

    @Override
    public E getMax() {
        if (heap.isEmpty()) return null;
        return heap.get(0); // root is always the max
    }

    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // --- Helpers ---
    private void bubbleDown(int index) {
        int size = heap.size();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < size && heap.get(left).compareTo(heap.get(largest)) > 0) {
                largest = left;
            }
            if (right < size && heap.get(right).compareTo(heap.get(largest)) > 0) {
                largest = right;
            }
            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else break;
        }
    }

    private void swap(int i, int j) {
        E temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}