package com.github.coderodde.pathfinding;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BidirectionalIterativeDeepeningDepthFirstSearchPathfinderTest
        extends AbstractPathfinderTest {

    public BidirectionalIterativeDeepeningDepthFirstSearchPathfinderTest() {
        super.pathfinder = new IterativeDeepeningDepthFirstSearchPathfinder(5);
    }
    
    @Test(expected = PathNotFoundException.class)
    public void throwsOnDepthExceeded() {
        pathfinder = 
            new BidirectionalIterativeDeepeningDepthFirstSearchPathfinder(2);
        
        tree.addTreeNode(0);
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        tree.addTreeNode(3);
        
        tree.connect(0, 1, 1.0);
        tree.connect(1, 2, 1.0);
        tree.connect(2, 3, 1.0);
        
        pathfinder.search(tree, 0, 3);
    }
    
    @Test
    public void completesProperlyOnSuffcientDepth() {
        pathfinder = 
            new BidirectionalIterativeDeepeningDepthFirstSearchPathfinder(3);
        
        WeightedTree.WeightedTreeNode node0 = tree.addTreeNode(0);
        WeightedTree.WeightedTreeNode node1 = tree.addTreeNode(1);
        WeightedTree.WeightedTreeNode node2 = tree.addTreeNode(2);
        WeightedTree.WeightedTreeNode node3= tree.addTreeNode(3);
        
        tree.connect(0, 1, 1.0);
        tree.connect(1, 2, 1.0);
        tree.connect(2, 3, 1.0);
        
        WeightedPath path = pathfinder.search(tree, 0, 3);
        
        assertEquals(4, path.getNumberOfNodes());
        assertEquals(node0, path.getNode(0));
        assertEquals(node1, path.getNode(1));
        assertEquals(node2, path.getNode(2));
        assertEquals(node3, path.getNode(3));
        assertEquals(3.0, path.getTotalCost(), 0.001);
    }
}
