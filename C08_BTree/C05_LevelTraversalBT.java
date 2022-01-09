package C08_BTree;

import java.util.LinkedList;
import java.util.Queue;

public class C05_LevelTraversalBT {
    public static void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        TreeNode n1_l = new TreeNode(2);
        TreeNode n1_r = new TreeNode(3);
        TreeNode n2 = new TreeNode(4);
        TreeNode n3 = new TreeNode(5);
        n1.left = n1_l;
        n1.right = n1_r;
        n1_l.left = n2;
        n1_l.right = n3;

        levelTraversalBT(n1);
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(int val) {value = val; }
    }

    // 从上到下、从左到右遍历
    static public void levelTraversalBT(TreeNode head) {
        if (head != null) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(head);
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.poll();
                    System.out.printf("%s ", node.value);
                    if (node.left != null)
                        queue.add(node.left);
                    if (node.right != null)
                        queue.add(node.right);
                }
                System.out.println();
            }
        }
    }
}
