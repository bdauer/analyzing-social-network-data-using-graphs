package graph;

import java.util.Comparator;

/**
 * 
 * @author Ben Dauer
 * 
 * Compares the number of neighbors
 * between two nodes. 
 *
 */
public class TrendSetterComparator implements Comparator<CapNode> {
	
	@Override
	public int compare(CapNode node1, CapNode node2) {
		return Double.compare(node1.getNumNeighbors(), node2.getNumNeighbors());
	}
}