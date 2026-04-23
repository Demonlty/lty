package practice;

import sorts.Sorts;
import sorts.Tree;

import java.util.HashMap;
import java.util.Map;

import static sorts.Sorts.swap;

public class Test_20260401 {

    //无序数组中，求最小的第K个数
    //方法一： 荷兰国旗问题，随机选一个数，分区，看k位置是小于区、等于区、大于区，等于区的话直接返回，递归时只递归一边
    //根据master公式推出最好时间O(N)、数组需要递归一遍推出最坏O（N^2）-->时间O(N)
    //BFPRT与荷兰国旗不同的时，最开始选数的方式不同
    /**
     * BFPRT
     */
    public static int select(int[] arr, int begin, int end, int i){
        if (begin == end){
            return arr[begin];
        }
        // 分组 + 组内排序 组成新arr + 选出新arr的上中位数 pivot
        int pivot = medianOfMedians(arr, begin, end);
        //根据pivot做划分值 <p ==p >p , 返回等于区域的左边界和右边界
        //选出分区后每次至少能淘汰约 30% 的元素，确保递归规模缩小到最多 7n/10 左右，从而整体时间复杂度保持 O(n)
        //pivotRange[0] 等于区域的左边界
        //pivotRange[0] 等于区域的右边界
        int[] pivotRange = partition(arr, begin, end, pivot);
        if (i >= pivotRange[0] && i <= pivotRange[1]){
            return arr[i];
        } else if (i < pivotRange[0]) {
            return select(arr, begin, pivotRange[0] - 1, i);
        }else {
            return select(arr, pivotRange[1] + 1, end, i);
        }
    }
    //荷兰国旗
    private static int[] partition(int[] arr, int begin, int end, int pivotValue) {
        int small = begin - 1;
        int cur = begin;
        int big = end + 1;
        while (cur != big){
            if (arr[cur] < pivotValue){
                swap(arr,++small,cur++);
            }else if (arr[cur] > pivotValue){
                swap(arr,--big,cur);
            }else {
                cur++;
            }
        }
        return new int[]{small + 1, big - 1};
    }

    private static int medianOfMedians(int[] arr, int begin, int end) {
        int num = end - begin + 1;
        int offset = num % 5 == 0 ? 0 : 1;
        //5个数一组，取每组的中位数形成新数组
        int[] mArr = new int[num / 5 + offset];
        for (int i = 0; i < mArr.length; i++) {
            int beginI = begin + i * 5;
            int endI = beginI + 4;
            //取中位数， Math.min(end, endI) 最后一组可能不满5个
            mArr[i] = getMedian(arr, beginI, Math.min(end, endI));
        }
        return select(mArr, 0 ,mArr.length - 1, mArr.length / 2);
    }

    private static int getMedian(int[] arr, int begin, int end) {
        insertionSort(arr, begin, end);
        int sum = end + begin;
        //偶数个情况下，中位数取较大的那个，，上中位数和下中位数取哪个都行
        int mid = (sum / 2) + (sum % 2);
        return arr[mid];
    }

