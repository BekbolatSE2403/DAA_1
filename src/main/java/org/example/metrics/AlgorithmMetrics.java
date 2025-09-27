package org.example.metrics;

public class AlgorithmMetrics {
    private long comparisons = 0;
    private int maxRecursionDepth = 0;
    private long startTime = 0;
    private long durationNs = 0;

    //Timer methods
    public void startTimer(){
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        if(startTime != 0) {
            durationNs = System.nanoTime() - startTime;
        }
    }

    //Recording comparisons
    public void recordComparison() {
        comparisons++;
    }

    public void recordComparisons(int count) {
        comparisons += count;
    }

    public void recordRecursionDepth(int depth) {
        if (depth > maxRecursionDepth) {
            maxRecursionDepth = depth;
        }
    }

    //Getters
    public long getComparisons() {
        return comparisons;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public long getDurationNs() {
        return durationNs;
    }

    public double getDurationMs() {
        return durationNs/1_000_000;
    }

    public void reset() {
        comparisons = 0;
        maxRecursionDepth = 0;
        durationNs = 0;
        startTime = 0;
    }

    @Override
    public String toString() {
        return String.format("Comparisons: %,d, Max Depth: %d, Time: %.3fms",
                comparisons, maxRecursionDepth, getDurationMs());
    }

}
