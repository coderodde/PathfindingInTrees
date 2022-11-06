package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
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
public final class IterativeDeepeningDepthFirstSearchPathfinder 
        implements Pathfinder {

    private final int maximumDepth;

    public IterativeDeepeningDepthFirstSearchPathfinder(int maximumDepth) {
        this.maximumDepth = checkMaximumDepth(maximumDepth);
    }

    public IterativeDeepeningDepthFirstSearchPathfinder() {
        this(Integer.MAX_VALUE);
    }

    @Override
    public WeightedPath search(WeightedTree tree, 
                               int sourceNodeId,
                               int targetNodeId) {

        Objects.requireNonNull(tree, "The tree is null.");
        checkTerminalNodes(tree, sourceNodeId, targetNodeId);

        WeightedTreeNode sourceNode = tree.getWeightedTreeNode(sourceNodeId);
        WeightedTreeNode targetNode = tree.getWeightedTreeNode(targetNodeId);

        Map<WeightedTreeNode, WeightedTreeNode> parentMap = new HashMap<>();

        if (sourceNode.equals(targetNode)) {
            parentMap.put(sourceNode, null);
            return tracebackPath(tree, targetNode, parentMap);
        }

        for (int depth = 0; depth <= maximumDepth; depth++) {
            parentMap.clear();
            parentMap.put(sourceNode, null);

            WeightedTreeNode found = depthLimitedSearch(sourceNode, 
                                                        targetNode,
                                                        depth, 
                                                        parentMap);

            if (found != null) {
                return tracebackPath(tree, targetNode, parentMap);
            }
        }

        throw new PathNotFoundException(sourceNodeId, targetNodeId);
    }

    private WeightedTreeNode 
        depthLimitedSearch(WeightedTreeNode node,
                           WeightedTreeNode targetNode, 
                           int depth, 
                           Map<WeightedTreeNode, 
                               WeightedTreeNode> parentMap) {

        if (depth == 0) {
            return node.equals(targetNode) ? targetNode : null;
        }

        for (WeightedTreeNode neighbor : node.getNeighbors()) {
            if (parentMap.containsKey(neighbor)) {
                continue;
            }

            parentMap.put(neighbor, node);

            WeightedTreeNode found = depthLimitedSearch(neighbor,
                                                        targetNode, 
                                                        depth - 1,
                                                        parentMap);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    private int checkMaximumDepth(int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException(
                    "The depth is negative: "
                            + depth
                            + ". Must be at least 0.");
        }

        return depth;
    }
}
