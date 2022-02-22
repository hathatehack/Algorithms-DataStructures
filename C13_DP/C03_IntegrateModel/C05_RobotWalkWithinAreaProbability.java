package C13_DP.C03_IntegrateModel;

public class C05_RobotWalkWithinAreaProbability {
    // 给定正整数N、M、K、X、Y，表示机器人在[X,Y]位置，机器人可以向上下左右4个方向走，只要离开N*M区域直接死亡，最多走K步，返回机器人还在N*M区域的概率。
    // 暴力递归，时间复杂度O(4^K)。
    public static double walkWithinAreaProbability(int N, int M, int K, int X, int Y) {
        double p = process(K, X, Y, N, M);
        return p / Math.pow(4, K);
    }
    // 机器人在[X,Y]位置向上下左右4个方向走，最多走restStep步，返回机器人还在N*M区域内的全部走法。
    private static long process(int restStep, int X, int Y, int N, int M) {
        // base case
        if (X < 0 || X >= N || Y < 0 || Y >= M) {  // 越界了，无效走法。
            return 0;
        }
        if (restStep == 0) {  // 步数走完了没越界，得到1种走法。
            return 1;
        }
        // 尝试上、下、左、右4个方向移动
        long p1 = process(restStep - 1, X, Y - 1, N, M);
        long p2 = process(restStep - 1, X, Y + 1, N, M);
        long p3 = process(restStep - 1, X - 1, Y, N, M);
        long p4 = process(restStep - 1, X + 1, Y, N, M);
        return p1 + p2 + p3 + p4;
    }



    // 动态规划
    public static double walkWithinAreaProbability2(int N, int M, int K, int X, int Y) {
        long[][][] dp = new long[K + 1][N][M];
        // base case
        for (int x = N - 1; x >= 0; x--) {
            for (int y = M - 1; y >= 0; y--) {
                dp[0][x][y] = 1;  // 步数走完了没越界，得到1种走法。
            }
        }
        // 尝试上、下、左、右4个方向移动。
        // 位置依赖：同层无依赖，顶层依赖低一层。
        for (int restStep = 1; restStep <= K; restStep++) { // 从底往顶
            for (int y = 0; y < M; y++) {  // 从上往下
                for (int x = 0; x < N; x++) {  // 从左往右填
                    long p1 =  process2(restStep - 1 , x, y - 1, N, M, dp);
                    long p2 =  process2(restStep - 1 , x, y + 1, N, M, dp);
                    long p3 =  process2(restStep - 1 , x - 1, y, N, M, dp);
                    long p4 =  process2(restStep - 1 , x + 1, y, N, M, dp);
                    dp[restStep][x][y] = p1 + p2 + p3 + p4;
                }
            }
        }
        return (double)dp[K][X][Y] / Math.pow(4, K);
    }
    private static long process2(int restStep, int X, int Y, int N, int M, long[][][] dp) {
        // base case
        if (X < 0 || X >= N || Y < 0 || Y >= M) {  // 越界了，无效走法。
            return 0;
        }
        // 查表
        return dp[restStep][X][Y];
    }




    public static void main(String[] args) {
        int testTimes = 100000;
        int maxN = 10;
        int maxM = 10;
        int maxK = 8;
        for (int i = 0; i < testTimes; i++) {
            int N = (int)(Math.random() * maxN + 1);  // [1, MaxN]
            int M = (int)(Math.random() * maxM + 1);  // [1, maxM]
            int K = (int)(Math.random() * maxK + 1);    // [1, maxK]
            int X = (int)(Math.random() * N);  // [0, N)
            int Y = (int)(Math.random() * M);  // [0, M)
//            System.out.printf("i=%d   N:%d M:%d K:%d X:%d Y:%d\n", i, N, M, K, X, Y);
            double ans1 = walkWithinAreaProbability(N, M, K, X, Y);
            double ans2 = walkWithinAreaProbability2(N, M, K, X, Y);
            if (ans1 != ans2) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%f, ans2=%f   N:%d M:%d K:%d X:%d Y:%d\n", ans1, ans2, N, M, K, X, Y);
            }
        }
    }
}
