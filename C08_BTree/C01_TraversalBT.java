package C08_BTree;

import java.util.Stack;

public class C01_TraversalBT {
    static public void main(String[] args) {
        TreeNode n1 = new TreeNode(1);
        TreeNode n1_l = new TreeNode(2);
        TreeNode n1_r = new TreeNode(3);
        TreeNode n2 = new TreeNode(4);
        TreeNode n3 = new TreeNode(5);
        TreeNode n2_l = new TreeNode(6);
        TreeNode n2_r = new TreeNode(7);
        TreeNode n3_l = new TreeNode(8);
        TreeNode n3_r = new TreeNode(9);
        n1.left = n1_l;
        n1.right = n1_r;
        n1_l.left = n2;
        n1_l.right = n3;
        n2.left = n2_l;
        n2.right = n2_r;
        n3.left = n3_l;
        n3.right = n3_r;

        pre2(n1);
        pre(n1);
        System.out.println("\n================");
        in2(n1);
        in(n1);
        System.out.println("\n================");
        post2(n1);
        post(n1);
    }

    static public class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            value = val;
        }
    }

    static public void f(TreeNode head) {
        if (head == null) {
            return;
        }
        // System.out.println(head.value);  先序遍历
        f(head.left);
        // System.out.println(head.value);  中序遍历
        f(head.right);
        // System.out.println(head.value);  后序遍历
    }

    // 递归版使用进程栈，容量有限，数据量大容易导致栈溢出！
    // 非递归版使用系统栈，容量大，支持更多层次的处理。

    // ===================递归版本===================
    // 先序遍历（头左右）
    static public void pre(TreeNode head) {
        if (head == null) {
            return;
        }
        System.out.printf("%s ", head.value);
        pre(head.left);
        pre(head.right);
    }

    // 中序遍历（头左右）
    static public void in(TreeNode head) {
        if (head == null) {
            return;
        }
        in(head.left);
        System.out.printf("%s ", head.value);
        in(head.right);
    }

    // 后序遍历（左右头）
    static public void post(TreeNode head) {
        if (head == null) {
            return;
        }
        post(head.left);
        post(head.right);
        System.out.printf("%s ", head.value);
    }



    // ===================非递归版本===================
    static public void pre2(TreeNode head) {
        if (head != null) {
            Stack<TreeNode> stack = new Stack<>();
            stack.push(head);
            while (!stack.isEmpty()) {
                head = stack.pop();
                System.out.printf("%s ", head.value);
                // 栈先进后出，先压右后压左！
                if (head.right != null)
                    stack.push(head.right);
                if (head.left != null)
                    stack.push(head.left);
            }
            System.out.println();
        }
    }

    // 先序的逆序可以得到后序
    static public void post2(TreeNode head) {
        if (head != null) {
            Stack<TreeNode> stackHRL = new Stack<>();  // 先序结果
            Stack<TreeNode> stackLRH = new Stack<>();  // 后序结果
            TreeNode cur;
            stackHRL.push(head);
            while (!stackHRL.isEmpty()) {
                cur = stackHRL.pop();  // 头 右 左
                stackLRH.push(cur);
                if (cur.left != null)
                    stackHRL.push(cur.left);
                if (cur.right != null)
                    stackHRL.push(cur.right);
            }
            // 左 右 头
            while (!stackLRH.isEmpty()) {
                System.out.printf("%s ", stackLRH.pop().value);
            }
            System.out.println();
        }
    }

    // 模拟中序遍历的递归调用栈
    static public void in2(TreeNode head) {
        if (head != null) {
            TreeNode cur = head;
            Stack<TreeNode> stack = new Stack<>();
            while (cur != null || !stack.isEmpty()) {
                if (cur != null) {  // 先把当前树的所有最左子节点都压栈
                    stack.push(cur);
                    cur = cur.left;
                } else {  // 已压完当前树的最后一个最左子节点，开始倒序弹出打印；cur没有右子节点的话下轮直接弹出cur的父节点，cur有右子节点则会继续压入所有的最左子节点。
                    cur = stack.pop();
                    System.out.printf("%s ", cur.value);
                    cur = cur.right;
                }
            }
            System.out.println();
        }
    }
}