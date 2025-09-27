package org.example.algorithms;

import org.example.metrics.AlgorithmMetrics;
import org.example.metrics.RecursionDepthTracker;

public class MergeSort {
    private AlgorithmMetrics metrics;
    private static final int Insertion_sort = 15;

    public MergeSort(AlgorithmMetrics metrics) {
        this.metrics = metrics;
    }

    public void sort(int[] arr) {
        metrics.startTimer();
        RecursionDepthTracker.reset();
        try {
            int [] buffer = new int[arr.length];
            mergeSort(arr, buffer, 0, arr.length-1);
        }
        finally {
            metrics.stopTimer();
        }
    }

    private void mergeSort(int[] arr, int[] buffer, int left, int right) {
        int depth = RecursionDepthTracker.enter();
        metrics.recordRecursionDepth(depth);

        try {
            if (right - left <= Insertion_sort) {
                insertionSort(arr, left, right);
                return;
            }

            if (left >= right) {
                return;
            }

            int mid = left + (right - left) / 2;

            mergeSort(arr, buffer, left, mid);
            mergeSort(arr, buffer, mid + 1, right);

            metrics.recordComparison();
            if (arr[mid] <= arr[mid + 1]) {
                return;
            }

            merge(arr, buffer, left, mid, right);
        } finally {
            RecursionDepthTracker.exit();
        }
    }

    private void merge(int[] arr, int[] buffer, int left, int mid, int right) {
        System.arraycopy(arr,left,buffer,left,right-left+1);

        int i = left, j = mid+1, k = left;

        while(i <= mid && j <= right) {
            metrics.recordComparison();
            if(buffer[i] <= buffer[j]) {
                arr[k++] = buffer[i++];
            }
            else {
                arr[k++] = buffer[j++];
            }
        }

        while(i<=mid) {
            arr[k++] = buffer[i++];
        }
    }

    private void insertionSort(int[] arr, int left, int right) {
        for(int i = left + 1; i <= right; i++ ) {
            int key = arr[i];
            int j = i-1;

            while (j >= left) {
                metrics.recordComparison();
                if(arr[j] > key) {
                    arr[j+1] = arr[j];
                    j--;
                }
                else {
                    break;
                }
            }
            arr[j+1] = key;
        }
    }
}


