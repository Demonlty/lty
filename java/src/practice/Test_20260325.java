package practice;

import sorts.Tree;

import java.util.*;

public class Test_20260325 {

    //洗衣机问题 leetCode --> Packing Machine
    public static int MinOps(int[] arr){
        if (arr == null || arr.length == 0){
            return 0;
        }
        int size = arr.length;
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += arr[i];
        }
        if (sum % size != 0){
            return - 1;
        }
        int avg = sum / size;
        int leftSum = 0;
        int res = 0;
        for (int i = 0; i < size; i++) {
            int leftRest = leftSum - i * avg; //i位置左边需要的衣服数量
            int rightRest = (sum - leftSum - arr[i]) - (size - i - 1) * avg; // i位置右边需要的衣服数量
            if (leftRest < 0 && rightRest < 0){
                //左右都负，即都需要i位置来提供衣服
                res = Math.max(res, Math.abs(leftRest) + Math.abs(rightRest));
            }else {
                //左右都正
                //左右一正一负
                res = Math.max(res, Math.max(Math.abs(leftRest), Math.abs(rightRest)));
            }
            leftSum += arr[i];
        }
        return res;
    }

    /**
     * 二维矩阵的宏观调度
     * 只关心宏观情况
     */
    //使用zigzag的方式打印矩阵，比如如下的矩阵
    //0 1 2  3
    //4 5 6  7
    //8 9 10 11
    //打印顺序为：0 1 4 8 5 2 3 6 9 10 7 11
    public static void zigzagOrderPrint(int[][] matrix){
        if (matrix == null) return;
        int aR = 0;
        int aC = 0;
        int bR = 0;
        int bC = 0;
        int endR = matrix.length - 1;
        int endC = matrix[0].length - 1;
        boolean fromUp = false;
        while(aR != endR + 1){
            printLevel(matrix,aR,aC,bR,bC,fromUp);
            aR = aC == endC ? aR + 1 : aR;
            aC = aC == endC ? aC : aC + 1;
            bC = bR == endR ? bC + 1 : bC;
            bR = bR == endR ? bR : bR + 1;
            fromUp = !fromUp;
        }
        System.out.println();
    }
    public static void printLevel(int[][] matrix, int tR, int tC, int dR, int dC, boolean f){
        if (f){ //右上到左下，打印斜线
            while (tR != dR + 1){
                System.out.print(matrix[tR++][tC--] + " ");
            }
        }else { //左下到右上，打印斜线
            while (dR != tR - 1){
                System.out.print(matrix[dR--][dC++] + " ");
            }
        }
    }

    //Z字形打印
    //0 1 2  3
    //4 5 6  7
    //8 9 10 11
    //打印顺序为：0 1 2 3 4 5 6 7 8 9 10 11
    public static void ZOrderPrint(int[][] matrix){
        if (matrix == null) return;
        int a = 0;
        int culLen = matrix[0].length;
        while (a < matrix.length){
            if ((a & 1) == 0){ //偶数列
                for (int i = 0; i < culLen; i++) {
                    System.out.print(matrix[a][i] + " ");
                }
            }else { //奇数列
                for (int i = culLen - 1; i >= 0; i--) {
                    System.out.print(matrix[a][i] + " ");
                }
            }
            a++;
        }
        System.out.println();
    }

    //用螺旋的方式打印矩阵，比如如下的矩阵
    //0 1 2  3
    //4 5 6  7
    //8 9 10 11
    //打印顺序为：0 1 2 3 7 11 10 9 8 4 5 6
    public static void spiralOrderPrint(int[][] matrix){
        if (matrix == null) return;
        int a = 0;
        int b = 0;
        int c = matrix.length - 1;
        int d = matrix[0].length - 1;
        while (a <= c && b <= d){
            printEdge(matrix,a++,b++,c--,d--);
        }

    }
    public static void printEdge(int[][] matrix, int a, int b, int c, int d){
        if (a == c){ //同行
            for (int i = c; i <= d; i++) {
                System.out.print(matrix[a][i] + " ");
            }
        } else if (b == d) { //同列
            for (int i = a; i <= c; i++) {
                System.out.print(matrix[i][b] + " ");
            }
        }else { //不同行，不同列。先右后下再左再上
            int curR = a;
            int curC = b;
            while (curC != d){
                System.out.print(matrix[a][curC] + " ");
                curC++;
            }
            while (curR != c){
                System.out.print(matrix[curR][d] + " ");
                curR++;
            }
            while (curC != b){
                System.out.print(matrix[c][curC] + " ");
                curC--;
            }
            while (curR != a){
                System.out.print(matrix[curR][b] + " ");
                curR--;
            }
        }
    }

    //给定一个正方形矩阵，只用有限几个变量，实现矩阵中每个位置的数顺时针转动90度，如下矩阵
    // 0  1  2  3
    // 4  5  6  7
    // 8  9 10 11
    //12 13 14 15
    //矩阵调整为如下：
    //12  8 4 0
    //13  9 5 1
    //14 10 6 2
    //15 11 7 3
    public static void rotateOrderPrint(int[][] matrix){
        if (matrix == null) return;
        int a = 0;
        int b = 0;
        int c = matrix.length - 1;
        int d = matrix[0].length - 1;
        while (a < c){ //正方形
            process(matrix,a++,b++,c--,d--);
        }
        printMatrix(matrix);
    }
    public static void process(int[][] matrix, int a, int b, int c, int d){
        for (int i = 0; i < d - b; i++) {
            swapMatrix(matrix,a,b+i,a+i,d,c,d-i,c-i,b);
        }
    }
    public static void swapMatrix(int[][] matrix, int a, int b, int a1, int b1, int a2, int b2, int a3, int b3){
        int temp = matrix[a][b];
        matrix[a][b] = matrix[a3][b3];
        matrix[a3][b3] = matrix[a2][b2];
        matrix[a2][b2] = matrix[a1][b1];
        matrix[a1][b1] = temp;
    }
    public static void printMatrix(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }


    //假设s和m初始化， s = “a”； m = s；
    //再定义两种操作，第一种操作：
    //m = s；
    //s = s + s；
    //第二种操作：s = s + m；
    //求最小的操作步骤数，可以将s拼接到长度等于n
    //两种情况
    //1、s的长度为质数时，最优：n-1次操作二
    //2、s的长度为合数时，分解因子，最优：因子的和-因子的个数
    // 例：N = X * Y * Z * P   每个因子都是质数（合数的性质）
    //    按情况一来看，N = （X * Y * Z） * P
    //    依次划分：得出最优操作 = X - 1 + Y - 1 + Z - 1 + P - 1
    public static int minOps(int n){
        if (n < 2){
            return 0;
        }
        if (isPrim(n)){
            return n - 1;
        }
        //n不是质数
        int[] divSumAndCount = divSumAndCount(n);
        return divSumAndCount[0] - divSumAndCount[1];
    }
    //返回值：所有因子的和，所有因子的个数，但是因子不包括1
    public static int[] divSumAndCount(int n){
        int sum = 0;
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (n % 2 == 0){
                sum += i;
                count++;
                n /= i;
            }
        }
        return new int[]{sum,count};
    }
    public static boolean isPrim(int n){
        if (n <= 1) return false;      // 1 和更小的数不是质数
        if (n <= 3) return true;       // 2 和 3 是质数
        if (n % 2 == 0 || n % 3 == 0) return false;  // 排除偶数和 3 的倍数

        // 只检查 6k±1 的形式
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {  //i+2 == 6k-1+2 == 6k+1
                return false;
            }
        }
        return true;
    }

    //给定一个字符串类型的数组arr，求其中出现次数最多的前K个
    //1、hashmap 词频统计
    //2、小根堆维护词频最多的K个数据
    public static List<String> getMax(String[] arr, int k){
        if (arr == null || arr.length == 0){
            return null;
        }
        HashMap<String,Integer> map = new HashMap();
        for (String s : arr){
            if (!map.containsKey(s)){
                map.put(s,0);
            }else {
                map.put(s,map.get(s)+1);
            }
        }
        PriorityQueue<Map.Entry<String,Integer>> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(Map.Entry::getValue));
        for (Map.Entry<String,Integer> entry : map.entrySet()){
            if (minHeap.size() < k){
                minHeap.offer(entry);
            }else if (entry.getValue() > minHeap.peek().getValue()){
                minHeap.poll();
                minHeap.offer(entry);
            }
        }
        List<String> result = new ArrayList<>();
        while (!minHeap.isEmpty()){
            result.add(minHeap.poll().getKey());
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * 自定义堆结构
     * 例：添加新字符串，实时得出topK的字符串，需要实时维护堆结构，所以不能使用系统自动维护而是需要自己维护堆结构
     */
    //添加新字符串，实时得出topK的字符串
    public static class Node{
        public String str;
        public int times;

        public Node(String str, int times) {
            this.str = str;
            this.times = times;
        }
    }
    public static class TopKRecord{
        private HashMap<String, Node> strNodeMap; //词频统计map
        private Node[] heap;
        private int index; //堆当前的元素个数
        private HashMap<Node, Integer> nodeIndexMap; //节点位置map

        public TopKRecord(int size){
            heap = new Node[size];
            index = 0;
            strNodeMap = new HashMap<>();
            nodeIndexMap = new HashMap<>();
        }

        public void add(String str){
            Node curNode = null;
            int preIndex = -1;
            if (!strNodeMap.containsKey(str)){
                curNode = new Node(str, 1);
                strNodeMap.put(str,curNode);
                nodeIndexMap.put(curNode,-1);
            }else {
                curNode = strNodeMap.get(str);
                curNode.times++;
                preIndex = nodeIndexMap.get(curNode);
            }
            if (preIndex == -1){
                if (index == heap.length){
                    if (curNode.times > heap[0].times){
                        nodeIndexMap.put(heap[0],-1);
                        nodeIndexMap.put(curNode,0);
                        heap[0] = curNode;
                        heapIfy(0,index);
                    }
                }else {
                    nodeIndexMap.put(curNode, index);
                    heap[index] = curNode;
                    heapInsert(index++);
                }
            }else {
                heapIfy(preIndex,index);
            }
        }

        public void heapInsert(int index){
            while (index != 0){
                int parent = (index - 1) / 2;
                if (heap[index].times < heap[parent].times){
                    swap(index,parent);
                    index = parent;
                }else {
                    break;
                }
            }
        }
        public void heapIfy(int preIndex, int index){
            int left = preIndex * 2 + 1;
            while (left < index){
                int min = left + 1 < index && heap[left + 1].times < heap[left].times ? left + 1 : left;
                min = heap[preIndex].times < heap[min].times ? preIndex : min;
                if (preIndex == min){
                    break;
                }else {
                   swap(preIndex, min);
                }
                preIndex = min;
                left = preIndex * 2 + 1;
            }
        }
        //交换位置
        public void swap(int index1, int index2){
            nodeIndexMap.put(heap[index1],index2);
            nodeIndexMap.put(heap[index2],index1);
            Node temp = heap[index1];
            heap[index1] = heap[index2];
            heap[index2] = temp;
        }

        //打印
        public void print(Node[] heap){
            for (int i = heap.length - 1; i >= 0; i--) {
                System.out.println(heap[i].str);
            }
        }

    }

    //两个栈实现一个队列
    //push栈 、 pop栈
    //添加时先添加 push栈，pop栈为空时，倒全部数据数据到pop栈中，弹出时弹 pop栈；
    //再弹数据时，如果pop栈弹空了，触发机制（pop栈为空时，倒全部数据数据到pop栈中），都为空则没有数据

    //两个队列实现一个栈
    //添加时先加进一个空队列中，弹出时，加数据的队列放数据到另一个队列中，留最后一个弹出即可
    //再添加时重复以上操作

    //字符串旋转
    //例如：12345  的结果是 12345、23451、34512、45123、51234
    //1、b的长度和a是不是相等。2、b 是不是 a + a 的字串

    //咖啡机问题

    //给定一个数组arr，如果通过调整可以做到arr中任意两个相邻的数字相乘是4的倍数，返回true；如果不能返回false
    //数组内数据分类计数：奇数 a个；偶数（只有一个2因子）b个；偶数在（包含4因子的数）c个
    //1、b == 0  摆法：奇 4 奇 4 奇 4 奇 4 奇 4 奇 4 ...... --> a == 1, c >= 1; a > 1, c >= a-1;
    //3、b == 1  摆法：2 4 奇 4 奇 4 奇 4 奇 4 奇 4 ...... --> c >= 1;
    //2、b > 1  摆法：2 2 2 2 ... 4 奇 4 奇 4 奇 4 ...... --> a == 0, c >= 0; a == 1, c >= 1; a > 1, c >= a; --> c >= a
    public boolean canArrange(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return true;  // 长度0或1时，无相邻元素，自然满足
        }

        int a = 0;  // 奇数个数
        int b = 0;  // 只有一个2因子（%4 == 2）的偶数个数
        int c = 0;  // 包含4因子（%4 == 0）的偶数个数

        for (int num : arr) {
            if (num % 2 != 0) {
                a++;                    // 奇数
            } else if (num % 4 == 0) {
                c++;                    // 4的倍数（包含4因子）
            } else {
                b++;                    // 2的倍数但不是4的倍数（只有一个2因子）
            }
        }

        // 核心判断逻辑
        //下面两种情况为true
        // b==0，a==1，c==0 ,结果为true，因为arr中任意两个相邻的数字相乘，而数组中只有一个数，返回true
        // b==1，a==0，c==0 ,结果为true，因为arr中任意两个相邻的数字相乘，而数组中只有一个数，返回true
        if (b == 0) {
            // 没有弱偶数（b类）时，奇数需要用c隔开
            // a <= 1 时，c >= 0 即可（单个奇数或没有奇数）
            // a >= 2 时，至少需要 a-1 个 c
            return c >= a - 1;
        } else {
            // 有弱偶数（b类）时，每个奇数都必须被c严格保护
            return c >= a;
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {0, 1, 2,  3},
                {4, 5, 6,  7},
                {8, 9, 10, 11}
        };
//        arr[3] = new int[]{12,13,14,15};
//        zigzagOrderPrint(matrix);
//        spiralOrderPrint(matrix);
//        System.out.println("90度旋转：");
//        rotateOrderPrint(matrix);
//        System.out.println(isPrim(11));
        System.out.println(1 / 2);
    }


}





























