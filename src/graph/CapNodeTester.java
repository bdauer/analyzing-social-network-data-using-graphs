package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class CapNodeTester {
	
	CapNode neighborlessNode;
	CapNode nodeWithNeighbors;
	CapNode neighborlessNode2;
	
	ArrayList<Integer> emptyArrayList;
	ArrayList<Integer> populatedArrayList;
	
	@Before
	public void setUp() throws Exception {
		// build the CapNodes to test here.
		neighborlessNode = new CapNode(1);
		neighborlessNode2 = new CapNode(2);
		nodeWithNeighbors = new CapNode(3);
		
		emptyArrayList = new ArrayList<Integer>();
		populatedArrayList = new ArrayList<Integer>();
		populatedArrayList.add(1);
		populatedArrayList.add(2);
		
		nodeWithNeighbors.addNeighbor(neighborlessNode.getName());
		nodeWithNeighbors.addNeighbor(neighborlessNode2.getName());
	}
	
	@Test
	public void testNodeValue() {
		
		assertEquals("check node name:", 1, neighborlessNode.getName());
		assertEquals("check node name:", 3, nodeWithNeighbors.getName());
	}
	
	@Test
	public void testGetNeighbors() {
		
		assertEquals("check neighborless nodes:", emptyArrayList, neighborlessNode.getNeighbors());
	
		assertEquals("check neighborless nodes:", populatedArrayList, nodeWithNeighbors.getNeighbors());
	}
}