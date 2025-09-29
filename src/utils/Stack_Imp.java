package utils;
import java.util.ArrayList;
import java.util.List;

public class Stack_Imp<E> implements Stack_Int<E> {
	
private List<E> data = new ArrayList<>();
	
	public void push(E x) {
		data.add(x);
	}
	
	public E pop() {
		if(data.size() == 0) {
			throw new IllegalStateException("popped from empty stack");
		}
		return data.remove(data.size() - 1);
	}
	
	public E peek() {
		if(data.size() == 0) {
			throw new IllegalStateException("peeked from empty stack");
		}
		return data.get(data.size() - 1);
	}
	
	public boolean isEmpty() {
		return data.size() == 0;
	}
	
	public String toString() {
	    return data.toString();
	}
 
}