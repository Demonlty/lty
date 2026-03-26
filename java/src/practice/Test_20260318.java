package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Test_20260318 {

    /**
     * 窗口不回退模型
     * */
    //给定一个有序数组arr，代表数轴上从左到右有n个点arr【0】、arr【1】....arr【n-1】，
    //给定一个正数L，代表一根长度为L的绳子，求绳子最多能覆盖其中的几个点
    //窗口问题
    public static int getMax(int[] arr,int L){
        if (arr == null || arr.length == 0 || L == 0){
            return 0;
        }
        int right = 0;
        int max = Integer.MIN_VALUE;
        for (int left = 0; left < arr.length; left++) {
            while (right < arr.length && arr[right] - arr[left] <= L){
                right++;
            }
            max = Math.max(max,right-left);
        }
        return max;
    }

    /**
     * 打表 找出数学规律，直接求解，不问意义
     * */
    //苹果 6个一袋或者8个一袋，求apple个苹果，最少装多少袋？如果不是正好装下，则不要苹果，返回-1。
    public static int minBags(int apple){
        if (apple < 0){
            return -1;
        }
        int bag6 = -1;
        int bag8 = apple / 8;
        int res = apple - bag8 * 8;
        while (bag8 >= 0 && res < 24){
            bag6 = res % 6 == 0? res / 6 : -1;
            if (bag6 != -1){
                break;
            }
            res = apple - 8 * (--bag8);
        }
        return bag6 == -1 ? bag6 : bag6 + bag8;
    }
    //打表法
    public static int minBagAwesome(int apple){
        if ((apple & 1) != 0){
            return -1;
        }
        if (apple < 18){
            return apple == 0 ? 0 : (apple == 6 || apple == 8) ? 1
                    : (apple == 12 || apple == 14 || apple == 16) ? 2 : -1;
        }
        return (apple - 18) / 8 + 3;
    }
    //牛羊吃草 每次吃 4^n 看先手还是后手赢
    public static String winner1(int n){
        if (n < 5){
            return (n == 0 || n == 2) ? "后手" : "先手";
        }
        // n >= 5
        int base = 1;
        while (base <= n){
            //当前 n份草，先手吃掉 base份，留给后手的是 n-base份
            //winner1的返回值代表是谁赢，所以winner1(n-base)是子过程，因此子过程的后手赢代表母过程的先手赢
            if (winner1(n-base).equals("后手")){
                return "先手";
            }
            if (base > n / 4){ //防止溢出
                break;
            }
            base = base * 4;
        }
        return "后手";
    }
    //打表法
    public static String winner2(int n){
        if (n % 5 == 0 || n % 5 == 2){
            return "后手";
        }else {
            return "先手";
        }
    }

    /**
     * 预处理结构 用辅助空间（比如 数组）先计算，解决遍历的问题，空间换时间达到降维的目的
     * */
    //染色问题
    // RGRGR -> RRRGG
    public static int minPaint(String s){
        if (s == null || s.length() == 0){
            return 0;
        }
        int len = s.length();
        char[] chs = s.toCharArray();
        int[] right = new int[len];
        right[len -1] = chs[len - 1] == 'R' ? 1 : 0;
        for (int i = len - 2; i >= 0; i--) {
            right[i] = right[i+1] + chs[i] == 'R' ? 1 : 0;
        }
        int res = right[0]; //最左为0个R的结果
        int left = 0;
        for (int i = 0; i < len - 1; i++) {
            left += chs[i] == 'G' ? 1 : 0;
            res = Math.min(res, left + right[i+1]);
        }
        return Math.min(res,left + (chs[len - 1] == 'G' ? 1 : 0));
    }
    //N*N的矩阵中多少个正方形？n^3量级
    //N*N的矩阵中多少个长方形？n^4量级
    //N*M的矩阵中多少个边界为1的正方形
    //计算一个01矩阵中，四条边全部为1的正方形个数（内部可以是0或1） O(n × m × min(n,m))，o（n^3）
    public static long countBorderSquares(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int n = grid.length;           // 行数
        int m = grid[0].length;        // 列数
        // 预处理：right[i][j] 表示从 (i,j) 向左连续1的个数（包含自己）
        int[][] right = new int[n][m];
        // 预处理：up[i][j] 表示从 (i,j) 向上连续1的个数（包含自己）
        int[][] up = new int[n][m];
        // 1. 计算 right 数组（横向）
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    right[i][j] = (j == 0 ? 0 : right[i][j - 1]) + 1;
                } else {
                    right[i][j] = 0;
                }
            }
        }
        // 2. 计算 up 数组（纵向）
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                if (grid[i][j] == 1) {
                    up[i][j] = (i == 0 ? 0 : up[i - 1][j]) + 1;
                } else {
                    up[i][j] = 0;
                }
            }
        }
        long ans = 0;
        // 3. 枚举右下角 (i,j)，尝试所有可能的边长
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 0) continue;
                int len = 1;
                while (true) {
                    int r = i - len + 1;   // 顶行
                    int c = j - len + 1;   // 左列
                    if (r < 0 || c < 0) break;
                    // 检查四条边是否都至少有 len 个连续的 1
                    if (right[i][j] >= len &&     // 底边
                            right[r][j] >= len &&     // 顶边
                            up[i][j] >= len &&        // 右边
                            up[i][c] >= len) {        // 左边
                        ans++;
                        len++;
                    } else {
                        break;
                    }
                }
            }
        }

        return ans;
    }

    /**
     * 二进制拼凑
     */
    //1、给定一个函数f，可以1~5的整数数字等概率返回一个。请加工出1~7的整数数字等概率返回一个的函数g
    //先 1 2 3 4 5，分成两份，组装成0 1发生器，小于3为0，大于等于3为1，等于5重新计算
    public static int f(){
        return (int) (Math.random() * 4) + 1;
    }
    public static int r01(){ //0 1发生器
        int res = 0;
        do {
            res = f();
        }while (res == 5); //1~5为奇数个，不要一个，分两份
        return res < 3 ? 0 : 1;
    }
    public static int g(){
        int res = 0;
        do {
            //1~7 等同于 （0~6）+ 1
            //0~6 使用二进制 三位即可解决
            res = r01() << 2 + r01() << 1 + r01();
        }while (res == 7); //表示0~6时7不要
        return res + 1; //（0~6）+ 1 == 1~7
    }
    //2、给定一个函数f，可以a~b的整数数字等概率返回一个。请加工出c~d的整数数字等概率返回一个的函数g
    //问题2 同理 问题1
    //给定一个函数f1，以p概率返回0，以1-p概率返回1。请加工出等概率返回0和1的函数g1
    public static int f1(){
        double p = 0.83;
        return Math.random() < p ? 0 : 1;
    }
    public static int g1(){
        int res = 0;
        do {
            //两位二进制来表示，
            //01 --> p *（1-p）
            //10 --> p *（1-p）
            //01和10概率相同
            res = f1() << 1 + f1();
        }while (res == 0 || res == 3);
        return res == 1 ? 0 : 1;
    }

    //给定一个非负整数n，代表二叉树的节点个数。返回能形成多少种不同的二叉树结构
    public static int getDiffNum(int n){
        return treeProcess(n);
    }
    private static int treeProcess(int n) {
        if (n < 0) return 0;
        if (n == 0) return 1;
        if (n == 1) return 1;
        if (n == 2) return 2;
        int res = 0;
        for (int i = 0; i <= n - 1; i++) { // i 左树的个数
            int leftWays = treeProcess(i); //左树的方法数
            int rightWays = treeProcess(n-1-i); //右树的方法数
            res += leftWays + rightWays;
        }
        return res;
    }
    //改为DP
    public static int numTrees(int n){
        if (n < 2){
            return 1;
        }
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i < n + 1; i++) { //节点个数为i的时候
            for (int j = 0; j <= i - 1; j++) { //左侧节点个数为j，右侧节点个数为i-j-1
                dp[i] += dp[j] * dp[i - j -1];
            }
        }
        return dp[n];
    }

    //括号  使用 count记录，左括号 count++，右括号 count--
    //count的最大值是深度
    public static int needParentheses(String str){
        if (str == null || str.length() == 0){
            return 0;
        }
        int count = 0;
        int need = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '('){
                count++;
            }else {
                if (count == 0){ //右括号多一个
                    need++;
                }else {
                    count--;
                }
            }
        }
        return need + count;
    }
    //括号，最长有效长度
    public static int MaxParentheses(String str){
        if (str == null || str.length() == 0){
            return 0;
        }
        int len = str.length();
        char[] charArray = str.toCharArray();
        int[] dp = new int[len];
        int j = 0;
        int res = 0;
        for (int i = 0; i < len; i++) {
            if (charArray[i] == '('){
                dp[i] = 0;
            }else {
                j = i-1-dp[i-1];
                if (j >= 0 && charArray[j] == '('){ //判断j位置是（，则加上j-1位置的长度，否则有效长度为0
                    dp[i] = 2 + dp[i-1] + (j-1 >= 0 ? dp[j-1] : 0);
                }
            }
            res = Math.max(res,dp[i]);
        }
        return res;
    }

    //给定一个数组arr，求差值为k的去重数字对
    public static List<List<Integer>> allPair(int[] arr,int k){
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            set.add(arr[i]);
        }
        List<List<Integer>> res = new ArrayList<>();
        for (Integer i : set) {
            if (set.contains(i+k)){
                res.add(Arrays.asList(i,i+k));
            }
        }
        return res;
    }





    public static void main(String[] args) {
        int[] arr = new int[]{2,4,8,9,15,16};
        for (int i = 0; i < 100; i++) {
            System.out.println(i + "-->" + winner1(i));
        }

    }
}
