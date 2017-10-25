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
    private static final int MAX_BIN_SIZE = 1000;

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

    
    private int hashValue(K key) {
    	if (key == null) {
            return 0;
    	}
    	return Math.abs(key.hashCode() % (chains.length - 1));
    }
    
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
    
    private void ensureArrayDictionary(int hashValue) {
    	if (chains[hashValue] == null) {
            chains[hashValue] = new ArrayDictionary();
    	}
    }
    
    @Override
    public V get(K key) {
    	if (!containsKey(key)) {
    	    throw new NoSuchKeyException();
    	}
        return chains[hashValue(key)].get(key);
    }

    @Override
    public void put(K key, V value) {
    	int hashValue = hashValue(key);
    	ensureArrayDictionary(hashValue);
    	if (!chains[hashValue].containsKey(key)) {
    	    numPairs++;
    	}
    	chains[hashValue].put(key, value);
    	
    	if (chains[hashValue].size() > MAX_BIN_SIZE || 1.0 * numPairs / chains.length >= LOAD_FACTOR) {
            resize();
    	}
        
        
    }

    @Override
    public V remove(K key) {
    	if (!containsKey(key)) {
    	    throw new NoSuchKeyException();
    	}
    	numPairs--;
        return chains[hashValue(key)].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
    	if (chains[hashValue(key)] == null) {
            return false;
    	}
        return chains[hashValue(key)].containsKey(key);
    }

    @Override
    public int size() {
        return numPairs;
    }

    @Override
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
        
        private Iterator<KVPair<K, V>> findNextIterator() {
            while (index < chains.length) {
                if (chains[index] != null) {
                    return chains[index].iterator();
                }
                index++;
            }
            return null;
        }

        @Override
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

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
            	throw new NoSuchElementException();
            }
            return nestedIterator.next();
        }
    }
}
