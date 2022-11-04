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
 * @version 1.6 ()
 * @since 1.6 ()
 */
public final class BreadthFirstSearchPathfinder implements Pathfinder {

    @Override
    public WeightedPath search(WeightedTree tree, int sourceId, int targetId) {
        Objects.requireNonNull(tree, "The tree is null.");
        checkTerminalNodes(tree, sourceId, targetId);
        
        WeightedTreeNode sourceNode = tree.getWeightedTreeNode(sourceId);
        WeightedTreeNode targetNode = tree.getWeightedTreeNode(targetId);
        
        Deque<WeightedTreeNode> deque = new ArrayDeque<>();
        Set<WeightedTreeNode> visitedSet = new HashSet<>();
        
        Map<WeightedTreeNode, WeightedTreeNode> parentMap = new HashMap<>();
        Map<WeightedTreeNode, Integer> distanceMap        = new HashMap<>();
        
        deque.add(sourceNode);
        parentMap.put(sourceNode, null);
        distanceMap.put(sourceNode, 0);
        
        int searchRadius = 0;
        WeightedTreeNode lastLayerNode = sourceNode;
        
        while (!deque.isEmpty()) {
            WeightedTreeNode currentNode = deque.removeFirst();
            
            if (currentNode.equals(targetNode)) {
                return tracebackPath(tree, targetNode, parentMap);
            }
            
            if (distanceMap.containsKey(currentNode)) {
                int currentNodeDistance = distanceMap.get(currentNode);
                
                if (currentNodeDistance < searchRadius) {
                    throw new GraphIsNotTreeException();
                }
            }
            
            if (visitedSet.contains(currentNode)) {
                throw new GraphIsNotTreeException();
            }
            
            visitedSet.add(currentNode);
            
            for (WeightedTreeNode neighborNode : 
                    tree.getNeighbors(currentNode.getId())) {
                if (visitedSet.contains(neighborNode)) {
                    System.out.println("yes 1");
                    continue;
                }
                
                parentMap.put(neighborNode, currentNode);
                distanceMap.put(neighborNode,
                                distanceMap.get(currentNode) + 1);
                
                deque.addLast(neighborNode);
            }
            
            if (currentNode.equals(lastLayerNode)) {
                lastLayerNode = currentNode;
                searchRadius++;
            }
        }
        
        throw new PathNotFoundException(sourceId, targetId);
    }
}
