
import java.util.ArrayList;
import java.util.Random;

/**
 * An implementation for a graph structure using an adjacency map for each
 * vertex.
 *
 * Every vertex stores an element of type V and a key of type Integer for
 * hashing. Every edge stores an element of type E.
 * @author Edward Shirinian 214456818
 * @author Jay Patel 21351195
 *
 */
public class AdjacencyGraph<V, E> implements Graph<V, E> {
	Random rand;
	private boolean isDirected;
	private PositionalList<Vertex<V>> vertices;
	private PositionalList<Edge<E>> edges;
	public HashTable<Vertex<V>, V> hashTable;

	/**
	 * Constructs an empty graph. The parameter determines whether this is an
	 * undirected or directed graph.
	 */

	public AdjacencyGraph() {
		this.rand = new Random();
		isDirected = false;
		vertices = new LinkedPositionalList<>();
		edges = new LinkedPositionalList<>();
		hashTable = new HashTable<Vertex<V>, V>();

	}

	/** Returns the number of vertices of the graph */
	public int numVertices() {
		return vertices.size();
	}

	/** Returns the vertices of the graph as an iterable collection */
	public Iterable<Vertex<V>> vertices() {
		return vertices;
	}

	/** Returns the number of edges of the graph */
	public int numEdges() {
		return edges.size();
	}

	/** Returns the edges of the graph as an iterable collection */
	public Iterable<Edge<E>> edges() {
		return edges;
	}
	/** Checks if the passed vertex is in the graph
	 * @param: Vertex<V> v
	 * */
	public Vertex<V> isInternal(Vertex<V> v)
	{
		Vertex<V> t = null;
		for (Vertex<V> temp: vertices)
		{
			if(temp.getElement().equals(v.getElement()))
			{
				//System.out.println(temp);
				return temp;
			}
		}
		return t;
	}
	/** Checks if the passed vertex is in the graph
	 * @param: Edge<E> e
	 * */
	public Edge<E> isInternalEdge(Edge<E> e)
	{
		Edge<E> t = null;
		for (Edge<E> temp: edges)
		{
			if(temp.getElement().equals(e.getElement()))
			{
				//System.out.println(temp);
				return temp;
			}
		}
		return t;
	}
	/**
	 * Returns the number of edges for which vertex v is the origin. Note that for
	 * an undirected graph, this is the same result returned by inDegree(v).
	 * 
	 * @throws IllegalArgumentException if v is not a valid vertex
	 */
	public int outDegree(Vertex<V> v) throws IllegalArgumentException {
		InnerVertex<V> vert = validate(v);
		return vert.elements.size();
	}

	/**
	 * Returns an iterable collection of edges for which vertex v is the origin.
	 * Note that for an undirected graph, this is the same result returned by
	 * incomingEdges(v).
	 * 
	 * @throws IllegalArgumentException if v is not a valid vertex
	 */
	public Iterable<Edge<E>> outgoingEdges(Vertex<V> v) throws IllegalArgumentException {
		InnerVertex<V> vert = validate(v);
		return vert.elements; // edges are the values in the adjacency map
	}

	/**
	 * Returns the number of edges for which vertex v is the destination. Note that
	 * for an undirected graph, this is the same result returned by outDegree(v).
	 * 
	 * @throws IllegalArgumentException if v is not a valid vertex
	 */
	public int inDegree(Vertex<V> v) throws IllegalArgumentException {

		return outDegree(v);
	}

	/**
	 * Returns an iterable collection of edges for which vertex v is the
	 * destination. Note that for an undirected graph, this is the same result
	 * returned by outgoingEdges(v).
	 * 
	 * @throws IllegalArgumentException if v is not a valid vertex
	 */
	public Iterable<Edge<E>> incomingEdges(Vertex<V> v) throws IllegalArgumentException {

		return this.outgoingEdges(v); // edges are the values in the adjacency map
	}

