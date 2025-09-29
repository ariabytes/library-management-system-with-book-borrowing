package utils;

public interface Queue_Int<E> {
	
	void enqueue(E x);
	
	E dequeue();
	
	E peek();
	
	boolean isEmpty();
	
}