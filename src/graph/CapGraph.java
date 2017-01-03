/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @author Ben Dauer.
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
		
		List<Integer> sccNodeIDs = new ArrayList<Integer>();
		
		vertices = getVertexIDs();
		Stack<Integer> vertexStack = new Stack<Integer>();
		vertexStack.addAll(vertices);
		
		Stack<Integer> finished = dfs(this, vertexStack, sccNodeIDs);

		CapGraph transposedGraph = transpose(this);
		sccNodeIDs.clear();
		List<Graph> graphList = dfsGraphList(transposedGraph, finished, sccNodeIDs);

		return graphList;
	}
	
	/*
	 * Performs depth first search on a graph.
	 * Returns a stack of nodes in the order that they were finished.
	 */
	public Stack<Integer> dfs(CapGraph graph, Stack<Integer> vertices, List<Integer> sccNodeIDs) {

		Set<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				dfsVisit(graph, v, visited, finished, sccNodeIDs);
			}
		}
		return finished;
	}
	
	/*
	 * Performs depth first search on a graph.
	 * Returns a list of strongly connected components.
	 */
	public List<Graph> dfsGraphList(CapGraph graph, Stack<Integer>vertices, List<Integer> sccNodeIDs) {
		
		Set<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		CapGraph scc;

		List<Graph> sccList = new ArrayList<Graph>();
		
		while (!vertices.isEmpty()) {

			int v = vertices.pop();

			if (!visited.contains(v)) {
				sccNodeIDs = dfsVisit(graph, v, visited, finished, sccNodeIDs); // v is the root on the second pass.
				vertices.removeAll(sccNodeIDs); // remove all node IDs to get it working.
			}
			
			scc = new CapGraph(v);
			
			// build the nodes of the new graph
			for (int nodeID : sccNodeIDs) {
				scc.addVertex(nodeID);
				CapNode origiNode = getVertex(nodeID);
				CapNode sccNode = scc.getVertex(nodeID);
				
				for (int n : origiNode.getNeighbors()) {
					sccNode.addNeighbor(n);
				}
				
			}
			sccList.add(scc);
			sccNodeIDs.clear();
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
	public List<Integer> dfsVisit(CapGraph graph, int v, Set<Integer> visited, Stack<Integer> finished, List<Integer>sccNodeIDs) {
		visited.add(v);
		CapNode vNode = graph.getVertex(v);
		
		for (int n: vNode.getNeighbors()) {
			if (!visited.contains(n)) {
				dfsVisit(graph, n, visited, finished, sccNodeIDs);

			}
		}
		finished.push(v);
		sccNodeIDs.add(v);
		return sccNodeIDs; // if this returns visited that would help. but then I'm not getting the grouped IDs.
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
	
	public void identifyTrendSetters() {
	
		List<CapGraph> SCCList = new ArrayList<CapGraph>();		
		SCCList.add((CapGraph) this.getSCCs());
		// find each SCC.
		
		// For each SCC in the graph, 
		// the trend setters are users with more followers
		// than the majority of their SCC.
		
		for (CapGraph scc: SCCList) {
			// used to find the nodes with the highest number of followers in each SCC.
			// store the number of followers for each node.
			PriorityQueue<CapNode> numFollowersQueue = new PriorityQueue<CapNode>(SCCList.size(), new TrendSetterComparator());
			
			int graphSize = scc.getVertexIDs().size();
			// assume top 10% in graph are trend setters because I'm feeling generous.
			double percentOfTrendSetters = .1; 
			double numTrendSetters = graphSize * percentOfTrendSetters;
			
			// add scc nodes to the numFollowerQueue.
			for (int nodeID : scc.getVertexIDs()) {
				CapNode node = listMap.get(nodeID);	
				numFollowersQueue.add(node);
			}
			
			CapNode trendyNode = new CapNode();
			
			for (int i = 0; i < numTrendSetters; i++) {
				trendyNode = numFollowersQueue.remove();					
				trendyNode.setIsTrendSetter(true);
			}
		}
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
		CapGraphWithNodes.addEdge(5, 2);
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
			// Should produce {{1, 2, 5,}, {3}, [4}}
			// currently it returns {{3}, {4}, {1, 2, 5}, {2}, {1}}
			// This means that it's never resetting the value returned, so the new graph gets tacked on.
			System.out.println("a graph: " + graph.exportGraph());
		}

	}

}
