import java.util.ArrayList;

import edu.rit.numeric.ExponentialPrng;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * A class to store the attributes of a node representing a distributed
 * database and to process queries traveling from between nodes.
 * 
 * @author Joseph Ville
 *
 */
public class Node
{
	/**
	 * Whether to print the transcripts of query processing
	 */
	public static boolean transcript = false;
	
	private ExponentialPrng muPrng;
	private Simulation sim;
	private int id;
	private ArrayList<Node> neighbors;
	
	/**
	 * Construct a Node object
	 * @param id - the node's id
	 */
	public Node(int id)
	{
		this.id = id;
	}
	
	/**
	 * Construct a Node object
	 * @param id - the node's id
	 * @param sim - a Simulation
	 * @param mu - mean request processing time
	 * @param prng - pseudorandom number generator
	 */
	public Node(int id, Simulation sim, double mu, Random prng)
	{
		this.id = id;
		this.sim = sim;
		
		this.muPrng = new ExponentialPrng(prng, mu);
		neighbors = new ArrayList<Node>();
	}
	
	/**
	 * @return id of the current node
	 */
	public int id()
	{
		return this.id;
	}
	
	/**
	 * @return neighbors of the current node
	 */
	public ArrayList<Node> neighbors()
	{
		return neighbors;
	}
	
	/**
	 * Add the given query
	 * @param query - Query
	 */
	public void add(Query query)
	{
		if(transcript)
		{
			System.out.printf("%.3f %s added %d%n", sim.time(), query, this.id);
		}
		startProcessing(query);
	}
	
	/** 
	 * Start processing
	 * @param query - Query
	 */
	private void startProcessing(Query query)
	{
		if(transcript)
		{
			System.out.printf("%.3f %s starts processing%n", sim.time(), query);
		}
		sim.doAfter(muPrng.next(), new Event()
		{
			public void perform()
			{
				finishProcessing(query);
			}
		});
	}
	
	/**
	 * Finish processing
	 * @param query - Query
	 */
	private void finishProcessing(Query query)
	{
		if(transcript)
		{
			System.out.printf ("%.3f %s finishes sending from %s to %s%n", 
					sim.time(), query, query.getSource(), query.getDest());
		}
		Node next = nextHop();
		
		if(this.id == query.getDest())
		{
			if(transcript)
			{
				System.out.printf("%.3f %s finishes processing%n", sim.time(), query);
			}
			query.finish();
		}
		else
		{
			next.add(query);
		}
	}
	
	/**
	 * Whether an edge exists between this node and the node 
	 * at the given index
	 * @param v
	 * @return true - if there is an edge
	 * 		   false - otherwise
	 */
	public boolean edgeExists(Node v)
	{
		if(neighbors.contains(v))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Choose the next hop from the list of this node's neighbors
	 * @return the index of the next hop
	 */
	public Node nextHop()
	{
		java.util.Random rand = new java.util.Random();
		int next = rand.nextInt(this.neighbors.size());
		return this.neighbors.get(next);
	}
	
	/**
	 * Adds a vertex number to the end of this vertex's neighbor list
	 * @param n - the neighbor to be added
	 */
	public void addNeighbor(Node n)
	{
		this.neighbors.add(n);
	}
	
	/**
	 * Determine whether the current node equals the node passed in
	 * @param n
	 * @return true - if they are equal
	 * 			false - otherwise
	 */
	public boolean equals(Node n)
	{
		if(this.id == n.id)
		{
			return true;
		}
		return false;
	}
}
