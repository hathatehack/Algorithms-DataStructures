package C08_BTree;

import C01_random.GenerateRandomBT;
import C01_random.GenerateRandomBT.TreeNode;

import java.util.ArrayList;

public class C17_MaxSubBSTHead {
    static public class Info {
        TreeNode maxSubBSTHead;  // 最大的二叉搜索子树的头节点
        int maxSubBSTSize;  // 最大的二叉搜索子树的节点数
        int nodes;  // 所有节点数
        int max;
        int min;
        public Info(TreeNode _maxSubBSTHead, int _maxSubBSTSize, int _nodes, int _max, int _min) {
            maxSubBSTHead = _maxSubBSTHead;
            maxSubBSTSize = _maxSubBSTSize;
            nodes = _nodes;
            max = _max;
            min = _min;
        }
    }

    // 给定一棵BT的头节点head，返回这颗BT中最大的二叉搜索子树的大小。
    // 使用递归套路
    static public TreeNode maxSubBSTHead(TreeNode head) {
        if (head == null)
            return null;
        return process(head).maxSubBSTHead;
    }
    // 递归套路，先生成左右树信息(后序遍历、深度优先)，自底向上汇总信息。
    static private Info process(TreeNode node) {
        if (node == null)
            return new Info(null, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Info leftInfo = process(node.left);
        Info rightInfo = process(node.right);
        int nodes = leftInfo.nodes + rightInfo.nodes + 1;
        // BST: leftMax < head < rightMin
        boolean isBST =
                (leftInfo.maxSubBSTSize == leftInfo.nodes) && (rightInfo.maxSubBSTSize == rightInfo.nodes) && // 左右树都是BST
                        ((leftInfo.max < node.value) && (rightInfo.min > node.value));  // 当前树也是BST
        TreeNode maxSubBSTHead = null;
        int maxSubBSTSize = 0;
        if (isBST) {
            maxSubBSTSize = nodes;
            maxSubBSTHead = node;
        } else {
            if (leftInfo.maxSubBSTSize >= rightInfo.maxSubBSTSize) {
                maxSubBSTSize = leftInfo.maxSubBSTSize;
                maxSubBSTHead = leftInfo.maxSubBSTHead;
            } else {
                maxSubBSTSize = rightInfo.maxSubBSTSize;
                maxSubBSTHead = rightInfo.maxSubBSTHead;
            }
        }
        int max = node.value;
        int min = node.value;
        if (leftInfo.nodes != 0) {  // 过滤null节点
            max = Math.max(max, leftInfo.max);
            min = Math.min(min, leftInfo.min);
        }
        if (rightInfo.nodes != 0) { // 过滤null节点
            max = Math.max(max, rightInfo.max);
            min = Math.min(min, rightInfo.min);
        }
        return new Info(maxSubBSTHead, maxSubBSTSize, nodes, max, min);
    }





    // 给定一棵BT的头节点head，返回这颗BT中最大的二叉搜索子树的大小.
    static public TreeNode maxSubBSTHead2(TreeNode head) {
        return (TreeNode)process2(head)[0];
    }
    // 使用递归+容器，自上向下判断。最坏情况下只有叶节点是BST，意味着每颗子树都得做一次中序遍历！
    static private Object[] process2(TreeNode head) {
        if (head == null)
            return new Object[]{null, 0};
        // 当前树是BST
        int BSTSize = getBSTSize(head);
        if (BSTSize != 0)
            return new Object[]{head, BSTSize};  // 返回，不再遍历剩余子树
        // 否则从左右子树找BST
        Object[] leftInfo = process2(head.left);
        Object[] rightInfo = process2(head.right);
        return (int)leftInfo[1] >= (int)rightInfo[1] ? leftInfo : rightInfo;
    }
    // 判断BST
    static private int getBSTSize(TreeNode head) {
        ArrayList<Integer> array = new ArrayList<>();
        in(head, array);
        int size = array.size();
        for (int i = 1; i < size; i++) {
            if (array.get(i) <= array.get(i - 1))  // BST中序遍历值必须是递增的！
                return 0;
        }
        return size;
    }
    // 中序遍历
    static private void in(TreeNode head, ArrayList<Integer> array) {
        if (head == null)
            return;
        in(head.left, array);
        array.add(head.value);
        in(head.right, array);
    }




    static public void main(String[] args) {
        int testTimes = 1000000;
        int maxHeight = 4;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            TreeNode ans1 = maxSubBSTHead(head);
            TreeNode ans2 = maxSubBSTHead2(head);
            if (ans1 != ans2) {
                System.out.printf("%d Oops!  ans1=%s, ans2=%s\n", i, ans1, ans2);
            }
        }
    }
}
