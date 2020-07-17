package com.hanl.dams.common.config;

import com.sun.deploy.util.ArrayUtil;

import java.util.Arrays;

/**
 * @author: Hanl
 * @date :2020/5/20
 * @desc:
 */
public class ChooseSort {

    public static void main(String[] args) {
        int[] arr = {10, 6, 3, 8, 1, 5, 7};
        sort(arr);
        System.out.println(Arrays.toString(arr));
        int[] arr1 = {10, 6, 3, 8, 1, 5, 7};
        sort(arr1);
        System.out.println(Arrays.toString(arr1));

        merge(new int[]{1, 4, 7, 8, 3, 6, 9});
    }


    public static void merge(int[] arr) {
        int[] tmp = new int[arr.length];
        int i = 0;
        int mid = arr.length / 2;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j < arr.length) {
            if (arr[i] < arr[j]) {
                tmp[k] = arr[i];
                i++;
                k++;
            } else {
                tmp[k] = arr[j];
                j++;
                k++;
            }
        }
        while (i <= mid) tmp[k++] = arr[i++];
        while (j < arr.length) tmp[k++] = arr[j++];

        System.out.println(Arrays.toString(tmp));

    }

    public static void sort(int arr[]) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            int a = arr[i];
            for (int j = i + 1; j < len; j++) {
                int b = arr[j];
                if (a > b) {
                    arr[i] = b;
                    arr[j] = a;
                    a = b;
                }
            }

        }
    }

    public static void sort1(int arr[]) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - 1; j++) {
                int a = arr[j];
                int b = arr[j];
                if (a > b) {

                    arr[j] = a;
                    arr[i] = b;
                }
            }

        }
    }
}
