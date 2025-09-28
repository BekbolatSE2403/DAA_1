package org.example;

import org.example.algorithms.MergeSort;
import org.example.metrics.AlgorithmMetrics;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    @Test
    public void testSortingCorrectness() {
        int[] arr = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        AlgorithmMetrics metrics = new AlgorithmMetrics();
        MergeSort sorter = new MergeSort(metrics);
        sorter.sort(arr);

        assertArrayEquals(expected, arr);
        assertTrue(metrics.getComparisons() > 0);
    }

    @Test
    public void testInsertionSortCutoff() {
        // Small array should use insertion sort
        int[] arr = {3, 1, 2};
        AlgorithmMetrics metrics = new AlgorithmMetrics();
        MergeSort sorter = new MergeSort(metrics);
        sorter.sort(arr);

        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }

    @Test
    public void testRecursionDepth() {
        int[] arr = new int[1000];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(10000);
        }

        AlgorithmMetrics metrics = new AlgorithmMetrics();
        MergeSort sorter = new MergeSort(metrics);
        sorter.sort(arr);

        // For n=1000, depth should be ~log2(1000) ≈ 10
        int expectedDepth = (int) (Math.log(arr.length) / Math.log(2)) + 2;
        assertTrue(metrics.getMaxRecursionDepth() <= expectedDepth);
        System.out.println("MergeSort depth: " + metrics.getMaxRecursionDepth() +
                ", expected ~" + expectedDepth);
    }

    @Test
    public void testEdgeCases() {
        AlgorithmMetrics metrics = new AlgorithmMetrics();
        MergeSort sorter = new MergeSort(metrics);

        // Empty array
        int[] empty = {};
        sorter.sort(empty);
        assertEquals(0, empty.length);

        // Single element
        int[] single = {42};
        sorter.sort(single);
        assertArrayEquals(new int[]{42}, single);

        // Two elements
        int[] two = {2, 1};
        sorter.sort(two);
        assertArrayEquals(new int[]{1, 2}, two);
    }
}