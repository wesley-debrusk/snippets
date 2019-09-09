
public class Node<E> {
	private E _element; // reference to the element stored at this node
	private Node<E> _next; // reference to the subsequent node in the list

	public Node(E e, Node<E> n) { // simple, full constructor
		_element = e;
		_next = n;
	}

	public E getElement() { // returns the element in the node
		return _element;
	}

	public Node<E> getNext() { // returns the next node
		return _next;
	}

	public void setNext(Node<E> n) { // sets the next node
		_next = n;
	}
}