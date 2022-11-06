package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Nov 5, 2022)
 * @since 1.6 (Nov 5, 2022)
 */
public final class BreadthFirstSearchPathfinder implements Pathfinder {

    @Override
    public WeightedPath search(WeightedTree tree, 
                               int sourceNodeId,
                               int targetNodeId) {
        
        Objects.requireNonNull(tree, "The tree is null.");
        checkTerminalNodes(tree, sourceNodeId, targetNodeId);
        
        WeightedTreeNode sourceNode = tree.getWeightedTreeNode(sourceNodeId);
        WeightedTreeNode targetNode = tree.getWeightedTreeNode(targetNodeId);
        
        Deque<WeightedTreeNode> deque = new ArrayDeque<>();
        Map<WeightedTreeNode, WeightedTreeNode> parentMap = new HashMap<>();
        
        deque.add(sourceNode);
        parentMap.put(sourceNode, null);
        
        while (!deque.isEmpty()) {
            WeightedTreeNode currentNode = deque.removeFirst();
            
            if (currentNode.equals(targetNode)) {
                return tracebackPath(tree, targetNode, parentMap);
            }
            
            for (WeightedTreeNode neighborNode : 
                    tree.getNeighbors(currentNode.getId())) {
                if (parentMap.containsKey(neighborNode)) {
                    continue;
                }
                
                parentMap.put(neighborNode, currentNode);
                deque.addLast(neighborNode);
            }
        }
        
        throw new PathNotFoundException(sourceNodeId, targetNodeId);
    }
}
