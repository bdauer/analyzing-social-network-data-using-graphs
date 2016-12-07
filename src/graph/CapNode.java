package graph;

import java.util.ArrayList;
import java.util.List;

public class CapNode implements Node {
	
	int name;
	ArrayList<Integer> neighbors;
	
	public CapNode(int name) {
		this.name = name;
		this.neighbors = new ArrayList<Integer>();
	}
	
	public void addNeighbor() {
		
	}
	
	public List<Integer> getNeighbors() {
		return neighbors;
		
	}
	
	public int getName() {
		return name;
	}
}