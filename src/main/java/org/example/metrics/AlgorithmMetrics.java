package org.example.metrics;

public class AlgorithmMetrics {
    private long comparisons = 0;
    private long arrayAccesses = 0;
    private int maxRecursionDepth = 0;
    private long startTime;
    private long durationNs = 0;

    // Timer methods
    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        if (startTime != 0) {
            durationNs = System.nanoTime() - startTime;
        }
    }

    // Recording methods
    public void recordComparison() {
        comparisons++;
    }

    public void recordComparisons(int count) {
        comparisons += count;
    }

    public void recordArrayAccess() {
        arrayAccesses++;
    }

    public void recordArrayAccess(int count) {
        arrayAccesses += count;
    }

    public void recordRecursionDepth(int depth) {
        if (depth > maxRecursionDepth) {
            maxRecursionDepth = depth;
        }
    }


    public long getComparisons() { return comparisons; }
    public long getArrayAccesses() { return arrayAccesses; }
    public int getMaxRecursionDepth() { return maxRecursionDepth; }
    public long getDurationNs() { return durationNs; }
    public double getDurationMs() { return durationNs / 1_000_000.0; }

    // Reset for reuse
    public void reset() {
        comparisons = 0;
        arrayAccesses = 0;  // ← RESET THIS TOO
        maxRecursionDepth = 0;
        durationNs = 0;
        startTime = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Metrics{comparisons=%,d, accesses=%,d, maxDepth=%d, time=%.2fms}",
                comparisons, arrayAccesses, maxRecursionDepth, getDurationMs()
        );
    }
}