package C08_BTree;

public class C21_PathSum1 {
    static public void main(String[] args) {
        TreeNode n1 = new TreeNode(0);
        TreeNode n1_l = new TreeNode(2);
        TreeNode n1_r = new TreeNode(3);
        TreeNode n1_l_l = new TreeNode(4);
        TreeNode n1_l_r = new TreeNode(5);
        TreeNode n1_r_l = new TreeNode(3);
        TreeNode n1_r_r = new TreeNode(6);
        n1.left = n1_l;
        n1.right = n1_r;
        n1_l.left = n1_l_l;
        n1_l.right = n1_l_r;
        n1_r.left = n1_r_l;
        n1_r.right = n1_r_r;

        System.out.println(hasPathSum(n1, 6));
        System.out.println(hasPathSum1(n1, 6));
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            value = val;
        }
    }

    static public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) {
            return false;
        }
        return process(root, sum);
    }
    // 目标值拆成多个小值
    static private boolean process(TreeNode root, int rest) {
        // 叶节点
        if (root.left == null && root.right == null) {
            return root.value == rest;  // 叶节点值是否等于目标小值
        }
        // 非叶节点
        boolean ans;
        ans = root.left != null ? process(root.left, rest - root.value) : false;
        ans |= root.right != null ? process(root.right, rest - root.value) : false;
        return ans;
    }





    static public boolean found = false;
    static public boolean hasPathSum1(TreeNode root, int sum) {
        if (root == null) {
            return false;
        }
        found = false;
        process1(root, 0, sum);
        return found;
    }
    // 累加和去跟目标值比对
    static private void process1(TreeNode root, int curSum, int sum) {
        // 叶节点
        if (root.left == null && root.right == null) {
            if (root.value + curSum == sum) {  // 累加和是否等于目标值
                found = true;
            }
            return;
        }
        // 非叶节点
        curSum += root.value;
        if (root.left != null) {
            process1(root.left, curSum, sum);
        }
        if (root.right != null) {
            process1(root.right, curSum, sum);
        }
    }
}
