import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Find the connected components of a graph
 * @author Joseph Ville
 * 
 * Usage: java ConnectedComponents <fileName>
 * 		  <fileName> the name of the graph file to be analyzed
 *
 */
public class ConnectedComponents
{
	private static String fileName;
	private static int smallestCCSize;
	private static int largestCCSize;
	private static int largestCC;
	private boolean printData;
	private static TreeMap<Integer, ArrayList<Integer>> ccIndexes;
	
	/**
	 * Default constructor
	 */
	public ConnectedComponents()
	{
		smallestCCSize = Integer.MAX_VALUE;
		largestCCSize = Integer.MIN_VALUE;
	}
	
	/**
	 * Construct an object of this class
	 * @param printResults
	 */
	public ConnectedComponents(boolean printData)
	{
		this.printData = printData;
		smallestCCSize = Integer.MAX_VALUE;
		largestCCSize = Integer.MIN_VALUE;
	}
	
	/**
	 * Main method for this class
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
		Vertex[] vertices = collab.readFile(fileName);
		ConnectedComponents cc = new ConnectedComponents(true);
		
		cc.findComponents(vertices);
		DecimalFormat df = new DecimalFormat("0.#####");
		System.out.println("Size of smallest CC = " + smallestCCSize);
		System.out.println("Size of largest CC = " + largestCCSize);
		System.out.println(largestCCSize + "/" + collab.V() + " = " + df.format((double)largestCCSize / collab.V()));
	}
	
	/**
	 * Find all connected components of an array of vertices
	 * @param vertices - the array of vertices
	 * @return the total number of connected components in the graph
	 */
	public int findComponents(Vertex[] vertices)
	{
		LinkedList<Integer> unvisited = new LinkedList<Integer>();
		LinkedList<Integer> queue = new LinkedList<Integer>();
		ccIndexes = new TreeMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> components;
		
		for(int a = 0; a < vertices.length; a++)
		{
			unvisited.add(a);
		}

		String outString = "connected components\nComp\tSize\n";
		int i = -1, m = -1, n = 0;
		while(unvisited.size() > 0)
		{
			i = unvisited.poll();
			queue.add(i);
			
			components = new ArrayList<Integer>();
			components.add(i);
			while(queue.size() > 0)
			{
				m = queue.poll();
				
				for(Integer neighbor : vertices[m].getNeighbors())
				{
					if(unvisited.contains(neighbor))
					{
						unvisited.remove(neighbor);
						queue.add(neighbor);
						components.add(neighbor);
					}
				}
			}
			
			if(components.size() > largestCCSize)
			{
				largestCC = n;
				largestCCSize = components.size();
			}
			if(components.size() < smallestCCSize)
			{
				smallestCCSize = components.size();
			}
			outString = outString.concat(n + "\t" + components.size() + "\n");
			Collections.sort(components);
			ccIndexes.put(n++, components);
		}
		
		if(printData)
		{
			System.out.print(n + " " + outString);
			printFormat(ccIndexes);
		}
		return n;
	}
	
	/**
	 * Print the CCs and their associated indexes in a readable format
	 * @param ccIndexes - a map of indexes to their CCs
	 */
	public static void printFormat(TreeMap<Integer, ArrayList<Integer>> ccIndexes)
	{
		for(int i = 0; i < ccIndexes.size(); i++)
		{
			System.out.printf("Comp %d = ", i);
			for(int j = 0; j < ccIndexes.get(i).size(); j++)
			{
				System.out.printf("%d ", ccIndexes.get(i).get(j));
			}
			System.out.println();
		}
	}
	
	/**
	 * @return the largest connected component
	 */
	public ArrayList<Integer> getLargestComponent()
	{
		return ccIndexes.get(largestCC);
	}
	
	/**
	 * Print a usage message and exit
	 */
	public static void usage()
	{
		System.err.println("Usage: java ConnectedComponents <fileName>\n" +
				"<fileName> = the name of the graph file to be analyzed");
		System.exit(0);
	}
}// end class ConnectedComponents