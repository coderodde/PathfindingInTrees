package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.Random;

/**
 *
 * @author Rodion "rodde" Efremov
 */
public class Demo {

    private static final int RADIUS = 3;
    private static final int DEGREE = 4;
    
    public static void main(String[] args) {
        WeightedTree tree = createWeightedTree();
        
        long startTime = System.currentTimeMillis();
        boolean isAcyclic = tree.isAcyclic();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("The tree candidate is acyclic: " + isAcyclic);
        System.out.println("Acyclicity check in " + duration + " ms.");
    }
    
    private static WeightedTree createWeightedTree() {
        return createWeightedTree(RADIUS, DEGREE);
    }
    
    private static WeightedTree createWeightedTree(int radius, int degree) {
        WeightedTree tree = new WeightedTree();
        WeightedTreeNode rootNode = new WeightedTree.WeightedTreeNode(0);
        Random random = new Random();
        
        int currentId = 1;
        
        for (int i = 0; i < degree; i++) {
            WeightedTreeNode node = tree.addTreeNode(currentId++);
            tree.connect(rootNode.getId(), node.getId(), random.nextDouble());
            createWeightedTreeUtil(tree,
                                   node,
                                   radius - 1, 
                                   degree - 1, 
                                   currentId, 
                                   random);
        }
        
        return tree;
    }
    
    private static void createWeightedTreeUtil(WeightedTree tree,
                                               WeightedTreeNode node,
                                               int radius,
                                               int degree,
                                               int currentId,
                                               Random random) {
        if (radius == 0) {
            return;
        }
        
        for (int i = 0; i < degree; i++) {
            WeightedTreeNode newNode = tree.addTreeNode(currentId++);
            
            tree.connect(newNode.getId(),
                         node.getId(),
                         random.nextDouble());
            
            createWeightedTreeUtil(tree,
                                   newNode,
                                   radius - 1, 
                                   degree,
                                   currentId, 
                                   random);
        }
    }
}
