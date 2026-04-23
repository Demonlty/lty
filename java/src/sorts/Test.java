package sorts;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Test {

    //与运算 全1为1 其他为0

    //异或运算 相同为0 不同为1


    //一堆数中，一种数出现奇数次，其他都是偶数次，结果为全部异或和
    public static int printNum1(int[] arr){
        if (arr.length < 3){
            return 0;
        }
        int eor = 0;
        for (int i = 0; i<arr.length; i++){
            eor = eor ^ arr[i];
        }
        return eor;
    }

    //一堆数中，两种数出现奇数次，其他都是偶数次
    public static String printNum2(int[] arr){
        if (arr.length < 3){
            return "";
        }
        int eor = 0;
        for (int i : arr){
            eor = eor ^ i;
        }
        int onlyOne = 0;
        //取最右侧的1
        int rightOne = eor & (~eor + 1);
        for (int i : arr){
//            if ((rightOne & i) == 0){
//            if ((rightOne & i) != 0){
            if ((rightOne & i) == rightOne){
                onlyOne = onlyOne ^ i;
            }
        }
        return onlyOne + " " + (onlyOne ^ eor);
    }

    //求arr[L..R]范围上的最大值
    public static int getMax(int[] arr){
        return process(arr, 0, arr.length - 1);
    }
    public static int process(int[] arr, int L, int R){
        if (L == R){
            return arr[L];
        }
        int mid = L + (R - L) >> 1;
        int maxLeft = process(arr, L, mid);
        int maxRight = process(arr, mid + 1, R);
        return Math.max(maxLeft, maxRight);
    }

    //小和问题 归并
    public static int getSmallSum(int[] arr){
        if (arr == null || arr.length < 1){
            return 0;
        }
        return process1(arr, 0, arr.length - 1);
    }
    public static int process1(int[] arr,int L, int R){
        if (L == R){
            return 0;
        }
        int mid = L + ((R - L) >> 1);
        return process1(arr, L, mid) + process1(arr, mid + 1, R) + merge1(arr, L, mid, R);
    }
    public static int merge1(int[] arr, int L, int M, int R){
        int[] help = new int[R - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = M + 1;
        int res = 0;
        while (p1 <= M && p2 <= R){
            res += arr[p1] < arr[p2] ? (R - p2 + 1) * arr[p1] : 0;
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= M){
            help[i++] = arr[p1++];
        }
        while (p2 <= R){
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++){
            arr[L + i] = help[i];
        }
        return res;
    }

    //逆序对问题 归并
    public static int getReverseOrder(int[] arr){
        if (arr == null || arr.length < 1){
            return 0;
        }
        return process2(arr, 0, arr.length - 1);
    }
    public static int process2(int[] arr,int L, int R){
        if (L == R){
            return 0;
        }
        int mid = L + ((R - L) >> 1);
        return process2(arr, L, mid) + process2(arr, mid + 1, R) + merge2(arr, L, mid, R);
    }
    public static int merge2(int[] arr, int L, int M, int R){
        int[] help = new int[R - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = M + 1;
        int res = 0;
        while (p1 <= M && p2 <= R){
            if (arr[p1] > arr[p2]) res++;
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= M){
            res += (M - p1) * (R - M);
            help[i++] = arr[p1++];
        }
        while (p2 <= R){
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++){
            arr[L + i] = help[i];
        }
        return res;
    }

    //荷兰国旗问题
    //小于区域 大于区域
    //<1:[i]<num，[i]和小于区域的下一个做交换，i++
    //<2:[i]==num，i++
    //<3:[i]>num，[i]和大于区域的前一个做交换，i不变
    public static void dutchFlagIssue(int[] arr, int num){
        if (arr == null || arr.length < 3){
            return;
        }
        int left = -1;
        int right = arr.length;
        int i = 0;
        while (i < right){
            if (arr[i] < num){
                Sorts.swap(arr, ++left, i++);
            }else if (arr[i] > num){
                Sorts.swap(arr, --right, i);
            }else {
                i++;
            }
        }
    }

    //快速排序
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
        int less = L -1;
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

    //堆
    public void heap(int[] arr){
        //默认小根堆
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        //大根堆
        PriorityQueue<Integer> MaxHeap = new PriorityQueue<>(new Acomp());
    }
    public class Acomp implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public static void main(String[] args) {
        int[] arr = {1,2,4,2,5};
//        int[] arr1 = {2,6,5,2,3};
//        System.out.println(printNum1(arr));
//        System.out.println(printNum2(arr1));
//        System.out.println(getSmallSum(arr));
//        System.out.println(getReverseOrder(arr1));
//
//        getReverseOrder(arr1);
//        Sorts.print(arr1);
//        dutchFlagIssue(arr, 3);
        quickSort(arr, 0, arr.length - 1);
        Sorts.print(arr);
    }
}
