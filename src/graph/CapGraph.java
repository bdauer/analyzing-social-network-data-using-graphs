/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	
	private Map<Integer, CapNode> listMap;
	private Set<Integer> vertices;
	
	public CapGraph() {
		
		listMap = new HashMap<Integer, CapNode>();
	}
	public CapGraph(int node) {
		listMap = new HashMap<Integer, CapNode>();
		this.addVertex(node);
	}

	/*
	 * returns a vertex with the passed number as its id.
	 */
	public CapNode getVertex(int num) {
		return listMap.get(num);
	}
	
	/*
	 * Returns the set of all vertex IDs.
	 */
	public Set<Integer> getVertexIDs() {
		return listMap.keySet();
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		vertices = getVertexIDs();		
		listMap.put(num, new CapNode(num));
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		CapNode fromNode = listMap.get(from);
		fromNode.addNeighbor(to);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		
		CapNode centerNode = getVertex(center);				
		List<Integer> egoNodes = centerNode.getNeighbors();
		egoNodes.add(center);
				
		CapGraph egonet = new CapGraph(center);
		
		// add nodes
		for (int n : egoNodes) {
			egonet.addVertex(n);			
		}
		
		// add edges
		for (int n : egoNodes) {
			CapNode egoNode = egonet.getVertex(n);

			if (egoNodes.contains(n)) {
				egoNode.addNeighbor(n);
			}
		}		
		return egonet;
	}

	
	
	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub
//		strongly connected component, everyone 
//		Perform DFS(G) keeping track of the order in which vertices finish
//		Compute the transpose of G
//		DFS(G), exploring in reverse order of finish time from step 1
		return null;
	}
	
	public Stack<Integer> dfs(CapGraph graph, Stack<Integer> vertices) {

		Set<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				dfsVisit(graph, v, visited, finished);
			}
		}
		return finished;
	}
	/*
	 * Recursively visits each neighbor's neighbors
	 * adding them to the visited set.
	 * If a node has no unvisited neighbors,
	 * it is pushed on the finished stack.
	 * 
	 */
	public void dfsVisit(CapGraph graph, int v, Set<Integer> visited, Stack<Integer> finished) {
		visited.add(v);
		CapNode vNode = getVertex(v);
		for (int n: vNode.getNeighbors()) {
			if (!visited.contains(n)) {
				dfsVisit(graph, n, visited, finished);
			}
		}
		finished.push(v);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		HashMap<Integer, HashSet<Integer>> graphRepresentation = new HashMap<Integer, HashSet<Integer>>();
		for (int num : listMap.keySet()) {
			CapNode node = listMap.get(num);
			HashSet<Integer> neighbors = (HashSet<Integer>) node.getNeighbors();
			
			graphRepresentation.put(num, neighbors);
		}
		return graphRepresentation;
	}

}
