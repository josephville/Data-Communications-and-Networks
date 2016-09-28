import java.util.ArrayList;
import edu.rit.util.PriorityQueue.Item;

/**
 * A class to represent a vertex object 
 * @author Joseph Ville
 */
public class Vertex extends Item
{
	private ArrayList<Integer> neighbors; // neighbors of the current vertex
	private double x, y; // the coordinates for this vertex
	private int index; // the index of this vertex
	private double distance; // the shortest distance from this to any other vertex in the graph
	private int predecessor; // predecessor to this vertex
	
	// number of min length paths where this vertex appears. Will become the centrality
	private double numPaths;
	
	/**
	 * Construct an object of this class
	 * @param index
	 */
	public Vertex(int index, int V)
	{
		neighbors = new ArrayList<Integer>();
		this.x = 0;
		this.y = 0;
		numPaths = 0.0;
	}
	
	/**
	 * Construct an object of this class
	 * @param index
	 * @param distance
	 */
	public Vertex(int index, double distance)
	{
		neighbors = new ArrayList<Integer>();
		this.x = 0;
		this.y = 0;
		this.distance = distance;
	}
	
	/**
	 * @return true - if the distance of this item is less than 
	 * 				the distance of the item passed in
	 * 		   false - otherwise
	 */
	@Override
	public boolean comesBefore(Item arg0)
	{
		if(this.distance < ((Vertex)arg0).distance)
		{
			return true;
		}
		return false;
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
	 * Adds a vertex number to the end of this vertex's neighbor list
	 * @param n - the neighbor to be added
	 */
	public void addNeighbor(int n)
	{
		this.neighbors.add(n);
	}

	/**
	 * @return the y
	 */
	public double getY()
	{
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y)
	{
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x)
	{
		this.x = x;
	}

	/**
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * @return the distance
	 */
	public double getDistance()
	{
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	/**
	 * @return the predecessor
	 */
	public int getPredecessor()
	{
		return predecessor;
	}

	/**
	 * @param predecessor the predecessor to set
	 */
	public void setPredecessor(int predecessor)
	{
		this.predecessor = predecessor;
	}
	
	/**
	 * Increment the numPaths variable
	 */
	public void incrementNumPaths()
	{
		numPaths++;
	}
	
	/**
	 * @return the number of paths for this vertex
	 */
	public double getNumPaths()
	{
		return numPaths;
	}
	
	/**
	 * divide the number of paths variable by the given divisor
	 * and store it back in the same variable
	 * @param divisor
	 */
	public void divideNumPaths(int divisor)
	{
		numPaths /= divisor;
	}
}// end class Vertex