/**
 * Interface for a key-value pair. As implemented in Data Structures and
 * Algorithms 6th Edition by Michael T. GoodRich, Roberto Tamassia, and Michael
 * H. Goldwasser
 *
 * @author Edward Shirinian 214456818
 * @author Jay Patel 21351195
 */
public interface Entry<K, V> {
	/**
	 * Returns the key stored in this entry.
	 * 
	 * @return the entry's key
	 */
	K getKey();

	/**
	 * Returns the value stored in this entry.
	 * 
	 * @return the entry's value
	 */
	V getValue();
}
