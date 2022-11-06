package com.github.coderodde.pathfinding;

public final class BidirectionalIterativeDeepeningDepthFirstSearchPathfinderTest
        extends AbstractIDDFSPathfinderTest {

    public BidirectionalIterativeDeepeningDepthFirstSearchPathfinderTest() {
        super.pathfinder = new IterativeDeepeningDepthFirstSearchPathfinder(5);
    }
}
