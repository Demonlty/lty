package sorts;

import java.util.*;
import java.util.LinkedList;

public class Tree1 {


    static class Node {
        int value;
        Node left;
        Node right;
        Node parent;

        public Node(int value) {
            this.value = value;
        }

        public Node(int value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public Node(int value, Node left, Node right, Node parent) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    //二叉树

    //深度优先遍历
    //递归
    //先序
    public static void preOrderUnRecur1(Node head) {
        if (head == null) {
            return;
        }
        System.out.println(head.value);
        preOrderUnRecur1(head.left);
        preOrderUnRecur1(head.right);

    }

    //中序
    public static void inOrderUnRecur1(Node head) {
        if (head == null) {
            return;
        }
        preOrderUnRecur1(head.left);
        System.out.println(head.value);
        preOrderUnRecur1(head.right);
    }

    //后序
    public static void posOrderUnRecur1(Node head) {
        if (head == null) {
            return;
        }
        preOrderUnRecur1(head.left);
        preOrderUnRecur1(head.right);
        System.out.println(head.value);
    }

    //非递归
    //先序
    public static void preOrderUnRecur(Node head) {
        System.out.print("先序：");
        if (head == null) {
            return;
        }
        //辅助栈
        Stack<Node> stack = new Stack<>();
        stack.add(head);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            System.out.print(cur.value + " ");
            if (cur.right != null) {
                stack.add(cur.right);
            }
            if (cur.left != null) {
                stack.add(cur.left);
            }
        }
        System.out.println();
    }

    //非递归
    //中序
    public static void inOrderUnRecur(Node head) {
        System.out.print("中序：");
        if (head == null) {
            return;
        }
        //辅助栈
        Stack<Node> stack = new Stack<>();
        while (head != null || !stack.isEmpty()) {
            if (head != null) {
                stack.add(head);
                head = head.left;
            } else {
                head = stack.pop();
                System.out.print(head.value + " ");
                head = head.right;
            }
        }
        System.out.println();
    }

    //非递归
    //后序
    public static void posOrderUnRecur(Node head) {
        System.out.print("后序：");
        if (head == null) {
            return;
        }
        //辅助栈
        Stack<Node> stack = new Stack<>();
        Stack<Node> stack1 = new Stack<>();
        stack.add(head);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            stack1.add(cur);
            if (cur.left != null) {
                stack.add(cur.left);
            }
            if (cur.right != null) {
                stack.add(cur.right);
            }
        }
        while (!stack1.isEmpty()) {
            System.out.print(stack1.pop().value + " ");
        }
        System.out.println();
    }

