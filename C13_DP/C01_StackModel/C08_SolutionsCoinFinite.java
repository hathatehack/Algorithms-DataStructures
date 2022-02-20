package C13_DP.C01_StackModel;

import C01_random.GenerateRandomArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class C08_SolutionsCoinFinite {
    // 给定一个正整数数组coins表示有多个货币，再给定一个正整数aim，返回各种面值组成aim的方式有多少种。
    // 例如：coins={1,1,1,1,2,2}, aim=3，有如下方式：1+1+1、1+2，一共2种。
    // 有两种试法：
    //   1. 对给定的货币顺序做要或不要的二分选择。
    //   2. 先对给定的货币按面值做压缩去重处理，枚举每种面值能使用的次数去凑目标值，剩余值和剩余未使用面值进入下一消除流程，周而复始直至消完。
    // 暴力递归（方案1），时间复杂度O(2 ^ coins.length)
    public static int solutionsCoinFinite1(int[] coins, int aim) {
        if (coins == null || coins.length == 0 || aim <= 0) {
            return 0;
        }
        return process(coins, new HashSet<>(), new LinkedList<>(), 0, aim);
    }
    // 返回各种面值组成aim的方式有多少种
    private static int process(int[] coins, HashSet<String> set, LinkedList<Integer> path, int curIndex, int rest) {
        // base case
        if (rest == 0) {  // 凑完了，结束分支。
            Object[] arr = path.toArray();
            Arrays.sort(arr);
            String s = Arrays.toString(arr);
            if (!set.contains(s)) {  // 去重
                set.add(s);
                return 1;
            }
            return 0;
        }
        if (curIndex == coins.length)  // 没凑够且没数凑了，返回-1结束无效分支。
            return 0;
        // 剪枝
        if (rest < 0)  // 返回-1结束无效分支。
            return 0;
        // 尝试要、不要当前货币
        int p0 = process(coins, set, path, curIndex + 1, rest);    // 不要当前货币
        path.addLast(coins[curIndex]);
        int p1 = process(coins, set, path, curIndex + 1, rest - coins[curIndex]); // 要当前货币
        path.removeLast();
        return p0 + p1;
    }



    // 暴力递归（方案2）, 压缩去重，时间复杂度O(multiply(counts))，重复面值越多则越快。
    public static int solutionsCoinFinite2(int[] coins, int aim) {
        if (coins == null || coins.length == 0 || aim <= 0) {
            return 0;
        }
        // 对货币做分类整理
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int coin : coins)
            map.put(coin, map.getOrDefault(coin, 0) + 1);
        int[] coinValues = new int[map.size()];
        int[] counts = new int[map.size()];
        int[] index = new int[] {0};
        map.forEach((key, value) -> { coinValues[index[0]] = key; counts[index[0]] = value; index[0]++; });
        return process2(coinValues, counts, 0, aim);
    }
    // 返回各种面值组成aim的方式有多少种
    private static int process2(int[] coinValues, int[] counts, int curIndex, int rest) {
        // base case
        if (rest == 0) {  // 凑完了，得到1种方式。
            return 1;
        }
        if (curIndex == coinValues.length) {  // 没凑够且没数凑了，返回0结束无效分支。
            return 0;
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest、不超过供应数量（从0张贴纸开始枚举）。
        int solutions = 0;
        for (int n = 0; coinValues[curIndex] * n <= rest && n <= counts[curIndex]; n++) {
            solutions += process2(coinValues, counts, curIndex + 1, rest - coinValues[curIndex] * n);
        }
        return solutions;
    }



    // 动态规划（方案2）
    public static int solutionsCoinFinite2_1(int[] coins, int aim) {
        if (coins == null || coins.length == 0 || aim <= 0) {
            return 0;
        }
        // 对货币做分类整理
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int coin : coins)
            map.put(coin, map.getOrDefault(coin, 0) + 1);
        int[] coinValues = new int[map.size()];
        int[] counts = new int[map.size()];
        int[] index = new int[] {0};
        map.forEach((key, value) -> { coinValues[index[0]] = key; counts[index[0]] = value; index[0]++; });
        int N = coinValues.length;
        int[][] dp = new int[N + 1][aim + 1];
        // base case
        for (int i = N; i >= 0; i--) {
            dp[i][0] = 1;
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest、不超过供应数量（从0张贴纸开始枚举）。
        // 位置依赖： 同行无依赖，上行依赖下行。
        for (int i = N - 1; i >= 0; i--) {  // 从下往上
            for (int rest = 1; rest <= aim; rest++) { // 从左往右填
                int solutions = 0;
                for (int n = 0; coinValues[i] * n <= rest && n <= counts[i]; n++) {
                    solutions += dp[i + 1][rest - coinValues[i] * n];
                }
                dp[i][rest] = solutions;
            }
        }
        return dp[0][aim];
    }



    // 动态规划优化（方案2）。对迭代进行斜率优化。
    public static int solutionsCoinFinite2_2(int[] coins, int aim) {
        if (coins == null || coins.length == 0 || aim <= 0) {
            return 0;
        }
        // 对货币做分类整理
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int coin : coins)
            map.put(coin, map.getOrDefault(coin, 0) + 1);
        int[] coinValues = new int[map.size()];
        int[] counts = new int[map.size()];
        int[] index = new int[] {0};
        map.forEach((key, value) -> { coinValues[index[0]] = key; counts[index[0]] = value; index[0]++; });
        int N = coinValues.length;
        int[][] dp = new int[N + 1][aim + 1];
        // base case
        for (int i = N; i >= 0; i--) {
            dp[i][0] = 1;
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest、不超过供应数量（从0张贴纸开始枚举）。
        // 位置依赖：同行无依赖，上行[i][rest]依赖下行[i-1][rest - coin*0]..[i-1][rest - coin*n]，0<=n<=counts[i]。
        // dp[i][rest-coin] = dp[i+1][rest-coin] + dp[i+1][rest-coin*2] + .. + dp[i+1][rest-coin*n]
        // dp[i][rest] = dp[i+1][rest] + dp[i+1][rest-coin] + dp[i+1][rest-coin*2] + .. + dp[i+1][rest-coin*n]
        // 对迭代O(1)优化有，dp[i][rest] = dp[i+1][rest] + dp[i][rest-coin] - dp[i+1][rest-coin - coin*n]
        for (int i = N - 1; i >= 0; i--) {  // 从下往上
            for (int rest = 1; rest <= aim; rest++) { // 从左往右填
                int solutions = dp[i + 1][rest];
                if (rest - coinValues[i] >= 0) {
                    solutions += dp[i][rest - coinValues[i]];
                    if (rest - coinValues[i] - coinValues[i] * counts[i] >= 0) {
                        solutions -= dp[i + 1][rest - coinValues[i] - coinValues[i] * counts[i]];
                    }
                }
                dp[i][rest] = solutions;
            }
        }
        return dp[0][aim];
    }





    public static void main(String[] args) {
        checkResult();
        compareTimeConsume();
    }
    // 验证结果
    private static void checkResult() {
        System.out.println("验证结果...");
        int testTimes = 100000;
        int maxSize = 10;
        int maxValue = 5;
        int maxAim = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] coins = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
            int aim = (int)(Math.random() * maxAim + 1);
            int ans1 = solutionsCoinFinite1(coins, aim);
            int ans2 = solutionsCoinFinite2(coins, aim);
            int ans2_1 = solutionsCoinFinite2_1(coins, aim);
            int ans2_2 = solutionsCoinFinite2_2(coins, aim);
            if (ans1 != ans2 || ans2 != ans2_1  || ans2 != ans2_2) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans2_1=%d, ans2_2=%d   aim: %d  array: ", ans1, ans2, ans2_1, ans2_2, aim);
                GenerateRandomArray.printArray(coins);
            }
        }
    }
    // 耗时对比
    private static void compareTimeConsume() {
        System.out.println("\n耗时对比：");
        int testTimes = 10;
        int maxSize = 30;
        int maxValue = 10;
        int maxAim = maxSize * maxValue;
        long startTime, endTime, time1 = 0, time2 = 0;
        LinkedList<Integer> ansList1 = new LinkedList<>(), ansList2 = new LinkedList<>();
        for (int i = 0; i < testTimes; i++) {
            int[] coins = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
            int aim = (int) (Math.random() * maxAim + 1);
            startTime = System.currentTimeMillis(); ansList1.add(solutionsCoinFinite1(coins, aim)); endTime = System.currentTimeMillis();
            time1 += endTime - startTime;
            startTime = System.currentTimeMillis(); ansList2.add(solutionsCoinFinite2(coins, aim)); endTime = System.currentTimeMillis();
            time2 += endTime - startTime;
        }
        System.out.printf("maxCoinsSize：%d  maxCoinValue：%d\n", maxSize, maxValue);
        System.out.printf("solutionsCoinFinite1（不做处理）     testTimes:%d  ansList：%s  总耗时：%dms\n", testTimes, ansList1, time1);
        System.out.printf("solutionsCoinFinite2（面值压缩去重） testTimes:%d  ansList：%s  总耗时：%dms\n", testTimes, ansList2, time2);
    }
}
