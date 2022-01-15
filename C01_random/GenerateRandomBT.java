package C01_random;

import java.util.ArrayList;

public class GenerateRandomBT {
    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(int val) {value = val; }
    }
    
    // for test
    public static TreeNode generateRandomBT(int maxHeight, int maxValue) {
        return generate(1, maxHeight, maxValue);
    }

    private static TreeNode generate(int height, int maxHeight, int maxValue) {
        if (height > maxHeight || Math.random() < 0.5) {
            return null;
        }
        TreeNode head = new TreeNode((int) (Math.random() * maxValue));
        head.left = generate(height + 1, maxHeight, maxValue);
        head.right = generate(height + 1, maxHeight, maxValue);
        return head;
    }

    // for test
    public static TreeNode[] pickRandom(TreeNode head, int num) {
        if (head == null)
            return new TreeNode[] {null, null};
        TreeNode[] random = new TreeNode[num];
        ArrayList<TreeNode> array = new ArrayList<>();
        fillPreList(head, array);
        for (int n = array.size(), i = 0; i < num; i++) {
            random[i] = array.get((int)(Math.random() * n));
        }
        return random;
    }

    private static void fillPreList(TreeNode head, ArrayList<TreeNode> array) {
        if (head == null)
            return;
        array.add(head);
        fillPreList(head.left, array);
        fillPreList(head.right, array);
    }

    // for test
    public static boolean isSameValueStructure(TreeNode head1, TreeNode head2) {
        if (head1 == null && head2 != null) {
            return false;
        }
        if (head1 != null && head2 == null) {
            return false;
        }
        if (head1 == null && head2 == null) {
            return true;
        }
        if (head1.value != head2.value) {
            return false;
        }
        return isSameValueStructure(head1.left, head2.left) && isSameValueStructure(head1.right, head2.right);
    }

    // for test
    public static void printTree(TreeNode head) {
        System.out.println("Binary Tree:");
        printInOrder(head, 0, "H", 17);
        System.out.println();
    }

    private static void printInOrder(TreeNode head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printInOrder(head.right, height + 1, "v", len);
        String val = to + head.value + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printInOrder(head.left, height + 1, "^", len);
    }

    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }
}
