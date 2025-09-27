package org.example.algorithms;

import jdk.dynalink.linker.LinkerServices;
import org.example.metrics.AlgorithmMetrics;
import org.example.metrics.RecursionDepthTracker;

import java.lang.classfile.attribute.InnerClassesAttribute;
import java.util.Random;

public class QuickSort {
    private AlgorithmMetrics metrics;
    private Random random;
    private static final int InsertionSort = 10;

    public QuickSort(AlgorithmMetrics metrics) {
        this.metrics = metrics;
        this.random = new Random();
    }

    public void sort(int[] arr) {
        metrics.startTimer();
        RecursionDepthTracker.reset();
        try {
            quickSort(arr, 0, arr.length - 1);
        }
        finally {
            metrics.stopTimer();
        }
    }

    private void quickSort(int [] arr, int low, int high) {
        int depth = RecursionDepthTracker.enter();
        metrics.recordRecursionDepth(depth);

        try {
            if (high - low <= InsertionSort) {
                insertionSort(arr, low, high);
                return;
            }

            if (low >= high) {
                return;
            }

            int pivotIndex = Partition(arr, low, high);

            int leftSize = pivotIndex - low;
            int rightSize = high - pivotIndex;

            if (leftSize < rightSize) {
                quickSort(arr, low, pivotIndex - 1);
                quickSort(arr, pivotIndex + 1, high);
            } else {
                quickSort(arr, pivotIndex + 1, high);
                quickSort(arr, low, pivotIndex - 1);
            }
        } finally {
            RecursionDepthTracker.exit();
        }
    }

    private int Partition(int[] arr, int low, int high) {
        int pivotIndex = low + random.nextInt(high - low + 1);
        swap(arr, pivotIndex, high);

        return partition(arr,low,high);
    }

    private int partition(int[] arr, int low , int high) {
        int pivot = arr[high];
        int i = low - 1;

        for(int j = low; j < high; j++) {
            metrics.recordComparison();
            if(arr[j] <= pivot) {
                i++;
                swap(arr,i,j);
            }
        }

        swap(arr, i+1, high);
        return i+1;
    }

    private void insertionSort(int[] arr , int low , int high) {
        for(int i = low + 1; i <= high; i++) {
            int key = arr[i];
            int j = i-1;

            while (j>=low) {
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

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
