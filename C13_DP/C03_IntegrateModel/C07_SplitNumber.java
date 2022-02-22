package C13_DP.C03_IntegrateModel;

public class C07_SplitNumber {
    // 4的裂开方式有：1+1+1+1、1+1+2、1+3、2+2、4，要求后面的数不能比前面小，且分裂出的数大于0。给定一个正数N，返回N有多少种裂开方式。
    // 暴力递归
    public static int splitNums(int N) {
        if (N < 0) {
            return 0;
        }
        if (N <= 1) {
            return 1;
        }
        return process(1, N);
    }
    // 从initial开始拼凑出number，要求后面的数不能比前面小，返回全部拼法。
    private static int process(int initial, int number) {
        // base case
        if (number == 0) {  // 拼完了，得到1种方式。
            return 1;
        }
        if (initial == number) {  // 拼凑出目标值，得到1种方式。
            return 1;
        }
        // 要求后面的数不小于前面，当前值尝试拼凑范围为initial~number。
        int p = 0;
        for (int i = initial; i <= number; i++) {
            p += process(i, number - i);
        }
        return p;
    }



    // 动态规划
    public static int splitNums2(int N) {
        if (N < 0) {
            return 0;
        }
        if (N <= 1) {
            return 1;
        }
        int[][] dp = new int[N + 1][N + 1];  // 分裂出的数大于0
        // base case
        for (int initial = 1; initial <= N; initial++) {
            dp[initial][0] = 1;  // 拼完了，得到1种方式。
            dp[initial][initial] = 1; // 拼凑出目标值，得到1种方式。
        }
        // 要求后面的数不小于前面，当前值尝试拼凑范围为initial~number。
        // 位置依赖：[i][n]依赖[i][n-i]..[n][0]
        for (int initial = N - 1; initial >= 1; initial--) {  // 从下往上
            int number = initial + 1;  // 要求后面的数不小于前面，dp[initial][initial]已有baseCase值，所以从initial+1开始。
            for (; number <= N; number++) {  // 从左往右填
                int p = 0;
                for (int i = initial; i <= number; i++) {  // 当前值尝试拼凑范围为initial~number
                    p += dp[i][number - i];
                }
                dp[initial][number] = p;
            }
        }
        return dp[1][N];
    }


    // 动态规划优化。对迭代进行斜率优化。
    public static int splitNums2_1(int N) {
        if (N < 0) {
            return 0;
        }
        if (N <= 1) {
            return 1;
        }
        int[][] dp = new int[N + 1][N + 1];  // 分裂出的数大于0
        // base case
        for (int initial = 1; initial <= N; initial++) {
            dp[initial][0] = 1;  // 拼完了，得到1种方式。
            dp[initial][initial] = 1; // 拼凑出目标值，得到1种方式。
        }
        // 要求后面的数不小于前面，当前值尝试拼凑范围为initial~number。
        // 位置依赖：上行[i][n]依赖下行[i][n-i],[i+1][n-(i+1)]...[n][0]
        // dp[i][n] = dp[i][n-i] + dp[i+1][n-(i+1)] + .. + [n][0]
        // dp[i+1][n] = dp[i+1][n-(i+1)] + .. + [n][0]
        // 对迭代O(1)优化有，dp[i][n] = dp[i][n-i] + dp[i+1][n]
        for (int initial = N - 1; initial >= 1; initial--) {  // 从下往上
            int number = initial + 1;  // 要求后面的数不小于前面，dp[initial][initial]已有baseCase值，所以从initial+1开始。
            for (; number <= N; number++) {  // 从左往右填
                dp[initial][number] = dp[initial][number - initial] + dp[initial + 1][number];
            }
        }
        return dp[1][N];
    }





    public static void main(String[] args) throws InterruptedException {
        int testTimes = 80;//90;
        for (int i = 0; i <= testTimes; i++) {
            System.out.printf("N:%d\n", i);
            int ans1 = splitNums(i);
            int ans2 = splitNums2(i);
            int ans2_1 = splitNums2_1(i);
            if (ans1 != ans2 || ans1 != ans2_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans2_1=%d  N:%d\n", ans1, ans2, ans2_1, i);
                return;
            }
        }

        // 用时对比
        System.out.println("\n用时对比:");
        int startN = 800;
        int N = 810;
        Thread t1 = new Thread(() -> {
            for (int n = startN; n <= N; n++) {
                long startTime = System.currentTimeMillis();
                int ans = splitNums(n);
                long endTime = System.currentTimeMillis();
                System.out.printf("splitNums        N:%- 5d ans:%- 9d 用时：%dms\n", n, ans, endTime - startTime);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int n = startN; n <= N; n++) {
                long startTime = System.currentTimeMillis();
                int ans = splitNums2(n);
                long endTime = System.currentTimeMillis();
                System.out.printf("splitNums2        N:%- 5d ans:%- 9d 用时：%dms\n", n, ans, endTime - startTime);
            }
        });
        Thread t2_1 = new Thread(() -> {
            for (int n = startN; n <= N; n++) {
                long startTime = System.currentTimeMillis();
                int ans = splitNums2_1(n);
                long endTime = System.currentTimeMillis();
                System.out.printf("splitNums2_1      N:%- 5d ans:%- 9d 用时：%dms\n", n, ans, endTime - startTime);
            }
        });
        t1.start();
        t2.start();
        t2_1.start();
        t1.join();
        t2.join();
        t2_1.join();
    }
}
