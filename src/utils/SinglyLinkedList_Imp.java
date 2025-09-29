package utils;

public class SinglyLinkedList_Imp<Z> implements SinglyLinkedList_Int<Z> {
	private Node<Z> head; //attributes that describe what nodes contain and how many
	private Node<Z> tail;
	private int size;
	
	public SinglyLinkedList_Imp() { //
		head = null;
		tail = null;
		size = 0;
	}
	
	public int size() { //returns the current number of nodes XD
		return size;
	}
	
	
	public boolean isEmpty() { //kabalo namo ani hehehe hart hart
		return size == 0;
	}
	
	public void clear() { //self explanatory
		head = null;
		tail = null;
		size = 0;
	}
	
	public void addFirst(Z item) { 
		Node<Z> newNode = new Node<>(item);
		if (isEmpty()) {
			head = newNode;
			tail = newNode;
		} else {
			newNode.next = head; //added node points to the current head node
			head = newNode; //the added node becomes the new head
		}
		size++;
	}
	
	public void addLast(Z item) {
		Node<Z> newNode = new Node<>(item);
		if (isEmpty()) {
			head = newNode;
			tail = newNode;
		} else {
			tail.next = newNode; //tail node points to the added node
			tail = newNode; //added node becomes the new tail node
		}
		size++;
	}
	
	public void add(int index, Z item) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		
		if (index == 0) {
			addFirst(item);
			return;
		}
		
		if (index == size) {
			addLast(item);
			return;
		}
		
		Node<Z> newNode = new Node<>(item);
		Node<Z> current = head;
		
		for (int i = 0; i < index - 1; i++) {
			current = current.next;
		}
		
		newNode.next = current.next; //explain ra nako ugma hahahaahhaahahaah
		current.next = newNode;
		
		size++;
	}
	
	public Z removeFirst() {
		if (isEmpty()) {
			throw new IllegalStateException("List is empty");
		}
		
		Z removedData = head.data;
		
		if (head == tail) { // only one element
			head = null;
			tail = null;
		} else {
			head = head.next;
		}
		
		size--;
		
		return removedData;
	}

	@Override
	public Z removeLast() {
		if (isEmpty()) {
			throw new IllegalStateException("List is empty");
		}
		Z removedData = tail.data;
		
		if (head == tail) { // only one element
			head = null;
			tail = null;
		} else {
			Node<Z> current = head;
 			while (current.next != tail) {
				current = current.next;
			}
			current.next = null; //ubuson nya tanan ka lantaw ang list asta maabot sya sa before sang tail kag ang next sina himuon nya null
			tail = current;
		}
		
		size--;
		return removedData;
	}

	@Override
	public Z remove(int index, Z item) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		
		if (index == 0) return removeFirst();
		if (index == size - 1) return removeLast();
		
		Node<Z> current = head;
		for (int i = 0; i < index - 1; i++) {
			current = current.next;
		}
		Z removedData = current.next.data;
		current.next = current.next.next;
		size--;
		return removedData;
	}

	@Override
	public boolean removeItem(Z item) {
		if (isEmpty()) return false;
		
		if (head.data.equals(item)) {
			removeFirst();
			return true;
		}
		
		Node<Z> current = head;
		while (current.next != null) {
			if (current.next.data.equals(item)) {
				if (current.next == tail) {
					tail = current;
				}
				current.next = current.next.next;
				size--;
				return true;
			}
			current = current.next;
		}
		return false;
	}

	@Override
	public Z getFirst() {
		if (isEmpty()) throw new IllegalStateException("List is empty");
		return head.data;
	}

	@Override
	public Z getLast() {
		if (isEmpty()) throw new IllegalStateException("List is empty");
		return tail.data;
	}

	@Override
	public Z get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		Node<Z> current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		return current.data;
	}

	@Override
	public int indexOf(Z item) {
		Node<Z> current = head;
		int index = 0;
		while (current != null) {
			if (current.data.equals(item)) return index;
			current = current.next;
			index++;
		}
		return -1; // not found
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		Node<Z> current = head;
		while (current != null) {
			sb.append(current.data);
			if (current.next != null) {
				sb.append(" -> ");
			}
			current = current.next;
		}
		sb.append("]");
		return sb.toString();
	}
	
}

	


