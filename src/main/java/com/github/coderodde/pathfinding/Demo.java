package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.Random;

/**
 *
 * @author Rodion "rodde" Efremov
 */
public class Demo {

    private static final int RADIUS = 11;
    private static final int DEGREE = 4;
    
    public static void main(String[] args) {
        long seed = System.nanoTime();
        Random random = new Random(seed);
        System.out.println("<<< Seed = " + seed + " >>>");
        
        long startTime = System.currentTimeMillis();
        WeightedTree tree = new StarTreeBuilder(DEGREE, RADIUS, random).build();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("Built the tree in " + duration + " milliseconds.");
        System.out.println(
                "Number of nodes in the tree: " + tree.getNumberOfNodes());
        
        startTime = System.currentTimeMillis();
        boolean isCyclic = tree.isCyclic();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        
        System.out.println("The tree candidate is cyclic: " + isCyclic);
        System.out.println(
                "Acyclicity check in " 
                        + duration 
                        + " milliseconds.");
    }
}
