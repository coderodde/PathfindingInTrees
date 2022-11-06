package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements a bidirectional breadth-first search. It alternates 
 * two search frontiers meeting somewhere in between.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Nov 6, 2022)
 * @since 1.6 (Nov 6, 2022)
 */
public final class BidirectionalBreadthFirstSearchPathfinder 
        implements Pathfinder {

    @Override
    public WeightedPath search(WeightedTree tree, int sourceNodeId, int targetNodeId) {
        
        Objects.requireNonNull(tree, "The tree is null.");
        checkTerminalNodes(tree, sourceNodeId, targetNodeId);
        
        WeightedTree.WeightedTreeNode sourceNode = 
                tree.getWeightedTreeNode(sourceNodeId);
        
        WeightedTree.WeightedTreeNode targetNode = 
                tree.getWeightedTreeNode(targetNodeId);
        
        if (sourceNode.equals(targetNode)) {
            // We need to deal with this special case separately since the 
            // algorithm logic cannot handle it:
            return new WeightedPath(tree, Arrays.asList(sourceNode));
        }   

        Deque<WeightedTreeNode> queueForward  = new ArrayDeque<>();
        Deque<WeightedTreeNode> queueBackward = new ArrayDeque<>();
        
        Map<WeightedTreeNode, WeightedTreeNode> parentMapForward = 
                new HashMap<>();
        
        Map<WeightedTreeNode, WeightedTreeNode> parentMapBackward = 
                new HashMap<>();
        
        Map<WeightedTreeNode, Integer> distanceMapForward = 
                new HashMap<>();
        
        Map<WeightedTreeNode, Integer> distanceMapBackward = 
                new HashMap<>();
        
        int bestCost = Integer.MAX_VALUE;
        WeightedTreeNode touchNode = null;
        
        queueForward.addLast(sourceNode);
        queueBackward.addLast(targetNode);
        
        parentMapForward.put(sourceNode, null);
        parentMapBackward.put(targetNode, null);
        
        distanceMapForward.put(sourceNode, 0);
        distanceMapBackward.put(targetNode, 0);
        
        while (!queueForward.isEmpty() && !queueBackward.isEmpty()) {
            int distanceForward = 
                    distanceMapForward.get(queueForward.getFirst());
            
            int distanceBackward = 
                    distanceMapBackward.get(queueBackward.getFirst());
            
            if (touchNode != null
                    && bestCost < distanceForward + distanceBackward) {
                return tracebackPath(tree,
                                     touchNode, 
                                     parentMapForward, 
                                     parentMapBackward);
            }
            
            int forwardSearchAreaSize = queueForward.size() 
                                      + parentMapForward.size();
            
            int backwardSearchAreaSize = queueBackward.size() 
                                       + parentMapBackward.size();
            
            // Trivial load balancing:
            if (forwardSearchAreaSize < backwardSearchAreaSize) {
                WeightedTreeNode currentNode = queueForward.removeFirst();

                if (distanceMapBackward.containsKey(currentNode) 
                        && bestCost > distanceForward + distanceBackward) {
                    
                    bestCost = distanceForward + distanceBackward;
                    touchNode = currentNode;
                }
                
                for (WeightedTreeNode neighbor : currentNode.getNeighbors()) {
                    if (!parentMapForward.containsKey(neighbor)) {
                        parentMapForward.put(neighbor, currentNode);
                        distanceMapForward.put(neighbor, 
                                               distanceMapForward.get(
                                                       currentNode) + 1);
                        
                        queueForward.addLast(neighbor);
                    }
                }
            } else {
                WeightedTreeNode currentNode = queueBackward.removeFirst();
                
                if (distanceMapForward.containsKey(currentNode) 
                        && bestCost > distanceForward + distanceBackward) {
                    
                    bestCost = distanceForward + distanceBackward;
                    touchNode = currentNode;
                }
                
                for (WeightedTreeNode neighbor : currentNode.getNeighbors()) {
                    if (!parentMapBackward.containsKey(neighbor)) {
                        parentMapBackward.put(neighbor, currentNode);
                        distanceMapBackward.put(neighbor, 
                                                distanceMapBackward.get(
                                                        currentNode) + 1);
                        
                        queueBackward.addLast(neighbor);
                    }
                }
            }
        }
        
        throw new PathNotFoundException(sourceNodeId, targetNodeId);
    }
}
