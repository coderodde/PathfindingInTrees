package com.github.coderodde.pathfinding;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Nov 4, 2022)
 * @since 1.6 (Nov 4, 2022)
 */
public final class WeightedTree {

    private final Map<Integer, WeightedTreeNode> nodeMap = 
            new LinkedHashMap<>();
    
    private final Map<WeightedTreeNode, 
                      Map<WeightedTreeNode, Double>> weightMap = 
            new HashMap<>();
    
    public static final class WeightedTreeNode {
        private final int id;
        private final Set<WeightedTreeNode> neighbors = new LinkedHashSet<>();
        
        WeightedTreeNode(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            
            if (o == null) {
                return false;
            }
            
            if (getClass() != o.getClass()) {
                return false;
            }
           
            WeightedTreeNode other = (WeightedTreeNode) o;
            return this.id == other.id;
        }
        
        @Override
        public String toString() {
            return "[WeightedTreeNode, id = " + id + "]";
        }
        
        public Set<WeightedTreeNode> getNeighbors() {
            return Collections.unmodifiableSet(neighbors);
        }
    }
    
    public WeightedTreeNode addTreeNode(int id) {
        if (!nodeMap.containsKey(id)) {
            WeightedTreeNode newTreeNode = new WeightedTreeNode(id);
            nodeMap.put(id, newTreeNode);
            return newTreeNode;
        }
        
        return nodeMap.get(id);
    }
    
    public boolean removeTreeNode(int id) {
        if (!nodeMap.containsKey(id)) {
            return false;
        }
        
        WeightedTreeNode treeNodeToRemove = nodeMap.get(id);
        
        for (WeightedTreeNode neighbor : treeNodeToRemove.neighbors) {
            neighbor.neighbors.remove(treeNodeToRemove);
            weightMap.get(neighbor).remove(treeNodeToRemove);
        }
        
        treeNodeToRemove.neighbors.clear();
        weightMap.remove(treeNodeToRemove);
        nodeMap.remove(id);
        return true;
    }
    
    public boolean connect(int id1, int id2, double weight) {
        WeightedTreeNode treeNode1 = nodeMap.get(id1);
        
        if (treeNode1 == null) {
            return false;
        }
        
        WeightedTreeNode treeNode2 = nodeMap.get(id2);
        
        if (treeNode2 == null) {
            return false;
        }
        
        if (treeNode1.neighbors.contains(treeNode2)) {
            // Here, we possibly updating an existing edge weight.
            double currentWeight = weightMap.get(treeNode1).get(treeNode2);
            
            if (currentWeight == weight) {
                return false;
            }
            
            weightMap.get(treeNode1).put(treeNode2, weight);
            weightMap.get(treeNode2).put(treeNode1, weight);
            return true;
        }
        
        treeNode1.neighbors.add(treeNode2);
        treeNode2.neighbors.add(treeNode1);
        
        connectImpl(treeNode1, treeNode2, weight);
        connectImpl(treeNode2, treeNode1, weight);
        return true;
    }
    
    public boolean disconnect(int id1, int id2) {
        WeightedTreeNode treeNode1 = nodeMap.get(id1);
        
        if (treeNode1 == null) {
            return false;
        }
        
        WeightedTreeNode treeNode2 = nodeMap.get(id2);
        
        if (treeNode2 == null) {
            return false;
        }
        
        if (treeNode1.neighbors.contains(treeNode2)) {
            treeNode1.neighbors.remove(treeNode2);
            treeNode2.neighbors.remove(treeNode1);
            
            weightMap.get(treeNode1).remove(treeNode2);
            weightMap.get(treeNode2).remove(treeNode1);
            
            if (weightMap.get(treeNode1).isEmpty()) {
                weightMap.remove(treeNode1);
            }
            
            if (weightMap.get(treeNode2).isEmpty()) {
                weightMap.remove(treeNode2);
            }
            
            return true;
        }
        
        return false;
    }
    
    public boolean hasEdge(int id1, int id2) {
        WeightedTreeNode treeNode1 = nodeMap.get(id1);
        
        if (treeNode1 == null) {
            return false;
        }
        
        WeightedTreeNode treeNode2 = nodeMap.get(id2);
        
        if (treeNode2 == null) {
            return false;
        }
        
        return treeNode1.neighbors.contains(treeNode2);
    }
    
    public WeightedTreeNode getWeightedTreeNode(int id) {
        if (!nodeMap.containsKey(id)) {
            throw new IllegalStateException(
                    "No node with ID " + id + " in the tree.");
        }
        
        return nodeMap.get(id);
    }
    
    public boolean containsNodeId(int id) {
        return nodeMap.containsKey(id);
    }
    
    public double getEdgeWeight(int id1, int id2) {
        WeightedTreeNode treeNode1 = nodeMap.get(id1);
        
        if (treeNode1 == null) {
            throw new IllegalStateException("No node " + id1 + ".");
        }
        
        WeightedTreeNode treeNode2 = nodeMap.get(id2);
        
        if (treeNode2 == null) {
            throw new IllegalStateException("No node " + id2 + ".");
        }
        
        if (!treeNode1.neighbors.contains(treeNode2)) {
            throw new IllegalStateException(
                    "No edge {" + id1 + ", " + id2 + "}.");
        }
        
        return weightMap.get(treeNode1).get(treeNode2);
    }
    
    public void clear() {
        nodeMap.clear();
        weightMap.clear();
    }
    
    public Set<WeightedTreeNode> getNeighbors(int id) {
        WeightedTreeNode treeNode = nodeMap.get(id);
        
        if (treeNode == null) {
            throw new IllegalStateException("No node " + id + ".");
        }
        
        return Collections.unmodifiableSet(treeNode.neighbors);
    }
    
    public boolean isCyclic() {
        if (nodeMap.isEmpty()) {
            throw new IllegalStateException("Empty tree.");
        }
        
        Set<WeightedTreeNode> visited = new HashSet<>();
        WeightedTreeNode parentSentinel = new WeightedTreeNode(-1);
        
        for (WeightedTreeNode node : nodeMap.values()) {
            if (!visited.contains(node)) {
                if (isCyclicImpl(node, parentSentinel, visited)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public WeightedTreeNode getNode(int id) {
        return nodeMap.get(id);
    }
    
    public int getNumberOfNodes() {
        return nodeMap.size();
    }
    
    private static boolean isCyclicImpl(WeightedTreeNode node, 
                                        WeightedTreeNode parent,
                                        Set<WeightedTreeNode> visitedSet) {
        visitedSet.add(node);
        Iterator<WeightedTreeNode> iterator = node.neighbors.iterator();
        
        while (iterator.hasNext()) {
            WeightedTreeNode neighbor = iterator.next();
            
            if (!visitedSet.contains(neighbor)) {
                if (isCyclicImpl(neighbor, node, visitedSet)) {
                    return true;
                }
            } else if (!neighbor.equals(parent)) {
                return true;
            }
        }
        
        return false;
    }
    
    private void connectImpl(WeightedTreeNode treeNode1, 
                             WeightedTreeNode treeNode2, 
                             double weight) {
        if (!weightMap.containsKey(treeNode1)) {
            weightMap.put(treeNode1, new HashMap<>());
        }
        
        weightMap.get(treeNode1).put(treeNode2, weight);
    }
}
