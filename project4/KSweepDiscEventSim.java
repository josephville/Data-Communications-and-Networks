import java.text.DecimalFormat;

/**
 * Perform a discrete event simulation that will sweep through a 
 * series of specified values for the graph density parameter k.
 * 
 * @author Joseph Ville
 *
 */
public class KSweepDiscEventSim
{
	private static long seed;
	private static int V;
	private static int lowerK;
	private static int upperK;
	private static double p;
	private static double lambda;
	private static double mu;
	private static int N;
	private static int t;
	
	/**
	 * Main method for this program
	 * @param args
	 */
	public static void main(String[] args)
	{
		/*
		 *  there can be either 9 or 10 command line args, because the last
		 *  one, transcript, is optional
		 */
		if(args.length < 9 || args.length > 10)
		{
			usage();
		}
		seed = Long.parseLong(args[0]);
		V = Integer.parseInt(args[1]);
		lowerK = Integer.parseInt(args[2]);
		upperK = Integer.parseInt(args[3]);
		p = Double.parseDouble(args[4]);
		lambda = Double.parseDouble(args[5]);
		mu = Double.parseDouble(args[6]);
		N = Integer.parseInt(args[7]);
		t = Integer.parseInt(args[8]);
		
		if(args.length == 10)
		{
			Node.transcript = Boolean.parseBoolean(args[9]);
		}
		
		if(upperK > V/2)
		{
			System.err.println("Usage: upperK should not be greater than V/2");
			System.exit(0);
		}
		
		System.out.print("java KSweepDiscEventSim ");
		for(String arg : args)
		{
			System.out.print(arg + " ");
		}
		System.out.println();
		double time = 0.0;
		System.out.println("k\tavg total time");
		for(int k = lowerK; k <= upperK; k++)
		{
			time = TotalTime.average(V, p, t, seed, k, mu, lambda, N);
			System.out.println(k + "\t" + new DecimalFormat("0.00000").format(time));
		}
	}

	/**
	 * Print a usage message and exit
	 */
	public static void usage()
	{
		System.err.println("Usage: java KSweepDiscEventSim <seed> <V> <lowerK> <upperK> <p> <lambda> <mu> <N> <t> <transcript>\n"
				+ "<seed> = seed for graph generation\n"
				+ "<V> = number of vertices\n"
				+ "<lowerK> = lower k parameter (an integer >= 1) to determine graph density\n"
				+ "<upperK> = upper k parameter to determine graph density\n"
				+ "<p> = rewiring probability	\n"
				+ "<lambda> = mean query arrival rate\n"
				+ "<mu> = mean query service rate\n"
				+ "<N> = number of queries to create\n"
				+ "<t> = number of trials\n"
				+ "<transcript> (optional) = a boolean whether to print the transcript. false if omitted.");
		System.exit(0);
	}
}
