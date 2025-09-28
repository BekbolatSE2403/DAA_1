package org.example.algorithms;

import org.example.metrics.AlgorithmMetrics;
import org.example.metrics.RecursionDepthTracker;
import java.util.Random;

public class QuickSort {
    private AlgorithmMetrics metrics;
    private Random random;
    private static final int INSERTION_SORT_CUTOFF = 10;

    public QuickSort(AlgorithmMetrics metrics) {
        this.metrics = metrics;
        this.random = new Random();
    }

    public void sort(int[] arr) {
        metrics.startTimer();
        RecursionDepthTracker.reset();
        try {
            quickSort(arr, 0, arr.length - 1);
        } finally {
            metrics.stopTimer();
        }
    }

    private void quickSort(int[] arr, int low, int high) {
        int depth = RecursionDepthTracker.enter();
        metrics.recordRecursionDepth(depth);

        try {
            // Use iterative approach for larger partitions (SMALLER-FIRST OPTIMIZATION)
            while (high - low > INSERTION_SORT_CUTOFF) {
                int pivotIndex = randomizedPartition(arr, low, high);

                int leftSize = pivotIndex - low;
                int rightSize = high - pivotIndex;

                // Recurse on smaller partition, iterate on larger (BOUNDED STACK)
                if (leftSize < rightSize) {
                    quickSort(arr, low, pivotIndex - 1);  // Recurse smaller left
                    low = pivotIndex + 1;                 // Iterate larger right
                } else {
                    quickSort(arr, pivotIndex + 1, high); // Recurse smaller right
                    high = pivotIndex - 1;                // Iterate larger left
                }
            }

            // Final insertion sort for small subarrays
            if (low < high) {
                insertionSort(arr, low, high);
            }

        } finally {
            RecursionDepthTracker.exit();
        }
    }

    private int randomizedPartition(int[] arr, int low, int high) {
        // RANDOMIZED PIVOT SELECTION
        int pivotIndex = low + random.nextInt(high - low + 1);
        swap(arr, pivotIndex, high);
        return partition(arr, low, high);
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.recordComparison();
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    private void insertionSort(int[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= low) {
                metrics.recordComparison();
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}