package com.github.coderodde.pathfinding;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public final class GraphIsNotTreeException extends RuntimeException {

    public GraphIsNotTreeException() {
        super("The graph is not a tree, it contains cycles.");
    }
}
