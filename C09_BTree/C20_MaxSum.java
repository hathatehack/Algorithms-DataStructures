package C09_BTree;

import C01_random.GenerateRandomBT;
import C01_random.GenerateRandomBT.TreeNode;

public class C20_MaxSum {
    static public class Info {
        int sum0;  // 不参加的最大累加值
        int sum1;  // 参加的最大累加值
        public Info(int _sum0, int _sum1) { sum0 = _sum0; sum1 = _sum1; }
    }
    // 给定一颗BT，如果某个节点被统计，那它的直接子节点都不被统计，求整颗BT被统计节点的最大累加值。
    // 使用递归套路
    static public int maxSum(TreeNode head) {
        Info info = process(head);
        return Math.max(info.sum0, info.sum1);
    }
    // 每个节点有两种可能性：1.不参加 2.参加！
    // 递归套路，先生成左右树信息(后序遍历、深度优先)，自底向上汇总信息。
    static private Info process(TreeNode head) {
        if (head == null) {
            return new Info(0, 0);
        }
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);
        // 当前节点参加，那么当前最大累加和等于左右子节点都不参加的累加和+当前节点值。
        int sum1 = head.value + leftInfo.sum0 + rightInfo.sum0;
        // 当前节点不参加，那么当前最大累加和等于左右子节点各自的参加/不参加的较大值的和。
        int sum0 = Math.max(leftInfo.sum0, leftInfo.sum1) + Math.max(rightInfo.sum0, rightInfo.sum1);
        return new Info(sum0, sum1);
    }






    // 给定一颗BT，如果某个节点被统计，那它的直接子节点都不被统计，求整颗BT被统计节点的最大累加值。
    static public int maxSum2(TreeNode head) {
        return process2(head, false);
    }
    // 每个节点有两种可能性：1.不参加 2.参加！
    // 递归，深度优先，统计所有可能性，取最优值。
    static private int process2(TreeNode head, boolean isExcluded) {
        if (head == null) {
            return 0;
        }
        // 1.当前节点不参加，那么当前最大累加和等于左右子节点各自的参加/不参加的较大值的和。
        int sum0 = process2(head.left, false) +
                process2(head.right, false);
        // 如果当前节点被排除，返回不参加的累加值!
        if (isExcluded) {
            return sum0;
        }
        // 2.当前节点参加，那么当前最大累加和等于左右子节点都不参加的累加和+当前节点值。
        int sum1 = head.value +
                process2(head.left, true) +
                process2(head.right, true);
        // 否则当前节点可以选择性参加(即选参加/不参加的最大累加值)!
        return Math.max(sum0, sum1);
    }






    static public void main(String[] args) {
        int testTimes = 1000000;
        int maxHeight = 4;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            int ans1 = maxSum(head);
            int ans2 = maxSum2(head);
            if (ans1 != ans2) {
                System.out.printf("%d Oops!  ans1=%s, ans2=%s\n", i, ans1, ans2);
            }
        }
    }
}
