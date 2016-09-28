import java.util.ArrayList;

/**
 * A class to store all the attributes of a vertex object
 * @author Joseph Ville
 */
public class Vertex
{
	private ArrayList<Integer> neighbors; // neighbors of the current vertex
	
	/**
	 * Construct an object of this class, by setting 
	 * the class variables to their default values
	 */
	public Vertex()
	{
		neighbors = new ArrayList<Integer>();
	}
	
	/**
	 * Find the degree of this vertex. Will only work after
	 * neighbors list has finished populating.
	 * @return the degree of this vertex
	 */
	public int degree()
	{
		return neighbors.size();
	}

	/**
	 * Get the list of vertices that are adjacent to the current vertex
	 * @return the list of neighbors
	 */
	public ArrayList<Integer> getNeighbors()
	{
		return neighbors;
	}
	
	/**
	 * Appends a vertex number to the end of this vertex's neighbor list
	 * @param n - the neighbor to be added
	 */
	public void addNeighbor(int n)
	{
		this.neighbors.add(n);
	}
}// end class Vertex