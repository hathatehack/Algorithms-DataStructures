package C08_BTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class C21_PathSum2 {
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

        List<List<Integer>> paths = hasPathSum1(n1, 6);
        for (List<Integer> p : paths) {
            System.out.println(Arrays.toString(p.toArray()));
        }

        System.out.println("==================================");

        paths = hasPathSum2(n1, 6);
        for (List<Integer> p : paths) {
            System.out.println(Arrays.toString(p.toArray()));
        }
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            value = val;
        }
    }

    static public List<List<Integer>> hasPathSum1(TreeNode root, int sum) {
        List<List<Integer>> paths = new ArrayList<>();
        if (root == null) {
            return paths;
        }
        ArrayList<Integer> path = new ArrayList<>();
        process1(root, sum, path, paths);
        return paths;
    }
    // 目标值拆成多个小值
    static private void process1(TreeNode root, int rest, ArrayList<Integer> path, List<List<Integer>> paths) {
        path.add(root.value);
        // 叶节点
        if (root.left == null && root.right == null) {
            if (root.value == rest) {  // 叶节点值是否等于目标小值
                paths.add(copy(path));
            }
        } else {
            // 非叶节点
            if (root.left != null) {
                process1(root.left, rest - root.value, path, paths);
            }
            if (root.right != null) {
                process1(root.right, rest - root.value, path, paths);
            }
        }
        path.remove(path.size() - 1);
    }

    static private List<Integer> copy(List<Integer> path) {
        List<Integer> cp = new ArrayList<>();
        for (Integer o : path) {
            cp.add(o);
        }
        return cp;
    }






    static public List<List<Integer>> hasPathSum2(TreeNode root, int sum) {
        List<List<Integer>> paths = new ArrayList<>();
        if (root == null) {
            return paths;
        }
        List<Integer> path = new ArrayList<>();
        process2(root, 0, sum, path, paths);
        return paths;
    }
    // 累加和去跟目标值比对
    static private void process2(TreeNode root, int curSum, int sum, List<Integer> path, List<List<Integer>> paths) {
        path.add(root.value);
        // 叶节点
        if (root.left == null && root.right == null) {
            if (root.value + curSum == sum) {  // 累加和是否等于目标值
                paths.add(copy(path));
            }
        } else {
            // 非叶节点
            curSum += root.value;
            if (root.left != null) {
                process2(root.left, curSum, sum, path, paths);
            }
            if (root.right != null) {
                process2(root.right, curSum, sum, path, paths);
            }
        }
        path.remove(path.size() - 1);
    }
}
