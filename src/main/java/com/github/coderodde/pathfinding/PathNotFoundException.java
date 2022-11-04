package com.github.coderodde.pathfinding;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 ()
 * @since 1.6 ()
 */
public final class PathNotFoundException extends RuntimeException {

    public PathNotFoundException(int sourceId, int targetId) {
        super("Path not found from " + sourceId + " to " + targetId + ".");
    }
}
