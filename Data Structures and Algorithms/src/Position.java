
/**
 * An interface for a position which is an abstraction for the location at which
 * a single element is stored in a positional container. As implemented in Data
 * Structures and Algorithms 6th Edition by Michael T. GoodRich, Roberto
 * Tamassia, and Michael H. Goldwasser
 * 
 * @author Edward Shirinian 214456818
 * @author Jay Patel 21351195
 */
public interface Position<E> {
	/**
	 * Returns the element stored at this position.
	 *
	 * @return the stored element
	 * @throws IllegalStateException if position no longer valid
	 */
	E getElement() throws IllegalStateException;
}
