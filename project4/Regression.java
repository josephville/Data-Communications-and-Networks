import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.TreeMap;

import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.XYSeries;

/**
 * Perform regressions on a graph, and analyze the results
 * @author Joseph Ville
 * 
 * Usage: java Regression <fileName>
 *		  <fileName> = the name of the graph file to be analyzed
 *
 */
public class Regression
{
	private static Collaboration collab;
	private static String fileName;
	private static Vertex[] vertices;
	private static XYSeries.Regression expReg;
	private static XYSeries.Regression powReg;
	
	/**
	 * Main method for this program
	 * @param args - the command line arguments
	 */
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			usage();
		}
		fileName = args[0];
		collab = new Collaboration();
		vertices = collab.readFile(fileName);
		TreeMap<Integer, Integer> occurrences = degreeOccurrences(vertices);
		
		executeRegressions(occurrences);

		MathContext mathContext = new MathContext(5);

		// power
		double cP = Math.exp(powReg.a);
		System.out.print("log pr(d) = " + new BigDecimal(Math.log(cP), mathContext) + " + " + new BigDecimal(powReg.b, mathContext) + " log d");
		System.out.println(", corr = " + new BigDecimal(powReg.corr, mathContext));
		System.out.println("pr(d) = " + new BigDecimal(cP, mathContext) + " d^" + new BigDecimal(powReg.b, mathContext));
		
		// exponential
		double cE = Math.exp(expReg.a);	
		double d = Math.exp(expReg.b);
		System.out.print("log pr(d) = " + new BigDecimal(Math.log(cE), mathContext) + " + " + new BigDecimal(Math.log(d), mathContext) + " d");
		System.out.println(", corr = " + new BigDecimal(expReg.corr, mathContext));
		System.out.println("pr(d) = " + new BigDecimal(cE, mathContext) + " * " + new BigDecimal(d, mathContext) + "^d");
	}

	/**
	 * Performs linear regressions on an exponential function and a power function
	 * @param degreeCounts - map of degrees to the number of occurrences
	 */
	public static void executeRegressions(TreeMap<Integer, Integer> degreeCounts)
	{
		ListXYSeries expXYSeries = new ListXYSeries();
		ListXYSeries powXYSeries = new ListXYSeries();
		DecimalFormat d = new DecimalFormat("#");
		DecimalFormat d1 = new DecimalFormat("0.00000E00");
		System.out.println("d\tcount\tpr");
		for(Integer key : degreeCounts.keySet())
		{
			double probD = (double)degreeCounts.get(key) / collab.V();
			System.out.println(key + "\t" + d.format(degreeCounts.get(key)) + "\t" + sFormat(d1.format(probD).toString()));
			expXYSeries.add(key, Math.log(probD));
			powXYSeries.add(Math.log(key), Math.log(probD));
		}
		expReg = expXYSeries.linearRegression();
		powReg = powXYSeries.linearRegression();
	}
	
	/**
	 * Find the number of occurrences of each degree in an of an 
	 * array of vertices
	 * @param vertices
	 */
	public static TreeMap<Integer, Integer> degreeOccurrences(Vertex[] vertices)
	{
		// Using a TreeMap so it will be ordered automatically by vertex degree
		TreeMap<Integer, Integer> occurrences = new TreeMap<Integer, Integer>();
		
		for(Vertex v : vertices)
		{
			if(occurrences.containsKey(v.degree()))
			{
				int value = occurrences.get(v.degree());
				occurrences.put(v.degree(), ++value);
			}
			else
			{
				occurrences.put(v.degree(), 1);
			}
		}
		return occurrences;
	}
	
	/**
	 * Format the given string so that "E"s are "e"s
	 * @param input - the given string
	 * @return the formatted string
	 */
	public static String sFormat(String input)
	{
		return input.replaceAll("E", "e");
	}
	
	/**
	 * Print a usage message and exit.
	 */
	public static void usage()
	{
		System.err.println("Usage: java Regression <fileName>\n" +
				"<fileName> = the name of the graph file to be analyzed");
		System.exit(0);
	}
}
