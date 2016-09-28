import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DecimalFormat;
import edu.rit.pj2.Task;
import edu.rit.util.Random;

import edu.rit.pj2.LongLoop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.pj2.vbl.DoubleVbl.Sum;

/**
 * Perform a Monte Carlo simulation, using seed, p, and increment as the knob values
 * 
 *  Usage: java pj2 MonteCarloPSmp <seed> <V> <lowerP> <upperP> <T> <increment>
 *	<seed> = Random seed
 * <V> = number of vertices
 *	<lowerP> = Lower bound of edge probability
 *	<upperP> = Upper bound of edge probability
 * <increment> = number by which to increment the knob
 *	<T> = Number of trials
 *
 * @author Joseph Ville
 *
 */
public class MonteCarloPSmp extends Task
{
	private long seed; // seed for pseudorandom graph generation
	private int V; // number of vertices
	private double lowerP; // upper bound edge probability
	private double upperP; // edge probability
	private long T; // # of trials
	private double increment; // the value by which to increment V

	/**
	 * The default constructor for the class
	 */
	public MonteCarloPSmp()
	{
	}

	/**
	 * Main method for the program
	 * @param args - the command line arguments
	 */
	public void main(String[] args) throws Exception
	{
		int V; // number of vertices for the current iteration

		if(args.length != 6)
		{
			usage();
		}
		
		seed = Long.parseLong(args[0]);
		V = Integer.parseInt(args[1]);
		lowerP = Double.parseDouble(args[2]);
		upperP = Double.parseDouble(args[3]);
		T = Long.parseLong(args[4]);
		increment = Double.parseDouble(args[5]);

		// print the command line used to run this code
		System.out.print("$ java pj2 MonteCarloPSeq");
		for(String arg : args)
		{
			System.out.print(" " + arg);
		}
		System.out.println();

		// int sum = 0;
		// double avg = 0.0;
		// double count = 0.0;
		System.out.println("p\t\tAvg d");

		for(double p1 = lowerP; p1 <= upperP; p1 += increment)		
		{
			final DoubleVbl.Sum sumVbl = new DoubleVbl.Sum();

			double pHold = p1;
			double avg;

			// do T trials in parallel
			parallelFor(0, T - 1).exec(new LongLoop()
			{
				// Per-thread variables
				ArrayList<Vertex> vertices;
				Random rand;
				Graph graph;
				DoubleVbl.Sum thrSum;

				/**
				 * initialize per-thread variables
				 */
				public void start()
				{
					rand = new Random(seed + rank());
					graph = new Graph(rand);
					thrSum = threadLocal(sumVbl);
				}				

				/**
				 * Loop body
				 */
				public void run(long t)
				{
					// tHold = (int)t; // assign the storage variable
					vertices = graph.generateGraph(V, pHold);
					thrSum.item += (double)graph.diameter(V, vertices);
				}
			});
			avg = sumVbl.doubleValue() / T;
			System.out.println(pHold + "\t\t" + avg);
			avg = 0.0;
		}
	}// end main()

	/**
	 * Print a usage message and throw exception
	 */
	private static void usage()
	{
		System.err.println("Usage: java pj2 MonteCarloPSmp <seed> <V> <lowerP> <upperP> <T> <increment>\n" +
				"<seed> = Random seed\n" + 
				"<V> = the number of vertices\n" +
				"<lowerP> = Lower bound of Edge probability range\n" +
				"<upperP> = Upper bound of Edge probability range\n" +
				"<T> = Number of trials\n" +
				"<increment> = the value by which to increment p (a decimal number)");
		throw new IllegalArgumentException();
	}
}
