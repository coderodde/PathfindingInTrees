package com.github.coderodde.pathfinding;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractPathfinderTest {

    protected Pathfinder pathfinder;
    protected final WeightedTree tree = new WeightedTree();
    
    @Before
    public void before() {
        tree.clear();
    }
    
    @Test
    public void trivialPath() {
        tree.addTreeNode(1);
        WeightedPath path = pathfinder.search(tree, 1, 1);
        
        assertEquals(1, path.getNumberOfNodes());
        assertEquals(0.0, path.getTotalCost(), 0.001);
    }
    
    @Test
    public void singleEdgePath() {
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        tree.connect(1, 2, 3.0);
        
        WeightedPath path = pathfinder.search(tree, 1, 2);
        
        assertEquals(2, path.getNumberOfNodes());
        assertEquals(3.0, path.getTotalCost(), 0.001);
        
        assertEquals(1, path.getNode(0).getId());
        assertEquals(2, path.getNode(1).getId());
        
        assertEquals(3.0, path.getEdge(0).getWeight(), 0.001);
        assertEquals(1, path.getNode(0).getId());
        assertEquals(2, path.getNode(1).getId());
    }
    
    @Test(expected = PathNotFoundException.class)
    public void throwsOnUnreachableTarget() {
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        tree.addTreeNode(3);
        
        tree.connect(1, 2, 1.0);
        
        pathfinder.search(tree, 3, 2);
    }
}
