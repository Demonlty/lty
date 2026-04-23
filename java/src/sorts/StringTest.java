package sorts;

import java.util.*;
import java.util.LinkedList;

public class StringTest {

    //是否字串
    //暴力求解：O(N*M)
    //KMP算法
    public static int getIndexOf(String s, String m){
        if (s == null || m == null || m.isEmpty() || s.length() < m.length()){
            return -1;
        }
        char[] str1 = s.toCharArray();
        char[] str2 = m.toCharArray();
        int i1 = 0;
        int i2 = 0;
        int[] next = getNextArray(str2); //O（m）
        //O(n)  看i1和i1-i2的变化范围，实际为2n
        while (i1 < str1.length && i2 < str2.length){
            if (str1[i1] == str2[i2]){
                i1++;
                i2++;
            }else if (next[i2] == -1){
                i1++;
            }else {
                i2 = next[i2];
            }
        }
        //i1越界 或者 i2越界
        return i2 == str2.length ? i1 - i2 : -1; //返回相同的串的开头位置 i1 - i2（m的长度）
    }
    //next数组i位置的值表示 0~i-1位置上的字符串的前缀和后缀相等的最大长度（不算整个字符串（0~i-1））
    private static int[] getNextArray(char[] str) {
        if (str == null || str.length == 1){
            return new int[]{-1};
        }
        int[] next = new int[str.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2;
        int cn = 0; //cn位置的字符和i位置比较，，cn又代表i-1的前缀和后缀相等的最大长度
        //O(n) 看i1和i1-cn的变化范围，实际为2n
        while (i < str.length){
            if (str[i-1] == str[cn]){
                next[i++] = ++cn;
            } else if (cn > 0){
                cn = next[cn];
            }else {
                next[i++] = 0;
            }
        }
        return next;
    }

    //Manacher算法

    //最长回文子串
    /**
     * 经典解法
     * 补位后 每个位置的左右扩充，得到最长的回文串（回文直径），最后取最大的值/2。例如：122131121 补位后：#1#2#2#1#3#1#2#2#1#
     * 补位的字符可以和原字符串中的字符相同
     * 最坏情况是 1111111111，时间复杂度是O(n^2)
     */
    /**
     * 使用Manacher算法加速，时间复杂度优化到O（N）
     */
    public static char[] manacherString(String str){
        char[] charArray = str.toCharArray();
        char[] res = new char[str.length() * 2 + 1];
        int index = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : charArray[index++]; //i取奇偶数
        }
        return res;
    }
    //最大回文长度（Manacher算法）
    public static int maxLcpsLength(String s){
        if (s == null || s.length() == 0){
            return 0;
        }
        char[] str = manacherString(s);
        int[] pArr = new int[str.length]; //回文半径数组
        int C = -1; //中心
        int R = -1; //0~i位置最远回文半径的右边界再往右一个位置，有边界实为R-1
        int max = Integer.MIN_VALUE; //扩充的最大值
        for (int i = 0; i < str.length; i++) { //求每个位置的回文半径
            //先求出不用验证的回文区域，即i至少的回文区域，赋值给pArr[i]
            pArr[i] = i < R ? Math.min(pArr[2 * C - i], R - i) : 1;
            //不越界的情况下继续扩回文半径
            while (i + pArr[i] < str.length && i - pArr[i] >= 0){ 
                if (str[i + pArr[i]] == str[i - pArr[i]]){ //扩成功
                    pArr[i]++;
                }else { //扩失败
                    break;
                }
            }
            //更新R，C
            if (i + pArr[i] > R){
                R = i + pArr[i];
                C = i;
            }
            max = Math.max(max, pArr[i]);
        }
        //最大回文长度 = 最大回文半径 - 1 
        return max - 1;
    }

    /**
     * 窗口内最大值，最小值更新结构
     * 双端队列 维持从大到小的状态
     * 双端队列 维持从小到大的状态
     */
    //求窗口内的最大值
    public static int windowsMax(int[] arr, int L, int R){
        if (L > R || R < 0){
            return 0;
        }
        if (L == R){
            return arr[L];
        }
        //大 -> 小
        Deque<Integer> deque = new LinkedList<>();
        //滑动R的操作
        for (int i = 0; i <= R; i++) {
            if (deque.isEmpty() || arr[deque.getLast()] > arr[i]){
                deque.offerLast(i);
            }else {
                while (!deque.isEmpty() && arr[deque.getLast()] <= arr[i]){
                    deque.pollLast();
                }
                deque.offerLast(i);
            }
        }
        //滑动L的操作
        for (int i = 0; i < L; i++) {
            if (deque.getFirst() == i){
                deque.pollFirst();
            }
        }
        return deque.isEmpty() ? 0 : arr[deque.getFirst()];
    }
    //求窗口内的最小值
    public static int windowsMin(int[] arr, int L, int R){
        if (L > R || R < 0){
            return 0;
        }
        if (L == R){
            return arr[L];
        }
        //小 -> 大
        Deque<Integer> deque = new LinkedList<>();
        //滑动R的操作
        for (int i = 0; i <= R; i++) {
            if (deque.isEmpty() || arr[deque.getLast()] < arr[i]){
                deque.offerLast(i);
            }else {
                while (!deque.isEmpty() && arr[deque.getLast()] >= arr[i]){
                    deque.pollLast();
                }
                deque.offerLast(i);
            }
        }
        //滑动L的操作
        for (int i = 0; i < L; i++) {
            if (deque.getFirst() == i){
                deque.pollFirst();
            }
        }
        return deque.isEmpty() ? 0 : arr[deque.getFirst()];
    }

