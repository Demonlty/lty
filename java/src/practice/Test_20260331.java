package practice;


import sorts.Sorts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

public class Test_20260331 {

    /**
     * 字符串str，str表示一个公式，公式中可能有整数、加减乘除符号和左右括号，返回公式的计算结果
     */
    //字符串str，str表示一个公式，公式中可能有整数、加减乘除符号和左右括号，返回公式的计算结果
    //例如：
    //str="48*((70-65)-43)+8*1",返回-1816
    //说明：
    //1、不需要对str做公式有效检查
    //2、负数需要用括号括起来，比如："4*(-3)"；但负数作为公式的开头开始没有括号，比如："-3*4"和"(-3*4)"都是合法的
    //3、不用考虑溢出

    //简单的情况，假如str中只有数字和加减乘除，没有括号，没有负数
    //不考虑合法问题
    public static int strToSum(String str){
        if (str == null || str.isEmpty()){
            return 0;
        }
        char[] chars = str.toCharArray();
        Stack<String> stack = new Stack<>();
        int num = 0;
        for (int i = 0; i < chars.length; i++) {
            //遇到符号位
            if (chars[i] == '+' || chars[i] == '-' || chars[i] == '*' || chars[i] == '/'){
                if (!stack.isEmpty()){
                    String peek = stack.peek();
                    //只有栈顶乘除的时候，开始计算乘除的结果，最后结果和当前的符号压栈
                    if (peek.equals("*") || peek.equals("/")){
                        num = stack.pop().equals("*") ? Integer.valueOf(stack.pop()) * num :
                                Integer.valueOf(stack.pop()) / num;
                    }
                }
                stack.push(String.valueOf(num));
                stack.push(String.valueOf(chars[i]));
                num = 0;
            } else {
                //当前是数字
                num = num * 10 + Integer.valueOf(String.valueOf(chars[i]));
                if (i == chars.length - 1){
                    stack.push(String.valueOf(num));
                }
            }
        }
        int sum = 0;
        Integer pop = 0;
        //栈不为空，栈顶是数字，下一个是它的符号，最后一个没有符号一定是正数
        while (!stack.isEmpty()){
            pop = Integer.valueOf(stack.pop());
            if (!stack.isEmpty() && stack.pop().equals("-")){
                pop = (-pop);
            }
            sum = sum + pop;
        }
        return sum;
    }

    //有括号的完整情况
    public static int strToSum1(String str){
       return value(str.toCharArray(),0)[0];
    }
    //value（str，i） 返回 a，b
    //a：从i计算到j，结束（最后结尾或者遇到右括号）时的结果
    //b：从i计算到j，结束（最后结尾或者遇到右括号）时的位置
    public static int[] value(char[] chars, int i){
        LinkedList<String> que = new LinkedList<>();
        int num = 0;
        int[] res = null;
        while (i < chars.length && chars[i] != ')'){
            if (chars[i] >= '0' && chars[i] <= '9'){
                num = num * 10 + (chars[i++] - '0');
            } else if (chars[i] != '('){ //运算符号
                addSum(que,num);
                que.offerLast(String.valueOf(chars[i++]));
                num = 0;
            }else { //遇到左括号，开始递归求解括号里的内容
                res = value(chars, i + 1);
                num = res[0];
                i = res[1] + 1; //i跳到括号结束的下一位置
            }
        }
        //越界时，把结束位置的结果继续压到que中
        addSum(que,num);
        return new int[] {getNum(que), i};
    }
    //计算que中的值与num结合出一个结果，再把结果压到que中
    private static void addSum(LinkedList<String> que, int num) {
        if (!que.isEmpty()){
            String top = que.pollLast();
            if (top.equals("+") || top.equals("-")){
                que.offerLast(top);
            }else { //合法，所以没有判断下一个栈的元素还有没有
                int cur = Integer.parseInt(que.pollLast());
                num = top.equals("*") ? (num * cur) : (cur / num);
            }
        }
        que.offerLast(String.valueOf(num));
    }
    //计算que中剩下的结果，里面只有加号、减号和数字
    private static int getNum(LinkedList<String> que) {
        int sum = 0;
        int num = 0;
        String cur = null;
        boolean add = true;
        while (!que.isEmpty()){
            cur = que.pollFirst();
            if (cur.equals("+")){
                add = true;
            } else if (cur.equals("-")) {
                add = false;
            } else {
                num = Integer.parseInt(cur);
                sum += add ? num : (-num);
            }
        }
        return sum;
    }

    /**
     * 动态规划空间压缩技巧
     */
    //子串：子串连续的一段字符序列
    //子序列：可以不连续，但保持原有相对顺序的字符序列

