package C08_BTree;

import C01_random.GenerateRandomBST;
import C01_random.GenerateRandomBST.TreeNode;

import java.util.ArrayList;

public class C15_IsBinarySearchTree {
    static public class Info {
        public boolean isBST;
        public int max;
        public int min;
        public Info(boolean _isBST, int _max, int _min) {
            isBST = _isBST;
            max = _max;
            min = _min;
        }
    }

    // 给定一棵BT的头节点head，判断这颗BT是否为二叉搜索树（对每一颗子树要求左树值<父节点值<右树值，即中序遍历值是递增的）。
    // 递归后序遍历
    static public boolean isBST(TreeNode root) {
        if (root == null)
            return true;
        return process(root).isBST;
    }
    // 递归套路，先生成左右树信息(后序遍历、深度优先)，自底向上汇总信息。
    static private Info process(TreeNode root) {
        if (root == null) {
            return null;
        }
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);

        int max = root.value;
        int min = root.value;
        if (leftInfo != null) {
            max = Math.max(leftInfo.max, max);
            min = Math.min(leftInfo.min, min);
        }
        if (rightInfo != null) {
            max = Math.max(rightInfo.max, max);
            min = Math.min(rightInfo.min, min);
        }

        boolean isBST = false;
        boolean leftIsBST = leftInfo == null ? true : leftInfo.isBST;
        boolean rightIsBST = rightInfo == null ? true : rightInfo.isBST;
        boolean leftMaxLessRoot = leftInfo == null ? true : (leftInfo.max < root.value);
        boolean rightMinGreaterRoot = rightInfo == null ? true : (rightInfo.min > root.value);
        // 左右树是BST，且左树值<当前节点值<右树值，则当前树是BST。
        if (leftIsBST && rightIsBST && leftMaxLessRoot && rightMinGreaterRoot) {
            isBST = true;
        }

        return new Info(isBST, max, min);
    }




    // 给定一棵BT的头节点head，判断这颗BT是否为二叉搜索树（对每一颗子树要求左树值<父节点值<右树值，即中序遍历值是递增的）。
    // 使用递归+容器
    static public boolean isBST2(TreeNode head) {
        if (head == null) {
            return true;
        }
        ArrayList<Integer> array = new ArrayList<>();
        in(head, array);
        int size = array.size();
        for (int i = 1; i < size; i++) {
            if (array.get(i) <= array.get(i - 1))  // BST中序遍历值必须是递增的！
                return false;
        }
        return true;
    }
    // 中序遍历
    static private void in(TreeNode head, ArrayList<Integer> array) {
        if (head == null) {
            return;
        }
        in(head.left, array);
        array.add(head.value);
        in(head.right, array);
    }






    // 中序遍历（头左右）
    static private void in(TreeNode head) {
        if (head == null) {
            return;
        }
        in(head.left);
        System.out.printf("%d  ", head.value);
        in(head.right);
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
        System.out.printf("%s,  %d,  %d\n", info.isBST, info.max, info.min);

        n2_l.value = 1;
        n2.value = 2;
        n2_r.value = 3;
        n1_l.value = 4;
        n3_l.value = 5;
        n3.value = 6;
        n3_r.value = 7;
        n1.value = 8;
        n1_r.value = 9;
        in(n1);
        info = process(n1);
        System.out.printf("\n%s,  %d,  %d\n", info.isBST, info.max, info.min);


        System.out.println("==========================================================");
        int testTimes = 100000;
        int maxHeight = 4;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBST.generateRandomBST(maxHeight, maxValue);
            boolean ans1 = isBST(head);
            boolean ans2 = isBST2(head);
            if (ans1 != ans2) {
                System.out.printf("%d Oops!  ans1=%s, ans2=%s", i, ans1, ans2);
            }
        }
    }
}
