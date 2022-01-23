package C09_BTree;

import C01_random.GenerateRandomBT;
import C01_random.GenerateRandomBT.TreeNode;

import java.util.HashMap;
import java.util.HashSet;

public class C19_LowestAncestor {
    static public class Info {
        TreeNode ancestor;
        boolean findA;
        boolean findB;
        public Info(TreeNode _ancestor, boolean _findA, boolean _findB) {
            ancestor = _ancestor;
            findA = _findA;
            findB = _findB;
        }
    }
    // 给定一颗二叉树的头节点head，和另两个节点a和b，返回a和b的最低公共祖先。
    // 使用递归套路
    static public TreeNode lowestAncestor(TreeNode head, TreeNode a, TreeNode b) {
        return process(head, a, b).ancestor;
    }
    // 递归套路，先生成左右树信息(后序遍历、深度优先)，自底向上汇总信息。
    static private Info process(TreeNode node, TreeNode a, TreeNode b) {
        if (node == null) {
            return new Info(null, false, false);
        }
        Info leftInfo = process(node.left, a, b);
        Info rightInfo = process(node.right, a, b);
        boolean findA = leftInfo.findA || rightInfo.findA || (a == node);  // 左右子树找到了a，或者当前节点就是a
        boolean findB = leftInfo.findB || rightInfo.findB || (b == node);  // 左右子树找到了b，或者当前节点就是b
        // lowestAncestor有2种可能：还不存在、存在；
        // 存在有2种可能性：当前节点、某子节点（左子树、右子树）
        TreeNode ancestor = (!findA || !findB) ? null : // 1.a或b没找全，自然也不会找到公共祖先；
                (  // 2.已经找到了a和b，那lowest祖先可能在左子树、右子树、或者就是当前节点。
                        leftInfo.ancestor != null ? leftInfo.ancestor :
                                rightInfo.ancestor != null ? rightInfo.ancestor :
                                        node);
        return new Info(ancestor, findA, findB);
    }





    // 给定一颗二叉树的头节点head，和另两个节点a和b，返回a和b的最低公共祖先。
    // 使用容器
    static public TreeNode lowestAncestor2(TreeNode head, TreeNode n1, TreeNode n2) {
        if (head == null) {
            return null;
        }
        // 先整理出每个节点和父节点的映射
        HashMap<TreeNode, TreeNode> parentMap = new HashMap<>();
        parentMap.put(head, null);
        fillParentMap(head, parentMap);
        // 再整理n1节点全部祖先集和
        HashSet<TreeNode> n1AncestorSet = new HashSet<>();
        TreeNode cur = n1;
        while (cur != null) {
            n1AncestorSet.add(cur);
            cur = parentMap.get(cur);
        }
        // 最后遍历n2节点祖先依次判断是否存在于n1祖先集合中
        cur = n2;
        while (!n1AncestorSet.contains(cur)) {
            cur = parentMap.get(cur);
        }
        return cur;
    }
    // 递归填充父子映射关系
    static private void fillParentMap(TreeNode head, HashMap<TreeNode, TreeNode> parentMap) {
        if (head.left != null) {
            parentMap.put(head.left, head);
            fillParentMap(head.left, parentMap);
        }
        if (head.right != null) {
            parentMap.put(head.right, head);
            fillParentMap(head.right, parentMap);
        }
    }





    static public void main(String[] args) {
        int testTimes = 1000000;
        int maxHeight = 4;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            TreeNode[] random = GenerateRandomBT.pickRandom(head, 2);
            TreeNode ans1 = lowestAncestor(head, random[0], random[1]);
            TreeNode ans2 = lowestAncestor2(head, random[0], random[1]);
            if (ans1 != ans2) {
                System.out.printf("%d Oops!  ans1=%s, ans2=%s\n", i, ans1, ans2);
            }
        }
    }
}
