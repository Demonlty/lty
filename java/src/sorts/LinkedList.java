package sorts;

import org.w3c.dom.NodeList;

import java.util.*;

public class LinkedList {

    public class Node {
        int val;
        public Node next;

        public Node() {
        }
        public Node(int val) {
            this.val = val;
        }
        public Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    class ListNode {
        int val;
        ListNode next;

        public ListNode() {
        }
        public ListNode(int val) {
            this.val = val;
        }
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    class DoubleListNode {
        int val;
        DoubleListNode last;
        DoubleListNode next;

        public DoubleListNode() {
        }
        public DoubleListNode(int val) {
            this.val = val;
        }
        public DoubleListNode(int val, DoubleListNode last , DoubleListNode next) {
            this.val = val;
            this.last = last;
            this.next = next;
        }
    }


    static class Solution {

        //反转单向链表
        public ListNode reverseList(ListNode head) {

            ListNode prev = null;
            ListNode curr = head;
            ListNode next = null;

            while (curr != null) {
                next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }
            return prev;
        }

        //反转双向链表
        public DoubleListNode reverseDoubleNodeList(DoubleListNode head) {
            DoubleListNode prev = null;
            DoubleListNode curr = head;
            DoubleListNode next = null;
            while (curr != null) {
                next = curr.next;
                curr.next = prev;
                curr.last = next;
                prev = curr;
                curr = next;
            }
            return prev;
        }

        //回文链表
        //笔试：使用栈辅助
        public static boolean isPalindrome1(ListNode head) {
            Stack<ListNode> stack = new Stack<>();
            ListNode curr = head;
            while (curr != null) {
                stack.push(curr);
                curr = curr.next;
            }
            while (head != null) {
                if (head.val != stack.pop().val) {
                    return false;
                }
                head = head.next;
            }
            return true;
        }

        //利用快慢指针找出后一半的链表数据进栈和前一半进行比对  空间省一半
        public static boolean isPalindrome2(ListNode head) {
            if (head == null || head.next == null) {
                return true;
            }
            ListNode right = head; //慢指针
            ListNode curr = head; //快指针

            while (curr.next != null && curr.next.next != null) {
                right = right.next;
                curr = curr.next.next;
            }
            Stack<ListNode> stack = new Stack<>();

            while (right != null) {
                stack.push(right);
                right = right.next;
            }
            while (!stack.isEmpty()) {
                if (head.val != stack.pop().val) {
                    return false;
                }
                head = head.next;
            }
            return true;
        }
        public static boolean isPalindrome3(ListNode head) {
            if (head == null || head.next == null) {
                return true;
            }
            ListNode slow = head;
            ListNode fast = head;

            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            ListNode curr = slow.next;
            slow.next = null;
            ListNode prev = null;
            ListNode next = null;
            //反向
            while (curr != null) {
                next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }
            ListNode reversedHead = prev;
            boolean result = true;
            //比较 得出回文结果
            while (prev != null) {
                if (prev.val != head.val) {
                    result = false;
                    break;
                }
                prev = prev.next;
                head = head.next;
            }
            //反向恢复
            curr = reversedHead;
            prev = null;
            while (curr != null) {
                next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }

            slow.next = prev;
            return result;
        }
        public static boolean isPalindrome4(ListNode head) {
            if (head == null || head.next == null) {
                return true;
            }
            ListNode n1 = head;
            ListNode n2 = head;

            while (n2.next != null && n2.next.next != null) {
                n1 = n1.next;
                n2 = n2.next.next;
            }

            n2 = n1.next;
            n1.next = null;
            ListNode n3 = null;
            //反向
            while (n2 != null) {
                n3 = n2.next;
                n2.next = n1;
                n1 = n2;
                n2 = n3;
            }
            //记录最后节点的位置
            n3 = n1;
            n2 = head;
            boolean result = true;
            //比较 得出回文结果
            while (n1 != null && n2 != null) {
                if (n1.val != n2.val) {
                    result = false;
                    break;
                }
                n1 = n1.next;
                n2 = n2.next;
            }
            //反向恢复
            n1 = n3.next;
            n3.next = null;
            while (n1 != null) {
                n2 = n1.next;
                n1.next = n3;
                n3 = n1;
                n1 = n2;
            }
            return result;
        }

        //链表分区（荷兰国旗）算法
        public static Node listPartition(Node head, int pivot) {
            if (head == null ||  head.next == null) {
                return head;
            }
            int len = 0;
            Node cur = head;
            //统计链表长度
            while (cur != null) {
                len++;
                cur = cur.next;
            }
            int[] arr = new int[len];
            cur = head;
            int i = 0;
            while (cur != null) {
                arr[i++] = cur.val;
                cur = cur.next;
            }

            //对数组分区
            partitionArrayTable(arr,pivot); //不稳定版本
//            partitionArrayTable1(arr,pivot); //稳定版本 不推荐

            //还原链表
            cur = head;
            i = 0;
            while (cur != null) {
                cur.val = arr[i++];
                cur = cur.next;
            }
            return head;
        }
        //快排的分区 不稳定
        private static void partitionArrayTable(int[] arr, int pivot) {
            int left = -1;
            int right = arr.length;
            int i = 0;
            while (i < right) {
                if (arr[i] < pivot) {
                    Sorts.swap(arr, i++ , ++left);
                }else if (arr[i] > pivot) {
                    Sorts.swap(arr, i , --right);
                }else {
                    i++;
                }
            }
        }
        //稳定版本
        private static void partitionArrayTable1(int[] arr, int pivot) {
            int n =  arr.length;
            int[] temp = new int[n];
            int left = 0;
            int mid = 0;
            int right = n - 1;

            for (int num : arr) {
                if (num < pivot) {
                    temp[left++] = num;
                }
            }
            mid = left;
            for (int num : arr) {
                if (num == pivot) {
                    temp[mid++] = num;
                }
            }

            for (int num : arr) {
                if (num > pivot) {
                    arr[right--] = num;
                }
            }
            System.arraycopy(temp, 0, arr, 0, n);
        }
        //链表分区（荷兰国旗）算法 纯链表
        public static Node listPartition2(Node head, int pivot) {
            Node sH = null; //small head
            Node sT = null; //small tail
            Node eH = null; //equal head
            Node eT = null; //equal tail
            Node mH = null; //big head
            Node mT = null; //big tail
            Node next = null;

            while (head != null) {
                next = head.next;
                head.next = null;
                if (head.val < pivot){
                    if (sH == null) {
                        sH = sT = head;
                    }else  {
                        sT = sT.next = head;
                    }
                }else if (head.val == pivot) {
                    if (eH == null) {
                        eH = eT = head;
                    }else   {
                        eT = eT.next = head;
                    }
                }else {
                    if (mH == null) {
                        mH = mT = head;
                    }else   {
                        mT = mT.next = head;
                    }
                }
                head = next;
            }
            //small and equal reconnect
            if (sT != null){
                sT.next = eH;
                eT = eT == null ? sT : eT;
            }
            if (eT != null){
                eT.next = mH;
            }
            return sH != null ? sH : (eH != null ? eH : mH);
        }

        //复制含有随机指针节点的链表
        //方法一 ： 使用哈希表
        public static RandomNode copyRandomNodeList1(RandomNode head) {
            HashMap<RandomNode, RandomNode> map = new HashMap<>();
            RandomNode cur = head;
            while (cur != null) {
                map.put(cur,new RandomNode(cur.val));
                cur = cur.next;
            }
            cur = head;
            while (cur != null) {
                map.get(cur).next = map.get(cur.next);
                map.get(cur).random = map.get(cur.random);
                cur = cur.next;
            }
            return map.get(head);
        }
        //方法二：纯链表，空间复杂度O（1）
        public static RandomNode copyRandomNodeList2(RandomNode head){
            if (head == null){
                return null;
            }
            RandomNode cur = head;
//            while (cur != null) {
//                RandomNode copyNode = new RandomNode(cur.val);
//                copyNode.next = cur.next;
//                cur.next = copyNode;
//                cur = cur.next.next;
//            }
            RandomNode next = null;
            while (cur != null) {
                next = cur.next;
                cur.next = new RandomNode(cur.val);
                cur.next.next = next;
                cur = next;
            }

            cur = head;
//            while (cur != null) {
//                cur.next.random = cur.random == null? null : cur.random.next;
//                cur = cur.next.next;
//            }
            RandomNode curCopy = null;
            while (cur != null) {
                next = cur.next.next;
                curCopy = cur.next;
                curCopy.random = cur.random == null ? null : cur.random.next;
                cur = next;
            }
            //解开链表
            RandomNode newHead = head.next;
            cur = head;
            while (cur != null ) {
                RandomNode copyNode = cur.next;
                cur.next = copyNode.next;
                copyNode.next = copyNode.next == null ? null : cur.next.next;
                cur = cur.next;
            }
            return newHead;
        }

        //两个单链表相交问题
        public static Node getIntersectionNode(Node head1, Node head2){
            if (head1 == null || head2 == null){
                return null;
            }
            Node loopNode1 = getLoopNode2(head1);
            Node loopNode2 = getLoopNode2(head2);
            if (loopNode1 == null && loopNode2 == null){
                return getNoLoopIntersectionNode(head1,head2);
            }
            if (loopNode1 != null && loopNode2 != null){
                return bothLoop(head1, loopNode1, head2, loopNode2);
            }
            return null;
        }
        //前置：判断单链表有环无环问题
        //找到链表第一个入环节点，如果无环，返回null
        //方法一：set集合
        //方法二：快慢指针，快指针（每次跳二个）与慢指针（每次跳一个）相遇后，快指针跳回起点并修改为每次跳一个，然后与慢指针再次相遇，相遇点即为入环节点；
        public  static Node getLoopNode1(Node head) {
            if (head == null) {return null;}
            Set<Node> set = new HashSet<>();
            Node cur = head;
            while (cur != null) {
                if (!set.contains(cur)) {
                    set.add(cur);
                }else {
                    return cur;
                }
            }
            return null;
        }
        public static Node getLoopNode2(Node head) {
            if (head == null) {
                return null;
            }
            Node slow = head.next;
            Node fast = head.next.next;
            while (slow != fast) {
                if (fast.next == null || fast.next.next == null) {
                    return null;
                }
                slow = slow.next;
                fast = fast.next.next;
            }
            fast = head;
            while (slow != fast) {
                slow = slow.next;
                fast = fast.next;
            }
            return slow;
        }
        //两个无环的单链表，返回相交结点
        public static Node getNoLoopIntersectionNode(Node head1, Node head2) {
            if (head1 == null || head2 == null) {
                return null;
            }
            Node cur1 = head1;
            Node cur2 = head2;
            int len = 0; //两个链表长度的差值
            while (cur1 != null){
                len++;
                cur1 = cur1.next;
            }
            while (cur2 != null){
                len--;
                cur2 = cur2.next;
            }
            //判断尾节点是否相同，相同才会有相交
            if (cur1 != cur2){
                return null;
            }
            //相交的话，长链表先走len步，再一起走，在相交的第一个节点相遇
            cur1 = len > 0 ? head1 : head2; //赋值长的链表
            cur2 = cur1 == head1 ? head2 : head1; //赋值短的链表
            len = Math.abs(len); //差值的绝对值
            while (len != 0){ //长链表先走len步
                len--;
                cur1 = cur1.next;
            }
            while (cur1 != cur2){
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
            return cur1;
         }
        //两个有环链表，返回第一个相交结点
        //情况一：不相交
        //情况二：相交，入环节点相同
        //情况三：相交，入环节点不同
        public static Node bothLoop(Node head1, Node loop1, Node head2, Node loop2){
            Node cur1 = null;
            Node cur2 = null;
            if (loop1 == loop2){ //情况二：相交，入环节点相同
                //入环节点为尾节点，求相交结点，同理于 getNoLoopIntersectionNode
                cur1 = head1;
                cur2 = head2;
                int len = 0; //两个链表长度的差值
                while (cur1 != loop1){
                    len++;
                    cur1 = cur1.next;
                }
                while (cur2 != loop2){
                    len--;
                    cur2 = cur2.next;
                }
                cur1 = len > 0 ? head1 : head2; //赋值长的链表
                cur2 = cur1 == head1 ? head2 : head1; //赋值短的链表
                len = Math.abs(len); //差值的绝对值
                while (len != 0){ //长链表先走len步
                    len--;
                    cur1 = cur1.next;
                }
                while (cur1 != cur2){
                    cur1 = cur1.next;
                    cur2 = cur2.next;
                }
                return cur1;
            }else {
                cur1 = loop1.next;
                while (cur1 != loop1){
                    if (cur1 == loop2){ //情况三：相交，入环节点不同
                        return loop1; //输出loop1或者loop2都可以
                    }
                    cur1 = cur1.next;
                }
                return null; //情况一：不相交
            }
        }
    }

    static class RandomNode {
        int val;
        RandomNode next;
        RandomNode random;
        RandomNode(int val) {
            this.val = val;
        }
    }
}









































