package org.example;

import org.example.algorithms.*;
import org.example.metrics.AlgorithmMetrics;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Divide and Conquer Algorithms Demo ===\n");

        Random random = new Random(42);

        int[] testArray = generateRandomArray(20, random);
        System.out.println("Original array: " + Arrays.toString(testArray));


        System.out.println("\n1. MERGESORT DEMO");
        int[] mergeSortArray = testArray.clone();
        AlgorithmMetrics mergeMetrics = new AlgorithmMetrics();
        MergeSort mergeSort = new MergeSort(mergeMetrics);
        mergeSort.sort(mergeSortArray);
        System.out.println("Sorted: " + Arrays.toString(mergeSortArray));
        System.out.println("Metrics: " + mergeMetrics);

        System.out.println("\n2. QUICKSORT DEMO");
        int[] quickSortArray = testArray.clone();
        AlgorithmMetrics quickMetrics = new AlgorithmMetrics();
        QuickSort quickSort = new QuickSort(quickMetrics);
        quickSort.sort(quickSortArray);
        System.out.println("Sorted: " + Arrays.toString(quickSortArray));
        System.out.println("Metrics: " + quickMetrics);


        System.out.println("\n3. SELECT (MEDIAN) DEMO");
        int[] selectArray = testArray.clone();
        AlgorithmMetrics selectMetrics = new AlgorithmMetrics();
        Select select = new Select(selectMetrics);
        int median = select.findMedian(selectArray);
        System.out.println("Array: " + Arrays.toString(testArray));
        System.out.println("Median: " + median);
        System.out.println("Metrics: " + selectMetrics);


        System.out.println("\n4. CLOSEST PAIR DEMO");
        ClosestPair.Point[] points = {
                new ClosestPair.Point(1, 2),
                new ClosestPair.Point(3, 4),
                new ClosestPair.Point(5, 6),
                new ClosestPair.Point(1, 3),
                new ClosestPair.Point(10, 10)
        };
        AlgorithmMetrics closestMetrics = new AlgorithmMetrics();
        ClosestPair closestPair = new ClosestPair(closestMetrics);
        ClosestPair.Pair result = closestPair.findClosestPair(points);
        System.out.println("Points: " + Arrays.toString(points));
        System.out.println("Closest pair: " + result);
        System.out.println("Metrics: " + closestMetrics);


        System.out.println("\n" + "=".repeat(50));
        System.out.println("PERFORMANCE COMPARISON");
        System.out.println("=".repeat(50));

        int[] largeArray = generateRandomArray(10000, random);


        AlgorithmMetrics mergeLarge = new AlgorithmMetrics();
        new MergeSort(mergeLarge).sort(largeArray.clone());
        System.out.printf("MergeSort (n=10,000): %.2f ms, depth=%d%n",
                mergeLarge.getDurationMs(), mergeLarge.getMaxRecursionDepth());


        AlgorithmMetrics quickLarge = new AlgorithmMetrics();
        new QuickSort(quickLarge).sort(largeArray.clone());
        System.out.printf("QuickSort (n=10,000): %.2f ms, depth=%d%n",
                quickLarge.getDurationMs(), quickLarge.getMaxRecursionDepth());

        System.out.println("\nFor full benchmarks, run: java CLI all 10000 5");
    }

    private static int[] generateRandomArray(int size, Random random) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(100);
        }
        return arr;
    }
}