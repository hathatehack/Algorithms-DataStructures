package C08_BTree;

public class C10_IsSameTree {
    static public void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        TreeNode n2 = new TreeNode(2);

        System.out.println(isSameTree(null, null));
        System.out.println(isSameTree(n1, null));
        System.out.println(isSameTree(n1, n2));
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            value = val;
        }
    }

    static public boolean isSameTree(TreeNode n1, TreeNode n2) {
        if (n1 == null ^ n2 == null) {
            return false;
        }
        if (n1 == null && n2 == null) {
            return true;
        }
         return n1.value == n1.value && isSameTree(n1.left, n2.left) && isSameTree(n1.right, n2.right);
    }
}
