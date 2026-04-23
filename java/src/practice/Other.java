package practice;

import sorts.Sorts;

import java.util.*;

public class Other {
    //线段树
    //ac自动机
    //后缀数组
    //状态压缩的动态规划


    public static void main(String[] args) {
        int[] arr = {0, 1, 0, 3, 12};
//        moveZeroes(arr);
//        Sorts.print(arr);
//        lengthOfLongestSubstring("pwwkew");
//        System.out.println('1' - '0');
//        System.out.println('a' - '0');
//        System.out.println('a' - 'a');
//        System.out.println('z' - '0');
//        System.out.println('z' - 'a');
//        System.out.println(' ' - '0');
//        System.out.println('c');

//        String s = "abcd";
//        String s1 = "ab";
//        findAnagrams(s,s1);
//        int[][] intervals = new int[][]{{1,3},{2,6},{8,10},{15,18},{3,10}};
//        Arrays.sort(intervals, (a, b) -> a[0]-b[0]);
//        System.out.println(intervals);
//        for (int i = 0; i < intervals.length; i++) {
//            for (int i1 = 0; i1 < intervals[0].length; i1++) {
//                System.out.println(intervals[i][i1]);
//            }
//        }
//
//        Collections.reverse();


        System.out.println((int) Math.sqrt(13));
        System.out.println("123".substring(0,0));
    }


    public static void climbStairs(int n) {
//        List<Integer> list = new ArrayList<>();
        for ( int i = 0; i < n; i++){
            System.out.println( i + "-->" + climbStairs1(i));
        }
//        return list;
    }

    public static int climbStairs1(int n) {
        if (n < 0){
            return 0;
        }
        if (n == 0){
            return 1;
        }
        return climbStairs1(n - 1) + climbStairs1(n - 2);
    }


    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1.val == 0 && l2.val == 0 && l1.next == null && l2.next == null) {
            return l1;
        }
        if (l1.val == 0 && l1.next == null) {
            return l2;
        }
        if (l2.val == 0 && l2.next == null) {
            return l1;
        }
        ListNode head = new ListNode(0);
        head.next = l1;
        boolean up = false;
        while (l1 != null && l2 != null) {
            int upNum = up ? 1 : 0;
            l1.val = l1.val + l2.val + upNum;
            up = l1.val > 9;
            l1.val = up ? l1.val % 10 : l1.val;
            if (up && l1.next == null && l2.next == null) {
                l1.next = new ListNode(1);
                break;
            }
            if (l1.next == null && l2.next != null) {
                l2 = l2.next;
                l1.next = l2;
                dealNode(up,l2);
                break;
            } else if (l1.next != null && l2.next == null) {
                l1 = l1.next;
                dealNode(up,l1);
                break;
            }
            l1 = l1.next;
            l2 = l2.next;

        }
        return head.next;
    }

    public void dealNode(boolean up, ListNode l1){
        while (up) {
            l1.val = up ? l1.val + 1 : l1.val;
            up = l1.val > 9;
            l1.val = up ? l1.val % 10 : l1.val;
            if (up && l1.next == null) {
                l1.next = new ListNode(1);
                break;
            }
            l1 = l1.next;
        }
    }
}
