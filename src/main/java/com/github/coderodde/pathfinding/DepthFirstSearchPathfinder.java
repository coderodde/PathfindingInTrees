package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public class DepthFirstSearchPathfinder implements Pathfinder {

    @Override
    public WeightedPath search(WeightedTree tree,
                               int sourceNodeId, 
                               int targetNodeId) {
        
        Objects.requireNonNull(tree, "The tree is null.");
        checkTerminalNodes(tree, sourceNodeId, targetNodeId);
        
        WeightedTreeNode sourceNode = tree.getWeightedTreeNode(sourceNodeId);
        WeightedTreeNode targetNode = tree.getWeightedTreeNode(targetNodeId);
        
        Map<WeightedTreeNode, WeightedTreeNode> parentMap = new HashMap<>();
        
        parentMap.put(sourceNode, null);
        
        if (sourceNode.equals(targetNode)) {
            // Handle the trvial case separately, since it is not covered by
            // algorithm logic:
            return tracebackPath(tree, targetNode, parentMap);
        }
        
        for (WeightedTreeNode neighbor : sourceNode.getNeighbors()) {
            if (parentMap.containsKey(neighbor)) {
                System.out.println("hello");
                continue;
            }
            
            WeightedPath path = searchImpl(tree, 
                                           targetNode,
                                           sourceNode,
                                           neighbor,
                                           parentMap);
            
            if (path != null) {
                return path;    
            }
        }
        
        throw new PathNotFoundException(sourceNodeId, targetNodeId);
    }
    
    private WeightedPath searchImpl(WeightedTree tree,
                                    WeightedTreeNode targetNode,
                                    WeightedTreeNode parentNode,
                                    WeightedTreeNode node, 
                                    Map<WeightedTreeNode, 
                                        WeightedTreeNode> parentMap) {
        parentMap.put(node, parentNode);
        
        if (node.equals(targetNode)) {
            return tracebackPath(tree, targetNode, parentMap);
        }
        
        for (WeightedTreeNode neighbor : node.getNeighbors()) {
            if (parentMap.containsKey(neighbor)) {
                continue;
            }
            
            WeightedPath path = searchImpl(tree,
                                           targetNode,
                                           node, 
                                           neighbor, 
                                           parentMap);
            
            if (path != null) {
                return path;
            }
        }
        
        return null;
    }
}
