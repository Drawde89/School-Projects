import java.util.LinkedList;

public class AirplaneTester {
	
	public static void  main(String [] args)
	{
		AirportList<String, Integer> airports = new AirportList<String, Integer>();
		Vertex<String> yyz =airports.addAirport("YYZ");
		Vertex<String> jfk = airports.addAirport("JFK");
		Vertex<String> hia = airports.addAirport("HIA");
		// add connections
		airports.addConnection(yyz, jfk, 200);
		airports.addConnection(yyz, hia, 200);
		airports.addConnection(hia, jfk, 200);
		Edge<Integer> e = airports.airports.getEdge(yyz, jfk);
		System.out.println("Full list of connections: " + airports.getAllConnections());
		//System.out.println("Shortest Distance: " + airports.getQuickestRoute(0, new LinkedList<AirportConnection<String,Integer>>(), yyz, jfk));
		System.out.println("connections for vertex yyz: " + airports.getConnectionsForVertex(yyz));
		
		// removing an airport
		airports.removeAirport(hia);
		System.out.println("connections after aiport r for vertex yyz: " + airports.getConnectionsForVertex(yyz));
		
		airports.removeConnection(jfk, yyz, e);
		System.out.println("connections for vertex yyz: " + airports.getConnectionsForVertex(yyz));
		
	}

}