
/**
 * An implementation of the connections interface
 * @author Edward Shirinian 214456818
 * @author Jay Patel 21351195
 */
public class AirportConnection<V,E> implements Connection<V,E>{
	Vertex<V> a;
	Vertex<V> b;
	Edge<E> e;
	/**
	 * Constructor
	 * @param: Vertex<V> v
	 * @param: Vertex<V> a2
	 * @param: Edge<E> temp
	 * */
	public AirportConnection(Vertex<V> v, Vertex<V> a2, Edge<E> temp) {
		this.a = v;
		this.b = a2;
		this.e = temp;
		
	}
	/**
	 * returns all the vertices 
	 * */
	public Vertex<V>[] getVertecies() {
		Vertex[] v = {a, b};
		return v;
		
	}
	/**
	 * returns the edge
	 * */
	public Edge<E> getEdge() {
		return e;
	}
	/**
	 * sets the Edge 
	 * @param: Edge<E> e
	 * */
	@Override
	public void setEdge(Edge<E> e) {
		this.e = e;
		
	}
	/**
	 * sets the vertices 
	 * @param: Vertex<V> a
	 * @param: Vertex<V> b
	 * */
	@Override
	public void setVertecies(Vertex<V> a, Vertex<V> b) {
		this.a = a;
		this.b = b;
		
	}
	/**
	 * returns all the vertices 
	 * @param: Vertex<V>[] v
	 * */
	@Override
	public void setVertecies(Vertex<V>[] v) {
		this.a = v[0];
		this.b = v[1];
		
	}
	/**
	 * returns String of all Values
	 * */
	public String toString()
	{
		String s = a.getElement().toString() + " " + b.getElement().toString() + " " + e.getElement().toString();
		return s;
	}
	/**
	 * returns boolean value is two AirportConnections are the same
	 * @param: Object o
	 * */
	public boolean isEqual(Object o)
	{
		if(o == null ) { return false;}
		if(this == o) { return true;}
		if (this.getClass() != o.getClass()) { return false;}
		@SuppressWarnings("unchecked")
		AirportConnection<V, E> other = (AirportConnection<V,E>) o;
		boolean b = false;
		if (this.a.equals(other.a))
		{
			if(this.b.equals(other.b))
			{
				b = true;
			}
		} else if (this.b.equals(other.a))
		{
			if (this.a.equals(other.b))
			{
				b = true;
			}
		}else 
		{
			b = false;
		}
		return b;
	}
}