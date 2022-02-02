package C09_BTree;

import C01_random.GenerateRandomBT;
import C01_random.GenerateRandomBT.TreeNode;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Stack;

public class C01_TraversalBT {
    // BT遍历每个节点会被进入3次，在不同的时机打印value分别称做先序遍历、中序遍历、后序遍历。
    static public void t(TreeNode head) {
        if (head == null) {
            return;
        }
        // System.out.println(head.value);  先序遍历
        t(head.left);
        // System.out.println(head.value);  中序遍历
        t(head.right);
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
                if (cur != null) {  // 先把当前树的所有最左子节点（包括null节点）都压栈
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
    static public void in2_1(TreeNode head) {
        if (head != null) {
            TreeNode cur;
            Stack<TreeNode> stack = new Stack<>();
            stack.push(head);  // 初始节点
            while (!stack.isEmpty()) {
                // DFS，cur子树全部最左节点（不包括null节点）都压栈。
                cur = stack.pop();
                while (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                }
                // 最左节点压完了，倒序弹出打印，直到某节点存在右子节点则将该右子节点压栈并暂停打印，进入下轮DFS。
                while (!stack.isEmpty()) {
                    cur = stack.pop();
                    System.out.printf("%s ", cur.value);
                    if (cur.right != null) {  // 存在右子节点则将该右子节点压栈并暂停打印
                        stack.push(cur.right);
                        break;  // 暂停打印，进行cur.right节点的DFS
                    }
                }
            }
            System.out.println();
        }
    }

    static private class Record {
        public TreeNode node;
        public int entryTimes;
        public Record(TreeNode _node, int _entryTimes) {
            node = _node;
            entryTimes = _entryTimes;
        }
    }
    static public void in3(TreeNode head) {
        if (head != null) {
            Record cur;
            Stack<Record> stack = new Stack<>();
            stack.push(new Record(head, 1));  // 初始节点
            while (!stack.isEmpty()) {
                cur = stack.pop();
                if (cur.entryTimes == 1) {  // DFS，cur子树全部最左节点（不包括null节点）若是第一次进入则压栈。
                    cur.entryTimes++;
                    stack.push(cur);
                    if (cur.node.left != null) {
                        stack.push(new Record(cur.node.left, 1));
                    }
                } else if (cur.entryTimes == 2) {  // 最左节点压完了，打印回退到的节点，直到某节点存在右子节点则将该右子节点压栈进入下轮DFS。
                    System.out.printf("%s ", cur.node.value);
//                    cur.entryTimes++;
//                    stack.push(cur);
//                } else {  // cur.entryTimes == 3
//                    if (cur.node.right != null)
//                        stack.push(new Record(cur.node.right, 1));
//                }
                    if (cur.node.right != null) {
                        stack.push(new Record(cur.node.right, 1));
                    }
                }
            }
            System.out.println();
        }
    }



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
        n1.left = n1_l; n1.right = n1_r;
        n1_l.left = n2; n1_l.right = n3;
        n2.left = n2_l; n2.right = n2_r;
        n3.left = n3_l; n3.right = n3_r;

        System.out.println("前序遍历");
        pre2(n1); pre(n1);
        System.out.println("\n中序遍历");
        in2(n1); in2_1(n1); in3(n1); in(n1);
        System.out.println("\n后序遍历");
        post2(n1); post(n1);
        System.out.println();

        System.out.println("\n--------------------------");
        int testTimes = 100000;
        int maxHeight = 5;
        int maxValue = 100;
        // https://vimsky.com/examples/usage/java-program-to-convert-outputstream-to-string.html
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        PrintStream systemOut = System.out;
        System.setOut(new PrintStream(b));  // 重定向out
        systemOut.println("先序遍历测试...");
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            pre(head); String ans1 = b.toString(); b.reset();
            pre2(head); String ans2 = b.toString().replace("\r\n", ""); b.reset();
            if (!ans1.equals(ans2)) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%s, ans2=%s\n", ans1, ans2);
            }
        }
        systemOut.println("中序遍历测试...");
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            in(head); String ans1 = b.toString(); b.reset();
            in2(head); String ans2 = b.toString().replace("\r\n", ""); b.reset();
            in2_1(head); String ans2_1 = b.toString().replace("\r\n", ""); b.reset();
            in3(head); String ans3 = b.toString().replace("\r\n", ""); b.reset();
            if (!ans1.equals(ans2) || !ans1.equals(ans2_1) || !ans1.equals(ans3)) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%s, ans2=%s, ans2_1=%s, ans3=%s\n", ans1, ans2, ans2_1, ans3);
            }
        }
        systemOut.println("后序遍历测试...");
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBT.generateRandomBT(maxHeight, maxValue);
            post(head); String ans1 = b.toString(); b.reset();
            post2(head); String ans2 = b.toString().replace("\r\n", ""); b.reset();
            if (!ans1.equals(ans2)) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%s, ans2=%s\n", ans1, ans2);
            }
        }
    }
}