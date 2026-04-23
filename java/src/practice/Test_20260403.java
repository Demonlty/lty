package practice;

import sorts.Sorts;

import java.util.*;

import static sorts.Sorts.swap;

public class Test_20260403 {

    //判定一个由[a-z]字符构成的字符串和一个包含'.‘和'*‘通配符的字符串是否匹配。
    //通配符'.'匹配任意单一字符，*'匹配任意多个字符包括0个字符。
    //字符串长度不会超过100，字符串不为空。
    //输入描述:
    //字符串str 和包含通配符的字符串pattern。1<=字符串长度〈=100输出描述:true 表示匹配，false表示不匹配
    //str 初始字符串， exp 匹配字符串
    public static boolean isMatch(String str, String exp){
        if ( str == null || exp == null){
            return false;
        }
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        return isVaild(s,e) && process(s,e,0,0);
    }
    //s[si...]能否被e[ei...]配出来
    //必须保证ei压中的不是*
    private static boolean process(char[] s, char[] e, int si, int ei) {
        if (ei == e.length){ //base case exp的字符已耗尽
            return si == s.length;
        }
        //可能性1，ei+1 不是 *
        //str[si]必须与exp[ei]配出来，并且后续能走通
        if (ei + 1 == e.length || e[ei + 1] != '*'){
            return si != s.length && (e[ei] == s[si] || e[ei] == '.')
                    && process(s, e, si + 1, ei + 1);
        }
        //可能性2，ei+1 是 *
        while(si != s.length && (e[ei] == s[si] || e[ei] == '.')){
            if (process(s, e, si, ei + 2)){
                return true;
            }
            si++;
        }
        return process(s, e, si, ei + 2);
    }
    //
    private static boolean isVaild(char[] s, char[] e) {
        for (int i = 0; i < s.length; i++) {
            if (s[i] < 'a' || s[i] > 'z'){
                return false;
            }
        }
        for (int i = 0; i < e.length; i++) {
            if (e[i] == '*' && (i == 0 || e[i - 1] == '*')){
                return false;
            }
        }
        return true;
    }

    public static boolean isMatchDP(String str, String exp){
        if ( str == null || exp == null){
            return false;
        }
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        boolean[][] dp = initDPMap(s,e);
        for (int i = s.length - 1; i >= 0; i--) {
            for (int j = e.length - 2; j >= 0; j--) {
                if (e[j + 1] != '*'){
                    dp[i][j] = (s[i] == e[j] || e[j] == '.') && dp[i + 1][j + 1];
                }else {
                    int si = i;
                    while(si != s.length && (e[j] == s[si] || e[j] == '.')){
                        if (dp[si][j + 2]){
                            dp[i][j] = true;
                            break;
                        }
                        si++;
                    }
                    if (dp[i][j] != true){
                        dp[i][j] = dp[si][j + 2];
                    }
                }
            }
        }
        return dp[0][0];
    }
    //初始化DP，补全初始位置
    private static boolean[][] initDPMap(char[] s, char[] e) {
        int slen = s.length;
        int elen = e.length;
        boolean[][] dp = new boolean[slen + 1][elen + 1];
        dp[slen][elen] = true;
        for (int j = elen - 2; j > -1; j -= 2){
            if (e[j] != '*' && e[j+1] == '*'){
                dp[slen][j] = true;
            }else {
                break;
            }
        }
        if (slen > 0 && elen > 0){
            if ((e[elen - 1] == '.' || s[slen - 1] == e[elen -1])){
                dp[slen - 1][elen - 1] = true;
            }
        }
        return dp;
    }


    //数组异或和的定义：把数组中所有的数异或起来得到的值
    //给定一个整型数组arr，其中可能有正、有负、有零，求其中子数组的最大异或和
    //【举例】
    //arr= [3}
    //数组只有1个数，所以只有一个子数组，就是这个数组本身，最大异或和为3
    //arr = [3,-28,-29,2)
    //子数组有很多，但是{-28，-29}这个子数组的异或和为7，是所有子数组中最大的
    public static void maxSubarrayXOR(){
        Test_20260327.maxSubarrayXOR(new int[]{});
    }

