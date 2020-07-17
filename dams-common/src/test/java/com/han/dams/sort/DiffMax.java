package com.han.dams.sort;


import java.util.Arrays;

public class DiffMax {

    public static void main(String[] args) {
        int[] a = new int[]{4, 9, 20, 3, 16, 6, 5, 7, 1};
         sort(a,0,30);//1,3,4,5,6,7,9,16,20
        lvxu(a);
    }

    public static void sort(int[] arr, int min, int max) {
        int len = arr.length;
        int k = max - min + 1;
        int[] b=new int[k];

        for(int i=0;i<len;i++){
            int a=arr[i];
            int m=a-min;
            b[m]=a;
        }
        System.out.println(Arrays.toString(b));
    }

    public static void lvxu(int arr[]){

         int len=arr.length;
         if(0==len||1==len){
             return;
         }
         int mid=len/2;

         for(int i=0;i<mid;i++){
             int a=arr[i];
             int b=arr[len-i-1];
             arr[i]=b;
             arr[len-i-1]=a;
         }

        System.out.println(Arrays.toString(arr));

    }


}
