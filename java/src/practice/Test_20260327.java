package practice;

import java.util.*;

public class Test_20260327 {

    //实现算法找到[1,n]中所有未出现在A中的整数，数组中部分整数回重复出现。 时间复杂度O（n），空间复杂度O（1）
    //数组下标i位置上放值i+1
    public static void printNumberNoInArray(int[] arr){
        if (arr == null || arr.length == 0){
            return;
        }
        for (int value : arr){
            modify(arr,value);
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != i + 1){
                System.out.println(i+1);
            }
        }
    }
    private static void modify(int[] arr,int value) {
        while (arr[value - 1] != value){
            int temp = arr[value - 1];
            arr[value - 1] = value;
            value = temp;
        }
    }

    /**
     * 递归无限循环跑不完，说明递归限制不够
     * 最优的方案肯定是低于频繁解的，这也是一个限制
     * 也可以通过业务问题推出限制
     */
    //业务题的递归优化思想


    //五种字符 0(假)、1(真)、&(逻辑与)、|(逻辑或)、^(异或)组成字符串express，再定一个布尔值desired
    //返回express能有多少种组合方式，可以达到desired的结果
    //例如：express="1^0|0|1", desired = false
    //只有 1^((0|0)|1) 和 1^(0|(0|1))的组合可以得到false，返回2
    //express="1", desired = false
    //无组合则可以得到false，返回0
    //根据加括号的位置不一样代表组合不同
    public static int num1(String express, boolean desired){
        if (express == null || express.isEmpty()){
            return 0;
        }
        char[] exp = express.toCharArray();
        if (!iValid(exp)){
            return 0;
        }
        return p(exp,desired,0,exp.length-1);
    }
    //[L,R]范围上为desired的exp的组合方法数
    //L，R的取值一定是0或者1的位置，不会是逻辑符号的位置
    private static int p(char[] exp, boolean desired, int L, int R) {
        if (L == R){
            if (exp[L] == '0'){
                return desired ? 0 : 1;
            }else {
                return desired ? 1 : 0;
            }
        }
        int res = 0;
        //[L,R]范围上以逻辑符号位（奇数位置）分割来谈论方法数
        if (desired){
            //i位置尝试L...R范围上的每一个逻辑符号，都是最后结合的
            for (int i = L + 1; i < R; i += 2) {
                switch (exp[i]){
                    case '&' :
                        res += p(exp,true,L,i-1) * p(exp,true,i+1,R);
                        break;
                    case '|' :
                        res += p(exp,true,L,i-1) * p(exp,false,i+1,R);
                        res += p(exp,false,L,i-1) * p(exp,true,i+1,R);
                        res += p(exp,true,L,i-1) * p(exp,true,i+1,R);
                        break;
                    case '^' :
                        res += p(exp,true,L,i-1) * p(exp,false,i+1,R);
                        res += p(exp,false,L,i-1) * p(exp,true,i+1,R);
                        break;
                }
            }
        }else { //false
            for (int i = L + 1; i < R; i += 2) {
                switch (exp[i]){
                    case '&' :
                        res += p(exp,true,L,i-1) * p(exp,false,i+1,R);
                        res += p(exp,false,L,i-1) * p(exp,true,i+1,R);
                        res += p(exp,false,L,i-1) * p(exp,false,i+1,R);
                        break;
                    case '|' :
                        res += p(exp,false,L,i-1) * p(exp,false,i+1,R);
                        break;
                    case '^' :
                        res += p(exp,true,L,i-1) * p(exp,true,i+1,R);
                        res += p(exp,false,L,i-1) * p(exp,false,i+1,R);
                        break;
                }
            }
        }
        return res;
    }
    public static int dpLive(String express, boolean desire){
        char[] exp = express.toCharArray();
        int N = exp.length;
        int[][] tMap = new int[N][N];
         int[][] fMap = new int[N][N];
        for (int i = 0; i < N; i+=2) {
            tMap[i][i] = exp[i] == '0' ? 0 : 1;
            fMap[i][i] = exp[i] == '0' ? 1 : 0;
        }
        for (int row = N - 3; row >= 0; row -= 2) {
            for (int col = row + 2; col < N; col += 2) {
                //row..col tMap fMap
                for (int i = row + 1; i < col; i += 2) {
                    switch (exp[i]){
                        case '&' :
                            tMap[row][col] += tMap[row][i-1] * tMap[i+1][col];
                            break;
                        case '|' :
                            tMap[row][col] += tMap[row][i-1] * fMap[i+1][col];
                            tMap[row][col] += fMap[row][i-1] * tMap[i+1][col];
                            tMap[row][col] += tMap[row][i-1] * tMap[i+1][col];
                            break;
                        case '^' :
                            tMap[row][col] += tMap[row][i-1] * fMap[i+1][col];
                            tMap[row][col] += fMap[row][i-1] * tMap[i+1][col];
                            break;
                    }
                    switch (exp[i]){
                        case '&' :
                            fMap[row][col] += tMap[row][i-1] * fMap[i+1][col];
                            fMap[row][col] += fMap[row][i-1] * tMap[i+1][col];
                            fMap[row][col] += fMap[row][i-1] * fMap[i+1][col];
                            break;
                        case '|' :
                            fMap[row][col] += fMap[row][i-1] * fMap[i+1][col];
                            break;
                        case '^' :
                            fMap[row][col] += tMap[row][i-1] * tMap[i+1][col];
                            fMap[row][col] += fMap[row][i-1] * fMap[i+1][col];
                            break;
                    }
                }
            }
        }
        return desire ? tMap[0][N-1] : fMap[0][N-1];
    }
    //判断是否合法
    private static boolean iValid(char[] exp) {
        if ((exp.length & 1) == 0){
            return false;
        }
        for (int i = 0; i < exp.length; i ++) {
            if ((i & 1) == 0){
                if (exp[i] != '0' && exp[i] != '1'){
                    return false;
                }
            }else {
                if (exp[i] != '&' && exp[i] != '|' && exp[i] != '^'){
                    return false;
                }
            }
        }
        return true;
    }

    //在一个字符串中找到没有重复字符子串中最长的长度 L
    //子串或者子数组问题，先想每个位置结尾的情况
    //i位置下没有重复字符子串中最长的长度
    //1、i位置的字符对应的L最多到上次此字符出现的位置
    //2、i位置的字符对应的L最多到i-1位置对应的L的位置
    //3、1 2 中最短的那个即为i位置对应的L的答案
    public static int maxUnique(String str){
        if (str == null || str.isEmpty()){
            return 0;
        }
        char[] chars = str.toCharArray();
        //假设字符是0~255
        int[] map = new int[256];
        int pre = -1; //i-1位置对应的L的长度
        int len = 0; //最大的长度
        int cur = 0; //当前的L长度
        for (int i = 0; i < chars.length; i++) {
            //求的是坐标，所以是max
            pre = Math.max(pre, map[chars[i]]);
            cur = i - pre; //求的是字串长度
            len = Math.max(len,cur);
            map[chars[i]] = i;
        }
        return len;
    }

    //给定两个字符串str1和str2，再给定三个整数ic、dc、和rc，分别是代表插入、删除和替换一个字符的代价，返回str1编辑成str2的最小代价
    //str[i][j] 位置上的可能性
    //1、dp[i-1][j] + dc
    //2、dp[i][j-1] + ic
    //3、dp[i-1][j-1] + rc
    //4、str1[i] == str2[j] --> dp[i-1][j-1] + 0（复制的代价，此题为0）
    //dp[0][0] == 0 代表空串到空串的可能性
    public static int minCost(String s1, String s2,int ic, int dc, int rc){
        if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()){
            return 0;
        }
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        int N1 = chars1.length;
        int N2 = chars2.length;
        int[][] dp = new int[N1+1][N2+1];
        for (int i = 1; i <= N2; i++) {
            dp[0][i] = ic * i;
        }
        for (int i = 1; i <= N1; i++) {
            dp[i][0] = dc * i;
        }

        for (int row = 1; row <= N1; row++) {
            for (int col = 1; col <= N2; col++) {
                if (chars1[row-1] == chars2[col-1]){
                    dp[row][col] = dp[row-1][col-1];
                }else {
                    dp[row][col] = dp[row-1][col-1] + rc;
                }
                dp[row][col] = Math.min(dp[row][col],dp[row-1][col] + dc);
                dp[row][col] = Math.min(dp[row][col],dp[row][col-1] + ic);
            }
        }
        return dp[N1][N2];
    }

    //给定一个全是小写字母的字符串str，删除多余字符，使得每种字符值保留一个，并让最终结果字符串的子典序最小
    //贪心，每次都挑码值小的，同时在挑选时维护右侧仍然能全部具备所有种字符
    //时间复杂度：O（K*N） k表示多少种字符
    public static String remove(String str){
        if (str == null || str.length() < 2){
            //null 或者 只有一个字符时返回 它自己
            return str;
        }
        int[] map = new int[256];
        //记录词频
        for (int i = 0; i < str.length(); i++) {
            map[str.charAt(i)]++;
        }
        int minACSIndex = 0;
        //找到minACSIndex的位置，找的范围是0~i，i表示有一个字符再往后就词频为0了，因为要至少要保留一个
        for (int i = 0; i < str.length(); i++) {
            if (--map[str.charAt(i)] == 0){
                break;
            }else {
                minACSIndex = str.charAt(i) < str.charAt(minACSIndex) ? minACSIndex : i;
            }
        }
        //上述已找到最小字典序的字符 + 后续递归结果
        //后续递归：截取字符串保留minACSIndex后的字符串，并把保留的这个字符串中的已找出的字符删除
        return String.valueOf(str.charAt(minACSIndex)) + remove(str.substring(minACSIndex+1).replace(String.valueOf(str.charAt(minACSIndex)),""));
    }
    //使用单调栈（时间复杂度：O（N）） 栈使用StringBuilder来模拟（空间O（1），适用于构建单调递增的字符序列）
    //空间O（1）：因为StringBuilder中最多只会存26字母，last[]、used[]都是固定的长度，视作1
    public static String remove1(String str){
        if (str == null || str.length() < 2){
            //null 或者 只有一个字符时返回 它自己
            return str;
        }
        //单调栈：从底到顶 递增
        StringBuilder stack = new StringBuilder();
        int[] last = new int[256]; //每个字符最后出现的位置
        boolean[] used = new boolean[256]; //字符是否已经在栈中存在

        for (int i = 0; i < str.length(); i++) {
            last[str.charAt(i)] = i;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (used[c]){ //如果c已经在栈中，直接跳过
                continue;
            }
            int len = stack.length();
            //栈不空，栈顶元素 比 c 大，且 栈顶元素还会出现，三种情况下，栈顶元素可以弹出不要
            if (len > 0 && stack.charAt(len - 1) > c && last[stack.charAt(len - 1)] > i){
                used[stack.charAt(len - 1)] = false; //标记未使用
                stack.deleteCharAt(len - 1); //弹出栈顶元素
            }
            stack.append(c); //c入栈
            used[c] = true;
        }
        return stack.toString();
    }

    //abc...z的子序列组成的编号集合
    //a（1），b（2），...，z（26），ab（27），ac（28）...
    //输入字符，返回编号
    public static int kth(String s){
        if (s == null || s.isEmpty()){
            return -1;
        }
        char[] chars = s.toCharArray();
        int len = chars.length;
        int sum = 0;
        //len-1个长度的子序列个数
        for (int i = 1; i < len; i++) {
            sum += f(i);
        }
        for (int i = 0; i < chars.length; i++) {
            int cur = chars[i] - 'a' + 1;
            //cur之前字符的长度为len-i的所有子序列个数
            for (int j = i == 0 ? 1 : chars[i-1] - 'a' + 2; j < cur; j++) {
                sum += g(j,len - i);
            }
        }
        return sum + 1;
    }
    public static int kth1(String s){
        if (s == null || s.isEmpty()){
            return -1;
        }
        char[] chars = s.toCharArray();
        int len = chars.length;
        int sum = 0;
        //len-1个长度的子序列个数
        for (int i = 1; i < len; i++) {
            sum += f(i);
        }
        //取第一个字符
        int frist = chars[0] - 'a' + 1;
        for (int i = 1; i < frist; i++) {
            sum += g(i,len);
        }
        int pre = frist;
        for (int i = 1; i < len; i++) {
            int cur = chars[i] - 'a' + 1;
            //cur之前字符的长度为len-i的所有子序列个数
            for (int j = pre + 1; j < cur; j++) {
                sum += g(j,len - i);
            }
            pre = cur;
        }
        return sum + 1;
    }
    //以i开头，总长度为len的子序列的个数，i取值范围为26个字母
    public static int g(int i, int len){
        int sum = 0;
        if (len == 1){
            return 1;
        }
        for (int j = i + 1; j <= 26; j++) {
            sum += g(j,len-1);
        }
        return sum;
    }
    //len长度的子序列的个数
    public static int f(int len){
        int sum = 0;
        for (int i = 1; i <= 26; i++) {
            sum += g(i, len);
        }
        return sum;
    }

    //一个数组的异或和是指数数组中所有的数异或在一起的结果。给定一个数组arr，求最大子数组的异或和
    //使用01的字典树（前缀树）
    public static int maxSubarrayXOR(int[] arr){
        if (arr == null || arr.length == 0){
            return 0;
        }
        TrieNode head = new TrieNode();
        insert(head,0);
        int pre = 0;
        int res = Integer.MIN_VALUE;
        for (int num : arr){
            pre ^= num;
            res = Math.max(res,query(head, num)); //query返回值是取与num最大的异或值
            insert(head,pre); //插入此时的异或结果
        }
        return res;
    }
    //01的字典树（前缀树）
    public static class TrieNode{
        TrieNode[] children = new TrieNode[2];
    }
    //插入sum的值进树中
    public static void insert(TrieNode head, int num){
        //从高位到地位
        TrieNode cur = head;
        //一直从高到低搞出sum二进制所有位的前缀树出来
        for (int i = 31; i >= 0; i++) {
            int bit = (num >> i) & 1; //取出i位置的数
            if (cur.children[bit] == null){
                cur.children[bit] = new TrieNode();
            }
            cur = cur.children[bit];
        }
    }
    //查询与 num 异或能得到的最大的数
    public static int query(TrieNode head, int num){
        TrieNode cur = head;
        int maxXor = 0;
        for (int i = 31; i >= 0; i++) {
            int bit = (num >> i) & 1; //取出i位置的数
            int opposite = 1 - bit; //取反

            if (cur.children[opposite] != null){ //反值存在，异或值为1
                maxXor |= i << 1; //i位置上最大的异或值增加，二进制变成1
                cur = cur.children[opposite]; //继续往下走
            } else if (cur.children[bit] != null) {
                cur = cur.children[bit]; //相同的值存在，异或为0，maxXor不变，继续往下走
            }else {
                break;
            }
        }
        return maxXor;
    }

    //start 变成 to ，每次变一个字符，每次变化后的结果必须在list中存在，求最短的变化路径
    // BFS 构建最短路径 + 前驱关系
    public List<List<String>> findLadders(String start, String to, List<String> list) {
        List<List<String>> result = new ArrayList<>();
        if (start == null || to == null || list == null) {
            return result;
        }

        // 将 list 转为 Set，便于 O(1) 判断是否存在
        Set<String> wordSet = new HashSet<>(list);
        if (!wordSet.contains(to)) {
            return result;   // 无法到达
        }

        // BFS 构建图 + 记录每一步的最短距离
        Map<String, Integer> distance = new HashMap<>();     // 记录从 start 到每个词的最短步数
        Map<String, List<String>> prev = new HashMap<>();    // 记录每个词的前驱列表（用于回溯所有路径）

        Queue<String> queue = new LinkedList<>();
        queue.offer(start);
        distance.put(start, 0);
        wordSet.add(start);   // start 可能不在 list 中

        boolean found = false;
        int minSteps = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            int step = distance.get(curr);

            if (curr.equals(to)) {
                found = true;
                minSteps = step;
                break;   // BFS 第一层到达 to 时就是最短距离，后续不再需要更长路径
            }

            // 尝试改变 curr 的每一个位置的每一个字符
            char[] chars = curr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char original = chars[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == original) continue;
                    chars[i] = c;
                    String next = new String(chars);

                    if (wordSet.contains(next)) {
                        // 如果 next 还没访问过，或者是同一层（距离相同）
                        if (!distance.containsKey(next)) {
                            distance.put(next, step + 1);
                            queue.offer(next);
                            prev.putIfAbsent(next, new ArrayList<>());
                            prev.get(next).add(curr);
                        } else if (distance.get(next) == step + 1) {
                            // 同一层到达，添加前驱（允许多条路径）
                            prev.get(next).add(curr);
                        }
                    }
                }
                chars[i] = original;   // 恢复
            }
        }

        if (!found) {
            return result;
        }

        // DFS 回溯所有最短路径
        List<String> path = new ArrayList<>();
        dfs(to, start, prev, path, result);

        return result;
    }

    // 从 to 回溯到 start，收集所有最短路径
    private void dfs(String curr, String start, Map<String, List<String>> prev,
                     List<String> path, List<List<String>> result) {
        path.add(curr);

        if (curr.equals(start)) {
            List<String> onePath = new ArrayList<>(path);
            Collections.reverse(onePath);   // 因为是从后往前收集的，需要反转
            result.add(onePath);
        } else if (prev.containsKey(curr)) {
            for (String pre : prev.get(curr)) {
                dfs(pre, start, prev, path, result);
            }
        }

        path.remove(path.size() - 1);   // 回溯
    }


    public static void main(String[] args) {
         int[] arr = new int[]{1, 2, 3, 5, 7, 9, 2, 2, 2};
//         printNumberNoInArray(arr);

//        System.out.println(num1("0&1|0^1", true));
//        System.out.println(dpLive("0&1|0^1", true));

        System.out.println(kth("cjloqyz"));
        System.out.println(kth1("cjloqyz"));
    }

}
