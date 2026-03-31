package practice;

import sorts.LinkedList.Node;
import sorts.Sorts;
import sorts.Tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Test_20260328 {

    /**
     * 舍弃可能性
     * 一个优良的平凡解 舍弃一部分可能性 而从只关注另一部分的可能性，达到优化流程的目的
     */
    //给定一个数组，求如果排序之后，相邻两数的最大差值。要求时间复杂度O（N），且要求不能用非基于比较的排序
    //O（N）
    public static int maxGap(int[] nums){
        if (nums == null || nums.length == 2){
            return 0;
        }
        int len = nums.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            min = Math.min(min,nums[i]);
            max = Math.max(max,nums[i]);
        }
        //数都一样
        if (min == max){
            return 0;
        }
        //准备len+1的桶
        /**
         * 为什么是len+1的桶？
         * len个数情况下len+1个桶会保证一定有一个空桶的存在，
         * 如此最大的差值一定不会出现在同一个桶内，去除掉这种可能性后，只剩下最大差值存在于两个不同的桶。
         * 两个不同的桶存在最大差值就是一个优良的平凡解，舍弃一部分可能性，
         * 从而只剩下的跨桶之间的可能性（最大差值存在于两个不同的桶）中求出最优解
         */
        boolean[] hasBucket = new boolean[len + 1]; //每个桶是否存在值
        int[] minBucket = new int[len + 1]; //每个桶的最小值
        int[] maxBucket = new int[len + 1]; //每个桶的最大值

        for (int i = 0; i < nums.length; i++) {
            //桶号
            int bucketNum = (int) (long)(((nums[i] - min) * len) / (max - min));
            if (nums[i] == max){ //因为浮点数的存在，特殊处理一下
                bucketNum = len;
            }
            minBucket[bucketNum] = hasBucket[bucketNum] ? Math.min(minBucket[bucketNum],nums[i]) : nums[i];
            maxBucket[bucketNum] = hasBucket[bucketNum] ? Math.max(maxBucket[bucketNum],nums[i]) : nums[i];
            hasBucket[bucketNum] = true;
        }
        int res = 0;
        int pre = maxBucket[0]; //上一个非空桶的最大值
        //从0号桶开始
        for (int i = 1; i < len + 1; i++) {
            if (hasBucket[i]){
                res = Math.max(res,minBucket[i] - pre);
                pre = maxBucket[i];
            }
        }
        return res;
    }


    //给出n个数字，问最多有多少不重叠的非空区间，使得每个区间内的数字的xor都等于0
    //子数组问题，先考虑结尾做法
    //结尾为i的子数组的最优划分怎么搞
    public static int xor(int[] arr){
        if (arr == null || arr.length == 0){
            return 0;
        }
        int n = arr.length;
        //key 异或和 value 最后出现的位置
        Map<Integer,Integer> map = new HashMap<>();
        int xor = 0; //此时的i位置的异或和
        map.put(xor,-1);
        //dp记录0-i，最好划分情况下的结果，dp[n-1]是最终的结果
        int[] dp = new int[n];
        //i位置的可能性
        //1、i位置的异或和没有出现过，i与之前位置都不能划分到一起，dp[i] == dp[i-1]
        //2、i位置的异或和出现过，表明i自己的划分部分异或和是0，0~k、k~i（异或和为0），结果为dp[k] + 1
        //3、去两种情况的最大值
        for (int i = 0; i < n; i++) {
            xor = xor ^ arr[i];
            if (map.containsKey(xor)){ //上一次异或和出现的位置
                Integer k = map.get(xor);
                //最优化划分：0~k，k+1~i（最后一块）
                dp[i] = k == -1 ? 1 : dp[k] + 1;
            }
            if (i > 0){
                dp[i] = Math.max(dp[i],dp[i-1]);
            }
            map.put(xor,i);
        }
        return dp[n-1];
    }


    //现有n1+n2中面值的硬币，其中前n1中为普通币，可以取任意枚，
    //后n2中为纪念币，每种最多只能取一枚，每种硬币有一个面值，问能用多少种方法拼出m的面值
    public static long coinWays(int[] coins, int n1, int m) {
        if (m < 0) return 0;
        if (m == 0) return 1;

        int n = coins.length;
        long[] dp = new long[m + 1];
        dp[0] = 1;  // 凑成 0 的方案数为 1

        // 第一步：处理普通币（无限背包，正序遍历）
        for (int i = 0; i < n1; i++) {
            int c = coins[i];
            if (c <= 0) continue; // 跳过无效面值
            for (int j = c; j <= m; j++) {
                dp[j] += dp[j - c];
            }
        }

        // 第二步：处理纪念币（0-1背包，逆序遍历）
        for (int i = n1; i < n; i++) {
            int c = coins[i];
            if (c <= 0 || c > m) continue;
            for (int j = m; j >= c; j--) {
                dp[j] += dp[j - c];
            }
        }

        return dp[m];
    }

    //int数组A和B，A是长度为m的有序递增数组，B是长度为n的有序递增数组,
    //希望从A和B数组中，找出最大的k个数字。要求：使用尽量少的比较次数。
    public static int getKMax(int[] A, int[] B, int k){
        if (A == null || A.length == 0 || B == null || B.length == 0){
            return -1;
        }

        if (k < 1 || k > A.length + B.length){
            return -1;
        }

        int[] longs = A.length >= B.length ? A : B;
        int[] shorts = A.length < B.length ? A : B;
        int s = shorts.length;
        int l = longs.length;

        if (k <= s){
            getUpMedian(shorts,0,k-1,shorts,0,k-1);
        }
        if (k > l){
            if (longs[k - s - 1] >= shorts[s - 1]){
                return longs[k - s - 1];
            }
            if (shorts[k - l -1] >= longs[l - 1]){
                return shorts[k - l - 1];
            }
            //上述两个if 排除了 longs[k - s - 1] shorts[k - l - 1] 是结果的可能性
            return getUpMedian(shorts,k - l, s - 1, longs, k - s, l - 1);
        }
        if (longs[k - s - 1] >= shorts[s - 1]){
            return longs[k - s - 1];
        }

        return getUpMedian(shorts, 0, s - 1,longs, k - s, k - 1);
    }
    // a1[s1..e1]  a2[s2..e2]
    // 要求两个区间长度必须相等，找出两个区间合并后的上中位数（偏小的中位数）
    public static int getUpMedian(int[] a1, int s1, int e1, int[] a2, int s2, int e2) {
        // 防御性检查：确保两个区间长度相同
        if (e1 - s1 != e2 - s2) {
            throw new IllegalArgumentException("两个区间的长度必须相等");
        }

        int mid1 = 0;
        int mid2 = 0;
        int offset = 0;

        while (s1 < e1) {
            mid1 = (s1 + e1) / 2;
            mid2 = (s2 + e2) / 2;

            // offset：当前区间长度为奇数时 offset=0，偶数时 offset=1
            offset = ((e1 - s1 + 1) & 1) ^ 1;

            if (a1[mid1] > a2[mid2]) {
                e1 = mid1;
                s2 = mid2 + offset;
            } else if (a1[mid1] < a2[mid2]) {
                s1 = mid1 + offset;
                e2 = mid2;
            } else {
                // a1[mid1] == a2[mid2] 时，直接返回该值（即为上中位数）
                return a1[mid1];
            }
        }

        // 循环结束时 s1 == e1，s2 == e2，此时两个位置的较小值就是上中位数
        return Math.min(a1[s1], a2[s2]);
    }

    //约瑟夫环
    public static Node josephusKill2(Node head, int m){
        if (head == null || head.next == head || m < 1){
            return head;
        }
        Node cur = head.next;
        int tmp = 1; // tem --> list size
        while (cur != head){
            tmp++;
            cur = cur.next;
        }
        tmp = getLive(tmp, m); // tmp 活下来的编号
        while (--tmp != 0){
            head = head.next;
        }
        head.next = head; //成环
        return head;
    }
    //现在一共有i个节点，数到m就杀死节点，最终会活下来的系欸但，请返回它在有i个节点时候的编号
    public static int getLive(int i, int m){
        if (i == 1){
            return 1;
        }
        return (getLive(i - 1, m) + m - 1) % i + 1;
    }













    public static void main(String[] args) {
        int[] nums = {2,5,56,34,57,85,99,33};
//        System.out.println(maxGap(nums));
//        Sorts.quickSort(nums,0,nums.length-1);
//        Sorts.print(nums);

//        System.out.println(xor(new int[]{3,2,1,4}));
//        System.out.println((0-1)/2);

        int[] nums1 = {11,33,66,77,99};
        int[] nums2 = {22,44,55,88,110};
        int upMedian = getUpMedian(nums1, 0, 4, nums2, 0, 4);
        System.out.println(upMedian);

    }

}