    //范围上尝试
    //打气球
    public static int maxValue(int[] arr){
        if (arr == null || arr.length == 0){
            return 0;
        }
        int n = arr.length;
        //根据规则处理成新的数组，方便处理边界条件
        int[] help = new int[n+2];
        help[0] = 1;
        help[n+1] = 1;
        for (int i = 1; i <= n; i++) {
            help[i] = arr[i-1];
        }
        return process(help,1,n);
    }
    //打爆arr[L...R]范围上的所有气球，返回最大的分数
    //假设arr[L-1]和arr[R+1]上的气球没有被打爆
    public static int process(int[] arr, int L, int R){
        if (L == R){ //只有一个气球，直接打爆
            return arr[L-1] * arr[L] * arr[R+1];
        }
        //先处理最后打爆arr[L]和最后打爆arr[R]，比较分数
        int max = Math.max(
                arr[L-1] * arr[L] * arr[R + 1] + process(arr, L+1, R),
                arr[L-1] * arr[R] * arr[R + 1] + process(arr, L, R-1));
        for (int i = L+1; i <= R-1; i++) {
            max = Math.max(
                    max,
                    arr[L-1] * arr[i] * arr[R + 1] + process(arr, L, i-1) + process(arr, i+1,R));
        }
        return max;
    }


    //汉诺塔 N层 最优解步数：2^N - 1
    //1、1~N-1 左->中；
    //2、N     左->右；
    //3、1~N-1 中->右；
    //数组下标表示几号盘，号越大盘越大。数组值表示 1：左柱，2：中间柱，3：右柱
    //返回值：这个数组形成的状态是不是从左到右最优解的中间某一步的状态，如果是，返回第几步？
    //例如：[3, 3, 2, 1] 代表 0号盘在右柱，1号盘在右柱，2号盘在中间柱，3号盘在左柱
    public static int step(int[] arr){
        if (arr == null || arr.length == 0){
            return -1;
        }
        return process(arr, arr.length - 1, 1, 2, 3);
    }
    //目标0~i号圆盘从from 到 to
    //1、1~i-1 from->other；
    //2、i     from->to；
    //3、1~i-1 other->to；
    //返回值 根据arr中的状态arr[0...i]，它是最优解的第几步
    public static int process(int[] arr, int i, int from, int other, int to){
        if (i == -1){
            return 0; //没有圆盘了，最优解的第0步
        }
        //最优解中不存在i圆盘在other上
        if (arr[i] != from || arr[i] != to){
            return -1;
        }
        if (arr[i] == from){ //i圆盘在from上，表示在目标的第二大步之前，结果需要看第一大步的走的情况
            return process(arr, i-1, from, to, other);
        }else { //arr[i] == to，i圆盘在to上，表示在目标的第二大步之后，第三大步之中，结果需要看第三大步的走的情况
            int rest = process(arr, i-1, other, from, to);
            if (rest == -1){
                return -1;
            }
            //结果 == 第一大步的步数：2^i - 1 + 第一大步的步数：1 + 第三大步的步数：rest
            return (1 << i) + rest;
        }
    }

