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
        if (head.left != null)
            preS(head.left, list);
        if (head.right != null)
            preS(head.right, list);
    }
    // KMP
    private static int indexOf(ArrayList<Integer> list, ArrayList<Integer> match) {
        if (list == null || match == null || list.size() < 1 || list.size() < match.size()) {
            return -1;
        }
        int listSize = list.size();
        int matchSize = match.size();
        int[] next = getNextArray(match);
        int lIndex = 0;
        int mIndex = 0;
        while (lIndex < listSize && mIndex < matchSize) {
            if (list.get(lIndex) == match.get(mIndex) ||
                    list.get(lIndex).equals(match.get(mIndex))) {
                lIndex++;
                mIndex++;
            } else if (next[mIndex] > -1) {
                mIndex = next[mIndex];
            } else {
                lIndex++;
            }
        }
        return mIndex == matchSize ? lIndex - mIndex : -1;
    }
    private static int[] getNextArray(ArrayList<Integer> list) {
        int listSize = list.size();
        if (listSize == 1) {
            return new int[] {-1};
        }
        int[] next = new int[listSize];
        next[0] = -1;
        next[1] = 0;
        int matchedIndex = 0;
        int i = 2;
        while (i < listSize) {
            if (list.get(i) == list.get(matchedIndex) ||
                    list.get(i).equals(list.get(matchedIndex))) {
                next[i++] = ++matchedIndex;
            } else if (matchedIndex > 0) {
                matchedIndex = next[matchedIndex];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }




    public static boolean isSubtree2(TreeNode tree, TreeNode subtree) {
        if (subtree == null) {  // 空树必定是子树
            return true;
        }
        if (tree == null) {
            return false;
        }
        if (isSameTree(tree, subtree)) {
            return true;
        }
        return isSubtree2(tree.left, subtree) &&
                isSubtree2(tree.right, subtree);
    }
    private static boolean isSameTree(TreeNode head1, TreeNode head2) {
        if (head1 == null && head2 == null) {
            return true;
        }
        if (head1 == null && head2 != null ||
                head1 != null && head2 == null ||
                head1.value != head2.value) {
            return false;
        }
        return isSameTree(head1.left, head2.left) &&
                isSameTree(head1.right, head2.right);
    }




    public static void main(String[] args) {
        TreeNode tree = new TreeNode(0);
        tree.left = new TreeNode(1);
        tree.left.left = new TreeNode(2);
        tree.left.right = new TreeNode(3);
        tree.right = new TreeNode(4);
        tree.right.right = new TreeNode(5);

        TreeNode subtree = new TreeNode(0);
        subtree.left = new TreeNode(1);
        subtree.left.left = new TreeNode(2);
        subtree.left.right = new TreeNode(3);
//        subtree.right = new TreeNode(4);
//        subtree.right.right = new TreeNode(5);

        boolean ans1 = isSubtree(tree, subtree);
        boolean ans2 = isSubtree2(tree, subtree);
        System.out.printf("ans1=%b, ans2=%b", ans1, ans2);


//        int testTimes = 10;
//        int treeMaxHeight = 8;
//        int subtreeMaxHeight = 4;
//        int maxValue = 5;
//        for (int i = 0; i < testTimes; i++) {
//            TreeNode tree = GenerateRandomBT.generateRandomBT(treeMaxHeight, maxValue);
//            TreeNode subtree = GenerateRandomBT.generateRandomBT(subtreeMaxHeight, maxValue);
//            boolean ans1 = isSubtree(tree, subtree);
//            boolean ans2 = isSubtree2(tree, subtree);
//            if (ans1 != ans2) {
//                System.out.printf("Oops! i=%d\n", i);
//                System.out.printf("ans1=%b, ans2=%b\n", ans1, ans2);
//            }
//        }
    }
}
