package org.example.util;

import org.example.metrics.AlgorithmMetrics;
import java.util.Random;

public class ArrayUtils {

    // Guard methods for input validation
    public static void checkNull(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
    }

    public static void checkBounds(int[] arr, int low, int high) {
        if (low < 0 || high >= arr.length || low > high) {
            throw new IllegalArgumentException(
                    String.format("Invalid bounds: low=%d, high=%d, array length=%d",
                            low, high, arr.length)
            );
        }
    }

    // Swap method used by QuickSort, Select, etc.
    public static void swap(int[] arr, int i, int j, AlgorithmMetrics metrics) {
        if (metrics != null) {
            metrics.recordArrayAccess(4); // 2 reads + 2 writes
        }
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Shuffle method for randomized algorithms
    public static void shuffle(int[] arr, Random random, AlgorithmMetrics metrics) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(arr, i, j, metrics);
        }
    }

    // Partition method used by QuickSort and Select
    public static int partition(int[] arr, int low, int high, int pivotIndex,
                                AlgorithmMetrics metrics) {
        int pivotValue = arr[pivotIndex];
        swap(arr, pivotIndex, high, metrics); // Move pivot to end

        int storeIndex = low;
        for (int i = low; i < high; i++) {
            if (metrics != null) metrics.recordComparison();
            if (arr[i] < pivotValue) {
                swap(arr, storeIndex, i, metrics);
                storeIndex++;
            }
        }

        swap(arr, storeIndex, high, metrics); // Move pivot to final position
        return storeIndex;
    }

    // Check if array is sorted (for validation)
    public static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }

    // Generate test arrays
    public static int[] generateRandomArray(int size, int maxValue, Random random) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(maxValue);
        }
        return arr;
    }
}