import java.util.LinkedList;
/**.
 * An Implementation of an airportList.
 * 
 * @author Edward Shirinian 214456818
 * @author Jay Patel 21351195
 */
public class AirportList<V,E> {
	AdjacencyGraph<V,E> airports;
	
	
	/**
	 * Constructs an empty Adjacency Graph, sets it to undirected
	 */

	public AirportList()
	{
		airports = new AdjacencyGraph<V,E>();
	}
	/**
	 * Removes an Airport from the graph
	 * @param: Vertex<V> v 	 
	 */
	public void removeAirport(Vertex<V> a)
	{
		Vertex<V> v = airports.isInternal(a);
		if (v == null)
		{
			throw new IllegalArgumentException();
		}
		airports.removeVertex(v);
	}
	/**
	 * Add airport
	 * @param: V a 	 
	 */
	public Vertex<V> addAirport(V a)
	{
		for (Vertex<V> v: airports.vertices())
		{
			if (v.getElement().equals(a))
			{
				return v;
			} 	
		}
		return airports.insertVertex(a);
		
	}
	
	public AdjacencyGraph<V,E> getGraph(){
		return this.airports;
	}
	/**
	 * Add a connection between two airports
	 * @param: Vertex<V> a
	 * @param: Vertex<V> b
	 * @param: E e 	 
	 */
	public void addConnection(Vertex<V> a, Vertex<V> b, E e)
	{
		boolean Acontained = false;
		boolean Bcontained = false;
		for (Vertex<V> v: airports.vertices())
		{
			if (v.getElement().equals(a.getElement()))
			{
				Acontained = true;
			} else if (v.getElement().equals(b.getElement()))
			{
				Bcontained = true;
			}
		}
		if (Acontained == false)
		{
			a = addAirport(a.getElement());
		} else 
		{
			a = airports.isInternal(a);
		}
		if(Bcontained == false)
		{
			b = addAirport(b.getElement());
		}else 
		{
			b = airports.isInternal(b);
		}
		
		airports.insertEdge(a, b, e);
	}
	
	/**
	 * remove a connection between two airports
	 * @param: Vertex<V> a
	 * @param: Vertex<V> b
	 * @param: E e 	 
	 */
	public void removeConnection(Vertex<V> a, Vertex<V> b, Edge<E> e1)
	{
		Vertex<V> inA = airports.isInternal(a);
		Vertex<V> inB = airports.isInternal(b);
		if(airports.getEdge(inA, inB).getElement().equals(e1.getElement()))
		{
			Edge<E> e = airports.isInternalEdge(e1);
			System.out.println(e);
			airports.removeEdge(e);
		} else 
		{
			throw new IllegalArgumentException ("The Command is invalid, the value is incorrect");
		}
	}
	/**
	 * Gets all connections for a certain Vertex
	 * @param: Vertex<V> a 	 
	 */
	public LinkedList<AirportConnection<V, E>> getConnectionsForVertex(Vertex<V> v)
	{
		Vertex<V> in = airports.isInternal(v);
		//System.out.println( Your are un);
		Iterable<Edge<E>> ed = airports.outgoingEdges(in);
		//System.out.println(ed);
		LinkedList<AirportConnection<V, E>> connections = new LinkedList<AirportConnection<V,E>>();
		for (Edge<E> temp : ed)
		{
			Vertex<V> a = airports.opposite(in, temp);
			AirportConnection<V,E> temp1 = new AirportConnection<V,E>(in, a , temp);
			connections.add(temp1);
		}
	return connections;
	
	}
	
	/**
	 * Gets all the connections in memory	 
	 */
	public LinkedList<AirportConnection<V,E>> getAllConnections()
	{
		LinkedList<AirportConnection<V, E>> connections = new LinkedList<AirportConnection<V,E>>();
		Iterable<Vertex<V>> v = airports.vertices();
		for(Vertex<V> temp: v)
		{
			LinkedList<AirportConnection<V, E>> Vconnections = getConnectionsForVertex(temp);
			connections = addNew (connections, Vconnections);
		}
		return connections;
		
		
	}
	
	private LinkedList<AirportConnection<V, E>> addNew(LinkedList<AirportConnection<V, E>> total, LinkedList<AirportConnection<V, E>> vconnections)
	{
		LinkedList<AirportConnection<V, E>> connections = total;
	
		for (AirportConnection<V, E> Vtemp: vconnections)
		{
			boolean b = false;
			
			for (AirportConnection<V, E> Ttemp: total)
			{
				b = Vtemp.isEqual(Ttemp);
				if (b == true)
				{
					break;
				}
			}
			if( b == false)
			{
				connections.add(Vtemp);
			}
			
		}
		return connections;
	}
	/**
	 * Gets the shortest path between two vertices
	 * @param: int value
	 * @param: LinkedList ShortestPath
	 * @param: Vertex<V> a
	 * @param: Vertex<V> b
	 */
	public LinkedList<AirportConnection<V,E>> getQuickestRoute(int value, LinkedList<AirportConnection<V,E>> smallestPath, Vertex<V> a, Vertex<V> b)
	{
		Edge<E> smallest = null;
		int small = value;
		try 
		{
			smallest = airports.getEdge(a, b);
			smallestPath.add(new AirportConnection<V,E>(a, b, smallest));
//			System.out.println(smallest.getElement());
//			small = (int) smallest.getElement();
//			System.out.println(small);
		} 
		catch (IllegalArgumentException e){}
		catch (NullPointerException e) {}
		Iterable<Edge<E>> ed = airports.outgoingEdges(a);
		
		for (Edge<E> temp: ed)
		{
			if(small == 0)
			{
				small = (int) temp.getElement();
				smallest = temp;

			} else if (small > (int) temp.getElement())
			{
				small = (int) temp.getElement();
				smallest = temp;
			}
			else
			{
				small = (int) smallest.getElement();
			} 
		}
		AirportConnection<V,E> temp1 = new AirportConnection<V,E>(a, b, smallest) ;
		Vertex<V> t = airports.opposite(a, smallest);
		smallestPath.add(temp1);
		LinkedList<AirportConnection<V, E>> temp = minPath(getQuickestRoute(small, smallestPath,t,b), smallestPath );
		
		return temp;
	}

	private LinkedList<AirportConnection<V, E>> minPath(LinkedList<AirportConnection<V, E>> a,
			LinkedList<AirportConnection<V, E>> b) {
		int count1 = 0;
		int count2 = 0;
		for (AirportConnection<V, E> temp: a)
			count1 += (int) temp.getEdge().getElement();
		
		for (AirportConnection<V, E> temp: b)
			count2 += (int) temp.getEdge().getElement();
		
		if (count1 < count2)
		{
			return a;
		} else {
			return b;
		}
	}
	
	

}