package C09_BTree;

import C01_random.GenerateRandomBT;
import C01_random.GenerateRandomBT.TreeNode;

public class C12_IsFullBinaryTree {
    static public class Info {
        int height;
        int nodes;
        public Info(int h, int n) { height = h; nodes = n; }
    }
    // 满二叉树：对每颗子树要求左树与右树的高度相等，即左右节点必须同时存在。
    static public boolean isFull(TreeNode head) {
        if (head == null) {
            return true;
        }
        Info all = process(head);
        return (1 << all.height) - 1 == all.nodes;
    }
    // 递归套路，先生成左右树信息(后序遍历、深度优先)，自底向上汇总信息。
    static private Info process(TreeNode node) {
        if (node == null) {
            return new Info(0, 0);
        }
        Info leftInfo = process(node.left);
        Info rightInfo = process(node.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        int nodes = leftInfo.nodes + rightInfo.nodes + 1;
        return new Info(height, nodes);
    }



    static public class Info2 {
        boolean isFull;
        int height;
        public Info2(boolean _isFull, int h) { isFull = _isFull; height = h; }
    }
    // 满二叉树：对每颗子树要求左树与右树的高度相等，即左右节点必须同时存在。
    static public boolean isFull2(TreeNode head) {
        if (head == null) {
            return true;
        }
        return process2(head).isFull;
    }
    // 递归套路，先生成左右树信息(后序遍历、深度优先)，自底向上汇总信息。
    static private Info2 process2(TreeNode node) {
        if (node == null)
            return new Info2(true, 0);  // 空节点也是FBT
        Info2 leftInfo = process2(node.left);
        Info2 rightInfo = process2(node.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        boolean isFull = leftInfo.isFull && rightInfo.isFull && // 左右树都是FBT，
                leftInfo.height == rightInfo.height;  // 且左右树高度相等，那么当前树才是FBT。
        return new Info2(isFull, height);
    }



    
    // 满二叉树：对每颗子树要求左树与右树的高度相等，即左右节点必须同时存在。
    static public boolean isFull3(TreeNode head) {
        if (head == null)
            return true;
        boolean[] isFBT = new boolean[] {true};
        process3(head, isFBT);
        return isFBT[0];
    }
    // 递归，先获取左右树高度(后序遍历、深度优先)，自底向上标记全局状态位，一旦状态位发生改变即可优化停止遍历后续子树。
    static private int process3(TreeNode node, boolean[] isFBT) {
        if (!isFBT[0] || // 存在不满子树，那后续子树也无需再遍历了！
                node == null)
            return 0;
        int leftHeight = process3(node.left, isFBT);
        int rightHeight = process3(node.right, isFBT);
        if (leftHeight != rightHeight) {
            isFBT[0] = false;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }




    static public void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        System.out.printf("%s, %s, %s\n", isFull(n1), isFull2(n1), isFull3(n1));

        TreeNode n1_l = new TreeNode(2);
        n1.left = n1_l;
        System.out.printf("%s, %s, %s\n", isFull(n1), isFull2(n1), isFull3(n1));

        TreeNode n1_r = new TreeNode(3);
        n1.right = n1_r;
        System.out.printf("%s, %s, %s\n", isFull(n1), isFull2(n1), isFull3(n1));


        System.out.println("==========================================================");
        int testTimes = 100000;
        int maxHeight = 4;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            boolean ans1 = isFull(head);
            boolean ans2 = isFull2(head);
            boolean ans3 = isFull3(head);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.printf("%d Oops!  ans1=%s, ans2=%s, ans3=%s", i, ans1, ans2, ans3);
            }
        }
    }
}
