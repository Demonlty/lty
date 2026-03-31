package practice;

import sorts.Sorts;

import java.util.*;

public class Test_20260329 {

    //大楼 [i,j,h]
    //[i,add,h] [j,del,h]
    //有序表
    //h变化的轮廓
    public static List<int[]> build(List<int[]> list){
        if (list == null || list.isEmpty()){
            return null;
        }
        List<Building> bList = new ArrayList<>();
        TreeMap<Integer,Integer> highTimesMap = new TreeMap();
        TreeMap<Integer,Integer> xHighMap = new TreeMap();
        for (int[] arr : list){
            bList.add(new Building(arr[0],true,arr[2]));
            bList.add(new Building(arr[1],false,arr[2]));
        }
        Collections.sort(bList,(a,b) -> a.x == b.x ? Boolean.compare(b.add , a.add) : a.x - b.x);
        for (Building b : bList){
            if (highTimesMap.containsKey(b.h)){
                if (b.add){
                    highTimesMap.put(b.h, highTimesMap.get(b.h)+1);
                }else {
                    if (highTimesMap.get(b.h) == 1){
                        highTimesMap.remove(b.h);
                    }else {
                        highTimesMap.put(b.h, highTimesMap.get(b.h)-1);
                    }
                }
            }else {
                highTimesMap.put(b.h, 1);
            }
            xHighMap.put(b.x,highTimesMap.isEmpty() ? 0 : highTimesMap.lastKey());
        }
        List<int[]> resList = new ArrayList<>();
        int pre = xHighMap.firstKey();
        int preHighest = xHighMap.get(pre);
        for (Integer x : xHighMap.keySet()){
            Integer h = xHighMap.get(x);
            if (h != preHighest){
                resList.add(new int[]{pre,x,preHighest});
                pre = x;
                preHighest = h;
            }
        }
        return resList;
    }
    public static class Building{
        int x;
        boolean add;
        int h;

        public Building(int x,boolean add, int h) {
            this.x = x;
            this.add = add;
            this.h = h;
        }

        @Override
        public String toString() {
            return "Building{" +
                    "x=" + x +
                    ", add=" + add +
                    ", h=" + h +
                    '}';
        }
    }


    //正数数组中 累加和为k的最长的子数组，利用正数数组累加和的单调性
    //[L,R] L == R , 窗口不再有数
    //sum < k R++; sum > k L++; sum == k res = Max(res,R-L+1),R++;
    public static int maxLength1(int[] arr, int k){
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int L = 0;
        int R = 0;
        int sum = 0;
        int res = 0;
        while (R < arr.length){
            if (sum < k){
                sum += arr[R];
                R++;
            }else if (sum > k){
                sum -= arr[L++];
            }else {
                res = Math.max(res,R-L);
                R++;
            }
            if (L > R){
                R = L;
                sum = 0;
            }
        }
        return res;
    }
    public static int maxLength2(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k <= 0) return 0;

        int left = 0, sum = 0, maxLen = 0;

        for (int right = 0; right < arr.length; right++) {
            sum += arr[right];

            while (sum > k && left <= right) {
                sum -= arr[left++];
            }

            if (sum == k) {
                maxLen = Math.max(maxLen, right - left + 1);
            }
        }
        return maxLen;
    }

    //无序数组（可正、可负，可0）中 累加和为k的最长的子数组
    //map 记录 {前缀和 ： 最早出现的位置} ，结果 ：i-最早出现的位置
    public static int maxLength(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }

        Map<Integer, Integer> map = new HashMap<>();  // {前缀和 : 最早出现的索引}
        map.put(0, -1);   // 重要初始化！前缀和为0时，子数组从0开始

        int maxLen = 0;
        int sum = 0;      // 当前前缀和

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];   // 更新前缀和

            // 检查是否存在 sum - k 的前缀和（即找到了和为k的子数组）
            if (map.containsKey(sum - k)) {
                int left = map.get(sum - k);
                maxLen = Math.max(maxLen, i - left);
            }

            // 只记录最早出现的位置（如果该前缀和之前没出现过）
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }

        return maxLen;
    }
    //无序数组（可正、可负，可0）中 累加和小于等于k的最长的子数组
    //arr[]
    //minSum[]   minSum[i] 从i出发的子数组，最小sum
    //minSumEnd[]  minSumEnd[i] 从i出发的子数组，取到最小sum时的右边界位置
    public static int maxLengthSumLeK(int[] arr,int k){
        if (arr == null || arr.length == 0){
            return -1;
        }
        int len = arr.length;
        int[] minSum = new int[len];
        int[] minSumEnd = new int[len];
        minSum[len - 1] = arr[len - 1]; // minSum[i] = 以 i 开头的所有子数组中，最小的累加和
        minSumEnd[len - 1] = len - 1; // minSumEnd[i] = 取得上面最小累加和时，子数组的结束位置（包含）
        for (int i = len - 2; i >= 0; i--) {
            int sum = arr[i] + minSum[i + 1];
            if (sum < arr[i]){
                minSum[i] = sum;
                minSumEnd[i] = minSumEnd[i + 1];
            }else {
                minSum[i] = arr[i];
                minSumEnd[i] = i;
            }
        }
        int end = 0;
        int sum = 0;
        int res = 0;
        //i是窗口最左的位置，end是窗口最右位置的下一个（终止位置），i == end 表示窗口没有数，重置end = i + 1
        for (int i = 0; i < len; i++) {
            //while循环结束后
            //1、如果以i开头的情况下，累加和小于等于k的最长子数组是arr[i...end-1]，看看这个长度能不能更新res
            //2、如果以i开头的情况下，累加和小于等于k的最长子数组比arr[i...end-1]短，更不更新res没有影响，相当于舍弃比原来结果更短的结果
            while (end < len && sum + minSum[end] <= k){
                sum += minSum[end];
                end = minSumEnd[end] + 1;
            }
            res = Math.max(res, end - i);
            if (end > i){ //窗口还有数
                sum -= arr[i];
            }else { //窗口没有数
                end = i + 1;
            }
        }
        return res;
    }


    //Nim 博弈
    //全部异或，异或和不是零，先手赢；异或和是零，后手赢
    //piles 每堆石子的数量数组
    //true 表示先手必胜，false 表示先手必败（后手必胜）
    public static boolean canFirstPlayerWin(int[] piles) {
        if (piles == null || piles.length == 0) {
            return false;   // 没有石子，先手输
        }

        int xor = 0;
        for (int num : piles) {
            xor ^= num;     // 累积异或
        }

        return xor != 0;
    }
    // 返回先手获胜时的最优第一步：[pileIndex, stonesToRemove]
    public static int[] getOptimalFirstMove(int[] piles) {
        int xor = 0;
        for (int num : piles) xor ^= num;

        if (xor == 0) return null;  // 先手必败，无最优走法

        for (int i = 0; i < piles.length; i++) {
            //假设 piles[i]变成 target，使新的异或和为0，y是除i位置外的异或和
            // y ^ target = 0;
            // y ^ piles[i] = xor
            // y ^ target ^ piles[i] = piles[i] = xor ^ target
            // target = piles[i] ^ xor
            int target = piles[i] ^ xor;   // 想让这一堆变成 target
            if (target < piles[i]) {       // 合法（拿走正数）
                return new int[]{i, piles[i] - target};
            }
        }
        return null;
    }


    public static void main(String[] args) {
        List<int[]> build = build(Arrays.asList(new int[]{4, 7, 5}, new int[]{1, 5, 6}, new int[]{3, 8, 1}));
        for (int[] arr : build){
            Sorts.print(arr);
        }
    }

}
