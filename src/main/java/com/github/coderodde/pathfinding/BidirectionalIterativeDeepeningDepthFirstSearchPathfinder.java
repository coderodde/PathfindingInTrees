package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * This class implements a bidirectional iterative-deepening depth-first search
 * (IDDFS for short). It alternates two search frontiers meeting somewhere in 
 * between.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Nov 6, 2022)
 * @since 1.6 (Nov 6, 2022)
 */
public final class BidirectionalIterativeDeepeningDepthFirstSearchPathfinder 
implements Pathfinder {

    private final int maximumDepth;

    public BidirectionalIterativeDeepeningDepthFirstSearchPathfinder(
            int maximumDepth) {

        this.maximumDepth = checkMaximumDepth(maximumDepth);
    }

    public BidirectionalIterativeDeepeningDepthFirstSearchPathfinder() {
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

        List<WeightedTreeNode> path = searchImpl(tree, 
                                                 sourceNode, 
                                                 targetNode);
        return new WeightedPath(tree, path);
    }

    private void depthLimitedSearchForwards(WeightedTreeNode node, 
                                            int depth,
                                            Set<WeightedTreeNode> frontier,
                                            Set<WeightedTreeNode> visited) {
        if (depth == 0) {
            frontier.add(node);
            return;
        }

        for (WeightedTreeNode neighbor : node.getNeighbors()) {
            if (visited.contains(neighbor)) {
                continue;
            }

            visited.add(neighbor);

            depthLimitedSearchForwards(neighbor, 
                                       depth - 1, 
                                       frontier, 
                                       visited);
        }
    }

    private WeightedTreeNode
        depthLimitedSearchBackward(
                WeightedTreeNode node, 
                int depth,
                Set<WeightedTreeNode> frontier,
                Set<WeightedTreeNode> visited,
                Deque<WeightedTreeNode> backwardSearchStack) {
        backwardSearchStack.addFirst(node);

        if (depth == 0) {
            if (frontier.contains(node)) {
                return node;
            }

            backwardSearchStack.removeFirst();
            return null;
        }

        for (WeightedTreeNode neighbor : node.getNeighbors()) {
            if (visited.contains(neighbor)) {
                continue;
            }

            visited.add(neighbor);

            WeightedTreeNode meetingNode = 
                    depthLimitedSearchBackward(
                            neighbor,
                            depth - 1,
                            frontier,
                            visited,
                            backwardSearchStack);

            if (meetingNode != null) {
                return meetingNode;
            }
        }

        backwardSearchStack.removeFirst();
        return null;
    }

    private List<WeightedTreeNode> searchImpl(WeightedTree tree,
                                              WeightedTreeNode sourceNode, 
                                              WeightedTreeNode targetNode) {

        if (sourceNode.equals(targetNode)) {
            return Arrays.asList(sourceNode);
        }

        int totalForwardDepth = maximumDepth / 2;
        int totalBackwardDepth = maximumDepth - totalForwardDepth;

        int currentForwardDepth = 0;
        int currentBackwardDepth = 0;

        boolean incrementForwardSearchDepth = true;

        Deque<WeightedTreeNode> backwardSearchStack = new ArrayDeque<>();
        Set<WeightedTreeNode> frontier = new HashSet<>();
        Set<WeightedTreeNode> visited = new HashSet<>();

        while (currentForwardDepth + currentBackwardDepth <= maximumDepth) {
            visited.clear();
            visited.add(sourceNode);

            depthLimitedSearchForwards(sourceNode, 
                                       currentForwardDepth, 
                                       frontier,
                                       visited);

            for (int depth = currentBackwardDepth;
                    depth <= Math.min(currentBackwardDepth + 1, 
                                      totalBackwardDepth);
                    depth++) {

                visited.clear();
                visited.add(targetNode);

                WeightedTreeNode meetingNode = 
                        depthLimitedSearchBackward(targetNode,
                                                   depth,
                                                   frontier, 
                                                   visited,
                                                   backwardSearchStack);
                if (meetingNode != null) {
                    return buildPath(tree,
                                     meetingNode,
                                     sourceNode, 
                                     backwardSearchStack);
                }
            }

            frontier.clear();
            backwardSearchStack.clear();

            if (incrementForwardSearchDepth) {
                incrementForwardSearchDepth = false;
                currentForwardDepth++;
            } else {
                incrementForwardSearchDepth = true;
                currentBackwardDepth++;
            }
        }

        throw new PathNotFoundException(sourceNode.getId(), targetNode.getId());
    }

    private List<WeightedTreeNode>
         buildPath(WeightedTree tree,
                   WeightedTreeNode meetingNode, 
                   WeightedTreeNode sourceNode,
                   Deque<WeightedTreeNode> backwardSearchStack) {

        List<WeightedTreeNode> path = new ArrayList<>();
        List<WeightedTreeNode> prefixPath = 
                searchImpl(tree,
                           sourceNode, 
                           meetingNode);

        path.addAll(prefixPath);
        path.remove(path.size() - 1);
        path.addAll(backwardSearchStack);
        return path;
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
