package com.github.coderodde.pathfinding;

public final class IterativeDeepeningDepthFirstSearchPathfinderTest 
        extends AbstractIDDFSPathfinderTest {
    
    public IterativeDeepeningDepthFirstSearchPathfinderTest() {
        super.pathfinder = new IterativeDeepeningDepthFirstSearchPathfinder(5);
    }
}
