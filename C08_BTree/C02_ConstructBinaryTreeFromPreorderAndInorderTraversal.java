package C08_BTree;

import java.util.HashMap;

public class C02_ConstructBinaryTreeFromPreorderAndInorderTraversal {
    static public void main(String[] args) {
        TreeNode head = buildTree2(new int[]{1,2,4,6,7,5,8,9,3}, new int[]{6,4,7,2,8,5,9,1,3});

        pre(head);
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            value = val;
        }
    }

    static public TreeNode buildTree(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length)
            return null;

        return f(pre, 0, pre.length - 1, in, 0, in.length - 1);
    }

    // 前序遍历结果通过中序遍历结果辅助划分左右树范围界限
    static public TreeNode f(int[] pre, int L1, int R1, int[] in, int L2, int R2) {
        // 左树或右树为空
        if (L1 > R1) {
            return null;
        }
        TreeNode head = new TreeNode(pre[L1]);
        // 说明该节点无子树，直接返回节点无需再找子树
        if (L1 == R1) {
            return head;
        }

        int found = L2;
        while (in[found] != pre[L1]) {
            found++;
        }
        head.left = f(pre, L1 + 1, L1 + (found - L2), in, L2, found - 1);
        head.right = f(pre, L1 + (found - L2) + 1, R1, in, found + 1, R2);
        return head;
    }





    // hashmap记录中序遍历每节点序号，省去每轮遍历一遍，空间换时间
    static public TreeNode buildTree2(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length)
            return null;

        HashMap<Integer, Integer> valueIndexMap = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            valueIndexMap.put(in[i], i);
        }

        return f2(pre, 0, pre.length - 1, in, 0, in.length - 1, valueIndexMap);
    }

    // 前序遍历结果通过中序遍历结果辅助划分左右树范围界限
    static public TreeNode f2(int[] pre, int L1, int R1, int[] in, int L2, int R2, HashMap<Integer, Integer> valueIndexMap) {
        // 左树或右树为空
        if (L1 > R1) {
            return null;
        }
        TreeNode head = new TreeNode(pre[L1]);
        // 说明该节点无子树，直接返回节点无需再找子树
        if (L1 == R1) {
            return head;
        }

        int found = valueIndexMap.get(pre[L1]);
        head.left = f(pre, L1 + 1, L1 + (found - L2), in, L2, found - 1);
        head.right = f(pre, L1 + (found - L2) + 1, R1, in, found + 1, R2);
        return head;
    }




    // 先序遍历（头左右）
    static public void pre(TreeNode head) {
        if (head == null) {
            return;
        }
        System.out.println(head.value);
        pre(head.left);
        pre(head.right);
    }
}