    //插入排序 扑克牌 从右往左插入
    public static void insertionSort(int[] arr, int begin, int end){
        int n = end - begin + 1;
        for (int i = begin + 1; i < n; i++) {
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }


    //给定一个正数1，裂开的方法有一种，（1）
    //给定一个正数2，裂开的方法有两种，（1和1）、（2）
    //给定一个正数3，裂开的方法有三种，（1、1、1）、（1、2）、（3）
    //给定一个正数4，裂开的方法有五种，（1、1、1、1）、（1、1、2）、（1、3）、（2、2）、（4）
    //给定一个正数n，求裂开的方法数
    public static int getways(int n){
        if (n < 0){
            return 0;
        }
        //第一个裂开1，裂开n的方法数
        return ways(1,n);
    }
    //pre 第一个裂开的部分
    //rest 剩下要裂开的数
    public static int ways(int pre, int rest){
        //base case
        if (rest == 0){ //裂开完了
            return 1;
        }
        if (pre > rest){ //要裂开的部分，大于剩余的数。方法为0
            return 0;
        }
        int ways = 0;
        for (int i = pre; i <= rest; i++) {
            ways += ways(i, rest - i);
        }
        return ways;
    }
    public static int getways1(int n){
        int[][] dp = new int[n+1][n+1];
        //base case
        for (int i = 1; i < n + 1; i++) {
            dp[i][0] = 1;
        }

        for (int pre = n; pre > 0; pre--) {
            for (int rest = pre; rest < n + 1; rest++) {
                for (int i = pre; i <= rest; i++) {
                    dp[pre][rest] += dp[i][rest - i];
                }
            }
        }
        return dp[1][n];
    }
    //斜率优化：看附近的位置的结果能不能代替枚举行为
    public static int getways2(int n){
        int[][] dp = new int[n+1][n+1];
        //base case
        for (int i = 1; i < n + 1; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i < n + 1; i++) {
            dp[i][i] = 1;
        }
        for (int pre = n - 1; pre > 0; pre--) {
            for (int rest = pre + 1; rest < n + 1; rest++) {
                //斜率优化
                dp[pre][rest] = dp[pre][rest - pre] + dp[pre + 1][rest];
            }
        }
        return dp[1][n];
    }

    //给定一棵二叉树的头节点head，已知所有节点的值都不一样，返回其中最大的且符合搜索二叉树条件的最大拓扑结构的大小。
    //拓扑结构:不是子树，只要能连起来的结构都算。
    //拓扑贡献记录
    //O（N）
    //树形DP：左树信息、右树信息加工出头节点的信息

    public static int bstTopsSize(Node head){
        //所有节点的拓扑贡献记录放到map中，操作也是在map中
        Map<Node, Record> map = new HashMap<>();
        return posOrder(head, map);
    }
    //树形DP：左树信息、右树信息加工出头节点的信息
    private static int posOrder(Node x, Map<Node, Record> map) {
        if (x == null){
            return 0;
        }
        //左树：最大拓扑结构的大小
        int ls = posOrder(x.left, map);
        //右树：最大拓扑结构的大小
        int rs = posOrder(x.right, map);
        //以左树（x.left）为头节点的拓扑贡献记录 修改为 以x为头节点的 拓扑贡献记录
        modifyMap(x.left, x.value, map, true); //true 表示左树
        //以右树（x.right）为头节点的拓扑贡献记录 修改为 以x为头节点的 拓扑贡献记录
        modifyMap(x.right, x.value, map, false);
        //修改后左树的拓扑贡献记录
        Record lr = map.get(x.left);
        //修改后右树的拓扑贡献记录
        Record rr = map.get(x.right);
        int lbst = lr == null ? 0 : lr.l + lr.r + 1;
        int rbst = rr == null ? 0 : rr.l + rr.r + 1;
        //以x为头节点的 拓扑贡献记录
        map.put(x, new Record(lbst, rbst));
        return Math.max(lbst + rbst + 1, Math.max(ls, rs));
    }
    //s 表示 true 左树
    //返回值 子树中不满足的数量
    private static int modifyMap(Node n, int v, Map<Node, Record> m, boolean s) {
        if (n == null || (!m.containsKey(n))){
            return 0;
        }
        Record r = m.get(n);
        if ((s && n.value > v) || ((!s) && n.value < v)){ //左树的值大于头节点的值，右树的值小于头节点的值
            m.remove(n);                                    //不是搜索二叉树
            return r.l + r.r + 1;
        } else {
            int minus = modifyMap(s ? n.right : n.left, v, m, s);
            if (s){
                r.r = r.r - minus;
            }else {
                r.l = r.l - minus;
            }
            m.put(n, r);
            return minus;
        }
    }
    public static class Record{
        int l;
        int r;

        public Record(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }
    public static class Node{
        public int value;
        public Node left;
        public Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    //完美洗牌问题
    //给定一个长度为偶数的数组arr，长度记为2*N。前N个为左部分，后N个为右部分。
    //arr就可以表示为{L1,L2,..,Ln, R1,R2..,Rn}，
    //请将数组调整成{R1,L1,R2,L2,..,Rn,Ln的样子。
    //要求 时间复杂度O（N），空间复杂度O（1）
    //最终能做到： 时间复杂度O（N*log3(n)），空间复杂度O（1）
    //下标循环放数：特殊偶数 N == 3^K -1 时，不同环的触发点 ： 3^(k-1)
    //a b c d e 甲 乙 --> 甲 乙 a b c d e 方法是：a b c d e 逆序，甲 乙 逆序，再 e d c b a 乙 甲 逆序即可
    //普遍偶数 N 通过上述方法 划分成 3^K -1 的块即可
    //主函数
    public static void shuffle(int[] arr){
        if (arr != null && arr.length != 0 && (arr.length & 1) == 0){
            shuffle(arr,0,arr.length-1);
        }
    }
    //在arr[L...R]上做完美洗牌
    public static void shuffle(int[] arr, int L, int R){
        while (R - L + 1 > 0){
            int len = R - L + 1;
            int base = 3;
            int k = 1;
            //找到最大的k，满足3^K - 1 <= len
            while (base <= (len + 1)/3){
                base *= 3;
                k++;
            }
            //当前要解决长度为base - 1 的块，一半就是再除2
            int half = (base - 1) /2 ;
            //[L...R]中点位置
            int mid = (L + R) / 2;
            //旋转，arr下标从0开始，左部分[L+half...mid][mid+1...mid+half]
            rotate(arr,L+half, mid, mid + half);
            //旋转完成后，从L开始算起，长度为base - 1的部分进行下标连续推
            cycles(arr, L, base - 1, k);
            //解决了前base-1的部分，剩下的部分继续处理
            L = L + base - 1;
        }
    }
    //从start位置开始，往右len的长度这一段，做下标连续推
    //出发位置依次为1，3，9...
    private static void cycles(int[] arr, int start, int len, int k) {
        //找到每一个出发位置trigger，一共k个
        //每一个出发位置trigger都进行下标连续推
        //出发位置从1开始算，数组下标是从0开始算
        for (int i = 0, trigger = 1; i < k; i++, trigger *= 3){
            int preValue = arr[trigger + start - 1];
            int cur = modifyIndex(trigger, len);
            while (cur != trigger){
                int tmp = arr[cur + start - 1];
                arr[cur + start - 1] = preValue;
                preValue = tmp;
                cur = modifyIndex(cur, len);
            }
            arr[cur + start - 1] = preValue;
        }
    }
    private static void rotate(int[] arr, int i, int mid, int i1) {
        // 三个区间：
        // 左： [i ... mid]          ← 要移动到最后
        // 中： [mid+1 ... i1]      ← 要移动到最前面
        // 右： 无（因为 i1 = mid + half）
        // 使用三次 reverse 实现右旋转（经典技巧，O(length) 时间）
        reverse(arr, i, mid);          // 反转左部分
        reverse(arr, mid + 1, i1);     // 反转中部分（即要前移的部分）
        reverse(arr, i, i1);           // 整体反转
    }
    // 辅助的反转函数（原地 reverse）
    private static void reverse(int[] arr, int left, int right) {
        while (left < right) {
            int temp = arr[left];
            arr[left++] = arr[right];
            arr[right--] = temp;
        }
    }
    //下标从1开始
    public static int modifyIndex(int i, int len){
//        if (i <= len / 2){
//            return 2 * i;
//        }else {
//            return 2 * (i - (len / 2)) -1;
//        }
        return (2 * i) % (len + 1);
    }


    public static void main(String[] args) {
        int[] arr = new int[]{1,2,4,2,6,1,5,9,2,8,5,7};
        System.out.println(select(arr, 0, arr.length - 1, 6));
        insertionSort(arr,0,arr.length-1);
        Sorts.print(arr);

    }
}
