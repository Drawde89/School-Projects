
/**
 * A interface describing the connection between two vertices
 * @author Edward Shirinian 214456818
 * @author Jay Patel 21351195
 */
public interface Connection<V,E> {
	/**
	 * returns all the vertices 
	 * */
	Vertex<V>[] getVertecies();
	/**
	 * returns the edge
	 * */
	Edge<E> getEdge();
	/**
	 * sets the Edge 
	 * @param: Edge<E> e
	 * */
	void setEdge(Edge<E> e);
	/**
	 * sets the vertices 
	 * @param: Vertex<V> a
	 * @param: Vertex<V> b
	 * */
	void setVertecies(Vertex<V> a, Vertex<V> b);
	/**
	 * returns all the vertices 
	 * @param: Vertex<V>[] v
	 * */
	void setVertecies(Vertex<V>[] v);
	/**
	 * returns boolean value is two AirportConnections are the same
	 * @param: Object o
	 * */
	boolean isEqual(Object o);
}