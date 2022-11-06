package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This interface specifies the entry point to the tree shortest pathfinders.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Nov 6, 2022)
 * @since 1.6 (Nov 6, 2022)
 */
public interface Pathfinder {
    
    WeightedPath search(WeightedTree tree, int sourceId, int targetId);
    
    default WeightedPath tracebackPath(
            WeightedTree tree,
            WeightedTreeNode targetNode,
            Map<WeightedTreeNode, WeightedTreeNode> parentMap) {
        
        List<WeightedTreeNode> nodeList = tracebackImpl(tree, 
                                                        targetNode,
                                                        parentMap);
        return new WeightedPath(tree, nodeList);
    }
    
    default WeightedPath tracebackPath(
            WeightedTree tree,
            WeightedTreeNode targetNode,
            Map<WeightedTreeNode, WeightedTreeNode> parentMapForward,
            Map<WeightedTreeNode, WeightedTreeNode> parentMapBackward) {
        
        List<WeightedTreeNode> nodeList = tracebackImpl(tree, 
                                                        targetNode,
                                                        parentMapForward,
                                                        parentMapBackward);
        return new WeightedPath(tree, nodeList);
    }
    
    default List<WeightedTreeNode>
         tracebackImpl(WeightedTree tree,
                       WeightedTreeNode targetNode, 
                       Map<WeightedTreeNode, WeightedTreeNode> parentMap) {
             
        List<WeightedTreeNode> path = new ArrayList<>();
        WeightedTreeNode currentNode = targetNode;
        
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = parentMap.get(currentNode);
        }
        
        Collections.reverse(path);
        return path;
    }
    
    default List<WeightedTreeNode>
        tracebackImpl(
                WeightedTree tree,
                WeightedTreeNode touchNode,
                Map<WeightedTreeNode, WeightedTreeNode> parentsMapForward,
                Map<WeightedTreeNode, WeightedTreeNode> parentsMapBackward) {
            
        List<WeightedTreeNode> path = tracebackImpl(tree, 
                                                    touchNode,
                                                    parentsMapForward);
        
        WeightedTreeNode currentNode = parentsMapBackward.get(touchNode);
        
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = parentsMapBackward.get(currentNode);
        }
        
        return path;
    }
     
    default void checkTerminalNodes(WeightedTree tree, 
                                    int sourceNodeId, 
                                    int targetNodeId) {
        
        if (!tree.containsNodeId(sourceNodeId)) {
            throw new IllegalStateException(
                    "Source node is not in the input graph.");
        }
        
        if (!tree.containsNodeId(targetNodeId)) {
            throw new IllegalStateException(
                    "Target node is not in the input graph.");
        }
    }
}