    /**
     * 单调栈结构
     */
    //i位置左边和右边比[i]大并且距离最近的分别是哪个位置，要求O(n)

    //数字不重复

    //重复数字 new Stack<LinkedList> 距离最近且最大
    public static Map<Integer,String[]> closestAndLargest(int[] arr){
        if (arr == null || arr.length == 0){
            return null;
        }
        Map<Integer,String[]> map = new HashMap<>();
        //取最大值，栈是底大顶小
        Stack<LinkedList<Integer>> s = new Stack<>();
        for (int i = 0; i < arr.length; i++) {

            LinkedList<Integer> list = new LinkedList<>();
            list.add(i);

            if (s.isEmpty()){
                s.push(list);
            }else {
                int value = (int) s.peek().get(0);
                if (arr[value] == arr[i]){
                    s.peek().add(i);
                }else if (arr[value] > arr[i]){
                    s.push(list);
                }else {
                    while (!s.isEmpty() && arr[s.peek().get(0)] < arr[i]){
                        LinkedList<Integer> pop = s.pop();
                        for (int j = 0; j < pop.size(); j++) {
                            int p = (int) pop.get(j);
                            String[] res = new String[2];
                            res[0] = s.isEmpty() ? null : String.valueOf(arr[s.peek().get(0)]);
                            res[1] = String.valueOf(arr[i]);
                            map.put(p,res);
                        }
                    }
                    if (!s.isEmpty() && arr[s.peek().get(0)] == arr[i]){
                        s.peek().add(i);
                    }else {
                        s.push(list);
                    }
                }
            }
        }
        while (!s.isEmpty()){
            LinkedList<Integer> pop = s.pop();
            for (int j = 0; j < pop.size(); j++) {
                int p = (int) pop.get(j);
                String[] res = new String[2];
                res[0] = s.isEmpty() ? null : String.valueOf(arr[s.peek().get(0)]);
                res[1] = null;
                map.put(p,res);
            }
        }
        return map;
    }

    //定义：数组中累积和与最小值的乘积，假设叫做指标A
    //给定一个正数数组，请返回子数组中，指标A最大的值
    //左边距离最近的小的，右边距离最近的小的，单调栈
    private static final long MOD = 1_000_000_007;

    public static int maxSumMinProduct(int[] arr) {
        int n = arr.length;
        long[] prefix = new long[n + 1];        // 前缀和（prefix[0] = 0）

        for (int i = 1; i <= n; i++) {
            prefix[i] = prefix[i - 1] + arr[i - 1];
        }

        // left[i]：arr[i] 左边第一个严格小于 arr[i] 的位置（-1 表示没有）
        // right[i]：arr[i] 右边第一个严格小于 arr[i] 的位置（n 表示没有）
        int[] left = new int[n];
        int[] right = new int[n];
        java.util.Arrays.fill(left, -1);
        java.util.Arrays.fill(right, n);

        // 单调递增栈（存索引）
        java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();

        // 1. 从左到右找 left
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }
            if (!stack.isEmpty()) {
                left[i] = stack.peek();
            }
            stack.push(i);
        }

        stack.clear();

        // 2. 从右到左找 right
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {  // 注意这里用 > ，保证严格小于
                stack.pop();
            }
            if (!stack.isEmpty()) {
                right[i] = stack.peek();
            }
            stack.push(i);
        }

        long maxProduct = 0;

        // 3. 对每个 i，计算以 arr[i] 为最小值的最大子数组和 * arr[i]
        for (int i = 0; i < n; i++) {
            // 子数组范围是 (left[i] + 1) 到 (right[i] - 1)
            long sum = prefix[right[i]] - prefix[left[i] + 1];
            long product = sum * arr[i];
            if (product > maxProduct) {
                maxProduct = product;
            }
        }

        return (int) (maxProduct % MOD);
    }


    public static void main(String[] args) {
        Stack<LinkedList<Integer>> s = new Stack<>();
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        s.push(list);
        s.peek().add(3);
        LinkedList<Integer> pop = s.pop();
        for (int i = 0; i < pop.size(); i++) {
            System.out.println(pop.get(i));
        }
    }
}








































