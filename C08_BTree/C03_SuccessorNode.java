package C08_BTree;

import java.util.HashMap;

public class C03_SuccessorNode {
    public static void main(String[] args) {
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
        n1_l.parent = n1_r.parent = n1;
        n2.parent = n3.parent = n1_l;
        n2_l.parent = n2_r.parent = n2;
        n3_l.parent = n3_r.parent = n3;
        HashMap<TreeNode, String> map = new HashMap<>();
        map.put(n1, "n1"); map.put(n1_l, "n1_l"); map.put(n1_r, "n1_r");  map.put(n2, "n2"); map.put(n3, "n3"); map.put(n2_l, "n2_l"); map.put(n2_r, "n2_r"); map.put(n3_l, "n3_l"); map.put(n3_r, "n3_r");

        System.out.printf("%s successor: %s\n", "n1_r", getSuccessorNode(n1_r));
        System.out.printf("%s successor: %s\n", "n1_l", map.get(getSuccessorNode(n1_l)));
        System.out.printf("%s successor: %s\n", "n3", map.get(getSuccessorNode(n3)));
        System.out.printf("%s successor: %s\n", "n3_r", map.get(getSuccessorNode(n3_r)));
        System.out.printf("%s successor: %s\n", "n3_l", map.get(getSuccessorNode(n3_l)));
        System.out.printf("%s successor: %s\n", "n2_r", map.get(getSuccessorNode(n2_r)));
        System.out.printf("%s successor: %s\n", "n2_l", map.get(getSuccessorNode(n2_l)));
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;
        public TreeNode parent;

        public TreeNode(int val) {
            value = val;
        }
    }

    // 获取当前节点在中序遍历的下一节点即successor。
    static public TreeNode getSuccessorNode(TreeNode node) {
        if (node == null) {
            return null;
        }
        if (node.right != null) {  // 存在右子节点，successor为右子树中的最左子节点。
            return getLeftmostChild(node.right);
        } else {
            // 不存在右子树，
            // 可能当前节点是整棵树的最右子节点了，即没有后继节点了；
            // 可能当前节点是某个左父亲的最右子节点，这个左父亲就是successor。
            TreeNode parent = node.parent;
            while (parent != null && parent.right == node) {  // 如果遍历到树顶都没找到左父亲，意味着最右子节点不存在successor，否则返回左父亲节点。
                node = parent;
                parent = node.parent;
            }
            return parent;
        }
    }

    static private TreeNode getLeftmostChild(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
}
