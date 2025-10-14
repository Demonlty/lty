package sorts;

import java.util.List;

public class Sorts {

    public static void print(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length -1){
                System.out.print(" ");
            }
        }
    }

    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    //选择排序
    public static void selectionSort(int[] arr){
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int r = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[r]){
                    r = j;
                }
            }
            swap(arr, i, r);
        }
    }


    public static void main(String[] args) {
        int[] arr = new int[]{8,7,2,1,6,3,9};
        selectionSort(arr);
        print(arr);
    }
}
