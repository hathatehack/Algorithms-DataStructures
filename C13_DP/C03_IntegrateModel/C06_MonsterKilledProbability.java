package C13_DP.C03_IntegrateModel;

public class C06_MonsterKilledProbability {
    // 给定3个正整数N、M、K，表示怪兽初始有N滴血，被攻击一次会随机流失0~M整数滴血，求K次攻击后怪兽死亡的概率。
    // 暴力递归，时间复杂度O((M+1)^K)。
    public static double killedProbability(int K, int N, int M) {
        if (K < 1 || N < 1 || M < 1) {
            return 0;
        }
        long p = process(K, N, M);
        return p / Math.pow(M + 1, K);
    }
    // 还剩余restTimes次攻击，怪兽还有hp滴血，被攻击一次会随机流失0~M整数滴血，返回成功杀死怪兽的次数。
    private static long process(int restTimes, int hp, int M) {
        // base case
        if (restTimes == 0) {  // 攻击次数用完且怪兽没血量了，成功杀死怪兽1次。
            return hp <= 0 ? 1 : 0;
        }
        // 尝试流失血量从0到M
        long p = 0;
        for (int m = M; m >= 0; m--) {
            p += process(restTimes - 1, hp - m, M);
        }
        return p;
    }


    // 暴力递归 + 剪枝
    public static double killedProbability_1(int K, int N, int M) {
        if (K < 1 || N < 1 || M < 1) {
            return 0;
        }
        long p = process_1(K, N, M);
        return p / Math.pow(M + 1, K);
    }
    // 还剩余restTimes次攻击，怪兽还有hp滴血，被攻击一次会随机流失0~M整数滴血，返回成功杀死怪兽的次数。
    private static long process_1(int restTimes, int hp, int M) {
        // 尝试流失血量从0到M
        long p = 0;
        for (int m = M; m >= 0; m--) {
            if (hp - m <= 0) {  // 剪枝！
                p += Math.pow(M + 1, restTimes - 1);  // 怪兽没血量了，后续攻击全都可以成功，直接结算！
            } else {  // 结算流失血量m
                p += process(restTimes - 1, hp - m, M);
            }
        }
        return p;
    }





    // 动态规划
    public static double killedProbability2(int K, int N, int M) {
        if (K < 1 || N < 1 || M < 1) {
            return 0;
        }
        long[][] dp = new long[K + 1][N + 1];  // 血量0~N
        // 尝试流失血量从0到M。
        // 位置依赖：同行无依赖，下行i依赖上行[i-M..i]。
        for (int restTimes = 1; restTimes <= K; restTimes++) {  // 从上往下
            for (int hp = 0; hp <= N; hp++) {  // 从左往右填
                long p = 0;
                for (int m = M; m >= 0; m--) {
                    if (hp - m > 0) {  // 结算流失血量m
                        p += dp[restTimes - 1][hp - m];
                    } else {  // 剪枝，压缩表！
                        p += (long)Math.pow(M + 1, restTimes - 1);  // 怪兽没血量了，后续攻击全都可以成功，直接结算！
                    }
                }
                dp[restTimes][hp] = p;
            }
        }
        return dp[K][N] / Math.pow(M + 1, K);
    }


    // 动态规划
    public static double killedProbability2_1(int K, int N, int M) {
        if (K < 1 || N < 1 || M < 1) {
            return 0;
        }
        long[][] dp = new long[K + 1][N + 1];  // 血量0~N
        // base case
        for (int restTimes = 0; restTimes <= K; restTimes++) {
            dp[restTimes][0] = (long)Math.pow(M + 1, restTimes);  // 怪兽没血量了，后续攻击全都可以成功，直接结算！
        }
        // 尝试流失血量从0到M。
        // 位置依赖：同行无依赖，下行n依赖上行[n-M..n]。
        for (int restTimes = 1; restTimes <= K; restTimes++) {  // 从上往下
            for (int hp = 1; hp <= N; hp++) {  // 从左往右填
                long p = 0;
                for (int m = M; m >= 0; m--) {
                    if (hp - m >= 0) {  // 结算流失血量m
                        p += dp[restTimes - 1][hp - m];
                    } else {  // 剪枝，压缩表
                        p += (long)Math.pow(M + 1, restTimes - 1);  // 怪兽负血量了，后续攻击全都可以成功，直接结算！
                    }
                }
                dp[restTimes][hp] = p;
            }
        }
        return dp[K][N] / Math.pow(M + 1, K);
    }


    // 动态规划优化。对迭代进行斜率优化。
    public static double killedProbability2_2(int K, int N, int M) {
        if (K < 1 || N < 1 || M < 1) {
            return 0;
        }
        long[][] dp = new long[K + 1][N + 1];  // 血量0~N
        // base case
        for (int restTimes = 0; restTimes <= K; restTimes++) {
            dp[restTimes][0] = (long)Math.pow(M + 1, restTimes);  // 怪兽没血量了，后续攻击全都可以成功，直接结算！
        }
        // 尝试流失血量从0到M。
        // 位置依赖：同行无依赖，下行[r][n]依赖上一行[r-1][n-M]..[r-1][n]。
        // dp[r][n-1] = dp[r-1][n-1 - M] + .. + dp[r-1][n-1]
        // dp[r][n] = dp[r-1][n - M] + .. + dp[r-1][n]
        // 对迭代O(1)优化有，dp[r][n] = dp[r][n-1] - dp[r-1][n-1 - M] + dp[r-1][n]
        for (int restTimes = 1; restTimes <= K; restTimes++) {  // 从上往下
            for (int hp = 1; hp <= N; hp++) {  // 从左往右填
                long p = dp[restTimes][hp - 1] - (hp - 1 - M >= 0 ? dp[restTimes - 1][hp - 1 - M] : (long)Math.pow(M + 1, restTimes - 1))
                        + dp[restTimes - 1][hp];
                dp[restTimes][hp] = p;
            }
        }
        return dp[K][N] / Math.pow(M + 1, K);
    }





    public static void main(String[] args) {
        int testTimes = 10000;
        int maxK = 6;
        int maxN = 10;
        int maxM = 10;
        for (int i = 0; i < testTimes; i++) {
            int K = (int)(Math.random() * maxK + 1);  // [1, maxK]
            int N = (int)(Math.random() * maxN + 1);  // [1, maxN]
            int M = (int)(Math.random() * (maxM + 1));// [0, maxM]
//            System.out.printf("i=%d  K:%d N:%d M:%d\n", i, K, N, M);
            double ans1 = killedProbability(K, N, M);
            double ans1_1 = killedProbability_1(K, N, M);
            double ans2 = killedProbability2(K, N, M);
            double ans2_1 = killedProbability2_1(K, N, M);
            double ans2_2 = killedProbability2_2(K, N, M);
            if (ans1 != ans1_1 || ans1 != ans2 || ans1 != ans2_1 || ans1 != ans2_2) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%f, ans1_1=%f, ans2=%f, ans2_1=%f, ans2_2=%f   K:%d N:%d M:%d\n", ans1, ans1_1, ans2, ans2_1, ans2_2, K, N, M);
            }
        }
    }
}
