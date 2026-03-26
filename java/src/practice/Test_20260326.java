package practice;

import sorts.Tree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

public class Test_20260326 {

    /**
     * 斐波那契数列套路 O(logN）必须是严格的递推式  可以使用打表来看一看是否是这个套路
     * F(N) = F(N-1) + F(N-2)
     * |F(N), F(N-1)| = |F(2), F(1)| * |{{a b},
     *                                   {c d}}|^(n-2)
     * F(N) = F(N-1) + F(N-2) + ... + F(N-i)  i的取值只看最后一个的F(N-i) 例如F(N) = F(N-1) + F(N-3) i是3
     * |F(N), F(N-1), ... F(N-i+1)| = |F(i), ... , F(2), F(1)| * i的矩阵^(n-i)
     * |i数量的项| = |i数量的项| * i的矩阵^(n-i)
     */
    //斐波那契数列套路 O(logN）
    //F(N) = F(N-1) + F(N-2) 像这种是严格的递推式
    //|F(N), F(N-1)| = |F(2), F(1)| * |{{1 1},
    //                                  {1 0}}|^(n-2)
    //得出结论
    //|F(N), F(N-1)| = |F(2), F(1)| * |{{a b},
    //                                  {c d}}|^(n-2)
    //求F(N)问题转化为求二阶矩阵的n-2次方问题
    //|a|^n,怎么求的快的问题
    //求a^n的值，O（log n）的解法：10^75 = 10^（1001011） 利用t=10，每次t=t*t，在看1001011的1的位置判断得出
    //                            res = 10^（1001011） = 1 * 10^1 * 10^2 * 10^8 * 10^64 = 10^75
    //                                  t翻倍了6次（O(log75)级别），res中1001011中4个1即乘了4次，log75级别--》O(logN）
    //|a|^n同理， 代表1是矩阵为左对角线为1，其他都是0
    //通用套路
    //斐波那契数列
    public static int fi(int n){
        if (n == 0){
            return -1;
        }
        if (n == 1 || n == 2){
            return 1;
        }
        int[][] base = {{1, 1},
                        {1, 0}};
        int[][] res = matrixPower(base,n-2);
        //|F(N), F(N-1)| = |F(2), F(1)| * |{{1 1},
        //                                  {1 0}}|^(n-2)
        //返回F(N)的结果
        return res[0][0] + res[1][0];
    }
    //求解矩阵的次方
    private static int[][] matrixPower(int[][] m, int p) {
        int[][] res = new int[m.length][m[0].length];
        //创建代表1的矩阵，左对角线为1
        for (int i = 0; i < res.length; i++) {
            res[i][i] = 1;
        }
        int[][] t = m; //m的1次方
        for (;  p !=0 ; p >>= 1) {
            if ((p & 1) != 0){ //位置上是1
                res = muliMatrix(res, t); //矩阵相乘
            }
            t = muliMatrix(t, t);
        }
        return res;
    }
    //矩阵相乘 对于斐波那契数列套路而言，矩阵内数据很少，都是常数，认为时间复杂度为O(1)
    private static int[][] muliMatrix(int[][] m1, int[][] m2) {
        int[][] res = new int[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    res[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return res;
    }

    //通过能力选工作获得钱数
    public static int[] getMoneys(Job[] job, int[] ability){
        Arrays.sort(job, (a,b) -> a.hard == b.hard ? b.money - a.money : a.hard - b.hard); //排序
        //难度为K的工作，最优钱数是多少
        TreeMap<Integer,Integer> map = new TreeMap<>();
        Job preJob = job[0]; //第一种难度工作的组长（钱最多的）
        map.put(preJob.hard, preJob.money);
        for (int i = 1; i < job.length; i++) {
            if (job[i].hard != preJob.hard && job[i].money > preJob.money){
                preJob = job[i];
                map.put(preJob.hard, preJob.money);
            }
        }
        int[] ans = new int[ability.length];
        for (int i = 0; i < ability.length; i++) {
            Integer key = map.floorKey(ability[i]);
            ans[i] = key == null? 0 : map.get(key);
        }
        return ans;
    }
    public static class Job{
        public Integer hard;
        public Integer money;

        public Job(Integer hard, Integer money) {
            this.hard = hard;
            this.money = money;
        }
    }

    //（业务题）日常书写数字，合法返回它自己的数字，不合法报错
    public static int convert(String s){
        if (s == null || s.length() == 0){
            return -1; //can not convert
        }
        char[] str = s.toCharArray();
        boolean valid = isValid(str);
        if (!valid){
            throw new RuntimeException("can not convert");
        }
        boolean neg = str[0] == '-';
        //越界的边界
        int res = getRes(neg, str);
        if (!neg && res == Integer.MIN_VALUE){ //原来的值是正数，但此时res是负数的最小值，无法转化（int中，正数比负数少一位）
            throw new RuntimeException("can not convert");
        }
        return neg ? res : -res;
    }
    private static int getRes(boolean neg, char[] str) {
        int minq = Integer.MIN_VALUE / 10;
        int minr = Integer.MIN_VALUE % 10;
        int res = 0;
        int cur = 0;
        for (int i = neg ? 1 : 0; i < str.length; i++) { //负数从位置1开始看
            //使用负数来计算数字
            //str[i] = '0', cur --> 0
            //str[i] = '1', cur --> -1
            //str[i] = '2', cur --> -2
            cur = '0' - str[i];
            //溢出边界，此时cur是负数，看 cur * 10 + cur 怎么溢出
            if ((res < minq) || (res == minq && cur < minr)){
                //溢出
                throw new RuntimeException("can not convert");
            }
            res += cur * 10 + cur;
        }
        return res;
    }
    //1、数字之外只允许有‘-’
    //2、如果有‘-’，必须是在开头，且后有数字存在，且这个数字不能是0开头
    //3、如果开头是0，后续必须没有数字存在
    public static boolean isValid(char[] str){
        if (str[0] != '-' && (str[0] < '0' || str[0] > 9)){
            return false;
        }
        if (str[0] == '-' && (str.length == 1 || str[1] == '0')){
            return false;
        }
        if (str[0] == '0' && str.length > 1){
            return false;
        }
        for (int i = 1; i < str.length; i++) {
            if (str[i] < '0' || str[i] > '9'){
                return false;
            }
        }
        return true;
    }

    //String[] arr = { "b\\cst", "d\\", "a\\d\\e", "a\\b\\c"}
    //上述目录结构画出来，子目录直接列在父目录的下面，并比父目录向右进两格，打印为
    //a
    //  b
    //    c
    //  d
    //    e
    //b
    //  cst
    //d
    //同一级的需要按字母顺序排列，不能乱
    public static void print(String[] folderPaths){
        if (folderPaths == null || folderPaths.length == 0){
            return;
        }
        Node head = generateFolderTree(folderPaths);
        printProcess(head,0);
    }
    public static class Node{
        String name;

        TreeMap<String, Node> nextMap;
        public Node(String name) {
            this.name = name;
            this.nextMap = new TreeMap<>();
        }
    }
    //生成路径的前缀树
    private static Node generateFolderTree(String[] folderPaths) {
        Node head = new Node("");
        for (String paths : folderPaths) {
            String[] path = paths.split("\\\\");
            Node cur = head;
            for (int i = 0; i < path.length; i++) {
                if (!cur.nextMap.containsKey(path[i])){
                    cur.nextMap.put(path[i],new Node(path[i]));
                }
                cur = cur.nextMap.get(path[i]);
            }
        }
        return head;
    }
    //深度遍历
    private static void printProcess(Node node, int level) {
        if (level != 0){
            //2 * （level - 1）个空格
            System.out.println(get2nSpace(level) + node.name);
        }
        for (Node next : node.nextMap.values()){
            printProcess(next, level + 1);
        }
    }
    public static String get2nSpace(int level){
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < level; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    //双向链表节点结构和二叉树节点结构是一样的，如果你把last认为是left，next认为是next的话。
    //给定一个搜索二叉树的头系欸但head，请转化成一条有序的双向链表，并返回链表的头节点
    public static Tree.Node convert(Tree.Node head){
        if (head == null){
            return null;
        }
        return process(head).start;
    }
    public static Info process(Tree.Node x){
        if (x == null) return new Info(null, null);

        Info leftInfo = process(x.left); //左树信息
        Info rightInfo = process(x.right); //右树信息
        if (leftInfo.end != null){
            leftInfo.end.right = x;
        }
        x.left = leftInfo.end;
        x.right = rightInfo.start;
        if (rightInfo.start != null){
            rightInfo.start.left = x;
        }
        return new Info(leftInfo.start != null ? leftInfo.start : x,
                rightInfo.end != null ? rightInfo.end : x);
    }
    //搜索二叉树转化成双向链表后，返回头和尾
    public static class Info{
        public Tree.Node start;
        public Tree.Node end;
        public Info(Tree.Node start, Tree.Node end) {
            this.start = start;
            this.end = end;
        }
    }

    //找到一颗二叉数中，最大的搜索二叉子树，返回最大搜索二叉子树的头节点和节点个数
    //1、与x无关，左树 maxBSTHead， maxBSTSize
    //2、与x无关，右树 maxBSTHead， maxBSTSize
    //3、与x有关，x， 左maxBSTSize + 1 + 右maxBSTSize
    public static class TreeInfo{
        public Tree.Node maxBSTHead;
        public boolean isBST;
        public int max;
        public int min;
        public int maxBSTSize;
    }

    /**
     * 假设答案法
     */
    //子数组最大累加和
    public static int MaxSum(int[] arr){
        if (arr == null || arr.length == 0){
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int cur = 0;
        for (int i = 0; i < arr.length; i++) {
            cur += arr[i];
            max = Math.max(max, cur);
            cur = cur < 0 ? 0 : cur;
        }
        return max;
    }
    /**
     * 压缩数组
     */
    //给定一个整型矩阵，返回子矩阵的最大累计和 使用压缩数组  （子矩阵 想 子数组的问题，再看能不能压缩为数组）
    //例如：3行，最大累加和的子矩阵的可能存在：0~0 0~1 0~2 1~1 1~2 2~2
    //像 0~1 0~2 这种，压缩矩阵为一个数组求解数组的最大累计和
    public static int matrixMaxSum(int[][] m){ //N行M列：时间复杂度O（N^2 * M）
        if (m == null || m.length == 0 || m[0].length == 0){
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int cur = 0;
        int[] temp = null;
        for (int i = 0; i < m.length; i++) { //开始的行号
            temp = new int[m[0].length];
            for (int j = i; j < m.length; j++) { //结束的行号 i~j是讨论的范围
                cur = 0;
                for (int k = 0; k < m[0].length; k++) {
                    temp[k] += m[j][k]; //随着j行数的增加，数组的数据在压缩
                    cur += temp[k];
                    max = Math.max(max, cur);
                    cur = cur < 0 ? 0 : cur;
                }
            }
        }
        return max;
    }

    //放路灯
    //"x..x...xxx..x" .的位置点亮，灯能照亮 左、自己、右 三个位置
    public static int minLight(String s){
        char[] str = s.toCharArray();
        int index = 0;
        int light = 0;
        //来到i位置时，一定要保证之前放的灯，彻底不会影响到i位置
        while (index < str.length){
            if (str[index] == 'x'){
                index++;
            }else { //str[index] == '.'
                //直接给灯，看后面的情况决定这个灯的位置
                light++;
                if (index + 1 == str.length){ //下一位置越界
                    break;
                } else { //后面有位置
                    if (str[index + 1] == 'x'){
                        index = index + 2;
                    }else {
                        //贪心
                        //后一个是 . ,灯就放在后一个（index+1）上，再后一个（index+2）不用看了，
                        // index直接跳到index+3位置即可
                        index = index + 3;
                    }
                }
            }
        }
        return light;
    }

    //已知一颗二叉树中没有重复节点，给定中序和先序，返回后序
    //int[] pre = {1，2，4，5，3，6，7}
    //int[] in = {4，2，5，1，6，3，7}
    //返回：{4，5，2，6，7，3，1}
    public static int[] getPosArray(int[] pre, int[] in){
        if (pre == null || pre.length == 0 || in == null || in.length == 0){
            return null;
        }
        int N = pre.length;
        int[] pos = new int[N];
        set(pre,in,pos,0,N-1,0,N-1,0,N-1);
        return pos;
    }
    private static void set(int[] pre, int[] in, int[] pos,
                            int prei, int prej,
                            int ini, int inj,
                            int posi, int posj) {
        if (prei > prej){
            return;
        }
        if (prei == prej){
            pos[posi] = pre[prei];
        }
        //先确定头节点位置，再分割左右树的位置边界
        pos[posj] = pre[prei];
        int find = ini;
        for (; find <= inj; find++) {
            if (in[find] == pre[prei]){
                break;
            }
        }
        //左树
        set(pre,in,pos,prei+1,prei+find-ini,ini,find-1,prei,prei+find-ini-1);
        //右树
        set(pre,in,pos,prei+find-ini+1,prej,find+1,inj,prei+find-ini,posj-1);
    }


    public static void main(String[] args) {

        String[] arr = { "b\\cst", "d\\", "a\\d\\e", "a\\b\\c"};
        print(arr);
    }

}








































