import edu.rit.numeric.ExponentialPrng;
import edu.rit.numeric.ListSeries;
import edu.rit.numeric.Series;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class Generator generates queries for the distributed database 
 * querying simulations
 *
 * @author  Alan Kaminsky
 * 		modified by Joseph Ville
 * @version 18-Apr-2014
 */
public class Generator
{
	private Node[] nodes;
	private Simulation sim;
	private ExponentialPrng lambdaPrng;
	private int N;
	private Node sourceNode;
	
	private ListSeries respTimeSeries;
	private int n;
	private int V;
	
	/**
	 * Create a new request generator.
	 *
	 * @param  sim     Simulation.
	 * @param  lambda    Request mean interarrival time.
	 * @param  N    Number of requests.
	 * @param  prng    Pseudorandom number generator.
	 * @param  sourceNode  Server.
	 * @param V - number of nodes
	 */
	public Generator(Simulation sim, double lambda, int N, Random prng, Node[] nodes, int V)
	{
		this.sim = sim;
		this.N = N;
		this.lambdaPrng = new ExponentialPrng (prng, lambda);
		this.nodes = nodes;
		this.V = V;
		respTimeSeries = new ListSeries();
		n = 0;

		generateQuery();
	}
	
	/**
	 * Generate the next request.
	 */
	private void generateQuery()
	{
		// starting node should be chosen uniformly at random here,
		// where the query is being added
		Query query = new Query (sim, respTimeSeries, V);
		sourceNode = nodes[query.getSource()];
		sourceNode.add(query);
		++n;
		if (n < N)
		{
			sim.doAfter (lambdaPrng.next(), new Event()
			{
				public void perform()
				{
					generateQuery();
				}
			});
		}
	}

	/**
	 * Returns a data series containing the response time statistics of the
	 * generated requests.
	 *
	 * @return  Response time series.
	 */
	public Series responseTimeSeries()
	{
		return respTimeSeries;
	}

	/**
	 * Returns the response time statistics of the generated requests.
	 *
	 * @return  Response time statistics (mean, standard deviation, variance).
	 */
	public Series.Stats responseTimeStats()
	{
		return respTimeSeries.stats();
	}

	/**
	 * Returns the drop fraction of the generated requests.
	 */
	public double dropFraction()
	{
		return (double)(N - respTimeSeries.length())/(double)N;
	}
}
