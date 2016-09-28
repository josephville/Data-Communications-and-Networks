import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Analyzes a Collaboration Graph
 *
 * @author Joseph Ville
 */
public class Collaboration
{
	private int V;
	private int E;
	private Vertex[] vertices;
	
	/**
	 * Construct an object of this class
	 */
	public Collaboration()
	{
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
			boolean eLine = false;
			
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

						initializeIfNull(vertices, index);
						initializeIfNull(vertices, neighbor);
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
						this.V = numVertices;
						this.E = numEdges;
						vertices = new Vertex[V];
					}
					// ignore any lines beginning with v, c, or d
					else if(lineArr[0].equals("v"))
					{
						continue;
					}
					else if(lineArr[0].equals("d"))
					{
						continue;
					}
					else if(lineArr[0].equals("c"))
					{
						continue;
					}
				}// end if
			}// end while

			buff.close();
			
			// make sure the file contained the required lines
			if(gLine == false || eLine == false)
			{
				System.err.println("The file is in an invalid format.");
				System.exit(0);
			}
		}
		catch(NumberFormatException nfe)
		{
			System.err.println("A number in the file had invalid format");
			System.exit(0);
		}
		catch(Exception ex)
		{
			System.err.println("There was an error reading the file");
			System.exit(0);
		}
		return vertices;
	}// end readFile()
	
	/**
	 * @return the number of vertices
	 */
	public int V()
	{
		return V;
	}

	/**
	 * @return the number of edges
	 */
	public int E()
	{
		return E;
	}

	/**
	 * Check if a vertex is null, and if it is, initialize it.
	 * @param v - the vertex to check
	 */
	private void initializeIfNull(Vertex[] vertices, int v)
	{
		if(vertices[v] == null)
		{
			vertices[v] = new Vertex();
		}
	}
}// end class Collaboration
