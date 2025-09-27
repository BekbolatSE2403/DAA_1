package org.example.metrics;

public class RecursionDepthTracker {
    private static final ThreadLocal<Integer> currentDepth = ThreadLocal.withInitial(() ->0);

    private static final int MaxSafeDepth = 1000;

    public static int enter() {
        int depth = currentDepth.get() + 1;
        if(depth > MaxSafeDepth) {
            throw new StackOverflowError("Recursion depth " + depth + "exceeds safe limit");
        }
        currentDepth.set(depth);
        return depth;
    }

    public static void exit() {
        int depth = currentDepth.get();
        if(depth > 0) {
            currentDepth.set(depth - 1);
        }
    }

    public static int getCurrentDepth() {
        return currentDepth.get();
    }

    public static void reset() {
        currentDepth.set(0);
    }
}
