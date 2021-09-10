
public class Tester {

	public static void main(String[] args) {
	
		AdjacencyGraph<String, Integer> graph = new AdjacencyGraph<String, Integer>();
		
		Vertex<String> york = graph.insertVertex("York");
		Vertex<String> home = graph.insertVertex("Home");
		Vertex<String> work = graph.insertVertex("Work");
		HashTable<Vertex<String>, String > hashTable = graph.hashTable;
		System.out.println("Getting work from hashTable: " + hashTable.get(work));
		Edge<Integer> homeWork = graph.insertEdge(home, work, 15);
		Edge<Integer> homeYork = graph.insertEdge(home, york, 20);
		Edge<Integer> workYork = graph.insertEdge(work, york, 18);
		
		System.out.println(graph.toString());		
		
	}

}
