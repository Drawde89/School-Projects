import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
/**.
 * The Controller that calls everything
 * 
 * @author Edward Shirinian 214456818
 * @author Jay Patel 21351195
 */
public class Controller {
	private static boolean isQuestion = false;
	static AirportList<String, Integer> airports = new AirportList<String, Integer>();
	private static int isPlus = 0;
	private static Vertex<String> v1;
	private static Vertex<String> v2;
	private static Integer e;
	private static boolean exit = false;

	public static void main(String[] args) {
		while (exit != true)
		{
			isPlus = 0;
			v1 = null;
			v2 = null;
			e = 0;
			isQuestion = false;
			Scanner s = new Scanner(System.in);
			System.out.println("Enter your command: ");
			String command = s.nextLine();
			
			String[] commands = command.split(" ");
			for(String temp: commands) {
				try {
					//System.out.println("isQuestion: " + isQuestion + ", isPlus: " + isPlus);
				Unpack(temp);
				}
				catch (IllegalArgumentException t) {
					System.out.println("37 The Command is Invalid");
					break;
				}
			}
			;
			//System.out.println("isQuestion: " + isQuestion + ", isPlus: " + isPlus);
			//System.out.println("v1: " + v1.getElement() + ", v2: " + v2.getElement() + ", edge: " + e);
			try {
			callFunction();
			}
			catch (IllegalArgumentException t)
			{
				System.out.println("49The Command is Invalid");
			}
		}
		
	}

	/**
	 * Method that calls all Methods in Airport
	 *
	 */
	@SuppressWarnings("unchecked")
	private static void callFunction() {
		
		if (isPlus != 0 && v1!= null && v2 != null && e != 0)
		{
			//System.out.println("Edge: " + e + " V1: " + v1.getElement() + ", V2: " + v2.getElement());
			if (isPlus == 1)
			{
				airports.addConnection(v1, v2, e);
				
				System.out.println(airports.getConnectionsForVertex(v1));
			} else 
			{
				//System.out.println(airports.getAllConnections());
				System.out.println(v1.getElement() + ", " +  v2.getElement() + " , " + e);
					airports.removeConnection(v1, v2, airports.airports.getEdge(v1, v2));
			}
		} else if ( isQuestion == true && v1 != null && v2 !=null)		{
			AdjacencyGraph<String, Integer> graph = airports.getGraph();
			try{
				Map<Vertex<String>, Integer> path = dijkstra(graph, v1);
				System.out.println(path.toString());
				}catch(java.util.NoSuchElementException e){
				System.out.println("The graph is Empty");
			}
			System.out.println(airports.getQuickestRoute(0, new LinkedList<AirportConnection<String,Integer>>(), v1, v2));
		} else if (isQuestion == true && v1 != null )
		{
			System.out.println(airports.getConnectionsForVertex(v1));
		} else if (isQuestion == true)
		{
			System.out.println(airports.getAllConnections());
		} else if (isPlus == 2 && v1 != null)
		{
			airports.removeAirport(v1);
		}
	}

	/**
	 * Method that translates what is stated by String s
	 *@param: String s
	 *
	 */
	private static void Unpack(String s){
		switch(s.toUpperCase())
		{
		case("?"):{	
			System.out.println("case 1");
			if (isPlus != 0|| isQuestion !=false)
			{
				throw new IllegalArgumentException ("102The Command is invalid");
			}
			
			
			isQuestion = true;
			
		}
		break;
		case("+"):
		{
			//System.out.println("case 2");
			if(isQuestion == true||isPlus == 1 || isPlus == 2)
			{
				throw new IllegalArgumentException ("116The Command is invalid");
			}
			isPlus = 1;
		}
		break;
		case("-"):
		{
		//	System.out.println("case 3");
			if(isQuestion == true||isPlus == 1 || isPlus == 2)
			{
				throw new IllegalArgumentException ("126The Command is invalid");
			}
			isPlus = 2;
		}
		break;
		case("QUIT"):
		{
			//System.out.println("case 4");
			exit = true;
			//System.out.println("You have now exited the Command Prompt");
		}
		break;
		default: 
			//System.out.println("case 5: " + s );
			if (isInteger(s))
			{
				//System.out.println("case 5.0: " + s );
				if (v1 != null && v2 != null)
				{
					//System.out.println("   Case 5.1a");
					e = Integer.parseInt(s);
				} else
				{
					//System.out.println("   Case 5.2a");
					throw new IllegalArgumentException ("150The Command is invalid");
				}
			} else 
			{
				//System.out.println("	case 5.0b");
				if (isQuestion == true || isPlus != 0 && v1 == null && v2 == null)
				{
					v1 = airports.addAirport(s);
				//System.out.println("	case 5.1b");
				} else if (isQuestion == true || isPlus != 0)
				{
					//System.out.println("	case 5.2b");
					v2 = airports.addAirport(s);
				} else if (s.toLowerCase() == "plane" && isQuestion !=true && isPlus != 0)
				{
					//System.out.println("	case 5.3b");
				} else 
				{
					//System.out.println("	case 5.4b");
					throw new IllegalArgumentException ("169The Command is invalid");
				}
			}
			
		}
	}
	private static boolean isInteger( String input ) { //Pass in string
		//System.out.println("	case 5.0c");
		try { //Try to make the input into an integer
	        Integer.parseInt( input );
	        return true; //Return true if it works
	    }
	    catch( Exception e ) { 
	        return false; //If it doesn't work return false
	    }
	}
	
	private static Map<Vertex<String>, Integer> dijkstra(Graph<String, Integer> g, Vertex<String> src) {

		Map<Vertex<String>, Integer> d = new HashTable<>();
		Map<Vertex<String>, Integer> cloud = new HashTable<>();
		AdaptablePriorityQueue<Integer, Vertex<String>> pq;
		pq = new HeapAdaptablePriorityQueue<>();
		Map<Vertex<String>, Entry<Integer, Vertex<String>>> pqTokens = new HashTable<>();

		// for each vertex v of the graph, add an entry to the priority queue, with
		// the source having distance 0 and all others having infinite distance.
		for (Vertex<String> v : g.vertices()) {
			if (v == src) {
				d.put(v, 0);
			} else {
				d.put(v, Integer.MAX_VALUE);
			}
			pqTokens.put(v, pq.insert(d.get(v), v)); // save entry for future updates
		}

		// now begin adding reachable vertices to the cloud
		while (!pq.isEmpty()) {
			Entry<Integer, Vertex<String>> entry = pq.removeMin();
			int key = entry.getKey();
			Vertex<String> u = entry.getValue();
			cloud.put(u, key); // this is the actual distance to u
			pqTokens.remove(u);

			for (Edge<Integer> e : g.edges()) {
				Vertex<String> v = g.opposite(u, e);
				if (cloud.get(v) == null) {
					// perform relaxation step on edge (u,v)
					int wgt = e.getElement();
					if (d.get(u) + wgt < d.get(v)) {
						d.put(v, d.get(u) + wgt); // update the distance
						pq.replaceKey(pqTokens.get(v), d.get(v)); // update the pq entry
					}
				}
			}
		}
		return cloud; // this only includes reachable vertices
	}
}