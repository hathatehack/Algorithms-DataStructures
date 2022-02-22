package C13_DP.C04_ConstraintModel;

public class C01_RobotWalk {
    // 假设有连续的1~N个位置（N>=2），如果机器人在1位置那么下一步只能往右去2位置，如果在N位置那么下一步只能往左去N-1位置，如果在中间位置则左右都可以走，
    // 开始时机器人在cur位置（1<=cur<=N），返回必须走K步（K>=1）最终到aim位置（1<=aim<=N）的走法有多少种。
    // 暴力递归
    public static int ways(int K, int cur, int aim, int N) {
        if (K < 1 || cur < 1 || cur > N || aim < 1 || aim > N || N < 2) {
            return 0;
        }
        return process(K, cur, aim, N);
    }
    // 还剩restStep步可走，当前在cur位置，目的地是aim位置，可走的位置1~N，返回从cur走rest步到aim的全部走法。
    private static int process(int restStep, int cur, int aim, int N) {
        // base case
        if (restStep == 0) {  // 步数用完且到目的地了，得到1种走法
            return cur == aim ? 1 : 0;
        }
        if (cur == 1) {  // 左边界
            return process(restStep - 1, 2, aim, N);
        }
        if (cur == N) {  // 右边界
            return process(restStep - 1, N - 1, aim, N);
        }
        // 尝试左、右移动
        int p1 = process(restStep - 1, cur - 1, aim, N);
        int p2 = process(restStep - 1, cur + 1, aim, N);
        return p1 + p2;
    }



    // 缓存表记忆化搜索。步数和出发位置组合存在重复计算，可组合做缓存。
    public static int ways2(int K, int cur, int aim, int N) {
        if (K < 1 || cur < 1 || cur > N || aim < 1 || aim > N || N < 2) {
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
    // 还剩restStep步可走，当前在cur位置，目的地是aim位置，可走的位置1~N，缓存表dp，返回从cur走rest步到aim的全部走法。
    private static int process2(int restStep, int cur, int aim, int N, int[][] dp) {
        if (dp[restStep][cur] != -1) {  // 之前算过此情况，直接返回缓存
            return dp[restStep][cur];
        }
        int ways;
        // base case
        if (restStep == 0) {  // 步数用完且到目的地了，得到1种走法
            ways = cur == aim ? 1 : 0;
        } else if (cur == 1) {  // 左边界
            ways = process2(restStep - 1, 2, aim, N, dp);
        } else if (cur == N) {  // 右边界
            ways = process2(restStep - 1, N - 1, aim, N, dp);
        } else {
            // 尝试左、右移动
            ways = process2(restStep - 1, cur - 1, aim, N, dp) +
                    process2(restStep - 1, cur + 1, aim, N, dp);
        }
        dp[restStep][cur] = ways;  // 缓存记录
        return ways;
    }



    // 动态规划
    public static int ways3(int K, int cur, int aim, int N) {
        if (K < 1 || cur < 1 || cur > N || aim < 1 || aim > N || N < 2) {
            return 0;
        }
        int[][] dp = new int[K + 1][N + 1];  // 移动位置1~N
        // base case
        dp[0][aim] = 1;  // 步数用完且到目的地了，是1种走法。
        // 尝试左、右移动。
        // 位置依赖：左上、右上。
        for (int r = 1; r <= K; r++) {  // 从上往下
            // base case
            dp[r][1] = dp[r - 1][2];      // 左边界
            dp[r][N] = dp[r - 1][N - 1];  // 右边界
            // 尝试左、右移动
            for (int i = 2; i < N; i++) {  // 从左往右填
                int p1 = dp[r - 1][i - 1];  // 左移
                int p2 = dp[r - 1][i + 1];  // 右移
                dp[r][i] = p1 + p2;
            }
        }
        return dp[K][cur];
    }

    // 动态规划优化。对于位置依赖：左上、右上（即同行无依赖，下行依赖上一行），可优化为复用一个一维数组进行自我更新、一个变量保存左上记录。
    public static int ways3_1(int K, int cur, int aim, int N) {
        if (K < 1 || cur < 1 || cur > N || aim < 1 || aim > N || N < 2) {
            return 0;
        }
        int[] dp = new int[N + 1];  // 所有行复用一个一维数组进行自我更新
        int topLeft;  // 所有行复用一个变量保存左上记录
        // base case
        dp[aim] = 1;  // 步数用完了且到目的地了，是一种走法。
        // 尝试左、右移动。
        // 位置依赖：左上、右上（即同行无依赖，下行依赖上一行）。
        for (int r = 1; r <= K; r++) {  // 从上往下
            topLeft = dp[1];  // 保存左上记录
            dp[1] = dp[2];  // 左边界
            // 尝试左、右移动
            for (int i = 2; i < N; i++) { // 从左往右填
                int p1 = topLeft;   // 左移
                int p2 = dp[i + 1]; // 右移
                topLeft = dp[i];  // 保存左上记录
                dp[i] = p1 + p2;
            }
            dp[N] = topLeft;  // 右边界
        }
        return dp[cur];
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
//            System.out.printf("i=%d  K:%d cur:%d aim:%d N:%d\n", i, K, cur, aim, N);
            int ans1 = ways(K, cur, aim, N);
            int ans2 = ways2(K, cur, aim, N);
            int ans3 = ways3(K, cur, aim, N);
            int ans3_1 = ways3_1(K, cur, aim, N);
            if (ans1 != ans2 || ans1 != ans3 || ans1 != ans3_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans3=%d, ans3_1=%d  K:%d cur:%d aim:%d N:%d\n", ans1, ans2, ans3, ans3_1, K, cur, aim, N);
            }
        }
    }
}
