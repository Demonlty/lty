package sorts;

import java.util.List;

public class Sorts {

    public static void print(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length -1){
                System.out.print(" ");
            }else {
                System.out.println("");
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

    //冒泡排序
    public static void bubbleSort(int[] arr){
        int n = arr.length;
        //方法一
        /*for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i -1; j++) {
                if (arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                }
            }
        }*/
        //方法二
        Boolean flag = true;
        while (flag){
            flag = false;
            for (int i = 0; i < n - 1; i++) {
                if (arr[i] > arr[i+1]){
                    flag = true;
                    swap(arr, i, i+1);
                }
            }
        }
    }
    //插入排序 砌墙
    public static void insertionSort(int[] arr){
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int index = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > index){
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = index;
        }

    }
    //计数排序
    public static void countingSort(int[] arr){
        int n = arr.length;
        int max = arr[0];
//        int min = arr[0];
        for (int i = 0; i < n; i++) {
            max = Math.max(max,arr[i]);
//            min = Math.min(min,arr[i]);
        }

        int[] r = new int[max+1];
        for (int i = 0; i < n; i++) {
            r[arr[i]]++;
        }
        int index = 0;
        //方法一
        /*for (int i = 0; i < r.length; i++) {
            for (int i1 = 0; i1 < r[i]; i1++) {
                arr[index++] = i;
            }
        }*/

        //方法二
        for (int i = 1; i < r.length; i++) {
            r[i] += r[i-1];
        }

        int[] t = new int[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) {
            t[--r[arr[i]]] = arr[i];
        }

        for (int i = 0; i < t.length; i++) {
            arr[index++] = t[i];
        }
    }


    public static void main(String[] args) {
        int[] arr = new int[]{8,7,2,1,6,3,9};
//        selectionSort(arr);
//        bubbleSort(arr);
//        insertionSort(arr);
        countingSort(arr);
        print(arr);
    }
}
