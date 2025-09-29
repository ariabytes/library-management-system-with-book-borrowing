package utils;
import java.util.ArrayList;
import java.util.List;

public class Queue_Imp<E> implements Queue_Int<E> {
	
private List<E> data = new ArrayList<>();
	
	public void enqueue(E x) {
		data.add(x);
	}
	
	public E dequeue() {
		return data.remove(data.size() - data.size());
	}
	
	public E peek() {
		System.out.println(data.get(data.size() - data.size()));
		return data.get(data.size() - data.size());
	}
	
	public boolean isEmpty() {
		return data.size() == 0;
	}
	public String toString() {
		return data.toString();
	}
}