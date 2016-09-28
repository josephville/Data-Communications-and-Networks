import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Analyze a graph and make conclusions about the top-40-ranked vertices
 * @author Joseph Ville
 *
 * Usage: java TopRank <fileName>
 * 		  <fileName> = the name of the graph file to be analyzed
 */
public class TopRank
{
	private static String fileName;
	private static Vertex[] vertices;
	
	/**
	 * Main method for this program
	 * @param args - the command line arguments
	 */
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			usage();
		}
		fileName = args[0];
		
		Collaboration collab = new Collaboration();
		ConnectedComponents cc = new ConnectedComponents(false);
		vertices = collab.readFile(fileName);
		int n = cc.findComponents(vertices);
		ArrayList<Integer> largestCC = cc.getLargestComponent();
		
		int vertex;
		List<SimpleEntry<Integer, Integer>> cloCent = degreeCent(largestCC);
		System.out.println("Rank\tVertex\tDegCen");
		for(int i = 0; i < 40; i++)
		{
			vertex = largestCC.get(i);
			System.out.println((i+1) + "\t" + cloCent.get(vertex).getKey() + "\t" + cloCent.get(vertex).getValue());
		}
		
		List<SimpleEntry<Integer, Float>> avgDistances = closenessCent(largestCC);
		System.out.println("Rank\tVertex\tCloCen");
		for(int i = 0; i < 40; i++)
		{
			vertex = largestCC.get(i);
			System.out.println((i+1) + "\t" + avgDistances.get(vertex).getKey() + "\t" + avgDistances.get(vertex).getValue());
		}
	}

	/**
	 * Compute the closeness centrality of the given connected component
	 * @param cc - the connected component
	 * @return - an ArrayList<SimpleEntry<Integer, Float>> which stores the vertex number and its 
	 * 		associated closeness centrality
	 */
	public static List<SimpleEntry<Integer, Float>> closenessCent(ArrayList<Integer> cc)
	{
		List<SimpleEntry<Integer, Float>> avgDistances = new ArrayList<SimpleEntry<Integer, Float>>(cc.size());
		int current = 0;
		for(int i = 0; i < cc.size(); i++)
		{
			current = cc.get(i);
			avgDistances.add(new SimpleEntry<Integer, Float>(current, avgDistance(current)));
		}
		
		// sort the distances
		Collections.sort(avgDistances, new Comparator<SimpleEntry<Integer, Float>>(){
				@Override
				public int compare(SimpleEntry<Integer, Float> arg0, SimpleEntry<Integer, Float> arg1)
				{
					return arg0.getValue().compareTo(arg1.getValue());
				}
			});
		return avgDistances;
	}
	
	/**
	 * Compute the degree centrality of the given connected component
	 * @param cc - the connected component
	 * @return an ArrayList<SimpleEntry<Integer, Integer>> which stores the vertex number and its
	 * 		associated degree centrality
	 */
	public static List<SimpleEntry<Integer, Integer>> degreeCent(ArrayList<Integer> cc)
	{
		List<SimpleEntry<Integer, Integer>> degrees = new ArrayList<SimpleEntry<Integer, Integer>>(cc.size());
		int current = 0;
		for(int v = 0; v < cc.size(); v++)
		{
			current = cc.get(v);
			degrees.add(new SimpleEntry<Integer, Integer>(current, vertices[current].degree()));
		}
		Collections.sort(degrees, new Comparator<SimpleEntry<Integer, Integer>>(){
			@Override
			public int compare(SimpleEntry<Integer, Integer> arg0, SimpleEntry<Integer, Integer> arg1)
			{
				return arg1.getValue().compareTo(arg0.getValue());
			}
		});
		return degrees;
	}

	/**
	 * Compute the average of the distances between the given vertex and every other vertex
	 * @param vertex - the vertex to find distances from
	 * @return the average distance from this vertex to every other vertex
	 */
	public static float avgDistance(int vertex)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>(); // queue of vertex indices
		boolean[] seen = new boolean[	vertices.length]; // whether this vertex has been seen
		int[] distances = new int[vertices.length];

		for(int i = 0; i < vertices.length; i++) // initialize the arrays
		{
			distances[i] = Integer.MAX_VALUE;
			seen[i] = false;
		}

		seen[vertex] = true;

		queue.add(vertex);
		int a = 0;
		int count = 0;
		distances[vertex] = 0;
		
		while(queue.size() != 0)
		{
			a = queue.poll(); // remove the head of the queue
			for(Integer b : vertices[a].getNeighbors()) // loop through all neighbors of current vertex
			{
				if(!seen[b])
				{
					seen[b] = true;
					queue.add(b); // add b to end of queue
					count++;
					distances[b] = distances[a] + 1;
				} // end if 
			} // end for
		} // end while
		
		int sum = 0;
		for(int i = 0; i < vertices.length; i++)
		{
			if(distances[i] != Integer.MAX_VALUE)
			{
				sum += distances[i];
			}
		}
		return (float) sum / count;
	}
	
	/**
	 * Print a usage message and exit
	 */
	public static void usage()
	{
		System.err.println("Usage: java TopRank <fileName>\n" + 
				"<fileName> = the name of the graph file to be analyzed");
		System.exit(0);
	}
}