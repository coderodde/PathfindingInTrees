package com.github.coderodde.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Rodion "rodde" Efremov
 */
public class Demo {

    private static final int RADIUS = 11;
    private static final int DEGREE = 4;
    
    private static final List<Pathfinder> pathfinders = new ArrayList<>(5);
    
    static {
        pathfinders.add(new BreadthFirstSearchPathfinder());
        pathfinders.add(new DepthFirstSearchPathfinder());
    }
    
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
        
        startTime = System.currentTimeMillis();
        warmup(tree, new Random(seed + 1L));
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        
        System.out.println("Warmup in " + duration + " milliseconds");
        
        startTime = System.currentTimeMillis();
        benchmark(tree, new Random(seed + 1L));
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        
        System.out.println("Warmup in " + duration + " milliseconds");
    }
    
    private static void warmup(WeightedTree tree, Random random) {
        run(tree, false, random);
    }
    
    private static void benchmark(WeightedTree tree, Random random) {
        run(tree, true, random);
    }
    
    private static void run(WeightedTree tree, boolean print, Random random) {
        int sourceNodeId = random.nextInt(tree.getNumberOfNodes());
        int targetNodeId = random.nextInt(tree.getNumberOfNodes());
        
        if (print) {
            System.out.println("Source: " + tree.getNode(sourceNodeId));
            System.out.println("Target: " + tree.getNode(targetNodeId));
        }
        
        List<WeightedPath> paths = new ArrayList<>();
        
        for (Pathfinder pathfinder : pathfinders) {
            long startTime = System.currentTimeMillis();
            WeightedPath path = pathfinder.search(tree,
                                                  sourceNodeId, 
                                                  targetNodeId);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            paths.add(path);
            
            if (print) {
                System.out.println(
                        pathfinder.getClass().getSimpleName() 
                                + " in " 
                                + duration 
                                + " milliseconds.");
            }
        }
        
        if (print) {
            boolean pathsEqual = pathsAreEqual(paths);
            
            if (!pathsEqual) {
                System.out.println("Path mismatch!");
            } else {
                System.out.println("Shortest path:");
                System.out.println(paths.get(0));
                System.out.println(
                        "Shortest path distance: " 
                                + paths.get(0).getTotalCost());
            }
        }
    }
    
    private static boolean pathsAreEqual(List<WeightedPath> paths) {
        for (int i = 0; i < paths.size() - 1; i++) {
            if (!paths.get(i).equals(paths.get(i + 1))) {
                return false;
            }
        }
        
        return true;
    }
}
