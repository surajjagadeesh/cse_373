//@author: Suraj Jagadeesh and Maggie Tsang
//DoubleLinkedList class

package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    /**
     * Adds the given item to the *end* of this IList.
     */
    @Override
    public void add(T item) {
    	Node<T> newNode;
        if (front == null) { //if empty
            newNode = new Node<T>(item);
            front = newNode;
        } else {
            newNode = new Node<T>(back, item, null);
            back.next = newNode;
        }
        back = newNode;
        size++;
    }

    /**
     * Removes and returns the item from the *end* of this IList.
     *
     * @throws EmptyContainerException if the container is empty and there is no element to remove.
     */
    @Override
    public T remove() {
    	if (size == 0) {
    	    throw new EmptyContainerException();
    	}
    	T backValue = back.data;
    	back = back.prev;
    	if (size > 1) {
    	    back.next = null;
    	} else { //removing from a list with 1 element makes it empty
    	    front = null;
    	}
    	size--;
    	return backValue;
    }
    
    
    /**
     * Checks if the given index is valid given the lower and upper bound.
     * Throws a IndexOutOfBoundsException if not
     */
    private void checkValidIndex(int lowerBound, int upperBound, int index) {
    	if (index <= lowerBound || index >= upperBound) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    /**
     * Traverses to the given index and returns the Node
     */
    private Node<T> traverse(int index) {
    	Node<T> temp;
    	if (index < (size / 2)) {
    	    temp = front;
	    for (int i = 0; i < index; i++) { //im doing this spacing only bc of checkstyle wtf
	        temp = temp.next;
	    }
    	} else {
    	    temp = back;
    	    for (int i = size-1; i > index; i--) {
	        temp = temp.prev;
	    }
    	}
        return temp;
    }
    
    /**
     * Returns the item located at the given index.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public T get(int index) {
    	checkValidIndex(-1, size, index);
        return traverse(index).data;
    }

    /**
     * Overwrites the element located at the given index with the new item.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public void set(int index, T item) {
    	checkValidIndex(-1, size, index);
    	Node<T> temp = traverse(index);
    	Node<T> newNode = new Node<T>(temp.prev, item, temp.next);
    	if (size == 1) { //only one item
    	    front = newNode;
    	    back = newNode;
    	} else if (index == 0) { //setting the front
    	    front.next.prev = newNode;
    	    front = newNode;
    	} else if (index == (size - 1)) { //setting the back
    	    back.prev.next = newNode;
    	    back = newNode;
    	} else { //changing middle of a list with 3 or more items
    	    temp.prev.next = newNode;
    	    temp.next.prev = newNode;
    	}
    }

    /**
     * Inserts the given item at the given index. If there already exists an element
     * at that index, shift over that element and any subsequent elements one index
     * higher.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size() + 1
     */
    @Override
    public void insert(int index, T item) {
        checkValidIndex(-1, size + 1, index); //allowed to insert at index=size  
        if (size == 0) { //if list is empty
            add(item);
        } else if (index == 0) { //if you want to insert at the head
            Node<T> newNode = new Node<T>(null, item, front);
            front.prev = newNode;
            front = newNode;
            size++;
        } else if (index == size) { //if you want to insert at the end
            add(item);
        } else { // if you want to insert in the middle somewhere
            Node<T> temp = traverse(index);
            Node<T> newNode = new Node<T>(temp.prev, item, temp); 
            temp.prev.next = newNode;
            temp.prev = newNode;
            size++;
        }
    }

    /**
     * Deletes the item at the given index. If there are any elements located at a higher
     * index, shift them all down by one.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    @Override
    public T delete(int index) {
    	checkValidIndex(-1, size, index);
    	T value = front.data;
        if (size == 1) { //if it's a list of size 1
    	    front = null;
    	    back = null;
    	} else if (index == 0) { //if you want to delete the front
    	    front = front.next;
    	    front.prev = null;
    	} else if (index == size - 1) { //if you want to delete the end
    	    value = back.data;
    	    back = back.prev;
    	    back.next = null;
    	} else { //if you want to delete from the middle
    	    Node<T> temp = traverse(index);
    	    value = temp.data;
    	    temp.prev.next = temp.next;
    	    temp.next.prev = temp.prev;
    	}
    	size--;
    	return value;
    }

    /**
     * Returns the index corresponding to the first occurrence of the given item
     * in the list.
     *
     * If the item does not exist in the list, return -1.
     */
    @Override
    public int indexOf(T item) {
        Node<T> temp = front;
    	for (int i = 0; i < size; i++) {
    	    if (temp.data == item || (temp.data != null && temp.data.equals(item))) {
    	        return i;
    	    }
            temp = temp.next;
        }
    	return -1;
    }

    /**
     * Returns the number of elements in the container.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns 'true' if this container contains no elements, and 'false' otherwise.
     */
    @Override
    public boolean contains(T other) {
        return indexOf(other) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null; //this seems right?? CHECK
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
            	throw new NoSuchElementException();
            }
            T value = current.data;
            current = current.next;
            return value;
        }
    }
}