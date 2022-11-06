package com.github.coderodde.pathfinding;

public final class PathNotFoundException extends RuntimeException {

    public PathNotFoundException(int sourceId, int targetId) {
        super("Path not found from " + sourceId + " to " + targetId + ".");
    }
}
