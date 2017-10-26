//@author: Suraj Jagadeesh and Allen Putich
//@date: 10/25/17
//@class: CSE373

package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int numPairs;
    private static final double LOAD_FACTOR = 0.75;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(16);
        numPairs = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    /**
     * Returns the element the given key should go in, scaling based on array length
     * @param key: The key for which you want to find it's element
     * @return: int corresponding to the element of the array the key should go in
     */
    private int hashValue(K key) {
    	if (key == null) {
            return 0;
    	}
    	return Math.abs(key.hashCode() % (chains.length - 1));
    }
    
    /**
     * Creates a new hashDictionary with double the length, copying
     * and rehashing all the values in the hashDictionary
     */
    private void resize() {
    	IDictionary<K, V>[] chainsOld = chains;
    	chains = makeArrayOfChains(chains.length * 2);
    	for (int i = 0; i < chainsOld.length; i++) {
    	    if (chainsOld[i] != null) {
    	        for (KVPair pair : chainsOld[i]) {
    	            K key = (K) pair.getKey();
    	            V value = (V) pair.getValue();
    	            int hashValue = hashValue(key);
    	            ensureArrayDictionary(hashValue);
    	            chains[hashValue].put(key, value);
    	        }
    	    }
    	}
    }
    
    /**
     * If the chains array's given element is null, creates a new
     * arrayDictionary at that element
     * @param hashValue: The element for which you want to check
     */
    private void ensureArrayDictionary(int hashValue) {
    	if (chains[hashValue] == null) {
            chains[hashValue] = new ArrayDictionary();
    	}
    }
    
    /**
     * Returns the value for the given key. Throws a NoSuchKeyException if the
     * key doesn't exist in the hash dictionary
     * @param key: The key for which value is needed
     * @return: The value for the specified key
     */
    public V get(K key) {
    	if (!containsKey(key)) {
    	    throw new NoSuchKeyException();
    	}
        return chains[hashValue(key)].get(key);
    }

    /**
     * Puts the given key and value into the hashDictionary
     * @param key: The key for the data that's being put in the hashDictionary
     * @param value: The value for the data that's being put into the hashDictionary
     */
    public void put(K key, V value) {
    	int hashValue = hashValue(key);
    	ensureArrayDictionary(hashValue);
    	if (!chains[hashValue].containsKey(key)) {
    	    numPairs++;
    	}
    	chains[hashValue].put(key, value);
    	
    	if (1.0 * numPairs / chains.length >= LOAD_FACTOR) { //1.0 in order to cast to double
            resize();
    	}
        
        
    }

    /**
     * Removes the given key and corresponding value. Throws a NoSuchKeyException
     * if the key doesn't exist in the hash dictionary
     * @param key: The key for the key/value pair that is going to be
     * deleted from the hashDictionary
     * @return: Returns the value for the given key
     */
    public V remove(K key) {
    	if (!containsKey(key)) {
    	    throw new NoSuchKeyException();
    	}
    	numPairs--;
        return chains[hashValue(key)].remove(key);
    }

    /**
     * Checks if the given key is in the chainedHashDictionary
     * @param key: The key for which is being checked if it is in the hashDictionary
     * @return: Returns true if the hashDictionary contains the key, false otherwise
     */
    public boolean containsKey(K key) {
    	if (chains[hashValue(key)] == null) {
            return false;
    	}
        return chains[hashValue(key)].containsKey(key);
    }

    /**
     * Returns the number of key-value pairs in the hash dictionary
     * @return: Size of the hash dictionary
     */
    public int size() {
        return numPairs;
    }

    /**
    * Returns the iterator for ChainedHashDictionary
    */
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int index;
        private Iterator<KVPair<K, V>> nestedIterator; 

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            index = 0;
            nestedIterator = findNextIterator();
        }
        
        /**
         * Finds the next iterator
         * @return: Returns the next iterator if there is one, null otherwise
         */
        private Iterator<KVPair<K, V>> findNextIterator() {
            while (index < chains.length) {
                if (chains[index] != null) {
                    return chains[index].iterator();
                }
                index++;
            }
            return null;
        }

        /**
         * Returns true if there is another array dictionary that is not null, false otherwise
         */
        public boolean hasNext() {
            if (nestedIterator == null) {
                return false;
            } else if (nestedIterator.hasNext()) {
                return true;
            } else {
            	index++;
            	nestedIterator = findNextIterator();
            	return nestedIterator != null;
            }
        }

        /**
         * Returns the next KVPair, throws a NoSuchElementException if there isn't one
         */
        public KVPair<K, V> next() {
            if (!hasNext()) {
            	throw new NoSuchElementException();
            }
            return nestedIterator.next();
        }
    }
}
