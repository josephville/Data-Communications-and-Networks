import java.util.LinkedList;
import java.util.ArrayList;
import edu.rit.util.Random;

/**
 * Generate a random graph
 * @author Joseph Ville
 */
public class Graph
{
	private Random rand; // pseudorandom number generator
	private int[][] distances;

	/**
	 * Construct a graph object
	 */
	public Graph(Random rand)
	{
		this.rand = rand;
	}

	/**
	 * Initialize the array to hold distances
	 * @param distances
	 * @param V
	 */
	public void initializeDist(int[][] distances, int V)
	{
		for(int i = 0; i < V; i++)
		{
			for(int j = 0; j < V; j++)
			{
				distances[i][j] = -1;
			}
		}
	}

	/**
	 * Compute the diameter of the graph
	 * @param V - number of vertices
	 * @param vertices - list of vertices
	 * @return diameter
	 */
	public int diameter(int V, ArrayList<Vertex> vertices)
	{
		distances = new int[V][V];
		initializeDist(distances, V);
		int radius = 0;
		int currDist = 0;
		int diameter = 0;

		// find distance - (# of edges in path) from every vertex to every other vertex
		for(Vertex vertex : vertices)
		{
			for(int i = 0; i < V; i++)
			{
				// find radius - max distance from one vertex to another vertex
				if(distances[vertex.index()][i] != -1)
				{
					currDist = distances[vertex.index()][i];
				}
				else
				{
					currDist = distance(vertices, V, vertex.index(), i);
					distances[vertex.index()][i] = currDist;
					distances[i][vertex.index()] = currDist;
					if(currDist > radius)
					{
						radius = currDist;
					}
				}
			}
			// find diameter - max radius over all vertices
			if(radius > diameter)
			{
				diameter = radius;
			}
		}
		return diameter;
	}

	/**
	 * Find the distance between two vertices
	 * @param adj - adjacency list of vertices
	 * @param V - the number of vertices
	 * @param start - index of the starting vertex
	 * @param dest - index of the destination vertex
	 * @return distance - from start to dest
	 */
	public int distance(ArrayList<Vertex> adj, int V, int start, int dest)
	{
		LinkedList<Integer> queue = new LinkedList<Integer>(); // queue of vertex indices
		Vertex current; // the current vertex
		int[] parent = new int[V]; // parents of each vertex
		boolean[] seen = new boolean[V]; // whether this vertex has been seen
		int distance = 0;

		for(int i = 0; i < V; i++) // initialize the arrays
		{
			parent[i] = -1;
			seen[i] = false;
		}

		seen[start] = true;
		int length = 0;

		queue.add(start);
		int a = -1;

		
		// 	find shortest path from A to B using breadth first traversal
		while(queue.size() != 0)
		{
			a = queue.poll(); // remove the head of the queue
			current = adj.get(a); // store current vertex

			for(Integer b : current.getNeighbors()) // loop through all neighbors of current vertex
			{
				if(!seen[b])
				{
					seen[b] = true;
					queue.add(b); // add b to end of queue
					parent[b] = a; // set the parent

					if(b == dest) // check if destination is reached
					{
						int y = b; // copy b into another variable so I don't have to change b
						while(y != start) // backtrack up the path until starting vertex is reached
						{
							if(parent[y] >= 0) // to ensure we don't try to access parent of root vertex
							{
								y = parent[y]; // y's parent now becomes y for the next pass
								distance++;
							} // end if
						} // end while
					} // end if
				} // end if 
			} // end for
			

		} // end while
		return distance;
	}

	/**
	 * Generate the graph
	 * @param V - the number of vertices
	 * @param p - the edge probability
	 * @return a list of the vertices of the graph
	 */
	public ArrayList<Vertex> generateGraph(int V, double p)
	{
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(V);

		for(int i = 0; i < V; i++)
		{
			vertices.add(new Vertex(i, new ArrayList<Integer>(V-1)));			
		}

		// For each vertex
		for(int a = 0; a < V - 1; a++)
		{
			for(int b = a + 1; b < V; b++)
			{
				if(rand.nextDouble() < p)
				{
					vertices.get(a).getNeighbors().add(b);
					vertices.get(b).getNeighbors().add(a);
				}
			}
		}
		return vertices;
	}
}
