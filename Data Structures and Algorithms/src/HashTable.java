
import java.util.ArrayList;
import java.util.Random;

/**
 * An abstract base class supporting Map implementations that use hash tables
 * with MAD compression.
 *
 * The base class provides the following means of support: 1) Support for
 * calculating hash values with MAD compression 2) Support for resizing table
 * when load factor reaches 1/2
 *
 * Subclass is responsible for providing abstract methods: createTable(),
 * bucketGet(h,k), bucketPut(h,k,v), bucketRemove(h,k), and entrySet() and for
 * accurately maintaining the protected member, n, to reflect changes within
 * bucketPut and bucketRemove. As implemented in Data Structures and Algorithms
 * 6th Edition by Michael T. GoodRich, Roberto Tamassia, and Michael H.
 * Goldwasser
 *
 * @author Edward Shirinian 214456818
 * @author Jay Patel 21351195
 */
public class HashTable<K, V> extends AbstractMap<K, V> {
	protected int n = 0; // number of entries in the dictionary
	protected int capacity; // length of the table
	private int prime; // prime factor
	private long scale, shift; // the shift and scaling factors
	private MapEntry<K, V>[] table; // a fixed array of entries (all initially null)
	private MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null); // sentinel

	/** Creates a hash table with the given capacity and prime factor. */
	public HashTable(int cap, int p) {
		prime = p;
		capacity = cap;
		Random rand = new Random();
		scale = rand.nextInt(prime - 1) + 1;
		shift = rand.nextInt(prime);
		createTable();
	}

	/** Creates a hash table with given capacity and prime factor 109345121. */
	public HashTable(int cap) {
		this(cap, 109345121);
	} // default prime

	/** Creates a hash table with capacity 17 and prime factor 109345121. */
	public HashTable() {
		this(17);
	} // default capacity

	// public methods
	/**
	 * Tests whether the map is empty.
	 * 
	 * @return true if the map is empty, false otherwise
	 */
	@Override
	public int size() {
		return n;
	}

	/**
	 * Returns the value associated with the specified key, or null if no such entry
	 * exists.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return the associated value, or null if no such entry exists
	 */
	@Override
	public V get(K key) {
		return bucketGet(hashValue(key), key);
	}

	/**
	 * Removes the entry with the specified key, if present, and returns its
	 * associated value. Otherwise does nothing and returns null.
	 * 
	 * @param key the key whose entry is to be removed from the map
	 * @return the previous value associated with the removed key, or null if no
	 *         such entry exists
	 */
	@Override
	public V remove(K key) {
		return bucketRemove(hashValue(key), key);
	}

	/**
	 * Associates the given value with the given key. If an entry with the key was
	 * already in the map, this replaced the previous value with the new one and
	 * returns the old value. Otherwise, a new entry is added and null is returned.
	 * 
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with the key (or null, if no such
	 *         entry)
	 */
	@Override
	public V put(K key, V value) {
		V answer = bucketPut(hashValue(key), key, value);
		if (n > capacity / 2) // keep load factor <= 0.5
			resize(2 * capacity - 1); // (or find a nearby prime)
		return answer;
	}

	// private utilities
	/** Hash function applying MAD method to default hash code. */
	private int hashValue(K key) {
		return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
	}

	/** Updates the size of the hash table and rehashes all entries. */
	private void resize(int newCap) {
		ArrayList<Entry<K, V>> buffer = new ArrayList<>(n);
		for (Entry<K, V> e : entrySet())
			buffer.add(e);
		capacity = newCap;
		createTable(); // based on updated capacity
		n = 0; // will be recomputed while reinserting entries
		for (Entry<K, V> e : buffer)
			put(e.getKey(), e.getValue());
	}

	// protected abstract methods to be implemented by subclasses
	/** Creates an empty table having length equal to current capacity. */
	@SuppressWarnings("unchecked")
	private void createTable() {
		table = (MapEntry<K, V>[]) new MapEntry[capacity];
	}

	/**
	 * Returns value associated with key k in bucket with hash value h. If no such
	 * entry exists, returns null.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @return associate value (or null, if no such entry)
	 */
	private V bucketGet(int h, K k) {
		int j = findSlot(h, k);
		if (j < 0)
			return null; // no match found
		return table[j].getValue();
	}

	/**
	 * Associates key k with value v in bucket with hash value h, returning the
	 * previously associated value, if any.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @param v the value to be associated
	 * @return previous value associated with k (or null, if no such entry)
	 */
	private V bucketPut(int h, K k, V v) {
		int j = findSlot(h, k);
		if (j >= 0) // this key has an existing entry
			return table[j].setValue(v);
		table[-(j + 1)] = new MapEntry<>(k, v); // convert to proper index
		n++;
		return null;
	}

	/**
	 * Removes entry having key k from bucket with hash value h, returning the
	 * previously associated value, if found.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @return previous value associated with k (or null, if no such entry)
	 */
	private V bucketRemove(int h, K k) {
		int j = findSlot(h, k);
		if (j < 0)
			return null; // nothing to remove
		V answer = table[j].getValue();
		table[j] = DEFUNCT; // mark this slot as deactivated
		n--;
		return answer;
	}

	private boolean isAvailable(int j) {
		return (table[j] == null || table[j] == DEFUNCT);
	}

	/**
	 * Searches for an entry with key equal to k (which is known to have hash value
	 * h), returning the index at which it was found, or returning -(a+1) where a is
	 * the index of the first empty or available slot that can be used to store a
	 * new such entry.
	 *
	 * @param h the precalculated hash value of the given key
	 * @param k the key
	 * @return index of found entry or if not found, value -(a+1) where a is index
	 *         of first available slot
	 */
	private int findSlot(int h, K k) {
		int avail = -1; // no slot available (thus far)
		int j = h; // index while scanning table
		do {
			if (isAvailable(j)) { // may be either empty or defunct
				if (avail == -1)
					avail = j; // this is the first available slot!
				if (table[j] == null)
					break; // if empty, search fails immediately
			} else if (table[j].getKey().equals(k))
				return j; // successful match
			j = (j + 1) % capacity; // keep looking (cyclically)
		} while (j != h); // stop if we return to the start
		return -(avail + 1); // search has failed
	}

	/**
	 * Returns an iterable collection of all key-value entries of the map.
	 *
	 * @return iterable collection of the map's entries
	 */
	@Override
	public Iterable<Entry<K, V>> entrySet() {
		ArrayList<Entry<K, V>> buffer = new ArrayList<>();
		for (int h = 0; h < capacity; h++)
			if (!isAvailable(h))
				buffer.add(table[h]);
		return buffer;
	}

}
