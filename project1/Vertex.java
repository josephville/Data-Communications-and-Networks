import java.util.LinkedList;
import java.util.ArrayList;

/**
 * A class to store attributes of a single vertex
 * @author Joseph Ville
 */
public class Vertex
{
	private int index; /** Index of this vertex */
	private ArrayList<Integer> neighbors; /** Neighbors of this vertex */

	/**
	 * Construct an object of this class
	 * @param index - the index of this vertex, that is, its ID
	 * @param neighbors - a list of all the neighbors of this vertex
	 */
	public Vertex(int index, ArrayList<Integer> neighbors)
	{
		this.index = index;
		this.neighbors = neighbors;
	}

	/**
	 * Add the specified Integer to this list of neighbors
	 * @param n - the Integer to add
	 * @return true - if the add was successful
	 * 		   false - if the add failed
	 */
	public boolean addNeighbor(Integer n)
	{
		return neighbors.add(n);
	}

	/**
	 * Retrieve the list of neighbors associated with this vertex
	 * @return the list of neighbors
	 */
	public ArrayList<Integer> getNeighbors()
	{
		return neighbors;
	}

	/**
	 * Retrieve the index location of this vertex
	 * @return the index location
	 */
	public int index()
	{
		return index;
	}

	/**
	 * String representation of this object
	 * @return a String representation of the vertex
	 */
	public String toString()
	{
		return "\n" + index + ": " + neighbors.toString();
	}
}
