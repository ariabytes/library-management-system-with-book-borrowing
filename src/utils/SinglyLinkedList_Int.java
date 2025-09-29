package utils;

public interface SinglyLinkedList_Int <Z> {
	
	int size();
	
	boolean isEmpty();
	
	void clear();
	
	void addFirst(Z item);
	
	void addLast(Z item);
	
	void add(int index, Z item);
	
	Z removeFirst();
	
	Z removeLast();
	
	Z remove(int index, Z item);
	
	boolean removeItem(Z item);
	
	Z getFirst();
	
	Z getLast();
	
	Z get(int index);
	
	int indexOf(Z item);
	
	String toString();

}