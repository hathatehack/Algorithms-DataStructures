package C13_DP.ConstraintModel;

public class C01_RobotWalk {
    // 假设有连续的1~N个位置（N>=2），如果机器人在1位置那么下一步只能往右去2位置，如果在N位置那么下一步只能往左去N-1位置，如果在中间位置则左右都可以走；
    // 开始时机器人在M位置（1<=M<=N），返回必须走K步最终到P位置（1<=P<=N）的走法有多少种。
    // 暴力递归
    public static int way(int N, int cur, int aim, int K) {
        if (N < 2 || K < 1 || cur < 1 || cur > N || aim < 1 || aim > N) {
            return 0;
        }
        return process(K, cur, aim, N);
    }
    // 还剩rest步可走，当前在cur位置，目的地是aim位置，可走的位置1~N，返回从cur走rest步到aim的走法有多少。
    private static int process(int rest, int cur, int aim, int N) {
        if (rest == 0) {  // 步数用完且到目的地了，返回得到一种走法
            return cur == aim ? 1 : 0;
        }
        // 尝试左右移动
        int ways = 0;
        if (cur == 1) {  // 边界
            ways = process(rest - 1, 2, aim, N);
        } else if (cur == N) {  // 边界
            ways = process(rest - 1, N - 1, aim, N);
        } else {  // 中间位置
            ways = process(rest - 1, cur - 1, aim, N) +
                    process(rest - 1, cur + 1, aim, N);
        }
        return ways;
    }



    // 缓存表记忆化搜索。步数和出发位置组合存在重复计算，可组合做缓存。
    public static int way2(int N, int cur, int aim, int K) {
        if (N < 2 || K < 1 || cur < 1 || cur > N || aim < 1 || aim > N) {
            return 0;
        }
        int[][] dp = new int[K + 1][N + 1];
        for (int r = K; r >= 0; r--) {
            for (int i = N; i >= 1; i--) {
                dp[r][i] = -1;
            }
        }
        return process2(K, cur, aim, N, dp);
    }
    // 还剩rest步可走，当前在cur位置，目的地是aim位置，可走的位置1~N，缓存表dp，返回从cur走rest步到aim的走法有多少。
    private static int process2(int rest, int cur, int aim, int N, int[][] dp) {
        if (dp[rest][cur] != -1) {  // 之前算过此情况，直接返回缓存
            return dp[rest][cur];
        }
        int ways;
        if (rest == 0) {  // 步数用完且到目的地了，返回得到一种走法
            ways = cur == aim ? 1 : 0;
        } else if (cur == 1) {
            ways = process2(rest - 1, 2, aim, N, dp);
        } else if (cur == N) {
            ways = process2(rest - 1, N - 1, aim, N, dp);
        } else {
            ways = process2(rest - 1, cur - 1, aim, N, dp) +
                    process2(rest - 1, cur + 1, aim, N, dp);
        }
        dp[rest][cur] = ways;  // 缓存记录
        return ways;
    }



    // 动态规划
    public static int way3(int N, int cur, int aim, int K) {
        if (N < 2 || K < 1 || cur < 1 || cur > N || aim < 1 || aim > N) {
            return 0;
        }
        int[][] dp = new int[K + 1][N + 1];
        dp[0][aim] = 1;  // base case
        for (int r = 1; r <= K; r++) {
            dp[r][1] = dp[r - 1][2];      // 边界
            dp[r][N] = dp[r - 1][N - 1];  // 边界
            for (int i = 2; i < N; i++) { // 中间位置
                dp[r][i] = dp[r - 1][i - 1] + dp[r - 1][i + 1];
            }
        }
        return dp[K][cur];
    }



    public static void main(String[] args) {
        int testTimes = 100000;
        int maxN = 10;
        int maxK = 15;
        for (int i = 0; i < testTimes; i++) {
            int N = (int)(Math.random() * maxN + 1); // [1, maxN]
            int cur = (int)(Math.random() * N + 1);  // [1, N]
            int aim = (int)(Math.random() * N + 1);  // [1, N]
            int K = (int)(Math.random() * maxK + 1); // [1, maxK]
//            System.out.printf("i=%d N:%d cur:%d aim:%d K:%d\n", i, N, cur, aim, K);
            int ans1 = way(N, cur, aim, K);
            int ans2 = way2(N, cur, aim, K);
            int ans3 = way3(N, cur, aim, K);
            if (ans1 != ans2 || ans1 != ans3) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d  N:%d cur:%d aim:%d K:%d\n", ans1, ans2, N, cur, aim, K);
            }
        }
    }
}
