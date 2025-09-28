package org.example.algorithms;

import org.example.metrics.AlgorithmMetrics;
import org.example.metrics.RecursionDepthTracker;
import java.util.Arrays;

public class Select {
    private AlgorithmMetrics metrics;
    private static final int GROUP_SIZE = 5;

    public Select(AlgorithmMetrics metrics) {
        this.metrics = metrics;
    }


    public int select(int[] arr, int k) {
        if (k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("k must be between 0 and " + (arr.length - 1));
        }

        metrics.startTimer();
        RecursionDepthTracker.reset();
        try {
            int[] copy = arr.clone(); // Work on copy to preserve input
            return select(copy, 0, copy.length - 1, k);
        } finally {
            metrics.stopTimer();
        }
    }

    private int select(int[] arr, int left, int right, int k) {
        int depth = RecursionDepthTracker.enter();
        metrics.recordRecursionDepth(depth);

        try {

            if (right - left + 1 <= GROUP_SIZE) {
                insertionSort(arr, left, right);
                return arr[left + k];
            }


            int numGroups = (right - left + 1 + GROUP_SIZE - 1) / GROUP_SIZE;
            int[] medians = new int[numGroups];

            for (int i = 0; i < numGroups; i++) {
                int groupLeft = left + i * GROUP_SIZE;
                int groupRight = Math.min(groupLeft + GROUP_SIZE - 1, right);
                medians[i] = findMedianOfFive(arr, groupLeft, groupRight);
            }


            int medianOfMedians = select(medians, 0, medians.length - 1, medians.length / 2);


            int pivotIndex = partition(arr, left, right, medianOfMedians);


            if (k == pivotIndex - left) {
                return arr[pivotIndex];
            } else if (k < pivotIndex - left) {
                return select(arr, left, pivotIndex - 1, k);
            } else {
                return select(arr, pivotIndex + 1, right, k - (pivotIndex - left + 1));
            }
        } finally {
            RecursionDepthTracker.exit();
        }
    }

    private int findMedianOfFive(int[] arr, int left, int right) {

        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left) {
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
        return arr[left + (right - left) / 2];
    }

    private int partition(int[] arr, int left, int right, int pivotValue) {

        int pivotIndex = left;
        for (int i = left; i <= right; i++) {
            if (arr[i] == pivotValue) {
                pivotIndex = i;
                break;
            }
        }
        swap(arr, pivotIndex, right);


        int storeIndex = left;
        for (int i = left; i < right; i++) {
            metrics.recordComparison();
            if (arr[i] < pivotValue) {
                swap(arr, storeIndex, i);
                storeIndex++;
            }
        }

        swap(arr, storeIndex, right);
        return storeIndex;
    }

    private void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left) {
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


    public int findMedian(int[] arr) {
        return select(arr, arr.length / 2);
    }
}