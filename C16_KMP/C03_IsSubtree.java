package C16_KMP;

import C01_random.GenerateRandomBT;
import C01_random.GenerateRandomBT.TreeNode;
import java.util.ArrayList;

public class C03_IsSubtree {
    public static boolean isSubtree(TreeNode tree, TreeNode subtree) {
        if (subtree == null) {  // 空树必定是子树
            return true;
        }
        if (tree == null) {
            return false;
        }
        return indexOf(preSerialize(tree), preSerialize(subtree)) != -1;
    }
    // 先序序列化
    private static ArrayList<Integer> preSerialize(TreeNode head) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        preS(head, list);
        return list;
    }
    private static void preS(TreeNode head, ArrayList<Integer> list) {
        if (head == null) {
            list.add(null);
            return;
        }
        list.add(head.value);
        preS(head.left, list);
        preS(head.right, list);
    }
    // KMP，时间复杂度O(N)
    private static int indexOf(ArrayList<Integer> list, ArrayList<Integer> match) {
        if (list == null || match == null || list.size() < 1 || list.size() < match.size()) {
            return -1;
        }
        int listSize = list.size();
        int matchSize = match.size();
        int[] preNext = generatePreNextArray(match);
        int lIndex = 0;
        int mIndex = 0;
        while (lIndex < listSize && mIndex < matchSize) {
            if (isEqual(list.get(lIndex), match.get(mIndex))) {
                lIndex++;
                mIndex++;
            } else if (preNext[mIndex] > -1) {
                mIndex = preNext[mIndex];
            } else {
                lIndex++;
            }
        }
        return mIndex == matchSize ? lIndex - mIndex : -1;
    }
    private static int[] generatePreNextArray(ArrayList<Integer> list) {
        int listSize = list.size();
        if (listSize == 1) {
            return new int[] {-1};
        }
        int[] preNext = new int[listSize];
        preNext[0] = -1;
        preNext[1] = 0;
        int matchIndex = 0;
        int i = 2;
        while (i < listSize) {
            if (isEqual(list.get(i - 1), list.get(matchIndex))) {
                preNext[i++] = ++matchIndex;
            } else if (matchIndex > 0) {
                matchIndex = preNext[matchIndex];
            } else {
                preNext[i++] = 0;
            }
        }
        return preNext;
    }
    private static boolean isEqual(Integer o1, Integer o2) {
        if (o1 == null && o2 == null)
            return true;
        if (o1 == null || o2 == null)
            return false;
        return o1.equals(o2);
    }




    // 暴力递归
    public static boolean isSubtree2(TreeNode tree, TreeNode subtree) {
        if (subtree == null) {  // 空树必定是子树
            return true;
        }
        if (tree == null) {
            return false;
        }
        // 两棵树是否完全相同
        if (isSameTree(tree, subtree)) {
            return true;
        }
        // 左树或者右树是否与subtree相同
        return isSubtree2(tree.left, subtree) ||
                isSubtree2(tree.right, subtree);
    }
    private static boolean isSameTree(TreeNode head1, TreeNode head2) {
        if (head1 == null && head2 == null) {  // 两树都为空，则相等
            return true;
        }
        if (head1 == null || head2 == null) {  // 有一颗树为空，则不相等
            return false;
        }
        // 比较两棵树的头节点值、左节点值、右节点值
        return head1.value == head2.value &&
                isSameTree(head1.left, head2.left) &&
                isSameTree(head1.right, head2.right);
    }




    public static void main(String[] args) {
        TreeNode tree = new TreeNode(0);
        tree.left = new TreeNode(1);
        TreeNode subtree = new TreeNode(1);
        System.out.printf("ans1=%b, ans2=%b\n", isSubtree(tree, subtree), isSubtree2(tree, subtree));

        tree.right = new TreeNode(1);
        subtree.right = new TreeNode(1);
        System.out.printf("ans1=%b, ans2=%b\n", isSubtree(tree, subtree), isSubtree2(tree, subtree));

        System.out.println("======================================");
        int testTimes = 1000000;
        int treeMaxHeight = 8;//3;
        int subtreeMaxHeight = 4;//2;
        int maxValue = 5;
        for (int i = 0; i < testTimes; i++) {
            tree = GenerateRandomBT.generateRandomBT(treeMaxHeight, maxValue);
            subtree = GenerateRandomBT.generateRandomBT(subtreeMaxHeight, maxValue);
            boolean ans1 = isSubtree(tree, subtree);
            boolean ans2 = isSubtree2(tree, subtree);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%b, ans2=%b\n", ans1, ans2);
                GenerateRandomBT.printTree(tree);
                GenerateRandomBT.printTree(subtree);
            }
        }
    }
}