	/** Returns the edge from u to v, or null if they are not adjacent. */
	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) throws IllegalArgumentException {
		InnerVertex<V> uVertex = validate(u);
		InnerVertex<V> vVertex = validate(v);
		Edge<E> edge = null;
		if (uVertex.elements.size() > vVertex.elements.size()) {
			for (int i = 0; i < uVertex.elements.size(); i++) {
				if (opposite(uVertex, uVertex.elements.get(i)) == v) {
					edge = uVertex.elements.get(i);
				}
			}

		} else {
			for (int i = 0; i < vVertex.elements.size(); i++) {
				if (opposite(vVertex, vVertex.elements.get(i)) == u) {
					edge = vVertex.elements.get(i);
				}
			}
		}

		return edge;
	}

	/**
	 * Returns the vertices of edge e as an array of length two. If the graph is
	 * directed, the first vertex is the origin, and the second is the destination.
	 * If the graph is undirected, the order is arbitrary.
	 */
	public Vertex<V>[] endVertices(Edge<E> e) throws IllegalArgumentException {
		InnerEdge<E> edge = validate(e);
		return edge.getEndpoints();
	}

	/** Returns the vertex that is opposite vertex v on edge e. */
	public Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException {
		InnerEdge<E> edge = validate(e);
		Vertex<V>[] endpoints = edge.getEndpoints();
		if (endpoints[0] == v)
			return endpoints[1];
		else if (endpoints[1] == v)
			return endpoints[0];
		else
			throw new IllegalArgumentException("v is not incident to this edge");
	}

	/** Inserts and returns a new vertex with the given element. */
	public Vertex<V> insertVertex(V element) {
		InnerVertex<V> v = new InnerVertex<>(element);
		v.setPosition(vertices.addLast(v));
		this.hashTable.put(v, v.getElement());
		return v;
	}

	/**
	 * Inserts and returns a new edge between vertices u and v, storing given
	 * element.
	 *
	 * @throws IllegalArgumentException if u or v are invalid vertices, or if an
	 *                                  edge already exists between u and v.
	 */
	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E element) throws IllegalArgumentException {
		InnerVertex<V> origin = validate(u);
		InnerVertex<V> dest = validate(v);
		if (getEdge(u, v) == null) {
			InnerEdge<E> e = new InnerEdge<>(u, v, element);
			e.setPosition(edges.addLast(e));
			origin.elements.add(e);
			dest.elements.add(e);
			return e;
		} else
			throw new IllegalArgumentException("Edge from " + origin.element + " to " + dest.element + " exists");
	}

	/** Removes a vertex and all its incident edges from the graph. */
	@SuppressWarnings("unchecked")
	public void removeVertex(Vertex<V> v) throws IllegalArgumentException {
		InnerVertex<V> vert = validate(v);
		for (int i = 0; i < vert.elements.size(); i++) {
			System.out.println(vert.elements);
			InnerEdge<E> edge = (InnerEdge<E>) vert.elements.get(i);
			InnerVertex<E> vertex = (InnerVertex<E>) this.opposite(vert, vert.elements.get(i));
			vertex.elements.remove(edge);
			vert.elements.remove(edge);
			edges.remove(edge.getPosition());

		}
		hashTable.remove(v);

		vertices.remove(vert.getPosition());
		vert.setPosition(null); // invalidates the vertex
	}

	@SuppressWarnings({})
	/** Removes an edge from the graph. */
	public void removeEdge(Edge<E> e) throws IllegalArgumentException {
		InnerEdge<E> edge = validate(e);
		// remove this edge from vertices' adjacencies
		
		Vertex<V>[] verts = edge.getEndpoints();
		
		InnerVertex<V> v1 = (InnerVertex<V>) verts[0];
		;
		InnerVertex<V> v2 = (InnerVertex<V>) verts[1];
	
		v1.elements.remove(edge);
		v2.elements.remove(edge);
		// remove this edge from the list of edges
		edges.remove(edge.getPosition());
		edge.setPosition(null); // invalidates the edge
	
	}

	@SuppressWarnings({})
	private InnerVertex<V> validate(Vertex<V> v) {
		if (!(v instanceof InnerVertex))
			throw new IllegalArgumentException("Invalid vertex");
		InnerVertex<V> vert = (InnerVertex<V>) v; // safe cast
		if (!vert.validate(this))
			throw new IllegalArgumentException("Invalid vertex");
		return vert;
	}

	@SuppressWarnings({})
	private InnerEdge<E> validate(Edge<E> e) {
		if (!(e instanceof InnerEdge))
			throw new IllegalArgumentException("Invalid edge");
		InnerEdge<E> edge = (InnerEdge<E>) e; // safe cast
		if (!edge.validate(this))
			throw new IllegalArgumentException("Invalid edge");
		return edge;
	}

	// ---------------- nested Vertex class ----------------
	/** A vertex of an adjacency map graph representation. */
	@SuppressWarnings("hiding")
	private class InnerVertex<V> implements Vertex<V> {
		private int key;
		private V element;
		private Position<Vertex<V>> pos;
		private ArrayList<Edge<E>> elements;

		/** Constructs a new InnerVertex instance storing the given element. */
		public InnerVertex(V elem) {
			element = elem;
			this.elements = new ArrayList<>();
			key = rand.nextInt(120);

		}

		/** Validates that this vertex instance belongs to the given graph. */
		public boolean validate(Graph<V, E> graph) {
			return (AdjacencyGraph.this == graph && pos != null);
		}

		/** Returns the element associated with the vertex. */
		public V getElement() {
			return element;
		}

		/** Stores the position of this vertex within the graph's vertex list. */
		public void setPosition(Position<Vertex<V>> p) {
			pos = p;
		}

		/** Returns the position of this vertex within the graph's vertex list. */
		public Position<Vertex<V>> getPosition() {
			return pos;
		}

		@Override
		public int getKey() {

			return key;
		}

	} // ------------ end of InnerVertex class ------------

	// ---------------- nested InnerEdge class ----------------
	/** An edge between two vertices. */
	@SuppressWarnings("hiding")
	private class InnerEdge<E> implements Edge<E> {
		private E element;
		private Position<Edge<E>> pos;
		private Vertex<V>[] endpoints;

		@SuppressWarnings({ "unchecked" })
		/** Constructs InnerEdge instance from u to v, storing the given element. */
		public InnerEdge(Vertex<V> u, Vertex<V> v, E elem) {
			element = elem;
			endpoints = (Vertex<V>[]) new Vertex[] { u, v }; // array of length 2
		}

		/** Returns the element associated with the edge. */
		public E getElement() {
			return element;
		}

		/** Returns reference to the endpoint array. */
		public Vertex<V>[] getEndpoints() {
			return endpoints;
		}

		/** Validates that this edge instance belongs to the given graph. */
		public boolean validate(Graph<V, E> graph) {
			return AdjacencyGraph.this == graph && pos != null;
		}

		/** Stores the position of this edge within the graph's vertex list. */
		public void setPosition(Position<Edge<E>> p) {
			pos = p;
		}

		/** Returns the position of this edge within the graph's vertex list. */
		public Position<Edge<E>> getPosition() {
			return pos;
		}
	} // ------------ end of InnerEdge class ------------

	/**
	 * Returns a string representation of the graph. This is used only for
	 * debugging; do not rely on the string representation.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
//     sb.append("Edges:");
//     for (Edge<E> e : edges) {
//       Vertex<V>[] verts = endVertices(e);
//       sb.append(String.format(" (%s->%s, %s)", verts[0].getElement(), verts[1].getElement(), e.getElement()));
//     }
//     sb.append("\n");
		System.out.println("Vertex");
		for (Vertex<V> v : vertices) {
			sb.append( v.getElement() + " --> ");
			if (isDirected)
				sb.append(" [outgoing]");
			//sb.append(" " + outDegree(v) + " adjacencies:");
			for (Edge<E> e : outgoingEdges(v))
				sb.append(String.format(" (%s, %s)", opposite(v, e).getElement(), e.getElement()));
			sb.append("\n");
			if (isDirected) {
				sb.append(" [incoming]");
				sb.append(" " + inDegree(v) + " adjacencies:");
				for (Edge<E> e : incomingEdges(v))
					sb.append(String.format(" (%s, %s)", opposite(v, e).getElement(), e.getElement()));
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}
