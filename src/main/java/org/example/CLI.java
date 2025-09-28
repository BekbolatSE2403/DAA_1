package org.example;

import org.example.algorithms.*;
import org.example.metrics.AlgorithmMetrics;
import org.example.metrics.MetricsCSVWriter;
import org.example.algorithms.ClosestPair;
import java.util.Arrays;
import java.util.Random;

public class CLI {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            String algorithm = args[0].toLowerCase();
            int size = args.length > 1 ? Integer.parseInt(args[1]) : 1000;
            int trials = args.length > 2 ? Integer.parseInt(args[2]) : 5;

            switch (algorithm) {
                case "mergesort":
                    runAlgorithmBenchmark("MergeSort", size, trials);
                    break;
                case "quicksort":
                    runAlgorithmBenchmark("QuickSort", size, trials);
                    break;
                case "select":
                    runSelectBenchmark(size, trials);
                    break;
                case "closest":
                    runClosestPairBenchmark(size, trials);
                    break;
                case "all":
                    runAllBenchmarks(size, trials);
                    break;
                default:
                    System.out.println("Unknown algorithm: " + algorithm);
                    printUsage();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Size and trials must be integers");
            printUsage();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runAlgorithmBenchmark(String algorithm, int size, int trials) {
        System.out.printf("Running %s benchmark: size=%,d, trials=%,d%n", algorithm, size, trials);
        System.out.println("==========================================");

        Random random = new Random(42); // Fixed seed for reproducibility
        double totalTime = 0;
        long totalComparisons = 0;
        int totalDepth = 0;

        for (int trial = 1; trial <= trials; trial++) {
            int[] arr = generateRandomArray(size, random);

            AlgorithmMetrics metrics = new AlgorithmMetrics();
            Runnable algorithmRunner = createAlgorithm(algorithm, metrics, arr);

            long startTime = System.nanoTime();
            algorithmRunner.run();
            long endTime = System.nanoTime();

            double trialTime = (endTime - startTime) / 1_000_000.0;
            totalTime += trialTime;
            totalComparisons += metrics.getComparisons();
            totalDepth += metrics.getMaxRecursionDepth();

            System.out.printf("Trial %d: %.3f ms, %,d comparisons, depth %d%n",
                    trial, trialTime, metrics.getComparisons(), metrics.getMaxRecursionDepth());

            // Export to CSV
            MetricsCSVWriter.writeMetrics("benchmark_results.csv", algorithm, size, metrics);

            // Verify sorting for sort algorithms
            if (!algorithm.equals("select") && !algorithm.equals("closest")) {
                boolean sorted = isSorted(arr);
                System.out.println("  ✓ Correctly sorted: " + sorted);
            }
        }

        printSummary(algorithm, size, trials, totalTime, totalComparisons, totalDepth);
    }

    private static void runSelectBenchmark(int size, int trials) {
        System.out.printf("Running Select benchmark: size=%,d, trials=%,d%n", size, trials);
        System.out.println("==========================================");

        Random random = new Random(42);
        double totalTime = 0;
        long totalComparisons = 0;
        int totalDepth = 0;

        for (int trial = 1; trial <= trials; trial++) {
            int[] arr = generateRandomArray(size, random);
            int k = random.nextInt(size); // Random k-th element to select

            AlgorithmMetrics metrics = new AlgorithmMetrics();
            Select select = new Select(metrics);

            long startTime = System.nanoTime();
            int result = select.select(arr, k);
            long endTime = System.nanoTime();

            double trialTime = (endTime - startTime) / 1_000_000.0;
            totalTime += trialTime;
            totalComparisons += metrics.getComparisons();
            totalDepth += metrics.getMaxRecursionDepth();

            // Verify against Arrays.sort
            int[] sorted = arr.clone();
            Arrays.sort(sorted);
            int expected = sorted[k];
            boolean correct = (result == expected);

            System.out.printf("Trial %d: %.3f ms, %,d comparisons, depth %d, correct: %s%n",
                    trial, trialTime, metrics.getComparisons(), metrics.getMaxRecursionDepth(), correct);

            MetricsCSVWriter.writeMetrics("benchmark_results.csv", "Select", size, metrics);
        }

        printSummary("Select", size, trials, totalTime, totalComparisons, totalDepth);
    }

    private static void runClosestPairBenchmark(int size, int trials) {
        System.out.printf("Running ClosestPair benchmark: size=%,d, trials=%,d%n", size, trials);
        System.out.println("==========================================");

        Random random = new Random(42);
        double totalTime = 0;
        long totalComparisons = 0;
        int totalDepth = 0;

        for (int trial = 1; trial <= trials; trial++) {
            ClosestPair.Point[] points = generateRandomPoints(size, random);
            AlgorithmMetrics metrics = new AlgorithmMetrics();
            ClosestPair closestPair = new ClosestPair(metrics);

            long startTime = System.nanoTime();
            ClosestPair.Pair result = closestPair.findClosestPair(points);
            long endTime = System.nanoTime();

            double trialTime = (endTime - startTime) / 1_000_000.0;
            totalTime += trialTime;
            totalComparisons += metrics.getComparisons();
            totalDepth += metrics.getMaxRecursionDepth();

            System.out.printf("Trial %d: %.3f ms, %,d comparisons, depth %d, distance: %.4f%n",
                    trial, trialTime, metrics.getComparisons(), metrics.getMaxRecursionDepth(), result.distance);

            MetricsCSVWriter.writeMetrics("benchmark_results.csv", "ClosestPair", size, metrics);
        }

        printSummary("ClosestPair", size, trials, totalTime, totalComparisons, totalDepth);
    }

    private static void runAllBenchmarks(int size, int trials) {
        String[] algorithms = {"MergeSort", "QuickSort", "Select", "ClosestPair"};

        for (String algorithm : algorithms) {
            System.out.println("\n" + "=".repeat(50));
            switch (algorithm) {
                case "MergeSort":
                case "QuickSort":
                    runAlgorithmBenchmark(algorithm, size, trials);
                    break;
                case "Select":
                    runSelectBenchmark(size, trials);
                    break;
                case "ClosestPair":
                    runClosestPairBenchmark(size, trials);
                    break;
            }
        }
    }

    private static Runnable createAlgorithm(String algorithm, AlgorithmMetrics metrics, int[] arr) {
        switch (algorithm) {
            case "MergeSort":
                MergeSort mergeSort = new MergeSort(metrics);
                return () -> mergeSort.sort(arr);
            case "QuickSort":
                QuickSort quickSort = new QuickSort(metrics);
                return () -> quickSort.sort(arr);
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }

    private static int[] generateRandomArray(int size, Random random) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }

    private static ClosestPair.Point[] generateRandomPoints(int size, Random random) {
        ClosestPair.Point[] points = new ClosestPair.Point[size];
        for (int i = 0; i < size; i++) {
            double x = random.nextDouble() * 1000;
            double y = random.nextDouble() * 1000;
            points[i] = new ClosestPair.Point(x, y);  // Use ClosestPair.Point
        }
        return points;
    }

    private static void printSummary(String algorithm, int size, int trials,
                                     double totalTime, long totalComparisons, int totalDepth) {
        System.out.println("------------------------------------------");
        System.out.printf("%s Summary (n=%,d, trials=%,d):%n", algorithm, size, trials);
        System.out.printf("  Average Time: %.3f ms%n", totalTime / trials);
        System.out.printf("  Average Comparisons: %,d%n", totalComparisons / trials);
        System.out.printf("  Average Depth: %d%n", totalDepth / trials);
        System.out.printf("  Total Time: %.3f ms%n", totalTime);
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) return false;
        }
        return true;
    }

    private static void printUsage() {
        System.out.println("Usage: java CLI <algorithm> [size] [trials]");
        System.out.println("Algorithms: mergesort, quicksort, select, closest, all");
        System.out.println("Default: size=1000, trials=5");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java CLI mergesort 10000 10");
        System.out.println("  java CLI quicksort 5000");
        System.out.println("  java CLI all 2000 3");
        System.out.println("  java CLI select 10000 5");
    }
}