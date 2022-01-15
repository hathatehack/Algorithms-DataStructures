package C08_BTree;

import C01_random.GenerateRandomBT.TreeNode;

public class C18_MaxDistance {
    static public class Info {
        int maxDistance;
        int height;
        public Info(int maxD, int h) { maxDistance = maxD; height = h; }
    }

    static public int maxDistance2(TreeNode head) {
        return process(head).maxDistance;
    }
    static private Info process(TreeNode node) {
        if (node == null) {
            return new Info(0, 0);
        }
        Info leftInfo = process(node.left);
        Info rightInfo = process(node.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        int maxDistance = Math.max(
                Math.max(leftInfo.maxDistance, rightInfo.maxDistance),  // 不经过当前node节点，例如node无右节点等等情况。
                leftInfo.height + rightInfo.height + 1);  // 经过当前node节点
        return new Info(maxDistance, height);
    }
}
