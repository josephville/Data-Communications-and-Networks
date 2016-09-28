import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * A class to hold the method for computing the average simulated time 
 * over t trials to process N queries
 * 
 * @author Joseph Ville
 *
 */
public class TotalTime
{	
	/**
	 * Creates simulation, generates graph and query generator, runs simulation,
	 * and averages total time over all the trials
	 * @param v - number of vertices
	 * @param p - rewiring probability
	 * @param t - number of trials
	 * @param seed - seed value for the prng
	 * @param k - graph density parameter
	 * @param lambda - mean arrival rate
	 * @param mu - mean service rate
	 * @param N - number of queries
	 * @return average total time for all the trials
	 */
	public static double average(int v, double p, int t, long seed, int k, 
			double lambda, double mu, int N)
	{
		double timeTotal = 0.0;
		SmallWorldGraph swg;
		Node[] nodes;
		Random prng;
		Simulation sim;
		
		// repeat for the given number of trials
		for(int i = 0; i < t; i++)
		{
			prng = new Random(seed);
			
			// Set up Simulation
			sim = new Simulation();
	
			// Set up the graph
			swg = new SmallWorldGraph(v, k, p, prng, sim, mu);
			nodes = swg.generateGraph();
			
			// Set up query generator and generate the first query
			new Generator(sim, lambda, N, prng, nodes, v);

			// Run the simulation
			sim.run();
			timeTotal += sim.time();
			
			Query.resetIdCounter();
		}
		return timeTotal/t;
	}
}
