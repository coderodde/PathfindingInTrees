package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public final class WeightedPath {

    private final List<WeightedTreeNode> nodeList = new ArrayList<>();
    private final List<WeightedEdge> edgeList = new ArrayList<>();
    private double totalCost;
    
    public WeightedPath(WeightedTree tree, List<WeightedTreeNode> nodeList) {
        this.nodeList.addAll(nodeList);
        loadEdgesAndTotalCost(tree, nodeList);
    }
    
    public int getNumberOfNodes() {
        return nodeList.size();
    }
    
    public WeightedTreeNode getNode(int index) {
        return nodeList.get(index);
    }
    
    public WeightedEdge getEdge(int index) {
        return edgeList.get(index);
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        nodeList.forEach((node) -> { sb.append(node).append('\n');});
        return sb.toString();
    }
    
    private void loadEdgesAndTotalCost(WeightedTree tree,
                                       List<WeightedTreeNode> nodeList) {
        double totalCost = 0.0;
        
        for (int i = 0; i < nodeList.size() - 1; i++) {
            WeightedTreeNode node1 = nodeList.get(i);
            WeightedTreeNode node2 = nodeList.get(i + 1);
            WeightedEdge edge = 
                    new WeightedEdge(
                            node1,
                            node2,
                            tree.getEdgeWeight(node1.getId(), 
                                               node2.getId()));
            
            totalCost += edge.getWeight();
            edgeList.add(edge);
        }
        
        this.totalCost = totalCost;
    }
}