    //二叉树
    //宽度优先遍历
    public static void widthPriorityTraversal(Node head) {
        System.out.print("宽度优先遍历：");
        if (head == null) {
            return;
        }
        //队列
        Queue<Node> queue = new LinkedList<>();
        queue.offer(head);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            System.out.print(cur.value + " ");
            if (cur.left != null) {
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }
        }
        System.out.println();
    }

    //求一颗二叉树的宽度
    //使用哈希表
    public static int widthTree1(Node head) {
        if (head == null) {
            return 0;
        }
        //哈希表记录节点的层数
        HashMap<Node, Integer> levelMap = new HashMap<>();
        int curLevel = 0;
        int curLevelNodeNum = 0;
        int maxNum = Integer.MIN_VALUE;
        levelMap.put(head, 0);//放入首节点
        //队列
        Queue<Node> queue = new LinkedList<>();
        queue.offer(head);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            Integer curNodeLevel = levelMap.get(cur);
            if (curNodeLevel != curLevel) {
                maxNum = Math.max(maxNum, curLevelNodeNum);
                curLevel++;
                curLevelNodeNum = 1;
            } else {
                curLevelNodeNum++;
            }
            if (cur.left != null) {
                levelMap.put(cur.left, curNodeLevel + 1);
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                levelMap.put(cur.right, curNodeLevel + 1);
                queue.offer(cur.right);
            }
        }
        return Math.max(maxNum, curLevelNodeNum);
    }

    //不使用哈希表求二叉树的宽度
    public static int widthTree2(Node head) {
        if (head == null) {
            return 0;
        }
        Node curLevelEnd = head;
        Node nextLevelEnd = null;
        int curLevelNodeNum = 0;
        int maxNum = Integer.MIN_VALUE;
        Queue<Node> queue = new LinkedList<>();
        queue.offer(head);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            curLevelNodeNum++;
            if (cur.left != null) {
                nextLevelEnd = cur.left;
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                nextLevelEnd = cur.right;
                queue.offer(cur.right);
            }
            if (cur == curLevelEnd) {
                maxNum = Math.max(maxNum, curLevelNodeNum);
                curLevelNodeNum = 0;
                curLevelEnd = nextLevelEnd;
                nextLevelEnd = null;
            }
        }
        return maxNum;
    }

    //使用队列的大小求二叉树的宽度
    public static int widthTree3(Node head) {
        if (head == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.offer(head);
        int maxNum = Integer.MIN_VALUE;
        while (!queue.isEmpty()) {
            int size = queue.size();
            maxNum = Math.max(maxNum, size);
            for (int i = 0; i < size; i++) {
                Node cur = queue.poll();
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
            }
        }
        return maxNum;
    }

    //如何判断一个二叉树是否是搜索二叉树
    //搜索二叉树（Binary Search Tree，BST）：左小头中右大
    public static boolean isBST(Node head) {
        return BSTprocess(head).flag;
    }

    public static BSTResult BSTprocess(Node head) {
        if (head == null) {
            return new BSTResult(true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        BSTResult leftRes = BSTprocess(head.left);
        BSTResult rightRes = BSTprocess(head.right);

        boolean flag = false;
        if ((head.left == null || (leftRes.flag && leftRes.max < head.value))
                && (head.right == null || (rightRes.flag && rightRes.min > head.value))) {
            flag = true;
        }
        int max = head.value;
        int min = head.value;
        if (head.left != null) {
            max = Math.max(max, leftRes.max);
            min = Math.min(min, leftRes.min);
        }
        if (head.right != null) {
            max = Math.max(max, rightRes.max);
            min = Math.min(min, rightRes.min);
        }
        return new BSTResult(flag, max, min);
    }

    static class BSTResult {
        boolean flag;
        int max;
        int min;

        BSTResult(boolean flag, int max, int min) {
            this.flag = flag;
            this.max = max;
            this.min = min;
        }
    }

    //如何判断一颗二叉树是完全二叉树
    //完全二叉树（Complete Binary Tree，CBT）完全二叉树从根结点到倒数第二层满足完美二叉树，最后一层可以不完全填充，其叶子结点都靠左对齐
    public static boolean isCBT(Node head) {
        if (head == null) {
            return true;
        }
        boolean isNotFull = false;
        //队列
        Queue<Node> queue = new LinkedList<>();
        queue.offer(head);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (isNotFull) {
                if (cur.left != null || cur.right != null) {
                    return false;
                }
            }
            if (cur.left != null) {
                queue.offer(cur.left);
            } else {
                isNotFull = true;
            }
            if (cur.right != null) {
                if (cur.left == null) {
                    return false;
                }
                queue.offer(cur.right);
            } else {
                isNotFull = true;
            }
        }
        return true;
    }

    //如何判断一颗二叉树是否是满二叉树
    public static boolean isFBT(Node head) {
        if (head == null) {
            return true;
        }
        boolean left = isFBT(head.left);
        boolean right = isFBT(head.right);
        if (head.left == null && head.right == null) {
            return true;
        }
        return head.left != null && left && head.right != null && right;
    }

    //如何判断一颗二叉树是否是平衡二叉树
    //平衡二叉树（Balanced BinaryTree，BBT，AVL）
    public static boolean isAVL(Node head) {
        return AVLProcess(head).flag;

    }

    public static AVLResult AVLProcess(Node head) {
        if (head == null) {
            return new AVLResult(true, 0);
        }

        AVLResult leftRes = AVLProcess(head.left);
        AVLResult rightRes = AVLProcess(head.right);
        boolean flag = leftRes.flag && rightRes.flag && Math.abs(leftRes.high - rightRes.high) <= 1;
        int high = Math.max(leftRes.high, rightRes.high);
        return new AVLResult(flag, high);
    }

    static class AVLResult {
        boolean flag;
        int high;

        AVLResult(boolean flag, int high) {
            this.flag = flag;
            this.high = high;
        }
    }

    //给定两个二叉树的节点node1和node2，找到他们的最低公共祖先节点
    public static Node owestCommonAncestor(Node head, Node n1, Node n2) {
        if (head == null) {
            return null;
        }

        if (head == n1 || head == n2) {
            return head;
        }

        Node left = owestCommonAncestor(head.left, n1, n2);
        Node right = owestCommonAncestor(head.right, n1, n2);

        if (left != null && right != null) {
            return head;
        }
        if (left != null) {
            return left;
        }
        if (right != null) {
            return right;
        }
        return null;
    }

    //在二叉树中找到一个节点的中序后继节点  Node中新增指向父节点的parent指针
    public static Node postNode(Node head) {
        if (head == null || head.parent == null) {
            return null;
        }
        if (head.right != null) {
            head = head.right;
            while (head.left != null) {
                head = head.left;
            }
            return head;
        } else {
            while (head.parent != null) {
                if (head == head.parent.left) {
                    return head.parent;
                }
                head = head.parent;
            }
        }
        return null;
    }

    //二叉树的序列化和反序列化
    public static String serialization(Node head) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(head, sb);
        return sb.toString();
    }

    private static void serializeHelper(Node node, StringBuilder sb) {
        if (node == null) {
            sb.append("#_");
            return;
        }
        sb.append(node.value).append("_");
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    /**
     * 反序列化：将字符串还原成二叉树
     * @param data 序列化后的字符串，例如 "1_2_#_#_3_4_#_#_5_#_#_"
     * @return 还原后的二叉树根节点
     */
    public static Node deserialize(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        // 使用下划线分割
        String[] tokens = data.split("_");

        // 使用索引来模拟队列/指针，逐个消耗 token
        int[] index = new int[1];  // 用数组模拟可变的索引（Java没有引用传递整数）
        index[0] = 0;

        return deserializeHelper(tokens, index);
    }

    private static Node deserializeHelper(String[] tokens, int[] index) {
        // 边界保护
        if (index[0] >= tokens.length) {
            return null;
        }

        String token = tokens[index[0]];
        index[0]++;  // 消耗当前 token

        // 遇到空节点标记
        if (token.equals("#")) {
            return null;
        }

        // 创建当前节点（假设 value 是 int 类型）
        int value = Integer.parseInt(token);
        Node node = new Node(value);

        // 先序：先左子树，再右子树
        node.left = deserializeHelper(tokens, index);
        node.right = deserializeHelper(tokens, index);
        return node;
    }



    //折纸问题
    public static String origamiProblem( int n){
        StringBuffer sb = new StringBuffer();
        origamiProcess( 1, n, true,sb);
        return sb.toString();
    }

    public static void origamiProcess( int high, int n, boolean isDown, StringBuffer sb){
        if (high > n){
            return;
        }
        origamiProcess( high+1,n, true,sb);
        sb.append(isDown? "凹" : "凸");
        origamiProcess( high+1,n, false,sb);
    }


    public static void main(String[] args) {
        int[] arr = new int[]{1,2,3,4,5,6,7};
        Node node = new Node(arr[0]);
        node.left = new Node(arr[1]);
        node.right = new Node(arr[2]);
        node.left.left = new Node(arr[3]);
        node.left.right = new Node(arr[4]);
        node.right.left = new Node(arr[5]);
        node.right.right = new Node(arr[6]);
        preOrderUnRecur(node);
        inOrderUnRecur(node);
        posOrderUnRecur(node);
        widthPriorityTraversal(node);
        System.out.println("当前最大宽度1：" + widthTree1(node));
        System.out.println("当前最大宽度2：" + widthTree2(node));
        System.out.println("当前最大宽度3：" + widthTree3(node));
        System.out.println("是否是搜索二叉树：" + isBST(node));
        System.out.println("是否是完全二叉树：" + isCBT(node));
        System.out.println("是否是满二叉树：" + isFBT(node));
        System.out.println("是否是平衡二叉树：" + isAVL(node));
        System.out.println("序列化：" + serialization(node));
        System.out.println("折纸："+origamiProblem(3));
    }


}













































