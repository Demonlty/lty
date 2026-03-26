package practice;

import sorts.Tree.Node;

import java.util.*;

public class Test_20260321 {

    //原始栈无序的数据变成有序，额外空间最多使用一个多辅助栈
    public static Stack<Integer> f(Stack<Integer> s1){
        if (s1 == null || s1.isEmpty()){
            return s1;
        }
        //栈底到顶是由大到小
        Stack<Integer> s2 = new Stack<>();
        while (!s1.isEmpty()){
            Integer cur = s1.pop();
            if (!s2.isEmpty()){
                while (cur > s2.peek()){
                    s1.push(s2.pop());
                }
            }
            s2.push(cur);
        }
        return s2;
    }

    //二叉树每个节点都有一个int型权值，给定一颗二叉树，要求计算出从根节点到叶节点的所有路径中，权值和最大的值是多少？
    //方法一：
    static Integer maxSum = Integer.MIN_VALUE;
    public static int getMax(Node head){
        process(head, 0);
        return maxSum;
    }
    //pre 代表从根节点到当前x节点的上方节点时的路径和
    public static void process(Node x, int pre){
        if (x.left == null && x.right == null){
            maxSum = Math.max(maxSum, pre + x.value);
        }
        if (x.left != null){
            process(x.left, pre + x.value);
        }
        if (x.right != null){
            process(x.right, pre + x.value);
        }
    }
    //方法二：尝试，往左或往右
    public static int getMaxDis(Node head){
        if (head == null) return 0;
        return process2(head);
    }
    //x为头的整棵树上，最大的路径和是多少，返回
    //路径要求：一定从x出发，到叶节点算作一个路径
    public static int process2(Node x){
        if (x.left == null && x.right == null){
            return x.value;
        }
        int next = Integer.MIN_VALUE;
        if (x.left != null){ //往左得出左树的最大路径和
            next = process2(x.left);
        }
        if (x.right != null){ //往右得出右树的最大路径和，和左树最大路径和对比得出子树的最大路径和
            next = Math.max(next, process2(x.right));
        }
        return x.value + next;
    }

    //给定一个元素为非负整数的二维数组matrix，每行和每列都是从小到大有序的。
    //在给定一个非负整数aim，请判断aim是否存在matrix中
    //时间复杂度O（M+N）
    public static boolean exists(int[][] matrix, int aim){
        //从右上角开始
        int M = matrix.length;
        int N = matrix[0].length;
        int i = 0;
        int j = N - 1;
        while (i < M && j >= 0){
            if (matrix[i][j] > aim){
                j--;
            }else if (matrix[i][j] < aim){
                i++;
            }else {
                return true;
            }
        }
        return false;
    }

    //连续的左零右一
    public static List<Integer> getMaxList(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return null;
        }
        int M = matrix.length;
        int N = matrix[0].length;
        int i = 0;
        int j = N - 1;
        List<Integer> res = new ArrayList<>();
//        int max = Integer.MIN_VALUE;
        while (i < M){
            if (matrix[i][j] == 1){
                while (j > 0 && matrix[i][j] == 1 && matrix[i][j-1] == 1) {
                    res.clear();
//                    max++;
                    j--;
                }
                res.add(i);
            }
            i++;
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] arr = new int[4][4];
        arr[0] = new int[]{0,0,0,0};
        arr[1] = new int[]{0,0,1,1};
        arr[2] = new int[]{0,0,0,0};
        arr[3] = new int[]{1,1,1,1};
//        System.out.println(exists(arr,14));
        System.out.println(getMaxList(arr));
    }
}
