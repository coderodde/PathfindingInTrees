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
    
    @Test
    public void starTree() {
        tree.addTreeNode(0);
        tree.addTreeNode(1);
        tree.addTreeNode(2);
        tree.addTreeNode(3);
        
        tree.connect(0, 1, 1.0);
        tree.connect(0, 2, 2.0);
        tree.connect(0, 3, 3.0);
        
        WeightedPath path = pathfinder.search(tree, 1, 3);
        
        assertEquals(3, path.getNumberOfNodes());
        assertEquals(tree.getNode(1), path.getNode(0));
        assertEquals(tree.getNode(0), path.getNode(1));
        assertEquals(tree.getNode(3), path.getNode(2));
        assertEquals(4.0, path.getTotalCost(), 0.001);
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
