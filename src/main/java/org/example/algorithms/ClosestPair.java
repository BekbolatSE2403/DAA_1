package org.example.algorithms;

import org.example.metrics.AlgorithmMetrics;
import org.example.metrics.RecursionDepthTracker;
import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair {
    private AlgorithmMetrics metrics;

    public ClosestPair(AlgorithmMetrics metrics) {
        this.metrics = metrics;
    }

    public static class Pair {
        public final Point p1;
        public final Point p2;
        public final double distance;

        public Pair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = p1.distanceTo(p2);
        }

        @Override
        public String toString() {
            return String.format("Pair: %s - %s (distance: %.4f)", p1, p2, distance);
        }
    }

    public Pair findClosestPair(Point[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        metrics.startTimer();
        RecursionDepthTracker.reset();
        try {

            Point[] pointsByX = points.clone();
            Point[] pointsByY = points.clone();


            Arrays.sort(pointsByX, Comparator.comparingDouble(p -> p.x));
            Arrays.sort(pointsByY, Comparator.comparingDouble(p -> p.y));

            return findClosestPair(pointsByX, pointsByY, 0, points.length - 1);
        } finally {
            metrics.stopTimer();
        }
    }

    private Pair findClosestPair(Point[] pointsByX, Point[] pointsByY, int left, int right) {
        int depth = RecursionDepthTracker.enter();
        metrics.recordRecursionDepth(depth);

        try {
            int n = right - left + 1;

            if (n <= 3) {
                return bruteForceClosestPair(pointsByX, left, right);
            }


            int mid = left + (right - left) / 2;
            Point midPoint = pointsByX[mid];


            Point[] leftPointsY = new Point[mid - left + 1];
            Point[] rightPointsY = new Point[right - mid];

            int leftIdx = 0, rightIdx = 0;
            for (Point p : pointsByY) {
                metrics.recordComparison();
                if (p.x < midPoint.x || (p.x == midPoint.x && p.y <= midPoint.y)) {
                    if (leftIdx < leftPointsY.length) {
                        leftPointsY[leftIdx++] = p;
                    }
                } else {
                    if (rightIdx < rightPointsY.length) {
                        rightPointsY[rightIdx++] = p;
                    }
                }
            }


            Pair leftClosest = findClosestPair(pointsByX, leftPointsY, left, mid);
            Pair rightClosest = findClosestPair(pointsByX, rightPointsY, mid + 1, right);


            Pair minPair = leftClosest;
            if (rightClosest.distance < minPair.distance) {
                minPair = rightClosest;
            }


            double minDistance = minPair.distance;
            Pair stripClosest = findClosestInStrip(pointsByY, midPoint, minDistance, left, right);

            if (stripClosest != null && stripClosest.distance < minDistance) {
                return stripClosest;
            }
            return minPair;

        } finally {
            RecursionDepthTracker.exit();
        }
    }

    private Pair bruteForceClosestPair(Point[] points, int left, int right) {
        double minDistance = Double.MAX_VALUE;
        Point p1 = null, p2 = null;

        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                metrics.recordComparison();
                double dist = points[i].distanceSquaredTo(points[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                    p1 = points[i];
                    p2 = points[j];
                }
            }
        }

        return new Pair(p1, p2);
    }

    private Pair findClosestInStrip(Point[] pointsByY, Point midPoint, double minDistance, int left, int right) {

        Point[] strip = new Point[pointsByY.length];
        int stripSize = 0;

        for (Point p : pointsByY) {
            metrics.recordComparison();
            if (Math.abs(p.x - midPoint.x) < minDistance) {
                strip[stripSize++] = p;
            }
        }


        double minStripDistance = minDistance;
        Pair closestStripPair = null;

        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < minStripDistance; j++) {
                metrics.recordComparison();
                if (j - i > 7) break;

                double dist = strip[i].distanceSquaredTo(strip[j]);
                if (dist < minStripDistance) {
                    minStripDistance = dist;
                    closestStripPair = new Pair(strip[i], strip[j]);
                }
            }
        }

        return closestStripPair;
    }


    public Pair bruteForce(Point[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        double minDistance = Double.MAX_VALUE;
        Point p1 = null, p2 = null;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                metrics.recordComparison();
                double dist = points[i].distanceTo(points[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                    p1 = points[i];
                    p2 = points[j];
                }
            }
        }

        return new Pair(p1, p2);
    }
}