package sorts;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    //生成随机数组
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        //Math.random() -> [0.1) 所有的小数，等概率返回一个
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
            arr[i] = Math.abs(arr[i]);
        }
        return arr;
    }

    //正常程序排序
    public static int[] Sort(int[] arr){
        int[] a = new int[arr.length];
        System.arraycopy(arr, 0, a, 0, arr.length);
        Arrays.sort(a);
        return a;
    }

    //对数器检查
    public static boolean checkSort(int[] arr, int[] arr1){
        boolean flag = true;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != arr1[i]) {
                flag = false;
            }
        }
        return flag;
    }

    //8,7,2,1,6,3,9
    //0 1 2 3 4 5 6
    //选择排序
    public static void selectionSort(int[] arr){
        int n = arr.length;
        //注意边界问题 n-1
        for (int i = 0; i < n - 1; i++) {
            int r = i;
            for (int j = i + 1; j < n; j++) {
                r = arr[j] < arr[r] ? j : r;
            }
            swap(arr, i, r);
        }
        //双向选择排序
        /*for (int i = 0; i < n/2; i++) {
            int minPos = i, maxPos = n - i -1;
            for (int j = i ; j < n - i; j++){
                minPos = arr[j]  < arr[minPos] ? j : minPos;
                maxPos = arr[j] > arr[maxPos] ? j : maxPos;
            }
            swap(arr, i, minPos);
            //修正位置
            if (maxPos == i) maxPos = minPos;
            swap(arr, n - i -1, maxPos);
            print(arr);
        }*/
        //双向选择排序
        /*int left = 0, right = n -1;
        while (left < right){
            int minPos = left, maxPos = left;
            for (int j = left ; j <= right; j++){
                minPos = arr[j]  < arr[minPos] ? j : minPos;
                maxPos = arr[j] > arr[maxPos] ? j : maxPos;
            }
            swap(arr, left, minPos);
            //修正位置
            if (maxPos == left) maxPos = minPos;
            swap(arr, right, maxPos);
            left++;
            right--;
        }*/
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
        for (int i = n - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                }
            }
        }
        //方法二
        /*Boolean flag = true;
        while (flag){
            flag = false;
            for (int i = 0; i < n - 1; i++) {
                if (arr[i] > arr[i+1]){
                    flag = true;
                    swap(arr, i, i+1);
                }
            }
        }*/
    }
    //插入排序 扑克牌 从右往左插入
    public static void insertionSort(int[] arr){
        int n = arr.length;
        /*for (int i = 1; i < n; i++) {
            int index = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > index){
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = index;
        }*/
        for (int i = 1; i < n; i++) {
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }

    //希尔排序
    public static void shellSort(int[] arr){
        int n = arr.length;
        //Kunth序列 h = 3*h + 1
        int h = 1;
        while (h < n/3) {
            h = h * 3 + 1;
        }
        for (int gap = h; gap > 0; gap = (gap - 1) / 3) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i - gap;
                while (j >= 0 && temp < arr[j]) {
                    arr[j+gap] = arr[j];
                    j -= gap;
                }
                arr[j+gap] = temp;
            }
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

    //快速排序
    /*public static void quickSort(int[] arr, int begin, int end){
        if (begin >= end) return ;
        //方法一
        *//*int index = quickSortIn(arr, left, right);
        quickSort(arr,left,index - 1);
        quickSort(arr,index + 1, right);*//*
        //方法二
        int mid = midThree(arr, begin, end);
        swap(arr, mid, begin);
        int[] ints = quickSortInTwo(arr, begin, end);
        int low = ints[0];
        int high = ints[1];
        quickSort(arr,begin, low - 1);
        quickSort(arr,high + 1, end);
    }*/

    public static int quickSortIn(int[] arr, int left, int right){
        int prev = left + 1;
        int cur = prev + 1;
        int key = arr[left];
        while (cur <= right){
            if (arr[cur] < key){
                swap(arr, prev, cur);
                prev++;
            }
            cur++;
        }
        swap(arr, left, prev);
        return prev;
    }
    //快速排序优化版 荷兰国旗分区(红、白、蓝)
    public static int[] quickSortInTwo(int[] arr, int left, int right){
        int key = arr[left];
        int cur = left + 1;
        while (cur <= right){
            if (arr[cur] < key){
                swap(arr, left++, cur++);
            }else if (arr[cur] > key){
                swap(arr, cur, right--);
            }else {
                cur++;
            }
        }
        return new int[]{left, right};
    }

    //取三者中的中间值
    public static int midThree(int[] arr, int left, int right){
        int mid = (left + right) / 2;
        if (arr[left] < arr[mid]){
            if (arr[mid] < arr[right]){
                return mid;
            } else {
                if (arr[left] < arr[right]){
                    return right;
                }else {
                    return left;
                }
            }
        }else {
            if (arr[mid] > arr[right]){
                return mid;
            }else {
                if (arr[left] < arr[right]){
                    return left;
                }else {
                    return right;
                }
            }
        }
        /*int mid = (left + right) / 2;
        if (arr[left] < arr[mid])
        {
            if (arr[mid] < arr[right])
            {
                return mid;
            }
            else if (arr[left] > arr[right])
            {
                return left;
            }
            else
            {
                return right;
            }
        }
        else   //arr[left] > arr[mid]
        {
            if (arr[mid] > arr[right])
            {
                return mid;
            }
            else if (arr[left] < arr[right])
            {
                return left;
            }
            else
            {
                return right;
            }
        }*/
    }

    //快速排序（荷兰国旗版）
    public static void quickSort(int[] arr, int L, int R){
        if (L < R){
            //去随机值交换位置
            Sorts.swap(arr, L + (int) (Math.random() * (R - L + 1)), R);
            int[] partition = partition(arr, L, R);
            quickSort(arr, L, partition[0] - 1);
            quickSort(arr, partition[1] + 1, R);
        }
    }
    public static int[] partition(int[] arr, int L, int R){
        int less = L - 1;
        int more = R;
        while (L < more){
            if (arr[L] < arr[R]){
                Sorts.swap(arr, ++less, L++);
            }else if (arr[L] > arr[R]){
                Sorts.swap(arr, --more, L);
            }else {
                L++;
            }
        }
        Sorts.swap(arr, more, R);
        return new int[]{less + 1, more};
    }

    //归并排序
    public static void mergeSort(int[] arr){
        if (arr == null || arr.length < 2) return;
        int n = arr.length;
        process(arr, 0, n-1);
    }
    public static void process(int[] arr, int L, int R){
        if (L == R){
            return;
        }
        int mid = L + ((R - L) >> 1);
        process(arr, L, mid);
        process(arr, mid + 1, R);
        merge(arr, L, mid, R);
    }
    public static void merge(int[] arr, int L, int mid, int R){
        int[] help = new  int[R - L + 1];
        int i = 0;
        int p1 =  L;
        int p2 = mid + 1;
        while (p1 <= mid && p2 <= R){
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid){
            help[i++] = arr[p1++];
        }
        while (p2 <= R){
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++){
            arr[L + i] = help[i];
        }
    }

    //堆排序
    //树左孩子 （2*i）+1
    //树右孩子 （2*i）+2
    //树父节点 （i-1）/2
    public static void heapSort(int[] arr){
        if (arr == null || arr.length < 2) return;
        //转化大根堆 方法一
//        for (int i = 0; i < arr.length; i++) {
//            heapInsert(arr, i);
//        }
        //转化大根堆 方法二
        for (int i = arr.length - 1; i >= 0; i--) {
            heapIfy(arr, i, arr.length);
        }
        //排序
        int heapSize = arr.length;
        swap(arr, 0, --heapSize);
        while (heapSize > 0){
            heapIfy(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
        }
    }
    public static void heapInsert(int[] arr,int index){
        while (arr[index] > arr[(index - 1) / 2]){
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }
    public static void heapIfy(int[] arr,int index, int heapSize){
        //左孩子
        int left = index * 2 + 1;
        //判断是否有孩子存在
        while (left < heapSize){
            //取左右孩子的最大值的位置
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            //与父节点比较，取最大值的位置并和父节点交换
            largest = arr[largest] > arr[index] ?  largest : index;
            swap(arr, largest, index);
            if (largest == index){
                break;
            }
            index = largest;
            left = index * 2 + 1;
        }
    }

    //基数排序
    public static void radixSort(int[] arr, int L, int R, int digit){
        final int radix = 10;
        int i = 0, j = 0;
        int[] bucket = new int[R - L + 1];

        for (int d = 1; d <= digit; d++){

            int[] count = new int[radix];

            for (i = L; i <= R; i++){
                j = getDigit(arr[i], d);
                count[j]++;
            }
            for (j = 1; j < radix; j++){
                count[j] += count[j - 1];
            }

            for (i = R; i >= L; i--){
                j = getDigit(arr[i], d);
                bucket[count[j]-1] = arr[i];
                count[j]--;
            }
            for (i = L, j = 0; i <= R; i++, j++){
                arr[i] = bucket[j];
            }
        }

    }

    private static int getDigit(int x, int d) {
        return ((Math.abs(x) / ((int) Math.pow(10, d -1))) % 10);
    }

    public static void main(String[] args) {
        int n = 10;
        boolean flag = true;
        long l = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {

//        int[] arr = new int[]{8,7,2,1,6,3,9};
            int[] arr = generateRandomArray(100,1000);
            int[] sort = Sort(arr);
//            selectionSort(arr);
//        bubbleSort(arr);
//            shellSort(arr);
//        insertionSort(arr);
//        countingSort(arr);
//        quickSort(arr, 0, arr.length - 1);
//            mergeSort(arr);
//            quickSort(arr,0,arr.length-1);
//            heapSort(arr);
            radixSort(arr,0, arr.length - 1, 3);
            flag = checkSort(sort, arr);
//        print(arr);
        }
        long r = System.currentTimeMillis();
        System.out.println(flag);
        System.out.println("time: " + (r-l));
    }
}
