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
 * Perform a Monte Carlo simulation using seed, V, and increment as the knob values
 * 
 *  Usage: java pj2 MonteCarloVSmp <seed> <lowerV> <upperV> <p> <T> <increment>
 *	 <seed> = Random seed
 *	 <lowerV> = Lower bound of number of vertices
 *	 <upperV> = Upper bound of number of vertices
 *  <p> = Edge probability
 *  <increment> = number by which to increment the knob
 *	 <T> = Number of trials
 *
 * @author Joseph Ville
 *
 */
public class MonteCarloVSmp extends Task
{
	private long seed; // seed for pseudorandom graph generation
	private int lowerV; // upper bound of number of vertices
	private int upperV; // lower bound of number of vertices
	private double p; // edge probability
	private long T; // # of trials
	private int increment; // the value by which to increment the knob

	/**
	 * The default constructor for the class
	 */
	public MonteCarloVSmp()
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
		lowerV = Integer.parseInt(args[1]);
		upperV = Integer.parseInt(args[2]);
		p = Double.parseDouble(args[3]);
		T = Long.parseLong(args[4]);
		increment = Integer.parseInt(args[5]);

		// print the command line used to run this code
		System.out.print("$ java pj2 MonteCarloPSeq");
		for(String arg : args)
		{
			System.out.print(" " + arg);
		}
		System.out.println();

		System.out.println("V\t\tAvg d");

		for(int v1 = lowerV; v1 <= upperV; v1 += increment)		
		{
			final DoubleVbl.Sum sumVbl = new DoubleVbl.Sum();

			int vHold = v1;
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
					vertices = graph.generateGraph(vHold, p);
					// thrSum.reduce(new DoubleVbl((double)graph.diameter(vHold, vertices)));
					thrSum.item += (double)graph.diameter(vHold, vertices);
				}
			});
			avg = sumVbl.item / T;
			System.out.println(vHold + "\t\t" + avg);
		}
	}// end main()

	/**
	 * Print a usage message and throw exception
	 */
	private static void usage()
	{
		System.err.println("Usage: java pj2 MonteCarloVSmp <seed> <lowerV> <upperV> <p> <T> <increment>\n" +
				"<seed> = Random seed\n" + 
				"<lowerV> = Lower bound of number of vertices\n" +
				"<upperV> = Upper bound of number of vertices\n" +
				"<p> = Edge probability\n" +
				"<T> = Number of trials\n" +
				"<increment> = the value by which to increment V (an integer)");
		throw new IllegalArgumentException();
	}
}
