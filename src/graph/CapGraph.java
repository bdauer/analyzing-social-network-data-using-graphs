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
	
	public void removeEdge(int from, int to) {
		CapNode fromNode = listMap.get(from);
		fromNode.removeNeighbor(to);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {

		CapNode centerNode = this.getVertex(center);
		
		List<Integer> egoNodes = new ArrayList<Integer>(); 
		egoNodes.addAll(centerNode.getNeighbors());
		egoNodes.add(center);

		CapGraph egonet = new CapGraph();		

		// add nodes
		for (int n : egoNodes) {
			egonet.addVertex(n);			
		}
		// add edges
		for (int n : egonet.getVertexIDs()) {
			CapNode egoNode = egonet.getVertex(n);
			CapNode origiNode = this.getVertex(n);
			
			List<Integer> origiNeighbors = origiNode.getNeighbors();
					
			for (int neighbor : origiNeighbors) {
				
				if (egoNodes.contains(neighbor)) {
					egoNode.addNeighbor(neighbor);
				}
			}
		}		
		return egonet;
	}

	
	
	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		
		vertices = getVertexIDs();
		Stack<Integer> vertexStack = new Stack<Integer>();
		vertexStack.addAll(vertices);
		
		Stack<Integer> finished = dfs(this, vertexStack);
		
		// it's taking the second to last item, then connecting it to everything.
		// need to figure out why. Looping error.
		CapGraph transposedGraph = transpose(this);
		List<Graph> graphList = dfsGraphList(transposedGraph, finished);
		return graphList;
	}
	
	/*
	 * Performs depth first search on a graph.
	 * Returns a stack of nodes in the order that they were finished.
	 */
	public Stack<Integer> dfs(CapGraph graph, Stack<Integer> vertices) {

		Set<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		List<Integer> sccNodeIDs = new ArrayList<Integer>();
		
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				dfsVisit(graph, v, visited, finished);
			}
		}
		return finished;
	}
	
	/*
	 * Performs depth first search on a graph.
	 * Returns a list of strongly connected components.
	 */
	public List<Graph> dfsGraphList(CapGraph graph, Stack<Integer>vertices) {
		
		Set<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		List<Integer> sccNodeIDs = new ArrayList<Integer>();
		List<Graph> sccList = new ArrayList<Graph>();
		
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				sccNodeIDs = dfsVisit(graph, v, visited, finished); // v is the root on the second pass.
			}
			
			CapGraph scc = new CapGraph(v);
			
			// build the nodes of the new graph
			for (int nodeID : sccNodeIDs) {
				scc.addVertex(nodeID);
				CapNode originalNode = getVertex(nodeID);
				CapNode sccNode = scc.getVertex(nodeID);
				
				for (int n : originalNode.getNeighbors()) {
					sccNode.addNeighbor(n);
				}
			}
			sccList.add(scc);
		}
		return sccList;  
	}
	
	/*
	 * Used by dfs.
	 * Recursively visits each neighbor's neighbors
	 * adding them to the visited set.
	 * If a node has no unvisited neighbors,
	 * it is pushed on the finished stack.
	 * 
	 */
	public List<Integer> dfsVisit(CapGraph graph, int v, Set<Integer> visited, Stack<Integer> finished) {
		visited.add(v);
		CapNode vNode = graph.getVertex(v);
		
		List<Integer>sccNodeIDs = new ArrayList<Integer>();
		
		for (int n: vNode.getNeighbors()) {
			if (!visited.contains(n)) {
				sccNodeIDs.add(n);
				dfsVisit(graph, n, visited, finished);
			}
		}
		finished.push(v);
		return sccNodeIDs;
	}
	
	/*
	 * Return the transpose
	 * of the passed graph.
	 */
	public CapGraph transpose(CapGraph graph) {
		
		Set<Integer> vertices = graph.getVertexIDs();
		CapGraph transposedGraph = new CapGraph();
		
		// access each node
		for (int v : vertices) {
			CapNode origiNode = graph.getVertex(v);
			
			if (!transposedGraph.getVertexIDs().contains(v)) {
				transposedGraph.addVertex(v);				
			}
			
			List<Integer> neighbors = origiNode.getNeighbors();

			for (int neighbor : neighbors) {
				
				if (!transposedGraph.getVertexIDs().contains(neighbor)) {
					transposedGraph.addVertex(neighbor);
				}
				CapNode transposedNeighbor = transposedGraph.getVertex(neighbor);				
				transposedNeighbor.addNeighbor(v);
			}
		}
		return transposedGraph;
	}
	

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {

		HashMap<Integer, HashSet<Integer>> graphRepresentation = new HashMap<Integer, HashSet<Integer>>();
		for (int num : listMap.keySet()) {
			CapNode node = listMap.get(num);
			HashSet<Integer> neighbors = new HashSet<Integer>();
			neighbors.addAll(node.getNeighbors());
			
			graphRepresentation.put(num, neighbors);
		}
		return graphRepresentation;
	}
	
	public static void main(String[] args) {
		

		CapGraph CapGraphWithNodes = new CapGraph();

		for (int i=1; i<6; i++) {
			CapGraphWithNodes.addVertex(i);
		}
		
		CapGraphWithNodes.addEdge(1, 2);
		CapGraphWithNodes.addEdge(2, 1);
		CapGraphWithNodes.addEdge(1, 5);
		CapGraphWithNodes.addEdge(3, 4);
		System.out.println("Checking graph...");
		System.out.println(CapGraphWithNodes.exportGraph());
		
		Graph egoNet = CapGraphWithNodes.getEgonet(1);
		
		System.out.println("Checking egonet...");
		System.out.println(egoNet.exportGraph());
		
		System.out.println("Checking transpose...");
		
		Graph transposedGraph = CapGraphWithNodes.transpose(CapGraphWithNodes);
		
		System.out.println(transposedGraph.exportGraph());
		
		System.out.println("Checking getSCCs...");
		for (Graph graph : CapGraphWithNodes.getSCCs()) {
			System.out.println(graph.exportGraph());
		}		
	}

}
