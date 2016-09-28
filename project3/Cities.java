import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

import edu.rit.util.PriorityQueue;

/**
 * Process a graph file with the locations of cities and construct a 
 * minimum spanning tree from that graph
 * @author Joseph Ville
 *
 */
public class Cities
{
	private static int V; // number of vertices
	private static int E; // number of edges
	private static Vertex[] vertices; // array of vertices
	private static double totalDistance; // total distance of the MST
	private static boolean eLine; // whether there's an edge line in the graph file
	
	/**
	 * Default constructor
	 */
	public Cities()
	{
	}
	
	/**
	 * Main method for this program
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			usage();
		}
		
		Cities cities = new Cities();
		cities.readFile(args[0]);
		
		// start at vertex 0, because it is the center city of the empire
		int startingVertex = 0;
		
		cities.minSpanningTree(startingVertex);
		totalDistance = 0.0;
		E = V - 1;
		String city = args[0].substring(0, args[0].indexOf('-'));
		cities.writeFile(city + "-roads.txt");
		
		// totalDistance is calculated in writeFile()
		System.out.println("Total distance = " + new DecimalFormat("0.000").format(totalDistance));
	}

	/**
	 * Compute the minimum spanning tree of the graph
	 * @param startingVertex - the starting vertex
	 * @return the vertices, as a minimum spanning tree
	 */
	public Vertex[] minSpanningTree(int startingVertex)
	{
		PriorityQueue<Vertex> minPQ = new PriorityQueue<Vertex>();
		
		for(int i = 0; i < vertices.length; i++)
		{
			vertices[i].setPredecessor(Integer.MIN_VALUE);
			vertices[i].setDistance(Double.POSITIVE_INFINITY);
		}
		vertices[startingVertex].setDistance(0);
		
		for(int i = 0; i < vertices.length; i++)
		{
			minPQ.add(vertices[i]);
		}
		
		while(!minPQ.isEmpty())
		{
			Vertex v = minPQ.remove();
			
			// the original graph is completely connected, so every other 
			// vertex is adjacent to v
			for(Vertex w : vertices)
			{
				if(v.getIndex() != w.getIndex())
				{
					double vwDist = euclideanDistance(v.getX(), v.getY(), w.getX(), w.getY());
					if(w.enqueued() && (vwDist < w.getDistance()))
					{
						w.setPredecessor(v.getIndex());
						w.setDistance(vwDist);
						w.increasePriority();
					}
				}
			}
		}
		return vertices;
	}

	/**
	 * Write to a file, first the list of vertices, each with it's (x, y) coordinates
	 * @param outFile - the name of the file to write to
	 */
	public void writeFile(String outFile)
	{
		MathContext mc = new MathContext(6);
		
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			bw.write("g " + V + " " + E + "\n");
			for(int i = 1; i < V; i++)
			{
				bw.write("d " + i + " " + new BigDecimal(vertices[i].getX(), mc) + " " + new BigDecimal(vertices[i].getY(), mc) + "\n");
			}
			
			for(int i = 1; i < V; i++)
			{
				bw.write("e " + i + " " + vertices[i].getPredecessor() + "\n");
				totalDistance += vertices[i].getDistance();
			}
			bw.close();
		} 
		catch (IOException e)
		{
			System.err.println("There was an error writing to the file.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads and processes a file in the Graph File Format
	 * @param fileName - the file to process
	 * @return vertices - an array of vertices for the graph
	 * @throws Exception
	 */
	public Vertex[] readFile(String fileName)
	{
		String line = "";
		
		try
		{
			BufferedReader buff = new BufferedReader(new FileReader(fileName));

			boolean gLine = false;
			eLine = false;
			
			while((line = buff.readLine()) != null)
			{
				String[] lineArr;

				// ignore any blank line
				if(!line.equals(null) && !line.isEmpty())
				{
					lineArr = line.split(" ");

					/* Required. Edges of the graph.
					First field - source vertex #, 0 <= an int <= V-1
					Second field - destination vertex #, 0 <= an int <= V-1
					Third field - edge weight, a floating pt #. If 3rd field omitted,
						assume to be 1 by default.
					*/
					if(lineArr[0].equals("e"))
					{
						eLine = true;
						int index = Integer.parseInt(lineArr[1]);
						int neighbor = Integer.parseInt(lineArr[2]);

						initializeIfNull(index, V);
						initializeIfNull(neighbor, V);
						vertices[index].addNeighbor(neighbor);
						vertices[neighbor].addNeighbor(index);
					}
					/* Required. Occurs once, at beginning of file
					The parameters of the graph.
					First field is # of vertices V, an int >= 0
					Second field is # of edges E, an int >= 0 
					*/
					else if(lineArr[0].equals("g"))
					{
						gLine = true;
						int numVertices = Integer.parseInt(lineArr[1]); // V and E are guaranteed to be >= 0
						int numEdges = Integer.parseInt(lineArr[2]);

						/* ok to do this here, because this will be executed before
						any of the other if statements on this or any other
						pass of the while loop */
						V = numVertices;
						E = numEdges;
						vertices = new Vertex[V];
					}
					// ignore
					else if(lineArr[0].equals("v"))
					{
						continue;
					}
					// coordinates of the vertex
					else if(lineArr[0].equals("d"))
					{
						int index = Integer.parseInt(lineArr[1]);
						double x = Double.parseDouble(lineArr[2]);
						double y = Double.parseDouble(lineArr[3]);
						
						initializeIfNull(index, V);
						vertices[index].setX(x);
						vertices[index].setY(y);
						vertices[index].setIndex(index);
					}
					// ignore
					else if(lineArr[0].equals("c"))
					{
						continue;
					}
				}// end if
			}// end while

			buff.close();
			
			// make sure the file contained the required lines
			if(gLine == false)
			{
				System.err.println("The file is in an invalid format.");
				System.exit(0);
			}
		}
		catch(NumberFormatException nfe)
		{
			System.err.println("A number in the file had invalid format");
			nfe.printStackTrace();
		}
		catch(Exception ex)
		{
			System.err.println("There was an error reading the file");
			ex.printStackTrace();
		}
		return vertices;
	}// end readFile()
	
	/**
	 * Compute the Euclidean distance between two points, given the (x, y)
	 * coordinates of each.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance
	 */
	public static double euclideanDistance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
	
	/**
	 * Check if a vertex is null, and if it is, initialize it.
	 * @param index - the vertex to check
	 * @param V - the number of vertices
	 */
	private static void initializeIfNull(int index, int V)
	{
		if(vertices[index] == null)
		{
			vertices[index] = new Vertex(index, V);
		}
	}
	
	/**
	 * @return true - if the file contains edge lines
	 * 		   false - if it does not
	 */
	public boolean fileHasEdges()
	{
		return eLine;
	}
	
	/**
	 * Print a usage message and exit
	 */
	public static void usage()
	{
		System.err.println("Usage: java Cities <fileName>"
				+ "<fileName> = name of a file in graph file format");
		System.exit(0);
	}
}
