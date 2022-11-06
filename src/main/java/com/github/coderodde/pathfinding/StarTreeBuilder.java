package com.github.coderodde.pathfinding;

import com.github.coderodde.pathfinding.WeightedTree.WeightedTreeNode;
import java.util.Objects;
import java.util.Random;

/**
 * This class is responsible for creating a star-shaped tree.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Nov 5, 2022)
 * @since 1.6 (Nov 5, 2022)
 */
public final class StarTreeBuilder {

    private final int degree;
    private final int radius;
    private final Random random;

    private WeightedTree tree;
    private int currentId;

    public StarTreeBuilder(int degree, int radius, Random random) {
        this.degree = checkDegree(degree);
        this.radius = checkRadius(radius);
        this.random = Objects.requireNonNull(random, "Random is null.");
    }

    public WeightedTree build() {
        tree = new WeightedTree();
        currentId = 0;

        WeightedTreeNode root = tree.addTreeNode(currentId++);

        if (radius == 0) {
            return tree;
        }

        for (int i = 0; i < degree; i++) {
            WeightedTreeNode node = tree.addTreeNode(currentId++);
            tree.connect(root.getId(), node.getId(), random.nextDouble());
            createWeightedTreeUtil(node, radius - 1);
        }

        return tree;
    }

    private void createWeightedTreeUtil(WeightedTreeNode node, int radius) {
        if (radius == -1) {
            return;
        }

        for (int i = 0; i < degree - 1; i++) {
            WeightedTreeNode newNode = tree.addTreeNode(currentId++);

            tree.connect(newNode.getId(), node.getId(), random.nextDouble());

            createWeightedTreeUtil(newNode, radius - 1);
        }
    }

    private int checkDegree(int degree) {
        if (degree < 2) {
            throw new IllegalArgumentException(
                    "The degree is too small: "
                            + degree 
                            + ". Must be at least 2.");
        }

        return degree;
    }

    private int checkRadius(int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException(
                    "The radius is too small: "
                            + radius 
                            + ". Must be at least 0.");
        }

        return radius;
    }
}
