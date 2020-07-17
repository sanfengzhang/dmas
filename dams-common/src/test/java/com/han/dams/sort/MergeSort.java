package com.han.dams.sort;

import java.util.Arrays;

public class MergeSort {


    public static void main(String args[]) {

        int arr[] = new int[]{1, 4, 7, 8, 3, 6, 9};
        int[] t = merge(arr);
        System.out.println(Arrays.toString(t));


        int[] a = {1, 4, 7, 8, 10, 14, 15};

        int[] b = {3, 6, 9, 10, 13, 56};

        twoArr(a, b, 4);
        getMax(new int[]{-2, 1, -3, 4, -1, 2, 1,4,5,6,7, -5, 4,2});

        getMaxChengji(new int[]{2,3,-2,4});

    }

    public static int[] merge(int arr[]) {
        int len = arr.length;
        int mid = arr.length / 2;
        int[] tmp = new int[len];
        int i = 0;
        int j = mid + 1;

        int k = 0;
        while (i <= mid && j < len) {

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
        while (i <= mid) {
            tmp[k] = arr[i];
            i++;
            k++;
        }
        while (j < len) {
            tmp[k] = arr[j];
            j++;
            k++;
        }

        return tmp;
    }

    /**
     * 两个有序数组求第k个最大数，可以用归并排序得合并步骤进行改进
     *
     * @param a
     * @param b
     * @param step
     */
    public static void twoArr(int a[], int b[], int step) {
        int len1 = a.length;
        int len2 = b.length;

        int[] m = new int[len1 + len2];

        int count = 0;
        int i = len1;
        int j = len2;
        while (i >= 0 && j >= 0 && count < len1 && count < len2) {
            int p = a[i - 1];
            int q = b[j - 1];
            if (p > q) {
                m[count] = p;
                i--;
            } else {
                m[count] = q;
                j--;
            }

            if (count == step) {
                System.out.println(m[count]);

            }
            count++;
        }

        System.out.println(Arrays.toString(m));

    }

    /**
     * 求某数组得最大和子序列，{-2,1,-3,4,-1,2,1,-5,4}
     * <p>
     *  注意你某一个子序列一定是以某个位置i结束的！！
     * 假设计算到第i个位置得时候，要求最大子序列。那么在前i-1个数字中一定已经求出
     * 最大子序列了{bn}，然后判断i位置的数对{bn}是正反馈还是负反馈
     *
     * @param arr
     */
    public static void getMax(int arr[]) {
        int result = arr[0];
        int len = arr.length;
        for (int i = 1; i < len; i++) {
            arr[i] += Math.max(arr[i - 1], 0);
            result = Math.max(result, arr[i]);

        }
        System.out.println(result);
    }

    public static void getMaxChengji(int arr[]) {
        int result=arr[0];
        int len = arr.length;
        for(int i=1;i<len;i++){
           arr[i]=Math.max(arr[i-1]*arr[i],arr[i]);
            result = Math.max(result, arr[i]);
        }
        System.out.println(result);
    }

    public static void getHigh(int arr[]){

    }


    /**
     * 要找出n和n-1和n-2得关系
     *
     * @param n
     * @return
     */
    public static int getSomeStep(int n){
          if(n==0||n==1){
              return n;
          }

        return getSomeStep(n-1)+getSomeStep(n-2);
    }

}
