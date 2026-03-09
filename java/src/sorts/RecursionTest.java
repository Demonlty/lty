package sorts;

import java.util.*;

public class RecursionTest {
    //暴力递归


    //打印一个字符串的全部子序列，包括空串
    public static void allSubPrint(String s){
        process(s.toCharArray(),0,new ArrayList<Character>());
    }
    public static void process(char[] str, int i, List<Character> list){
        if (i == str.length){
            printList(list);
            return;
        }
        List<Character> usedList = copyList(list);
        usedList.add(str[i]);
        process(str,i+1,usedList);
        List<Character> unusedList = copyList(list);
        process(str,i+1,unusedList);
    }
    public static List<Character> copyList(List<Character> list){
        List<Character> ts = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ts.add(list.get(i));
        }
        return ts;
    }
    public static void printList(List<Character> list){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        System.out.println(sb.toString());
    }

    public static void allSubPrint1(String s){
       process1(s.toCharArray(),0);
    }
    public static void process1(char[] str, int i){
        if (i == str.length){
            System.out.println(String.valueOf(str));
            return;
        }
        process1(str,i+1);
        char temp = str[i];
        str[i] = 0;
        process1(str,i+1);
        str[i] = temp;
    }

    public static void printAllSubs(String str) {
        process(str.toCharArray(), 0, 0);  // pos: 当前考虑的位置, len: 当前有效长度
    }
    private static void process(char[] str, int i, int len) {
        if (i == str.length) {
            System.out.println(new String(str, 0, len));
            return;
        }

        // 保存被覆盖的值
        char tmp = str[len];

        // 选当前字符
        str[len] = str[i];
        process(str, i + 1, len + 1);

        // 回溯：恢复原值
        str[len] = tmp;

        // 不选当前字符
        process(str, i + 1, len);
    }

    public static void printAllSubsequences(String s) {
        char[] str = s.toCharArray();
        process(str, 0, new StringBuilder());
    }
    private static void process(char[] str, int i, StringBuilder path) {
        if (i == str.length) {
            System.out.println(path.length() == 0 ? "\"\"" : path.toString());
            return;
        }

        // 选当前字符
        path.append(str[i]);
        process(str, i + 1, path);
        path.deleteCharAt(path.length() - 1); // 回溯

        // 不选
        process(str, i + 1, path);
    }


    //字符串的全排列
    public static ArrayList<String> fullArrayOfStrings(String s){
        ArrayList<String> res = new ArrayList<>();
        process(s.toCharArray(),0,res);
        for (String string : res){
            System.out.println(string);
        }
        return res;
    }
    //str[i..]范围上，所有的字符都可以在i位置上尝试，后续的字符都去尝试
    //str[0...i]范围上，是之前所作的选择
    //把所有的字符串形成的全排列，放入res中
    //visit，标记已经尝试过的字符，为了得到不重复的结果
    public static void process(char[] str, int i, ArrayList<String> res){
        if (i == str.length){
            res.add(String.valueOf(str));
            return;
        }
        boolean[] visit = new boolean[26];
        for (int j = i; j < str.length; j++){
            if (!visit[str[j] - 'a']){ //分支限界
                visit[str[j] - 'a'] = true;
                swap(str,i,j);
                //后续的都去尝试
                process(str,i+1,res);
                //恢复原str
                swap(str,i,j);
            }
        }
    }
    public static void swap(char[] str,int i, int j){
        char c = str[i];
        str[i] = str[j];
        str[j] = c;
    }

    //依次拿纸牌，拿左边或者右边，获胜的最大分数
    public static int win(int[] arr){
       if (arr == null || arr.length == 0){
           return 0;
       }
       return Math.max(f(arr, 0, arr.length - 1), s(arr, 0, arr.length - 1));
    }
    //先手拿
    public static int f(int[] arr, int L, int R){
        if (L == R) return arr[L];
        return Math.max(arr[L] + s(arr, L + 1, R), arr[R] + s(arr, L, R -1));
    };
    //后手拿
    public static int s(int[] arr, int L, int R){
        if (L == R) return 0;
        return Math.min(f(arr, L + 1, R), f(arr, L , R -1));
    };

    //给你一个栈，请你逆序这个栈，不能申请额外的数据结构，只能使用递归函数。
    public static void reverseStack(Stack<Integer> stack){
        if (stack.isEmpty()){
            return;
        }
        int last = f(stack);
        reverseStack(stack);
        stack.push(last);
    }
    public static int f(Stack<Integer> stack){
        int pop = stack.pop();
        if (stack.isEmpty()){
            return pop;
        }else {
            int last = f(stack);
            stack.push(pop);
            return last;
        }
    }

    //数字转化字符串
    public static int numToStr(int num){
        return process(String.valueOf(num).toCharArray(),0);
    }

    //i位置上有多少转化方法，i位置之前的都已做好决定
    public static int process( char[] str, int i){
        if (i == str.length){
            return 1; //i位置之前的都已做好决定，i是最后一个，那方法只有一种
        }
        if (str[i] == '0'){
            return 0; //i位置之前的都已做好决定，i是最后一个且为0不合法，那总体看这个转化方法不合法，即为0
        }
        if (str[i] == '1'){
            int res = process(str,i+1); //i作为单独的部分转化，后续有多少方法
            if (i + 1 < str.length){
                res += process(str, i + 2); //（i和i+1）作为单独的部分，后续有多少方法
            }
            return res;
        }
        if (str[i] == '2'){
            int res = process(str,i+1); //i作为单独的部分转化，后续有多少方法
            if (i + 1 < str.length && str[i+1] >= '0' && str[i+1] <= '6'){
                res += process(str, i + 2); //（i和i+1）作为单独的部分，后续有多少方法
            }
            return res;
        }
        //i == 3 ~ 9
        return process(str,i+1); //i作为单独的部分转化，后续有多少方法
    }

    //货物选择，重量：weight[i]，价值：values[i]，自由选择，返回最大价值
    //重量不查过 bag
    //已经确定后的所形成的总重量
    public static int process(int[] weight, int[] values, int i, int alreadyWeight, int bag){
        if (alreadyWeight > bag){
            return 0;
        }
        if (i == weight.length){
            return 0;
        }
        return Math.max(process(weight,values,i+1,alreadyWeight,bag), values[i]
                + process(weight,values,i+1,alreadyWeight+weight[i],bag));
    }

    //N*N 皇后问题
    public static int QueenNum(int n){
        if (n == 0){
            return 0;
        }
        int[] record = new int[n]; //record[i] 表示 i行的皇后放在了第几列
        return process(0,record,n);
    }
    //在i行上放皇后
    public static int process(int i,int[] record, int n){
        if (i == n){
            return 1;
        }
        int res = 0;
        //在i行，0~n-1列放皇后
        for (int j = 0; j < n; j++) {
            //先判断合法不合法
            if (isVaild(record,i,j)){
                //合法
                record[i] = j; //放上皇后
                res += process(i+1, record, n);
            }
        }
        return res;
    }
    //判断i行j列合不合法
    //合法的要求：i行j列的皇后和 0~i-1行的皇后不同行不同列，并且不共斜线
    public static boolean isVaild(int[] record, int i, int j){
        for (int k = 0; k < i; k++) { //k行的皇后
            if (record[k] == j || Math.abs(k - i) == Math.abs(record[k] - j)){
                return false;
            }
        }
        return true;
    }
    //使用位运算加速，不要超过32皇后问题
    public static int QueenNum1(int n){
        if (n < 1 || n > 32){
            return 0;
        }
        int limit = n == 32 ? -1 : (1 << n - 1);
        return process1(limit, 0, 0, 0);
    }
    //colLim 列限制，1的位置不能放，0的位置可以放
    //leftDiaLim 左斜线限制，1的位置不能放，0的位置可以放
    //rightDiaLim 右斜线限制，1的位置不能放，0的位置可以放
    public static int process1(int limit, int colLim, int leftDiaLim, int rightDiaLim){
        if (colLim == limit){
            return 1;
        }
        int pos = 0; //可以放皇后的总体列位置
        int mostRightOne = 0;
        int res = 0;
        pos = limit & (~(colLim | leftDiaLim | rightDiaLim));
        while (pos != 0){
            mostRightOne = pos & (~pos + 1);
            pos = pos - mostRightOne;
            res += process1(limit, colLim | mostRightOne,
                    (leftDiaLim | mostRightOne) << 1,  //控制的是左斜线的方向，所以只需要和放的位置取|再左移即可
                    (rightDiaLim | mostRightOne) >>> 1);  //控制的是右斜线的方向，所以只需要和放的位置取|再无符号右移即可，无符号右移高位补零
        }                                                   //左移(<<)：低位补0，高位丢弃
        return res;                                         //无符号右移(>>>)：高位始终补0，不管原数是正还是负
    }                                                       //右移(>>)：高位补符号位（正数补0，负数补1）







    public static void main(String[] args) {
//        allSubPrint("abc");
//        allSubPrint1("abc");
//        printAllSubs("abc".toCharArray());
//        printAllSubsequences("abc");
//        printAllSubs("abc");

//        fullArrayOfStrings("");
//        System.out.println(win(new int[]{1, 4, 100, 5, 9}));
        System.out.println(numToStr(2133));
    }

}
