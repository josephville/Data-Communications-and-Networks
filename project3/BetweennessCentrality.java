import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

/**
 * Calculate the betweenness centrality of a graph, 
 * given the graph in Graph File Format
 * @author Joseph Ville
 *
 */
public class BetweennessCentrality
{
	private static int V; // number of vertices
	
	/**
	 * The main method for this program
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			usage();
		}
		Vertex[] vertices;
		int capital; // the rank of the capital city
		Cities cities = new Cities();
		
		cities.readFile(args[0]);
		vertices = cities.minSpanningTree(0);
		V = vertices.length;
		populateNeighbors(vertices);
		List<SimpleEntry<Integer, Double>> bc = betweennessCent(vertices);
		MathContext mc = new MathContext(6); // to format numbers to 6 sig digs
		capital = -1; // should never be -1 one when trying to access it, and we want to know if it is.
		
		System.out.println("Rank\tVertex\tBetweenness");
		for(int i = 0; i < V; i++)
		{
			if(i < 40)
			{
				System.out.println((i+1) + "\t" + bc.get(i).getKey() + "\t" + new BigDecimal(bc.get(i).getValue(), mc));
			}
			if(bc.get(i).getKey() == 0)
			{
				capital = i;
			}
		}
		System.out.println("\nCapital city:");
		System.out.println((capital+1) + "\t" + bc.get(capital).getKey() + "\t" + new BigDecimal(bc.get(capital).getValue(), mc));
	}
	
	/**
	 * Populate the neighbor field for each vertex if there are no 
	 * "e" lines in the graph file
	 * @param vertices
	 */
	public static void populateNeighbors(Vertex[] vertices)
	{
		for(int i = 1; i < vertices.length; i++)
		{
			vertices[i].addNeighbor(vertices[i].getPredecessor());
			vertices[vertices[i].getPredecessor()].addNeighbor(i);
		}
	}
	
	/**
	 * Compute the betweenness centrality of every vertex 
	 * @param vertices
	 * @return a List<SimpleEntry<Integer, Double>> that maps the vertex number (the index) to its betweenness
	 */
	public static List<SimpleEntry<Integer, Double>> betweennessCent(Vertex[] vertices)
	{
		List<SimpleEntry<Integer, Double>> bc = new ArrayList<SimpleEntry<Integer, Double>>();
		int countTotal = 0;
		for(int i = 0; i < V; i++)
		{
			for(int j = i; j < V; j++)
			{
				// if they are not the same vertex
				if(i != j)
				{
					bfs(vertices, i, j);
					countTotal++;
				}
			}
		}
		for(int i = 0; i < V; i++)
		{
			vertices[i].divideNumPaths(countTotal);
			bc.add(new SimpleEntry<Integer, Double>(i, vertices[i].getNumPaths()));
		}
		
		// sort the list according to the values (in this case, the betweenness)
		Collections.sort(bc, new Comparator<SimpleEntry<Integer, Double>>(){
			
			/**
			 * Compare the values of two of the entries of the comparator
			 * @param arg0
			 * @param arg1
			 * @return a negative integer if arg0 <  arg1
			 * 		   zero 			  if arg0 == arg1
			 * 		   a positive integer if arg0 >  arg1
			 */
			@Override
			public int compare(SimpleEntry<Integer, Double> arg0, SimpleEntry<Integer, Double> arg1)
			{
				return arg1.getValue().compareTo(arg0.getValue());
			}
			
		});
		return bc;
	}
	
	/**
	 * Perform a breadth-first search on an array of vertices
	 * @param vertices - array of vertices
	 * @param start - starting vertex
	 * @param dest - destination vertex
	 */
	public static void bfs(Vertex[] vertices, int start, int dest)
	{
		int[] parent = new int[vertices.length];
		boolean[] seen = new boolean[vertices.length];
		LinkedList<Integer> queue;
		
		queue = new LinkedList<Integer>();
		seen[start] = true;
		queue.add(start);
		
		int current = Integer.MIN_VALUE;
		while(!queue.isEmpty())
		{
			current = queue.poll();
			ArrayList<Integer> neighbors = vertices[current].getNeighbors();
			for(int n = 0; n < neighbors.size(); n++)
			{
				if(!seen[neighbors.get(n)])
				{
					seen[neighbors.get(n)] = true;
					queue.add(neighbors.get(n));
					parent[neighbors.get(n)] = current;
				}
			}
		}
		
		// backtrack to update number of paths
		int y = dest;
		while(y != start)
		{
			if(parent[y] != start)
			{
				vertices[parent[y]].incrementNumPaths();
			}
			y = parent[y];
		}
	}
	
	/**
	 * Print a usage message and exit
	 */
	public static void usage()
	{
		System.err.println("Usage: java BetweennessCentrality <fileName>\n"
				+ "<fileName> = a file in Graph File Format");
		System.exit(0);
	}
}
