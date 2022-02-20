package C13_DP.C01_StackModel;

import C01_random.GenerateRandomArray;

public class C07_SolutionsCoinInfinite {
    // 给定一个正整数数组coinValues表示有多种货币面值（都是正数且不重复），再给定一个正整数aim，每种面值货币都有无限多，返回各种面值组成aim的方式有多少种。
    // 例如：coinValues={1,2}, aim=4，有如下方式：1+1+1+1、1+1+2、2+2，一共3种。
    // 暴力递归
    public static int solutionsCoinInfinite(int[] coinValues, int aim) {
        if (coinValues == null || coinValues.length == 0 || aim <= 0) {
            return 0;
        }
        return process(coinValues, 0, aim);
    }
    // 返回各种面值组成aim的方式有多少种
    private static int process(int[] coinKinds, int curIndex, int rest) {
        // base case
        if (rest == 0) {  // 凑完了，得到1种方式。
            return 1;
        }
        if (curIndex == coinKinds.length) {  // 没凑够且没数凑了，返回0结束无效分支。
            return 0;
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest（从0张贴纸开始枚举）。
        int solutions = 0;
        for (int n = 0; coinKinds[curIndex] * n <= rest; n++) {
            solutions += process(coinKinds, curIndex + 1, rest - coinKinds[curIndex] * n);
        }
        return solutions;
    }



    // 动态规划
    public static int solutionsCoinInfinite2(int[] coinValues, int aim) {
        if (coinValues == null || coinValues.length == 0 || aim <= 0) {
            return 0;
        }
        int N = coinValues.length;
        int[][] dp = new int[N + 1][aim + 1];
        // base case
        for (int i = N; i >= 0; i--) {
            dp[i][0] = 1;
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest（从0张贴纸开始枚举）。
        // 位置依赖：同行无依赖，上行依赖下行。
        for (int i = N - 1; i >= 0; i--) {  // 从下往上
            for (int rest = 1; rest <= aim; rest++) { // 从左往右填
                int solutions = 0;
                for (int n = 0; coinValues[i] * n <= rest; n++) {
                    solutions += dp[i + 1][rest - coinValues[i] * n];
                }
                dp[i][rest] = solutions;
            }
        }
        return dp[0][aim];
    }



    // 动态规划优化。对迭代进行斜率优化。
    public static int solutionsCoinInfinite2_1(int[] coinValues, int aim) {
        if (coinValues == null || coinValues.length == 0 || aim <= 0) {
            return 0;
        }
        int N = coinValues.length;
        int[][] dp = new int[N + 1][aim + 1];
        // base case
        for (int i = N; i >= 0; i--) {
            dp[i][0] = 1;
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest（从0张贴纸开始枚举）。
        // 位置依赖：同行无依赖，上行[i][rest]依赖下行[i-1][rest - coin*0]..[i-1][rest - coin*n]。
        // dp[i][rest-coin] = dp[i+1][rest-coin] + dp[i+1][rest-coin*2] + .. + dp[i+1][rest-coin*n]
        // dp[i][rest] = dp[i+1][rest] + dp[i+1][rest-coin] + dp[i+1][rest-coin*2] + .. + dp[i+1][rest-coin*n]
        // 对迭代O(1)优化有，dp[i][rest] = dp[i+1][rest] + dp[i][rest-coin]
        for (int i = N - 1; i >= 0; i--) {  // 从下往上
            for (int rest = 1; rest <= aim; rest++) { // 从左往右填
                int solutions = dp[i + 1][rest];
                if (rest - coinValues[i] >= 0) {
                    solutions += dp[i][rest - coinValues[i]];
                }
                dp[i][rest] = solutions;
            }
        }
        return dp[0][aim];
    }





    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 10;
        int maxAim = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] coinValues = GenerateRandomArray.generateRandomArrayNonRepeat(maxSize, 1);
            int aim = (int)(Math.random() * maxAim + 1);
            int ans1 = solutionsCoinInfinite(coinValues, aim);
            int ans2 = solutionsCoinInfinite2(coinValues, aim);
            int ans2_1 = solutionsCoinInfinite2_1(coinValues, aim);
            if (ans1 != ans2  || ans1 != ans2_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans2_1=%d   aim: %d  array: ", ans1, ans2, ans2_1, aim);
                GenerateRandomArray.printArray(coinValues);
            }
        }
    }
}
