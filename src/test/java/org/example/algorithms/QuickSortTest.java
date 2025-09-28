package org.example.algorithms;

import org.example.metrics.AlgorithmMetrics;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class QuickSortTest {

    @Test
    public void testSortingCorrectness() {
        int[] arr = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        AlgorithmMetrics metrics = new AlgorithmMetrics();
        QuickSort sorter = new QuickSort(metrics);
        sorter.sort(arr);

        assertArrayEquals(expected, arr);
        assertTrue(metrics.getComparisons() > 0);
    }

    @Test
    public void testRandomizedPivot() {
        // Test that randomized pivot doesn't break sorting
        int[] arr = new int[100];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(1000);
        }

        AlgorithmMetrics metrics = new AlgorithmMetrics();
        QuickSort sorter = new QuickSort(metrics);
        sorter.sort(arr);

        assertTrue(isSorted(arr), "Array should be sorted");
    }

    @Test
    public void testRecursionDepthBounded() {
        // Test that smaller-first recursion bounds depth to O(log n)
        int[] arr = new int[10000];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(100000);
        }

        AlgorithmMetrics metrics = new AlgorithmMetrics();
        QuickSort sorter = new QuickSort(metrics);
        sorter.sort(arr);

        // For n=10000, depth should be ~2*log2(10000) ≈ 26-30
        int maxExpectedDepth = (int) (2.5 * (Math.log(arr.length) / Math.log(2)));
        assertTrue(metrics.getMaxRecursionDepth() <= maxExpectedDepth,
                "Recursion depth should be bounded by O(log n). Got: " +
                        metrics.getMaxRecursionDepth() + ", expected <= " + maxExpectedDepth);
    }

    @Test
    public void testEdgeCases() {
        AlgorithmMetrics metrics = new AlgorithmMetrics();
        QuickSort sorter = new QuickSort(metrics);

        // Empty array
        int[] empty = {};
        sorter.sort(empty);
        assertEquals(0, empty.length);

        // Single element
        int[] single = {42};
        sorter.sort(single);
        assertArrayEquals(new int[]{42}, single);

        // Already sorted
        int[] sorted = {1, 2, 3, 4, 5};
        sorter.sort(sorted);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, sorted);

        // All equal elements
        int[] equal = {5, 5, 5, 5, 5};
        sorter.sort(equal);
        assertArrayEquals(new int[]{5, 5, 5, 5, 5}, equal);
    }

    private boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) return false;
        }
        return true;
    }
}