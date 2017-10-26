//@author: Suraj Jagadeesh and Allen Putich
//@date: 10/25/17
//@class: CSE373

package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See ISet for more details on what each method is supposed to do.
 */
public class ChainedHashSet<T> implements ISet<T> {
    // This should be the only field you need
    private IDictionary<T, Boolean> map;

    public ChainedHashSet() {
        this.map = new ChainedHashDictionary<>();
    }

    /**
     * Adds the given item to the chained hash set
     * @param item: The item which is going to be added to the hash set
     */
    public void add(T item) {
        map.put(item, true);
    }

    /**
     * Removes the given item from the chained hash set. Throws NoSuchElementException
     * if the item doesn't exist in the hash set.
     * @param item: The item which is going to be removed from the hash set
     */
    public void remove(T item) {
    	if (!contains(item)) {
            throw new NoSuchElementException();
    	}
        map.remove(item);
    }

    /**
     * Returns true if the hash set contains the item, false otherwise
     * @param item: The item that is being checked whether it is in the hash set
     */
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /**
     * Returns the size of the hash set
     */
    public int size() {
        return map.size();
    }

    /**
     * Returns the iterator for chained hash set
     */
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;

        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            this.iter = iter;
        }

        /**
         * Returns true if the iterator has a next element, false otherwise
         */
        public boolean hasNext() {
            return iter.hasNext();
        }

        /**
         * Returns the value of the next item in the ChainedHashSet
         */
        public T next() {
            return iter.next().getKey();
        }
    }
}
