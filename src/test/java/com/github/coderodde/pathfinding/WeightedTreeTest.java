package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.Set;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public final class WeightedTreeTest {
    
    private final WeightedTree tree = new WeightedTree();
    
    @After
    public void tearDown() {
        tree.clear();
    }

    @Test
    public void addTreeNode() {
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        tree.addTreeNode(4);
        
        assertTrue(tree.containsNodeId(1));
        assertTrue(tree.containsNodeId(2));
        assertTrue(tree.containsNodeId(4));
        assertFalse(tree.containsNodeId(3));
    }
    
    @Test
    public void addExistingTreeNode() {
        WeightedTreeNode node1 = tree.addTreeNode(1);
        WeightedTreeNode node2 = tree.addTreeNode(1);
        
        assertEquals(node1, node2);
    }
    
    @Test
    public void removeTreeNode() {
        tree.addTreeNode(1);
        assertFalse(tree.removeTreeNode(2));
        assertTrue(tree.removeTreeNode(1));
        assertFalse(tree.removeTreeNode(1));
    }
    
    @Test
    public void connect() {
        tree.addTreeNode(1);
        assertFalse(tree.hasEdge(1, 2));
        tree.addTreeNode(2);
        assertFalse(tree.hasEdge(1, 2));
        assertTrue(tree.connect(2, 1, 4.0));
        assertFalse(tree.connect(1, 2, 4.0));
        assertTrue(tree.hasEdge(2, 1));
        assertTrue(tree.connect(2, 1, 5.0));
    }
    
    @Test
    public void containsNodeId() {
        assertFalse(tree.containsNodeId(1));
        tree.addTreeNode(1);
        assertTrue(tree.containsNodeId(1));
    }
    
    @Test
    public void disconnect() {
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        
        tree.connect(2, 1, 1.0);
       
        assertTrue(tree.hasEdge(1, 2));
        assertTrue(tree.disconnect(1, 2));
        assertFalse(tree.disconnect(2, 1));
    }
    
    @Test
    public void disconnectOnNonExistingNodesReturnsFalse() {
        assertFalse(tree.disconnect(1, 2));
        tree.addTreeNode(1);
        assertFalse(tree.disconnect(1, 2));
    }

    @Test
    public void hasEdge() {
        tree.addTreeNode(1);
        tree.addTreeNode(3);
        
        assertFalse(tree.hasEdge(3, 1));
        assertTrue(tree.connect(1, 3, 0.0));
        assertTrue(tree.hasEdge(3, 1));
        assertTrue(tree.disconnect(1, 3));
        assertFalse(tree.hasEdge(1, 3));
    }
    
    @Test
    public void getNeighbors() {
        WeightedTreeNode node0 = tree.addTreeNode(0);
        WeightedTreeNode node1 = tree.addTreeNode(1);
        WeightedTreeNode node2 = tree.addTreeNode(2);
        WeightedTreeNode node3 = tree.addTreeNode(3);
        WeightedTreeNode node4 = tree.addTreeNode(4);
        
        assertTrue(tree.connect(0, 1, 0.0));
        assertTrue(tree.connect(0, 2, 0.0));
        assertTrue(tree.connect(0, 3, 0.0));
        
        Set<WeightedTreeNode> neighbors = tree.getNeighbors(0);
        
        assertEquals(3, neighbors.size());
        assertTrue(neighbors.contains(node1));
        assertTrue(neighbors.contains(node2));
        assertTrue(neighbors.contains(node3));
        assertFalse(neighbors.contains(node4));
    }    
    
    @Test
    public void getEdgeWeight() {
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        tree.connect(1, 2, 1.0);
        assertEquals(1.0, tree.getEdgeWeight(2, 1), 0.00001);
        tree.connect(2, 1, 2.0);
        assertEquals(2.0, tree.getEdgeWeight(1, 2), 0.00001);
    }
    
    @Test(expected = IllegalStateException.class)
    public void getEdgeWeightThrowsOnFirstBadId() {
        tree.addTreeNode(2);
        tree.getEdgeWeight(1, 2);
    }
    
    @Test(expected = IllegalStateException.class)
    public void getEdgeWeightThrowsOnSecondBadId() {
        tree.addTreeNode(1);
        tree.getEdgeWeight(1, 2);
    }
    
    @Test(expected = IllegalStateException.class) 
    public void getEdgeWeightThrowsOnNoEdge() {
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        tree.getEdgeWeight(1, 2);
    }
    
    @Test
    public void removeTreeNodeDisconnectFromNeighbors() {
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        tree.addTreeNode(3);
        tree.addTreeNode(0);
        
        tree.connect(0, 1, 1.0);
        tree.connect(0, 2, 1.0);
        tree.connect(0, 3, 1.0);
        
        assertTrue(tree.hasEdge(0, 2));
        
        tree.removeTreeNode(0);
        
        assertFalse(tree.hasEdge(0, 2));
    }
    
    @Test
    public void returnFalseOnConnectionToNonExistingNode() {
        tree.addTreeNode(1);
        assertFalse(tree.connect(1, 2, 0));
        
        tree.clear();
        
        tree.addTreeNode(2);
        assertFalse(tree.connect(1, 2, 0));
    }
    
    @Test
    public void connectThrowsOnNonExistingId1() {
        tree.addTreeNode(2);
        assertFalse(tree.connect(1, 2, 1.0));
    }
    
    @Test
    public void connectThrowsOnNonExistingId2() {
        tree.addTreeNode(1);
        assertFalse(tree.connect(1, 2, 1.0));
    }
    
    @Test(expected = IllegalStateException.class)
    public void getNeighborsThrowsOnBadId() {
        tree.getNeighbors(0);
    }
    
    @Test
    public void getTreeNodeId() {
        WeightedTreeNode node = new WeightedTreeNode(2);
        assertEquals(2, node.getId());
    }
    
    @Test
    public void treeNodeEquals() {
        WeightedTreeNode node = new WeightedTreeNode(3);
        
        assertTrue(node.equals(node));
        
        assertFalse(node.equals(new WeightedTreeNode(1)));
        
        assertFalse(node.equals(new Object()));
        assertFalse(node.equals(null));
        
        WeightedTreeNode nodeCopy = new WeightedTreeNode(3);
        assertTrue(node.equals(nodeCopy));
    }
}
