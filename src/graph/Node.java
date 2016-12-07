package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ben Dauer
 * 
 * A Node interface to be implemented
 * with the Graph interface.
 *
 */
public interface Node {
	
	public void addNeighbor();
	
	public List<Integer> getNeighbors();
	
	public int getName();


}