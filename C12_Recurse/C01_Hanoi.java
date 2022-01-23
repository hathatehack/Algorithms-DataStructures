package C12_Recurse;

import java.util.Stack;

// 有左中右三个汉诺塔，左边有n个从大到小叠放的圆盘，且大盘只能在小盘下面，打印n层汉诺塔从最左边移动到最右边的全部步骤。
public class C01_Hanoi {
    public static void main(String[] args) {
        hanuoi(3);
        System.out.println("===========================");
        hanoi2(3);
        System.out.println("===========================");
        hanoi3(3);
    }

    // 递归版本
    public static void hanuoi(int n) {
        move(n, "left", "right", "mid");
    }
    // 整体上看分3步，先移动topK个圆盘到无关塔，再把底下的圆盘移到目标塔，最后再把topK个圆盘移到目标塔。
    private static void move(int n, String from, String to, String help) {
        if (n == 1) {
            System.out.printf("Move 1 from %s to %s.\n", from, to);
            return;
        }
        int topK = n - 1;
        move(topK, from, help, to);
        System.out.printf("Move %d from %s to %s.\n", n, from, to);
        move(topK, help, to, from);
    }


    // 迭代版本
    private static class Record {
        int n;
        boolean canMove;
        String from, to, help;
        public Record(int _n, boolean _canMove, String _from, String _to, String _help) {
            n = _n; canMove = _canMove; from = _from; to = _to; help = _help;
        }
    }
    public static void hanoi3(int n) {
        Stack<Record> stack = new Stack<>();
        stack.push(new Record(n, false, "left", "right", "mid"));
        while (!stack.isEmpty()) {
            Record cur = stack.pop();
            if (cur.n == 1) {
                System.out.printf("Move 1 from %s to %s.\n", cur.from, cur.to);
                if (!stack.isEmpty())
                    stack.peek().canMove = true;
            } else {
                if (!cur.canMove) {
                    stack.push(cur);
                    stack.push(new Record(cur.n - 1, false, cur.from, cur.help, cur.to));
                } else {
                    System.out.printf("Move %d from %s to %s.\n", cur.n, cur.from, cur.to);
                    stack.push(new Record(cur.n - 1, false, cur.help, cur.to, cur.from));
                }
            }
        }
    }



    // 递归版本
    public static void hanoi2(int n) {
        left2right(n);
    }
    // 整体上看分3步，先移动topK个圆盘到无关塔，再把底下的圆盘移到目标塔，最后再把topK个圆盘移到目标塔。
    private static void left2right(int n) {
        if (n == 1) {
            System.out.println("Move 1 from left to right.");
            return;
        }
        int topK = n - 1;
        left2mid(topK);
        System.out.printf("Move %d from left to right.\n", n);
        mid2right(topK);
    }

    private static void left2mid(int n) {
        if (n == 1) {
            System.out.println("Move 1 from left to mid.");
            return;
        }
        int topK = n - 1;
        left2right(topK);
        System.out.printf("Move %d from left to mid.\n", n);
        right2mid(topK);
    }

    private static void mid2right(int n) {
        if (n == 1) {
            System.out.println("Move 1 from mid to right.");
            return;
        }
        int topK = n - 1;
        mid2left(topK);
        System.out.printf("Move %d from mid to right.\n", n);
        left2right(topK);
    }

    private static void mid2left(int n) {
        if (n == 1) {
            System.out.println("Move 1 from mid to left.");
            return;
        }
        int topK = n - 1;
        mid2right(topK);
        System.out.printf("Move %d from mid to left\n", n);
        right2left(topK);
    }

    private static void right2left(int n) {
        if (n == 1) {
            System.out.println("Move 1 from right to left.");
            return;
        }
        int topK = n - 1;
        right2mid(topK);
        System.out.printf("Move %d from right to left.\n", n);
        mid2left(topK);
    }

    private static void right2mid(int n) {
        if (n == 1) {
            System.out.println("Move 1 from right to mid.");
            return;
        }
        int topK = n - 1;
        right2left(topK);
        System.out.printf("Move %d from right to mid.\n", n);
        left2mid(topK);
    }
}
