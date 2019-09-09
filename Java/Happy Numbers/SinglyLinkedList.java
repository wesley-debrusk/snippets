
public class SinglyLinkedList<E> {

	// --- instance variables of the SinglyLinkedList ---
	private Node<E> _head = null; // head node of the list(or null if empty)
	private Node<E> _tail = null; // last node of the list(or null if empty)
	private int _size = 0;

	public SinglyLinkedList() {
	}

	// --- access methods ---
	public int size() { // returns the size of a list
		return _size;
	}

	public boolean isEmpty() { // checks to see if the list is empty
		return _size == 0;
	}

	public E first() { // returns, but does not remove, the first element
		if (isEmpty())
			return null;
		else
			return _head.getElement();
	}

	public E last() { // returns, but does not remove the last element
		if (isEmpty())
			return null;
		else
			return _tail.getElement();
	}

	// --- update methods ---
	public void addFirst(E e) { // adds element e to the front of the list
		_head = new Node<>(e, _head); // create and link a new node
		if (_size == 0)
			_tail = _head; // special case: new node becomes tail also
		_size++;
	}

	public void addLast(E e) { // adds element e the the end of the list
		Node<E> newest = new Node<>(e, null); // node will eventually be tail
		if (isEmpty())
			_head = newest; // special case: previously empty list
		else
			_tail.setNext(newest); // new node after existing tail
		_tail = newest; // new node becomes tail
		_size++;
	}

	public E removeFirst() { // removes and returns the first element
		if (isEmpty()) // nothing to remove
			return null;
		E answer = _head.getElement();
		_head = _head.getNext(); // will become null if list has only one node
		_size--;
		if (_size == 0)
			_tail = null; // special case: list is now empty
		return answer;
	}

	public boolean isInList(int n) { // checks if n is present in the list
		Node<E> next = _head; // start at the head of the list
		boolean answer = false; // will default to false if the value is not
								// found
		while (next != null) {
			if (next.getElement().equals(n)) {
				answer = true; // if the two are equal the answer is true
				break; // and the loop breaks because we now have out answer
			} else {
				next = next.getNext(); // move on the the next element in the
										// list
			}
		}
		return answer; // returns the final answer
	}
}
