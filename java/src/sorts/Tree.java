package sorts;

import java.util.*;
import java.util.LinkedList;

public class Tree {


    static class Node{
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }

        public Node(int value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    //二叉树

    //深度优先遍历
    //先序：头左右
    //中序：左头右
    //后序：左右头

    //递归
    //递归序：每个节点会打印三次
    //先序：全取第一个打印
    //中序：全取第二个打印
    //后序：全取第三次打印

    //非递归
    //先序（利用辅助栈）
    //1、栈中压入头节点
    //2、从栈中弹出一个cur节点
    //3、处理（打印）cur节点
    //4、cur节点的孩子先右再左（如果存在）压栈
    //5、回到步骤2继续
    public static void preOrderUnRecur(Node head){
        System.out.println("pre-order: ");
        if (head == null){
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.add(head);
        while (!stack.isEmpty()){
            head = stack.pop();
            System.out.println(head.value + " ");
            if (head.right != null){
                stack.push(head.right);
            }
            if (head.left != null){
                stack.push(head.left);
            }
        }
    }

    //非递归
    //中序（利用两个辅助栈）
    //1、树的左边界（左孩子）全部进栈
    //2、从栈中弹出一个cur节点
    //3、处理（打印）cur节点
    //4、cur节点有右树的话
    //5、回到步骤1
    public static void inOrderUnRecur(Node head){
        System.out.println("in-order: ");
        if (head == null){
            return;
        }
        Stack<Node> stack = new Stack<>();
        while (!stack.isEmpty() || head != null){
            if (head != null){
                stack.push(head);
                head = head.left;
            }else {
                head = stack.pop();
                System.out.println(head.value + " ");
                head = head.right;
            }
        }
    }

    //非递归
    //后序（利用两个辅助栈）
    //1、栈中压入头节点
    //2、从栈中弹出一个cur节点，并记录在收集栈中
    //3、处理（打印）cur节点
    //4、cur节点的孩子先左再右（如果存在）压栈
    //5、回到步骤2继续
    //6、打印收集栈
    //第一个栈的弹出的顺序是 头右左 ，收集栈刚好相反为 左右头
    public static void posOrderUnRecur(Node head){
        System.out.println("pos-order: ");
        if (head == null){
            return;
        }
        Stack<Node> stack = new Stack<>();
        Stack<Node> collectionStack = new Stack<>();
        stack.add(head);
        while (!stack.isEmpty()){
            head = stack.pop();
            collectionStack.push(head);
            if (head.left != null){
                stack.push(head.left);
            }
            if (head.right != null){
                stack.push(head.right);
            }
        }
        while (!collectionStack.isEmpty()){
            System.out.println(collectionStack.pop() + " ");
        }
    }

    //二叉树
    //宽度优先遍历
    //使用队列，先放左再放右，弹出，先放左再放右；重复此过程
    public static void widthPriorityTraversal(Node head){
        if (head == null){
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.offer(head);
        while (!queue.isEmpty()){
            Node cur = queue.poll();
            System.out.println(cur.value + " ");
            if (head.left != null){
                queue.offer(cur.left);
            }
            if (head.right != null){
                queue.offer(cur.right);
            }
        }

    }
    //求一颗二叉树的宽度
    //宽度优先遍历的基础上，利用map记录每个节点的层级，再在遍历时使用变量记录层级的节点个数
    public static int widthTree1(Node head){
        if (head == null){
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        HashMap<Node, Integer> map = new HashMap<>();
        int curLevel = 1;
        int curLevelNodesNum = 0;
        int maxNum = Integer.MIN_VALUE;
        queue.offer(head);
        map.put(head,curLevel);
        while (!queue.isEmpty()){
            Node cur = queue.poll();
            int curNodeLevel = map.get(cur);
            if (curNodeLevel == curLevel){
                curLevelNodesNum++;
            }else {
                curLevel++;
                maxNum = Math.max(maxNum, curLevelNodesNum);
                curLevelNodesNum = 1;
            }
            if (cur.left != null){
                queue.offer(cur.left);
                map.put(cur.left,curNodeLevel+1);
            }
            if (cur.right != null){
                queue.offer(cur.right);
                map.put(cur.right,curNodeLevel+1);
            }
        }
        return Math.max(maxNum, curLevelNodesNum);
    }
    //不使用哈希表求二叉树的宽度
    public static int widthTree2(Node head){
        if (head == null){
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        Node curLevelEnd = head; //当前层级的最后一个节点
        Node nextLevelEnd = null; //下一层级的最后一个节点
        int curLevelNodeNum = 0; //当前层级发现的节点数
        int maxNum = 0; //最大宽度
        queue.offer(head);
        while (!queue.isEmpty()){
            Node curNode = queue.poll();
            curLevelNodeNum++;
            if (curNode.left != null){
                queue.offer(curNode.left);
                nextLevelEnd = curNode.left;
            }
            if (curNode.right != null){
                queue.offer(curNode.right);
                nextLevelEnd = curNode.right;
            }
            if (curNode == curLevelEnd){
                maxNum = Math.max(maxNum,curLevelNodeNum);
                curLevelNodeNum = 0;
                curLevelEnd = nextLevelEnd;
                nextLevelEnd = null;
            }
        }
        return maxNum;
    }
    //使用队列的大小求二叉树的宽度
    public static int widthTree3(Node head){
        if (head == null){
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.offer(head);
        int maxNum = 0; //最大宽度
        while (!queue.isEmpty()){
            int size = queue.size();
            maxNum = Math.max(maxNum, size);

            for (int i = 0; i< size; i++){
                Node curNode = queue.poll();
                if (curNode.left != null){
                    queue.offer(curNode.left);
                }
                if (curNode.right != null){
                    queue.offer(curNode.right);
                }
            }
        }
        return maxNum;
    }

    //如何判断一个二叉树是否是搜索二叉树

    //如何判断一颗二叉树是完全二叉树

    //如何判断一颗二叉树是否是满二叉树

    //如何判断一颗二叉树是否是平衡二叉树

    //给定两个二叉树的节点node1和node2，找到他们的最低公共祖先节点

    //在二叉树中找到一个节点的后继节点  Node中新增指向父节点的parent指针

    //二叉树的序列化和反序列化

    //折纸问题




    public static void main(String[] args) {
        Node node = new Node(1);
        node.left = new Node(2);
        node.right = new Node(3);
        node.left.left = new Node(4);
        node.left.right = new Node(5);
        node.right.left = new Node(6);
        node.right.right = new Node(7);
        System.out.println(widthTree3(node));

    }




}













































