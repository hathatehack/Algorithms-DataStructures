package C13_DP.C01_StackModel;

import C01_random.GenerateRandomArray;

public class C06_LeastCoinsCoinInfinite {
    // 给定一个正整数数组coinValues表示有多种货币面值（都是正数且不重复），再给定一个正整数aim，每种面值货币都有无限多，返回各种面值组成aim的最少货币数量。
    // 例如：coinValues={1,2}, aim=4，有如下方式：1+1+1+1、1+1+2、2+2，所以最少货币数量为2个。
    // 暴力递归
    public static int leastCoinsCoinInfinite(int[] coinValues, int aim) {
        if (coinValues == null || coinValues.length == 0 || aim <= 0) {
            return -1;
        }
        return process(coinValues, 0, aim);
    }
    // 返回各种面值组成rest的最少货币数量
    private static int process(int[] coinValues, int curIndex, int rest) {
        // base case
        if (rest == 0) {  // 凑完了，结束分支。
            return 0;
        }
        if (curIndex == coinValues.length) {  // 没凑够且没数凑了，返回-1结束无效分支。
            return -1;
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest（从0张贴纸开始枚举）。
        int min = Integer.MAX_VALUE;
        for (int n = 0; coinValues[curIndex] * n <= rest; n++) {
            int p = process(coinValues, curIndex + 1, rest - coinValues[curIndex] * n);
            if (p != -1) {  // 若能要当前货币n个，则使用货币数量为当前n个+后续p个。
                p = n + p;
                min = Math.min(min, p);  // 选最少货币数量
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }



    // 动态规划
    public static int leastCoinsCoinInfinite2(int[] coinValues, int aim) {
        if (coinValues == null || coinValues.length == 0 || aim <= 0) {
            return -1;
        }
        int N = coinValues.length;
        int[][] dp = new int[N + 1][aim + 1];
        // base case
        for (int rest = aim; rest >= 1; rest--) {
            dp[N][rest] = -1;  // 没凑够且没数凑了，返回-1结束无效分支。
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest（从0张贴纸开始枚举）。
        // 位置依赖：同行无依赖，上行依赖下行。
        for (int i = N - 1; i >= 0; i--) {  // 从下往上
            for (int rest = 1; rest <= aim; rest++) {  // 从左往右填
                int min = Integer.MAX_VALUE;
                for (int n = 0; coinValues[i] * n <= rest; n++) {
                    int p = dp[i + 1][rest - coinValues[i] * n];
                    if (p != -1) {  // 若能成功，则使用货币数量为当前n个+后续p个。
                        p = n + p;
                        min = Math.min(min, p);  // 选最少货币数量
                    }
                }
                dp[i][rest] = min == Integer.MAX_VALUE ? -1 : min;
            }
        }
        return dp[0][aim];
    }



    // 动态规划优化。对迭代进行斜率优化。
    public static int leastCoinsCoinInfinite2_1(int[] coinValues, int aim) {
        if (coinValues == null || coinValues.length == 0 || aim <= 0) {
            return -1;
        }
        int N = coinValues.length;
        int[][] dp = new int[N + 1][aim + 1];
        // base case
        for (int rest = aim; rest >= 1; rest--) {
            dp[N][rest] = -1;  // 没凑够且没数凑了，返回-1结束无效分支。
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest（从0张贴纸开始枚举）。
        // 位置依赖：同行无依赖，上行[i][rest]依赖下行[i-1][rest - coin*0]..[i-1][rest - coin*n]。
        // dp[i][rest-coin] = min(dp[i+1][rest-coin], dp[i+1][rest-coin*2], .. ,dp[i+1][rest-coin*n])
        // dp[i][rest] = min(dp[i+1][rest], dp[i+1][rest-coin], dp[i+1][rest-coin*2], .. ,dp[i+1][rest-coin*n])
        // 对迭代O(1)优化有，dp[i][rest] = min(dp[i+1][rest], dp[i][rest-coin] + 1)
        for (int i = N - 1; i >= 0; i--) {  // 从下往上
            for (int rest = 1; rest <= aim; rest++) {  // 从左往右填
                int p0 = dp[i + 1][rest];  // 不使用coins[i]面值的结果
                int p1 = -1;  // 使用coins[i]面值的结果
                if (rest - coinValues[i] >= 0) {
                    if ((p1 = dp[i][rest - coinValues[i]]) != -1)  // 若使用coins[i]面值的方式能成功，则使用货币数量为当前面值货币1个+余额所用最少货币数量！
                        p1 += 1;
                }
                dp[i][rest] = p0 == -1 ? p1 : (p1 == -1 || p0 < p1 ? p0 : p1);
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
            int ans1 = leastCoinsCoinInfinite(coinValues, aim);
            int ans2 = leastCoinsCoinInfinite2(coinValues, aim);
            int ans2_1 = leastCoinsCoinInfinite2_1(coinValues, aim);
            if (ans1 != ans2  || ans1 != ans2_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans2_1=%d   aim: %d  array: ", ans1, ans2, ans2_1, aim);
                GenerateRandomArray.printArray(coinValues);
            }
        }
    }
}
