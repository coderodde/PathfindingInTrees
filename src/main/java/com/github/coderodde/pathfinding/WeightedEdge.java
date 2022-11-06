package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.Objects;

/**
 * This class specifies the weighted edges.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Nov 6, 2022)
 * @since 1.6 (Nov 6, 2022)
 */
public final class WeightedEdge {

    private final WeightedTreeNode node1;
    private final WeightedTreeNode node2;
    private final double weight;

    public WeightedEdge(WeightedTreeNode node1, 
                        WeightedTreeNode node2,
                        double weight) {

        this.node1 = Objects.requireNonNull(node1, "The first node is null.");
        this.node2 = Objects.requireNonNull(node2, "The second node is null.");
        this.weight = weight;
    }

    public WeightedTreeNode getNode1() {
        return node1;
    }

    public WeightedTreeNode getNode2() {
        return node2;
    }

    public double getWeight() {
        return weight;
    }
}
