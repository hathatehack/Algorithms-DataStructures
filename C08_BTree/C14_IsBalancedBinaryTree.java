package C08_BTree;

import C01_random.GenerateRandomBT;
import C01_random.GenerateRandomBT.TreeNode;

public class C14_IsBalancedBinaryTree {
    static public class Info {
        public boolean isBalanced;
        public int height;
        public Info(boolean b, int h) {
            isBalanced = b;
            height = h;
        }
    }

    // 平衡二叉树：对每颗子树要求左树与右树的高度差不超过1。
    // 递归后序遍历
    static public boolean isBalance(TreeNode root) {
        return process(root).isBalanced;
    }
    // 递归套路，先生成左右树信息(后序遍历、深度优先)，自底向上汇总信息。
    static private Info process(TreeNode root) {
        if (root == null) {
            return new Info(true, 0);  // 空节点也是BBT
        }
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        boolean isBalanced = leftInfo.isBalanced && rightInfo.isBalanced &&
                Math.abs(leftInfo.height - rightInfo.height) <= 1;
        return new Info(isBalanced, height);
    }





    // 平衡二叉树：对每颗子树要求左树与右树的高度差不超过1。
    // 递归后序遍历
    static public boolean isBalance2(TreeNode root) {
        if (root == null) {
            return true;
        }
        boolean[] isBBT = new boolean[] {true};
        process2(root, isBBT);
        return isBBT[0];
    }
    // 递归，先获取左右树高度(后序遍历、深度优先)，自底向上标记全局状态位，一旦状态位发生改变即可优化停止遍历后续子树。
    static private int process2(TreeNode root, boolean[] isBBT) {
        if (!isBBT[0] || // 存在非平衡子树，那后续子树也无需再遍历了！
                root == null)
            return 0;
        int leftHeight = process2(root.left, isBBT);
        int rightHeight = process2(root.right, isBBT);
        if (Math.abs(leftHeight - rightHeight) > 1) {
            isBBT[0] = false;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }




    static public void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        TreeNode n1_l = new TreeNode(2);
        TreeNode n1_r = new TreeNode(3);
        TreeNode n2 = new TreeNode(4);
        TreeNode n3 = new TreeNode(5);
        TreeNode n2_l = new TreeNode(6);
        TreeNode n2_r = new TreeNode(7);
        TreeNode n3_l = new TreeNode(8);
        TreeNode n3_r = new TreeNode(9);
        n1.left = n1_l;
        n1.right = n1_r;
        n1_l.left = n2;
        n1_l.right = n3;
        n2.left = n2_l;
        n2.right = n2_r;
        n3.left = n3_l;
        n3.right = n3_r;
        Info info = process(n1);
        System.out.format("%s,  %d\n", info.isBalanced, info.height);

        System.out.println("==========================================================");
        int testTimes = 100000;
        int maxHeight = 4;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            boolean ans1 = isBalance(head);
            boolean ans2 = isBalance2(head);
            if (ans1 != ans2) {
                System.out.printf("%d Oops!  ans1=%s, ans2=%s", i, ans1, ans2);
            }
        }
    }
}