    //给定两个str1和str2，求两个字符串的最长公共子串
    //dp[i][j] 公共子串必须是以i结尾和为j结尾，最大长度
    //dp[i][j] = str1[i] == str2[j] ? dp[i-1][j-1] + 1 : 0;
    public static String maxLen(String str1, String str2){
        if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty()){
            return null;
        }
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int row = 0;
        int col = chars2.length;
        int max = 0; //最长长度
        int end = 0; //位置
        while (row < chars1.length){
            int i = row;
            int j = col;
            int len = 0; //上一位置的长度
            while (i < chars1.length && j < chars2.length){
                if (chars1[i] != chars2[j]){
                    len = 0;
                }else {
                    len++;
                }
                //更新最大长度以及位置
                if (len > max){
                    end = i;
                    max = len;
                }
                //左斜线
                i++;
                j++;
            }
            //更新左斜线的起点位置
            if (col > 0){
                col--;
            }else {
                row++;
            }
        }
        return str1.substring(end-max+1,end+1);
    }

    //给定两个str1和str2，求两个字符串的最长公共子序列
    //dp[i,j] str1[0...i-1] str2[0...j-1] 最长公共子序列长度
    //分析可能性（使用最长公共子序列的结尾来分类）
    //1、不以i结尾，不以j结尾 ：dp[i-1,j-1]
    //2、以i结尾，不以j结尾 ：dp[i,j-1]
    //3、不以i结尾，以j结尾 ：dp[i-1,j]
    //4、以i结尾，以j结尾 str1[i] == str2[j]：dp[i-1,j-1] + 1
    public static String maxLen1(String str1, String str2){
        if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty()){
            return "";
        }
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int m = str1.length();
        int n = str2.length();
        int[][] dp = new int[m+1][n+1];
        //i=0，j=0,都表示空串
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (chars1[i-1] == chars2[j-1]){
                    dp[i][j] = dp[i-1][j-1] + 1;
                }else {
                    dp[i][j] = Math.max(dp[i][j-1],dp[i-1][j]);
                }
            }
        }

        // 回溯构建 LCS 字符串
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                lcs.append(str1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        // 由于回溯是从后向前添加字符，需要反转
        return lcs.reverse().toString();
    }


    //正数数组arr，长度N，代表N个人的体重。正数limit，代表一条船的载重。
    //规则：1、每艘船最多只能坐两人；2、乘客的体重和不能超过limit。
    //返回如果同时让这N个人过河最少需要多少船？
    //左右指针从（limit/2）的体重位置开始左右移动
    public static int minBoat(int[] arr, int limit) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int N = arr.length;
        Sorts.quickSort(arr, 0, N);
        if (arr[0] > limit / 2){
            return N;
        }
        if (arr[N-1] <= limit / 2){
            return (N+1)/2;
        }
        int index = 0;
        while (arr[index] <= limit / 2){
            index++;
        }
        int left = index - 1;
        int right = index;
        // x的个数，y的个数 = index - x
        // 不能和右边匹配的数的个数
        int x = 0;
        while (left >= 0){
            //贪心
            //看左边要出多少数能搞定右边
            int solved = 0;
            if (right < arr.length && arr[left] + arr[right] > limit){
                right++;
                solved++;
            }
            if (solved == 0){
                x++;
                left--;
            }else {
                //要搞定的数solved 大于 左边的总数，left - solved 负值 和 -1 取最大值
                left = Math.max(-1, left - solved);
            }

        }
        //左边剩下的数量
        int y = index - x;
        //右边剩下的数量
        int z = N - index - x;
        //原本是y + x / 2 + z 但是x / 2需要向上取整
        return y + ((x + 1) >> 1) + z;
    }

    //给定一个字符串str，求最长的回文子序列。
    //求字符串的最长回文子序列，可以转化为求原串和逆串的最长公共子序列
    //范围尝试模型 str[i...j] i到j范围上 最长的回文子序列多大
    //范围尝试的可能性以开头和结尾分类
    //1、不以i开头，不以j结尾 ：dp[i+1,j-1]
    //2、以i开头，不以j结尾 ：dp[i,j-1]
    //3、不以i开头，以j结尾 ：dp[i+1,j]
    //4、以i开头，以j结尾 str1[i] == str2[j]：dp[i+1,j-1] + 2


    //给定一个字符串str，求至少添加几个字符，使它变成回文串。
    //范围尝试模型 str[i...j] i到j范围上 至少添加几个字符
    //范围尝试的可能性以开头和结尾分类
    //1、以i开头，不以j结尾 --> 先搞定[i,j-1]，再搞定[j]：dp[i,j-1]+1
    //2、不以i开头，以j结尾 --> 先搞定[i+1,j]，再搞定[i]：dp[i+1,j]+1
    //3、以i开头，以j结尾 str1[i] == str2[j] --> 搞定[i+1,j-1] ：dp[i+1,j-1]
    //根据路径的来源还原，得出添加的字符的结果

    //给定一个字符串str，返回把str全部切成回文字串的最小分割数 O（n^2）
    //从左到右的尝试模型
    //str[i...] 最少能分割多少回文的部分
    //尝试每一个end，枚举str[i...end]中回文的部分，就去尝试这个部分是作为回文的第一块
    //枚举str[i...end]中回文的部分，验证是不是回文，可以优化-》O（1）
    //范围尝试模型 str[i...j] i到j范围上 是不是回文

    //给定一个字符串str，保留一些字符，使保留的形成回文，有多少种保留方法，相同字符不同位置算不同的方法
    //范围尝试模型 str[i...j] i到j范围上 有dp[i][j]种方法
    //范围尝试的可能性以开头和结尾分类
    //1、不以i开头，不以j结尾 -->  dp[i+1][j-1]
    //2、以i开头，不以j结尾 -->
    //3、不以i开头，以j结尾 -->
    //4、以i开头，以j结尾 str1[i] == str2[j] --> dp[i+1,j-1] + 1
    // dp[i,j-1] 表示 [i...j-1]范围上的方法，不以j结尾，但是它包括了以i结尾和不以i结尾的可能性，即情况1+情况2
    // dp[i+1,j] 表示 [i...j-1]范围上的方法，不以i结尾，但是它包括了以j结尾和不以j结尾的可能性，即情况1+情况3
    // str1[i] == str2[j] --> [i][j] 两个字符形成回文，算一种方法，[i][i+1,j-1][j] 以i开头，以j结尾加上中间的形成回文的方法
    // 情况1+情况2+情况3 == dp[i,j-1] + dp[i+1,j] - dp[i+1][j-1]
    // 情况4 == dp[i+1][j-1] + 1


    public static void main(String[] args) {
        System.out.println(strToSum1("2-(1*6+4/2)-(5+9)"));
        System.out.println(maxLen("aaabcd","abcd"));
        System.out.println((3 + 1)/2);


    }

}












































