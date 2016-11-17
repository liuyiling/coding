package com.liuyiling.java.basic;

/**
 * Created by liuyl on 16/8/19.
 */
public class BubbleSort {

    public static void main(String[] agrs){
        int[] arr = new int[]{7, 2, 3, 1, 4, 5, 6};
        bubbleSort(arr);

        for(int i : arr){
            System.out.println(i);
        }
    }

    private static void bubbleSort(int[] arr) {

        int size = arr.length;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size - 1 - i; j++){
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

}
