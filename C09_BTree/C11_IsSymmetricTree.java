package C09_BTree;

public class C11_IsSymmetricTree {
    static public void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        TreeNode n1_l = new TreeNode(2);
        TreeNode n1_r = new TreeNode(2);
        TreeNode n1_l_l = new TreeNode(3);
        TreeNode n1_r_r = new TreeNode(3);
        n1.left = n1_l;
        n1.right = n1_r;
        n1_l.left = n1_l_l;
        n1_r.right = n1_r_r;

        System.out.println(isSymmetric(n1));
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            value = val;
        }
    }

    static public boolean isSymmetric(TreeNode root) {
        return isMirror(root, root);
    }

    static public boolean isMirror(TreeNode h1, TreeNode h2) {
        if (h1 == null ^ h2 == null) {
            return false;
        }
        if (h1 == null && h2 == null) {
            return true;
        }
        return h1.value == h2.value && isMirror(h1.left, h2.right) && isMirror(h1.right, h2.left);
    }
}
