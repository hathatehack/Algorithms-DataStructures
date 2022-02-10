package C13_DP.ConstraintModel;

import java.util.HashMap;

public class C02_NQueens {
    // 给定一个整数N，表示在N*N的棋盘上摆放N个皇后，要求任何两个皇后在不同行、不同列、不同斜线上，已知N=1有1种，N=2或3有0种，请返回N皇后的摆法有多少种。
    // 暴力递归，时间复杂度O(N^N)，常数时间大。
    public static int nQueens(int N) {
        if (N < 1) {
            return 0;
        }
        return process(0, new int[N], N);
    }
    // row为当前要摆放皇后的行，colRecord记录了前面行的摆放情况，要把N行都摆完，返回全部摆法。
    private static int process(int row, int[] colRecord, int N) {
        // base case
        if (row == N) {  // 全部行都处理完了，得到1种摆法。
            return 1;
        }
        // 尝试每一列能否摆放当前行的皇后
        int p = 0;
        for (int col = 0; col < N; col++) {  // 尝试每一列
            if (canPut(row, col, colRecord)) {
                colRecord[row] = col;  // 记录当前行皇后摆放的列
                p += process(row + 1, colRecord, N);
            }
        }
        return p;
    }
    // 检查能否在row行col列摆放皇后
    private static boolean canPut(int row, int col, int[] colRecord) {
        for (int r = 0; r < row; r++) {  // 检查位置(row,col)是否与0~row-1行的皇后摆放位置有冲突
            if (col == colRecord[r] ||  // 要求不同列，
                    Math.abs(row - r) == Math.abs(col - colRecord[r])) { // 不同斜线
                return false;
            }
        }
        return true;
    }



    // 暴力递归+位运算优化，时间复杂度O(N^N)，常数时间小。
    public static int nQueens_bit(int N) {
        if (N < 1) {
            return 0;
        }
        if (N > 32) {  // int运算最多支持32bit
            return 0;
        }
        return process_bit(0, 0, 0, N == 32 ? -1 : ((1 << N) - 1));
    }
    // colLimit记录了前面所有行的摆放情况，left45DegLimit表示前面所有行皇后所在左斜线， right45DegLimit表示前面所有行皇后所在右斜线，要把所有行都摆完，返回全部摆法。
    private static int process_bit(int colLimit, int left45DegLimit, int right45DegLimit, int full) {
        // base case
        if (colLimit == full) {  // 全部行都处理完了，得到1种摆法。
            return 1;
        }
        // 尝试空置的列能否摆放当前行的皇后
        int p = 0;
        int cols = full & (~(colLimit | left45DegLimit | right45DegLimit));  // 位运算求所有空置列
        int mostRight1;
        for (; cols != 0; cols ^= mostRight1) {  // 剪枝，只尝试空置的列！
            mostRight1 = cols & (~cols + 1);  // 位运算取最右的空置列
            p += process_bit(colLimit | mostRight1,  // 记录当前行皇后摆放的列
                    (left45DegLimit | mostRight1) << 1,  // 记录当前行皇后所在左斜线
                    (right45DegLimit | mostRight1) >>> 1,  // 记录当前行皇后所在右斜线。无符号右移！
                    full);
        }
        return p;
    }

    // bit版本缓存表大小为(2^N)^3，内存占用指数级暴增，不适合使用缓存表。


