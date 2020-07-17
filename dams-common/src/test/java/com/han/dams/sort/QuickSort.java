package com.han.dams.sort;

import java.util.Arrays;

public class QuickSort {


    public static void main(String args[]) {

        int arr[] = new int[]{1, 4, 7, 8, 0, 7, 5, 5, 3, 9};

        sort(arr, 0, arr.length - 1);
        System.out.println("end=" + Arrays.toString(arr));
    }

    public static void sort(int arr[], int left, int right) {
        if (left >= right) {

            return;
        }
        int pivot = arr[right];
        int i = left;
        int j = right;

        while (i < j) {
            while (i < j && arr[i] <= pivot) {
                i++;
            }

            while (i < j && arr[j] >= pivot) {
                j--;
            }
            swap(arr, i, j);
        }

        arr[right] = arr[i];
        arr[i] = pivot;
        sort(arr, left, i - 1);
        sort(arr, i + 1, right);

    }

    /**
     * 交换数组中两个位置的元素
     */
    private static void swap(int[] list, int left, int right) {
        System.out.println(list[left] + "," + list[right]);
        int temp;
        if (list != null && list.length > 0) {
            temp = list[left];
            list[left] = list[right];
            list[right] = temp;
        }
    }
}
