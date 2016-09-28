import java.util.ArrayList;
import java.util.LinkedList;

import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Create and generate a small-world graph object
 * @author Joseph Ville
 *
 */
public class SmallWorldGraph
{
	private static Node[] nodes;
	private int V;
	private int k;
	private double p;
	private int E;
	private Random prng;
	private Simulation sim;
	private double mu;

	/**
	 * Construct a small world graph
	 * @param V - number of vertices
	 * @param k - graph density parameter
	 * @param p - rewiring probability
	 * @param prng - Random
	 * @param sim - Simulation
	 * @param mu - mean service rate
	 */
	public SmallWorldGraph(int V, int k, double p, Random prng, Simulation sim, double mu)
	{
		nodes = new Node[V];
		this.V = V;
		this.k = k;
		this.p = p;
		this.prng = prng;
		this.sim = sim;
		this.mu = mu;
	}

	/**
	 * Generate a small-world graph by the Watts-Strogatz procedure
	 * @return array of nodes, representing the graph
	 */
	public Node[] generateGraph()
	{
		Node a, b = null, c = null;
		double rand = 0.0;
		
		for(int d = 0; d < V; d++)
		{
			nodes[d] = new Node(d, sim, mu, prng);
		}
		
		for(int i = 0; i <= V-1; i++)
		{
			a = nodes[i];
			for(int j = 1; j <= k; j++)
			{
				b = nodes[(i+j) % V]; // Edge for k-regular graph
				
				if((rand = prng.nextDouble()) < p)
				{
					c = nodes[(int)(rand * V)];
					while(c.equals(a) || c.equals(b) || a.edgeExists(c) || c.edgeExists(a))
					{
						c = nodes[(int)(prng.nextDouble() * V)];
					}
					b = c; // Rewired edge for small-world graph
				}
				a.addNeighbor(b);
				b.addNeighbor(a);
				E++;
			}
		}
		if(!connectedGraph(nodes))
		{
			generateGraph();
		}
		return nodes;
	}
	
	/**
	 * Determine whether a graph is connected by performing a breadth-first 
	 * search on an array of nodes
	 * @param vertices - array of nodes
	 * @return true - if connected
	 * 		   false - otherwise
	 */
	public boolean connectedGraph(Node[] nodes)
	{
		boolean[] seen = new boolean[nodes.length];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int start = 0;
		
		seen[start] = true;
		queue.add(start);
		
		int current = Integer.MIN_VALUE;
		while(!queue.isEmpty())
		{
			current = queue.poll();
			ArrayList<Node> neighbors = nodes[current].neighbors();
			for(int n = 0; n < neighbors.size(); n++)
			{
				if(!seen[neighbors.get(n).id()])
				{
					seen[neighbors.get(n).id()] = true;
					queue.add(neighbors.get(n).id());
				}
			}
		}
		for(int i = 0; i < seen.length; i++)
		{
			if(seen[i] == false)
			{
				return false;
			}
		}
		return true;
	}
}
