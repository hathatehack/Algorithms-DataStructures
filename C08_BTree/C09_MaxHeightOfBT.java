package C08_BTree;

public class C09_MaxHeightOfBT {
    static public void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        TreeNode n1_l = new TreeNode(2);
        TreeNode n1_r = new TreeNode(2);
        TreeNode n1_l_l = new TreeNode(3);
        n1.left = n1_l;
        n1.right = n1_r;
        n1_l.left = n1_l_l;

        System.out.println(maxHeight(n1));
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            value = val;
        }
    }

    static public int maxHeight(TreeNode root) {
        if (root == null)
            return 0;

        return Math.max(maxHeight(root.left), maxHeight(root.right)) + 1;
    }
}
