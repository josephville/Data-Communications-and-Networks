import java.util.Random;

import edu.rit.numeric.ListSeries;
import edu.rit.sim.Simulation;

/**
 * Class Query provides a query in the the distributed database 
 * querying simulations
 *
 * @author Joseph Ville
 */
public class Query
{	
	private static int idCounter = 0;
	
	private int id;
	private Simulation sim;
	private double startTime;
	private double finishTime;
	private ListSeries respTimeSeries;
	private int V;
   
	private int source; // index of the source node
	private int dest; // index of the destination node
   
	private Random rand;

	/**
	 * Construct a new request. The request's start time is set to the current
     * simulation time.
     * @param  sim - Simulation.
	 */
	public Query(Simulation sim)
	{
		this.sim = sim;
		this.id = ++idCounter;
		this.startTime = sim.time();
	}

	/**
	 * Construct a new request. The request's start time is set to the current
	 * simulation time. The request's response time will be recorded in the
	 * given series.
	 * series.
	 * @param sim - Simulation
	 * @param series - Response time series
	 * @param V - number of vertices
	 */
	public Query(Simulation sim, ListSeries series, int V)
	{
		this (sim);
		this.respTimeSeries = series;
		this.V = V;
		rand = new Random();
		source = rand.nextInt(V);
		dest = rand.nextInt(V);
	}
	
	/**
	 * To reset the id counter after all the trials have been performed for a
	 * one sweep
	 */
	public static void resetIdCounter()
	{
		idCounter = 0;
	}
	
	/**
	 * @return the index of the source node of this query object
	 */
	public int getSource()
	{
		return source;
	}
	
	/**
	 * @returnthe index of the destination node of this query object
	 */
	public int getDest()
	{
		return dest;
	}
	
	/**
	 * Mark this request as finished. The request's finish time is set to the
     * current simulation time. The request's response time is recorded in the
     * response time series.
	 */
	public void finish()
	{
		finishTime = sim.time();
		if (respTimeSeries != null) respTimeSeries.add (responseTime());
	}
	
	/**
	 * Returns this request's response time
	 * @return Response time
	 */
	public double responseTime()
	{
		return finishTime - startTime;
	}

	/**
	 * Returns a string version of this request
	 * @return String version
	 */
	public String toString()
	{
		return "Query " + id;
	}
}