//@author: Suraj Jagadeesh and Allen Putich
//@date: 10/25/17
//@class: CSE373

package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;

    
    /**
     * Constructor for ArrayDictionary class
     */
    public ArrayDictionary() {
    	pairs = makeArrayOfPairs(10);
    	size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    /**
     * Returns the value corresponding to the given key.
     * Throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if (key == pairs[i].key || (key != null && key.equals(pairs[i].key))) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    /**
     * Given a key, returns the key-value Pair object.
     * If the key does not exist, returns null.
     */
    private Pair<K, V> getPair(K key) {
    	for (int i = 0; i < size; i++) {
            if (key == pairs[i].key || (key != null && key.equals(pairs[i].key))) {
                return pairs[i];
            }
    	}
    	return null;
    }
    
    /**
     * Given an array of type Pair, this doubles the size of the current array
     * and copies the existing objects into the new array
     */
    private Pair<K, V>[] extendArray(Pair<K, V>[] currentArray){
    	Pair<K, V>[] newPairs = makeArrayOfPairs(currentArray.length * 2);
    	for (int i = 0; i < size; i++) {
            newPairs[i] = currentArray[i];
    	}
    	return newPairs;
    }
    
    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replace its value with the given one.
     */
    @Override
    public void put(K key, V value) {
    	Pair<K, V> keyPair = getPair(key);
    	if (keyPair == null) { //Adds a new pair
            Pair<K, V> newPair = new Pair(key, value);
            if (size == pairs.length) { //Extends if list length is not long enough
                Pair<K, V>[] newPairs = extendArray(pairs);
                pairs = newPairs;
            }
            pairs[size] = newPair;
            size++;
    	} else { //Key already exists, changes to the new value
            keyPair.value = value;
    	}	
    }

    /**
     * Finds and returns the index of a given key.
     * If the key does not exists, return -1.
     */
    private int getIndex(K key) {
    	for (int i = 0; i < size; i++) {
            if (key == pairs[i].key || (key != null && key.equals(pairs[i].key))) {
      		return i;
            }
    	}
    	return -1;
    }
    
    /**
     * Remove the key-value pair corresponding to the given key from the dictionary.
     * If the dictionary does not contain the given key, throw NoSuchKeyException.
     */    
    @Override
    public V remove(K key) {
    	if (!containsKey(key)) {
            throw new NoSuchKeyException();
    	}
        int index = getIndex(key);
        V value = pairs[index].value;
        for (int i = index; i < size - 1; i++) {
            pairs[i] = pairs[i+1];
        }
        pairs[size] = null;
        size--;
        return value;
    }

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     */
    @Override
    public boolean containsKey(K key) {
        return getPair(key) != null;
    }

    /**
     * Returns the number of key-value pairs stored in this dictionary.
     */
    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;
        

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(this.pairs);
    }
	
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int index;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs) {
            this.pairs = pairs;
            this.index = 0;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            if (index >= pairs.length) {
                return false;
            }
            return pairs[index] != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public KVPair<K, V> next() {
            if (!hasNext()) {
            	throw new NoSuchElementException();
            }
            index++;
            return new KVPair(pairs[index - 1].key, pairs[index - 1].value);
        }
    }
}