    // 查看有哪些子问题被重复尝试超过指定次数
    private static int nQueens_bit_checkRepeatStatus(int N, int repeatThreshold) {
        if (N < 1 || N > 32)  // int运算最多支持32bit
            return 0;
        HashMap<String, Integer> map = new HashMap<>();
        int p = process_bit_checkRepeatStatus(0, 0, 0, N == 32 ? -1 : ((1 << N) - 1), N, map);
        map.entrySet().stream().sorted((kv1, kv2) -> kv2.getValue() - kv1.getValue()).forEach(keyVal -> {
            if (keyVal.getValue() > repeatThreshold) System.out.printf("  %s\n  重复尝试次数%d\n  -------------------------\n", keyVal.getKey(), keyVal.getValue());});
        return p;
    }
    // colLimit记录了前面所有行的摆放情况，left45DegLimit表示前面所有行皇后所在左斜线， right45DegLimit表示前面所有行皇后所在右斜线，要把所有行都摆完，返回全部摆法。
    private static int process_bit_checkRepeatStatus(int colLimit, int left45DegLimit, int right45DegLimit, int full, int N, HashMap<String, Integer> map) {
        String status = String.format("Cols:%s\n  L45°:%s\n  R45°:%s", dec2bin(colLimit, N), dec2bin(left45DegLimit & full, N), dec2bin(right45DegLimit, N));
        map.put(status, map.getOrDefault(status, 0) + 1);
        // base case
        if (colLimit == full)  // 全部行都处理完了，得到1种摆法。
            return 1;
        // 尝试空置的列能否摆放当前行的皇后
        int p = 0;
        int emptyColsBit = full & (~(colLimit | left45DegLimit | right45DegLimit));  // 位运算求所有空置列
        for (int mostRight1 = 0; emptyColsBit != 0; emptyColsBit ^= mostRight1) {  // 剪枝，只尝试空置的列！
            mostRight1 = emptyColsBit & (~emptyColsBit + 1);  // 位运算取最右的空置列
            p += process_bit_checkRepeatStatus(colLimit | mostRight1,  // 记录当前行皇后摆放的列
                    (left45DegLimit | mostRight1) << 1,  // 记录当前行皇后所在左斜线
                    (right45DegLimit | mostRight1) >>> 1,  // 记录当前行皇后所在右斜线。无符号右移！
                    full,
                    N, map);
        }
        return p;
    }
    private static String dec2bin(int dec, int len) {
        String pad = "", bin = Integer.toBinaryString(dec);
        for (int binLen = bin.length(); binLen < len; pad += "0", binLen++) {}
        return pad + bin;
    }





    public static void main(String[] args) throws InterruptedException {
        checkResult();
        compareTimeConsume();
        checkRepeatStatus(10);
    }
    // 验证结果
    private static void checkResult() {
        System.out.println("验证结果:");
        int testTimes = 13;
        for (int i = 0; i <= testTimes; i++) {
            System.out.printf("N:%d", i);
            int ans1 = nQueens(i);
            int ans2 = nQueens_bit(i);
            if (ans1 == ans2) {
                System.out.println("  OK.");
            }
            else {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d  N:%d\n", ans1, ans2, i);
                return;
            }
        }
    }
    // 耗时对比
    private static void compareTimeConsume() throws InterruptedException {
        System.out.println("\n耗时对比:");
        int N = 15;
        Thread t1 = new Thread(() -> {
            for (int n = 0; n <= N; n++) {
                long startTime = System.currentTimeMillis(), ans = nQueens(n), endTime = System.currentTimeMillis();
                System.out.printf("nQueens      N:%- 5d ans:%- 9d 用时：%dms\n", n, ans, endTime - startTime);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int n = 0; n <= N; n++) {
                long startTime = System.currentTimeMillis(), ans = nQueens_bit(n), endTime = System.currentTimeMillis();
                System.out.printf("nQueens_bit  N:%- 5d ans:%- 9d 用时：%dms\n", n, ans, endTime - startTime);
            }
        });
        t1.start(); t2.start();
        t1.join(); t2.join();
    }
    // 查看有哪些子问题被重复尝试超过指定次数
    private static void checkRepeatStatus(int repeatThreshold) {
        System.out.printf("\n查看有哪些子问题被重复尝试超过%d次\n", repeatThreshold);
        int N = 13;
        for (int n = 0; n <= N; n++) {
            System.out.printf("N:%d\n", n);
            nQueens_bit_checkRepeatStatus(n, repeatThreshold);
        }
    }
}