    //旋变字符串
    public static boolean isSpinChange(String s1, String s2){
        if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()){
            return true;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        //str1和str2一定是等长，字符种类也相同
        if (!sameTypeSameNumber(str1,str2)){
            return false;
        }
        int N = str1.length;
        return process(str1,str2,0,0,N);
    }
    //str1[L1...] str2[L2...] 长度都是size
    //返回值，str1和str2是否互为旋变字符串
    public static boolean process(char[] str1, char[] str2, int L1, int L2, int size){
        if (size == 1){
            return str1[L1] == str2[L2];
        }
        //枚举每一种情况，有一个计算出是互为旋变就返回true，否则false
        for (int leftPart = 1; leftPart < size; leftPart++) { //leftPart 左边部分的长度 1~size-1
            if (
                    //str1 ： 左1 右1
                    //str2 ： 左2 右2
                    //（左1和左2）&& （右1和右2）
                    (process(str1,str2,L1,L2,leftPart)
                    && process(str1,str2,L1+leftPart,L2+leftPart,size-leftPart))
                    ||
                    //（左1和右2）&& （右1和左2）
                    (process(str1,str2,L1,L2 + size - leftPart,leftPart)
                            && process(str1,str2,L1+leftPart,L2,size-leftPart)))
                return true;
        }
        return false;
    }
    private static boolean sameTypeSameNumber(char[] str1, char[] str2) {
        if (str1.length != str2.length) {
            return false;
        }

        int[] count = new int[256];  // 因为是char，使用256足够（ASCII扩展）
        // 统计 str1 中每个字符出现的次数
        for (char c : str1) {
            count[c]++;
        }
        // 减去 str2 中对应字符的次数
        for (char c : str2) {
            count[c]--;
        }
        // 如果所有字符计数都为0，说明字符种类和数量完全相同
        for (int i = 0; i < 256; i++) {
            if (count[i] != 0) {
                return false;
            }
        }
        return true;
    }
    public static boolean isSpinChangeDP(String s1, String s2){
        if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()){
            return true;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        //str1和str2一定是等长，字符种类也相同
        if (!sameTypeSameNumber(str1,str2)){
            return false;
        }
        int N = str1.length;
        //L1 L2 size
        boolean[][][] dp = new boolean[N][N][N+1];
        //先计算size等于1的情况，初始条件
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dp[i][j][1] = str1[i] == str2[j];
            }
        }
        //size变化范围 1~N
        for (int size = 1; size <= N; size++) {
            //i和j的取值范围是 0~ N-size，否则是越界，分成两部分，要保证后一部分能取到size个值
            for (int i = 0; i < N - size; i++) {
                for (int j = 0; j < N - size; j++) {
                    int L1 = i;
                    int L2 = j;
                    //枚举每一种情况，有一个计算出是互为旋变就返回true，否则false
                    for (int leftPart = 1; leftPart < size; leftPart++) { //leftPart 左边部分的长度 1~size-1
                        if (
                            //str1 ： 左1 右1
                            //str2 ： 左2 右2
                            //（左1和左2）&& （右1和右2）
                                (dp[L1][L2][leftPart]
                                        && dp[L1+leftPart][L2+leftPart][size-leftPart])
                                        ||
                                        //（左1和右2）&& （右1和左2）
                                        (dp[L1][L2 + size - leftPart][leftPart]
                                                && dp[L1+leftPart][L2][size-leftPart]))
                            dp[L1][L2][size] = true;
                    }
                }
            }
        }
        return dp[0][0][N];
    }

    //给定字符串str1和str2，求str1的子串中含有str2所有字符的最小子串长度
    //[举例】
    //str1="abcde",str2="ac"
    //因为"abc"包含 str2所有的字符，并且在满足这一条件的str1的所有子串中，"abc"是最短的，返回3。
    //str1="12345",str2="344" 最小包含子串不存在，返回0
    //str1玩窗口  str2 先记录出每个字符的个数，以及总个数
    //总个数!=0时，R++；
    //总个数==0时，此时窗口的答案
    //L++，总个数!=0时，R++ ，总个数==0时，此时窗口的答案；
    //窗口的答案取最小
    public static int minLen(String s1, String s2){
        if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()){
            return 0;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();

        int[] need = new int[256];
        for (int i = 0; i < str2.length; i++) {
            need[str2[i]]++;
        }
        int left = 0;
        int validCount = 0;        // 当前窗口中已经满足需要的字符种类数
        int required = str2.length;   // 需要匹配的总字符个数
        int minLength = Integer.MAX_VALUE;

        // 统计当前窗口中各字符的数量
        int[] window = new int[256];

        for (int right = 0; right < str1.length; right++) {
            char c = str1[right];
            window[c]++;

            if (need[c] > 0 && window[c] <= need[c]){
                validCount++;
            }
            // 当窗口已经包含了 s2 的所有字符
            while (validCount == required && left <= right) {
                minLength = Math.min(minLength, right - left + 1);

                // 收缩左边界
                char leftChar = str1[left];
                window[leftChar]--;

                if (need[leftChar] > 0 && window[leftChar] < need[leftChar]) {
                    validCount--;
                }
                left++;
            }

        }
        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }

    public static int getNewTotal(char[] str, char c, Map<String,Integer> map, boolean add, int total){
        String cur = String.valueOf(c);
        if (map.containsKey(cur)){
            map.put(cur, add? map.get(cur)-1 : map.get(cur)+1);
            total = map.get(cur) <= 0 ? total : add? total - 1 : total + 1;
        }
        return total;
    }

    //LFU cache LFU缓存替换算法
    //二维双向链表

    //加油站问题


    public static void main(String[] args) {
//        groupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"});
        System.out.println(minLen("ADOBECODEBANC", "BANC"));
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0){
            return null;
        }
        List<List<String>> res = new ArrayList<>();
        Map<String,List<String>> map = new HashMap<>();
        for(String s : strs){
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String s1 = String.valueOf(chars);
            if(map.containsKey(s1)){
                List<String> strings = map.get(s1);
                strings.add(s);
            }else{
                List<String> list = new ArrayList<>();
                list.add(s);
                map.put(s1,list);
            }
        }
        for(String s : map.keySet()){
            res.add(map.get(s));
        }
        return res;
    }
















































































}
