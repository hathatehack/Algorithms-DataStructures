package C08_BTree;

import C01_random.GenerateRandomBT;
import C01_random.GenerateRandomBT.TreeNode;

import java.util.LinkedList;

public class C13_IsCompleteBinaryTree {
    static public class Info {
        public boolean isCBT;
        public boolean isFull;
        public int height;
        public Info(boolean _isCBT, boolean _isFull, int _height) {
            isCBT = _isCBT;
            isFull = _isFull;
            height = _height;
        }
    }

    // 完全二叉树：每一层从左到右不能有空隙，除了最底层其它层必须是满二叉树。满二叉树是CBT，堆也是CBT。
    // 递归后序遍历
    static public boolean isCBT(TreeNode head) {
        if (head == null)
            return true;
        return process(head).isCBT;
    }
    // 递归套路，先生成左右树信息(后序遍历、深度优先)，自底向上汇总信息。
    static private Info process(TreeNode head) {
        if (head == null)
            return new Info(true, true, 0);
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        boolean isFull = leftInfo.isFull && rightInfo.isFull // 左右树都是FBT，
                && leftInfo.height == rightInfo.height;  // 且左右树的高度相等，那么当前树才是FBT。
        // 每一层从左到右不能有空隙，除了最底层其它层必须是满二叉树!
        boolean isCBT = isFull || // 1.当前树满，则一定是CBT;
                (  // 2.当前树不满：
                        // 左右树等高，左树满、右树CBT不满，那么当前树是CBT；
                        (leftInfo.isFull && rightInfo.isCBT && leftInfo.height == rightInfo.height)
                                // 左树比右树多1层，左树满、右树满，那么当前树是CBT；
                                || (leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height + 1)
                                // 左树比右树多1层，左树CBT不满、右树满，那么当前树是CBT；
                                || (leftInfo.isCBT && rightInfo.isFull && leftInfo.height == rightInfo.height + 1)
                );
        return new Info(isCBT, isFull, height);
    }



    // 完全二叉树：每一层从左到右不能有空隙，除了最底层其它层必须是满二叉树。满二叉树是CBT，堆也是CBT。
    // 层顺序遍历、宽度优先
    static public boolean isCBT2(TreeNode head) {
        if (head == null)
            return true;
        LinkedList<TreeNode> queue = new LinkedList<>();
        boolean existLeaf = false;
        queue.add(head);
        while (!queue.isEmpty()) {
            head = queue.poll();
            // 每一层从左到右不能有空隙，除了最底层其它层必须是满二叉树!
            if ((head.left != null && existLeaf) ||  // 1.左子节点前有空隙，或者上一层不满，不是CBT
                    (head.left == null && head.right != null)) {// 2.右子节点前有空隙，不是CBT。
                return false;
            }
            if (head.left != null) {
                queue.add(head.left);
            }
            if (head.right != null) {
                queue.add(head.right);
            }
            if (head.left == null || head.right == null) {
                existLeaf = true;
            }
        }
        return true;
    }




    static public void main(String[] args) {
        TreeNode n1 = new TreeNode(0);
        n1.left = new TreeNode(1);
        n1.left.left = new TreeNode(2);
        n1.right = new TreeNode(3);
        n1.right.right = new TreeNode(4);
        System.out.println(isCBT(n1));
        System.out.println(isCBT2(n1));

        int testTimes = 1000000;
        int maxHeight = 4;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            boolean ans1 = isCBT(head);
            boolean ans2 = isCBT2(head);
            if (ans1 != ans2) {
                System.out.printf("%d Oops!  ans1=%s, ans2=%s\n", i, ans1, ans2);
            }
        }
    }
}
