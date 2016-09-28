import java.text.DecimalFormat;

/**
 * Perform a discrete event simulation that will sweep through a 
 * series of specified values for the rewiring probability p.
 * 
 * @author Joseph Ville
 *
 */
public class PSweepDiscEventSim
{
	private static long seed;
	private static int V;
	private static int k;
	private static double lowerP;
	private static double upperP;
	private static double pIncrement;
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
		 *  there can be either 10 or 11 command line args, because the last
		 *  one, transcript, is optional
		 */
		if(args.length < 10 || args.length > 11)
		{
			usage();
		}
		seed = Long.parseLong(args[0]);
		V = Integer.parseInt(args[1]);
		k = Integer.parseInt(args[2]);
		lowerP = Double.parseDouble(args[3]);
		upperP = Double.parseDouble(args[4]);
		pIncrement = Double.parseDouble(args[5]);
		lambda = Double.parseDouble(args[6]);
		mu = Double.parseDouble(args[7]);
		N = Integer.parseInt(args[8]);
		t = Integer.parseInt(args[9]);
		
		if(args.length == 11)
		{
			Node.transcript = Boolean.parseBoolean(args[10]);
		}
		
		if(k > V/2)
		{
			System.err.println("Usage: k should not be greater than V/2");
			System.exit(0);
		}
		
		System.out.print("java PSweepDiscEventSim ");
		for(String arg : args)
		{
			System.out.print(arg + " ");
		}
		System.out.println();
		double time = 0.0;
		System.out.println("p\tavg total time");
		for(double p = lowerP; p <= upperP; p+=pIncrement)
		{
			time = TotalTime.average(V, p ,t, seed, k, mu, lambda, N);
			System.out.println(new DecimalFormat("0.000").format(p) + "\t" +
					new DecimalFormat("0.00000").format(time));
		}
	}
	
	/**
	 * Print a usage message and exit
	 */
	public static void usage()
	{
		System.err.println("Usage: java DiscEventSim <seed> <V> <k> <lowerP> <upperP> <pIncrement> <lambda> <mu> <N> <t> <transcript>\n"
				+ "<seed> = seed for graph generation\n"
				+ "<V> = number of vertices\n"
				+ "<k> = parameter to determine graph density\n"
				+ "<lowerP> = lower bound of p = rewiring probability\n"
				+ "<upperP> = upper bound of p = rewiring probability\n"
				+ "<pIncrement> = the value by which to increment p\n"
				+ "<lambda> = mean query arrival rate\n"
				+ "<mu> = mean query service rate\n"
				+ "<N> = number of queries to create\n"
				+ "<t> = number of trials\n"
				+ "<transcript> (optional) = a boolean whether to print the transcript. false if omitted.");
		System.exit(0);
	}
}
