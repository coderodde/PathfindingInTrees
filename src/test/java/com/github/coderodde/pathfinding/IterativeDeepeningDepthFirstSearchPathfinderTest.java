package com.github.coderodde.pathfinding;

public final class IterativeDeepeningDepthFirstSearchPathfinderTest 
        extends AbstractPathfinderTest {
    
    private static final int MAXIMUM_DEPTH = 5;

    public IterativeDeepeningDepthFirstSearchPathfinderTest() {
        super.pathfinder = 
                new IterativeDeepeningDepthFirstSearchPathfinder(MAXIMUM_DEPTH);
    }
}
