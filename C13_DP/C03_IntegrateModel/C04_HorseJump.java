package C13_DP.C03_IntegrateModel;

public class C04_HorseJump {
    // 给定一个10*9大小的象棋棋盘，“马”在起始位置[0,0]，“马”可以跳向8个方向的“日”对角，要求走K步最后落在[A,B]位置，返回有多少种走法。
    // 暴力递归，时间复杂度O(8^K)。
    public static int horseJumps(int K, int A, int B) {
        return process(K, 0, 0, A, B);
    }
    // "马"在[X,Y]位置可以跳向8个方向的“日”对角，最多走restStep步，返回能到达[aimX,aimY]位置的全部走法。
    private static int process(int restStep, int X, int Y, int aimX, int aimY) {
        // base case
        if (X < 0 || X > 9 || Y < 0 || Y > 8) {  // 越界了，无效走法。
            return 0;
        }
        if (restStep == 0) {  // 步数走完了且到目的地了，得到1种走法。
            return (X == aimX && Y == aimY) ? 1 : 0;
        }
        // 尝试跳向8个方向的“日”对角
        int p1 = process(restStep - 1, X + 1, Y + 2, aimX, aimY);
        int p2 = process(restStep - 1, X - 1, Y + 2, aimX, aimY);
        int p3 = process(restStep - 1, X - 2, Y + 1, aimX, aimY);
        int p4 = process(restStep - 1, X - 2, Y - 1, aimX, aimY);
        int p5 = process(restStep - 1, X - 1, Y - 2, aimX, aimY);
        int p6 = process(restStep - 1, X + 1, Y - 2, aimX, aimY);
        int p7 = process(restStep - 1, X + 2, Y - 1, aimX, aimY);
        int p8 = process(restStep - 1, X + 2, Y + 1, aimX, aimY);
        return p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8;
    }



    // 动态规划
    public static int horseJumps2(int K, int A, int B) {
        int[][][] dp = new int[K + 1][10][9];
        // base case
        dp[0][A][B] = 1;  // 步数走完了且到目的地了，得到1种走法。
        // 尝试跳向8个方向的“日”对角。
        // 位置依赖关系：同层无依赖，顶层依赖低一层。
        for (int restStep = 1; restStep <= K; restStep++) { // 从底往顶
            for (int x = 0; x < 10; x++) {  // 从上往下
                for (int y = 0; y < 9; y++) {  // 从左往右填
                    int p1 = process2(restStep - 1, x + 1, y + 2, dp);
                    int p2 = process2(restStep - 1, x - 1, y + 2, dp);
                    int p3 = process2(restStep - 1, x - 2, y + 1, dp);
                    int p4 = process2(restStep - 1, x - 2, y - 1, dp);
                    int p5 = process2(restStep - 1, x - 1, y - 2, dp);
                    int p6 = process2(restStep - 1, x + 1, y - 2, dp);
                    int p7 = process2(restStep - 1, x + 2, y - 1, dp);
                    int p8 = process2(restStep - 1, x + 2, y + 1, dp);
                    dp[restStep][x][y] = p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8;
                }
            }
        }
        return dp[K][0][0];
    }
    private static int process2(int restStep, int X, int Y, int[][][] dp) {
        // base case
        if (X < 0 || X > 9 || Y < 0 || Y > 8) {  // 越界了，无效走法。
            return 0;
        }
        // 查表
        return dp[restStep][X][Y];
    }





    public static void main(String[] args) {
        int testTimes = 10000;
        int N = 10;
        int M = 9;
        int maxK = 8;
        for (int i = 0; i < testTimes; i++) {
            int K = (int)(Math.random() * maxK + 1);    // [1, maxK]
            int X = (int)(Math.random() * N);  // [0, N)
            int Y = (int)(Math.random() * M);  // [0, M)
//            System.out.printf("i=%d   N:%d M:%d K:%d X:%d Y:%d\n", i, N, M, K, X, Y);
            int ans1 = horseJumps(K, X, Y);
            int ans2 = horseJumps2(K, X, Y);
            if (ans1 != ans2) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d   N:%d M:%d K:%d X:%d Y:%d\n", ans1, ans2, N, M, K, X, Y);
            }
        }
    }
}
