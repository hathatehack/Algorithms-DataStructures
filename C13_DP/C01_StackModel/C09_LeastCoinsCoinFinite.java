package C13_DP.C01_StackModel;

import C01_random.GenerateRandomArray;
import java.util.HashMap;
import java.util.LinkedList;

public class C09_LeastCoinsCoinFinite {
    // 给定一个正整数数组coins表示有多个货币，再给定一个正整数aim，返回各种面值组成aim的最少货币数量。
    // 例如：coins={1,1,1,1,2,2}, aim=3，有如下方式：1+1+1、1+2，所以最少货币数量为2个。
    // 有两种试法：
    //   1. 对给定的货币依次进行要或不要的二分选择。
    //   2. 先对给定的货币按面值做压缩去重处理，枚举每种面值能使用的次数去凑目标值，剩余值和剩余未使用面值进入下一消除流程，周而复始直至消完。
    //   当存在大量重复面值，方案2比方案1更快。
    //
    // 暴力递归（方案1），时间复杂度O(2 ^ coins.length)
    public static int leastCoinsCoinFinite1(int[] coins, int aim) {
        if (coins == null || coins.length == 0 || aim <= 0) {
            return -1;
        }
        return process1(coins, 0, aim);
    }
    // 返回各种面值组成rest的最少货币数量
    private static int process1(int[] coins, int curIndex, int rest) {
        // base case
        if (rest == 0)  // 凑完了，结束分支。
            return 0;  // 找货币数不用去重处理，但如果是找组合方式需要做去重处理。
        if (curIndex == coins.length)  // 没凑够且没数凑了，返回-1结束无效分支。
            return -1;
        // 剪枝
        if (rest < 0)  // 返回-1结束无效分支。
            return -1;
        // 尝试要、不要当前货币
        int p0 = process1(coins, curIndex + 1, rest);  // 不要当前货币
        int p1 = process1(coins, curIndex + 1, rest - coins[curIndex]); // 要当前货币
        if (p1 != -1) {  // 若能要当前货币，则使用货币数量为当前1个+后续p1个。
            p1 = 1 + p1;
        }
        return p0 == -1 ? p1 : (p1 == -1 || p0 < p1 ? p0 : p1);  // 选最少货币数量
    }


    // 暴力递归（方案2）, 压缩去重，时间复杂度O(coins.length) + O(multiply(counts))，重复面值越多则越快。
    public static int leastCoinsCoinFinite2(int[] coins, int aim) {
        if (coins == null || coins.length == 0 || aim <= 0) {
            return -1;
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
    // 返回各种面值组成rest的最少货币数量
    private static int process2(int[] coinValues, int[] counts, int curIndex, int rest) {
        // base case
        if (rest == 0)  // 凑完了，结束分支。
            return 0;
        if (curIndex == coinValues.length)  // 没凑够且没数凑了，返回-1结束无效分支。
            return -1;
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest、不超过供应数量（从0张贴纸开始枚举）。
        int min = Integer.MAX_VALUE;
        for (int n = 0; coinValues[curIndex] * n <= rest && n <= counts[curIndex]; n++) {
            int p = process2(coinValues, counts, curIndex + 1, rest - coinValues[curIndex] * n);
            if (p != -1) {  // 若能要当前货币n个，则使用货币数量为当前n个+后续p个。
                p = n + p;
                min = Math.min(min, p);  // 选最少货币数量
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }



    // 动态规划（方案2）, 时间复杂度O(coins.length) + O(aim * coinValues.length * average(counts))。
    public static int leastCoinsCoinFinite3_1(int[] coins, int aim) {
        if (coins == null || coins.length == 0 || aim <= 0) {
            return -1;
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
        for (int rest = aim; rest >= 1; rest--) {
            dp[N][rest] = -1;  // 没凑够且没数凑了，返回-1结束无效分支。
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest、不超过供应数量（从0张贴纸开始枚举）。
        // 位置依赖：同行无依赖，上行依赖下行。
        for (int i = N - 1; i >= 0; i--) {  // 从下往上
            for (int rest = 1; rest <= aim; rest++) {  // 从左往右填
                int min = Integer.MAX_VALUE;
                for (int n = 0; coinValues[i] * n <= rest && n <= counts[i]; n++) {  // 枚举
                    int p = dp[i + 1][rest - coinValues[i] * n];
                    if (p != -1) {  // 若能要当前货币n个，则使用货币数量为当前n个+后续p个。
                        p = n + p;
                        min = Math.min(min, p);  // 选最少货币数量
                    }
                }
                dp[i][rest] = min == Integer.MAX_VALUE ? -1 : min;
            }
        }
        return dp[0][aim];
    }


    // 动态规划优化（方案2）, 对迭代进行斜率优化（使用窗口最小值结构），时间复杂度O(coins.length) + O(aim * coinValues.length)。
    public static int leastCoinsCoinFinite3_2(int[] coins, int aim) {
        if (coins == null || coins.length == 0 || aim <= 0) {
            return -1;
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
        for (int rest = aim; rest >= 1; rest--) {
            dp[N][rest] = -1;  // 没凑够且没数凑了，返回-1结束无效分支。
        }
        // 枚举当前面值货币次数来消掉rest，总价值不超过rest、不超过供应数量（从0张贴纸开始枚举）。
        // 位置依赖：同行无依赖，上行[i][rest]依赖下行[i-1][rest - coin*0]..[i-1][rest - coin*n]，0<=n<=counts[i]。
        // dp[i][rest-coin] = min(dp[i+1][rest-coin], dp[i+1][rest-coin*2], .. ,dp[i+1][rest-coin*n])
        // dp[i][rest] = min(dp[i+1][rest], dp[i+1][rest-coin], dp[i+1][rest-coin*2], .. ,dp[i+1][rest-coin*(n-1)])
        // 由于每种面值只有有限个货币，所以不能直接由dp[i][rest-coin]推导得到dp[i][rest]的解，后者与前者有相交但不是包含关系！可以使用窗口结构求解。
        LinkedList<Integer> minWindow = new LinkedList<>();  // 窗口最小值结构
        for (int i = N - 1; i >= 0; i--) {
            int curCoinValue = coinValues[i];
            int windows = Math.min(aim + 1, curCoinValue);  // 可以形成多少个窗口结构（0~aim范围内有多少个差值为curCoinValue的等差数列）
            for (int winLeft = 0; winLeft < windows; winLeft++) {  // 处理每个窗口
                minWindow.clear();
                minWindow.add(winLeft);  // 初始化新窗口内的最小值
                dp[i][winLeft] = dp[i + 1][winLeft]; // 默认值，窗口当前就只有这1个元素！
                int windowSize = curCoinValue + curCoinValue * counts[i];
                for (int L = winLeft + curCoinValue; L <= aim; L += curCoinValue) {  // 当前窗口连续右滑至不超过aim的最右边界
                    while (!minWindow.isEmpty() &&
                            (dp[i + 1][minWindow.peekLast()] == -1 || (dp[i + 1][L] != -1 &&
                                    (dp[i + 1][minWindow.peekLast()] + (L - minWindow.peekLast()) / curCoinValue) >= dp[i + 1][L]))) {  // 清理窗口内所有比新方案差的旧方案
                        minWindow.pollLast();
                    }
                    minWindow.addLast(L);  // 入队新元素的下标!!
                    if (minWindow.peekFirst() == L - windowSize) {  // 窗口每次右滑一格后需弹出在左窗口之前的下标
                        minWindow.pollFirst();
                    }
                    // 结算当前窗口内哪种方式使用最少的货币数量
                    int preMin = dp[i + 1][minWindow.peekFirst()];
                    dp[i][L] = preMin == -1 ? -1 : ((L - minWindow.peekFirst()) / curCoinValue + preMin);
                }
            }
        }
        return dp[0][aim];
    }






    public static void main(String[] args) {
        checkResult();
        compareTimeConsumeRecursion();
        compareTimeConsumeDP();
    }
    // 验证结果
    private static void checkResult() {
        System.out.println("验证结果...");
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 5;
        int maxAim = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] coins = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
            int aim = (int) (Math.random() * maxAim + 1);
//            System.out.printf("i=%d  aim: %d  array: ", i, aim); GenerateRandomArray.printArray(coins);
            int ans1 = leastCoinsCoinFinite1(coins, aim);
            int ans2 = leastCoinsCoinFinite2(coins, aim);
            int ans3_1 = leastCoinsCoinFinite3_1(coins, aim);
            int ans3_2 = leastCoinsCoinFinite3_2(coins, aim);
            if (ans1 != ans2 || ans1 != ans3_1 || ans1 != ans3_2) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans3_1=%d, ans3_2=%d   aim: %d  array: ", ans1, ans2, ans3_1, ans3_2, aim);
                GenerateRandomArray.printArray(coins);
            }
        }
    }
    // 暴力递归耗时对比
    private static void compareTimeConsumeRecursion() {
        System.out.println("\n暴力递归耗时对比：");
        int testTimes = 10;
        int maxSize = 30;
        int maxValue = 10;
        int maxAim = maxSize * maxValue;
        long startTime, endTime, time1 = 0, time2 = 0;
        LinkedList<Integer> ansList1 = new LinkedList<>(), ansList2 = new LinkedList<>();
        for (int i = 0; i < testTimes; i++) {
            int[] coins = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);  // 有重复面值
            int aim = (int) (Math.random() * maxAim + 1);
            startTime = System.currentTimeMillis(); ansList1.add(leastCoinsCoinFinite1(coins, aim)); endTime = System.currentTimeMillis();
            time1 += endTime - startTime;
            startTime = System.currentTimeMillis(); ansList2.add(leastCoinsCoinFinite2(coins, aim)); endTime = System.currentTimeMillis();
            time2 += endTime - startTime;
        }
        System.out.printf("maxCoinsSize：%d  maxCoinValue：%d\n", maxSize, maxValue);
        System.out.printf("leastCoinsCoinFinite1（货币全展开）   testTimes:%d  ansList：%s  总耗时：%dms\n", testTimes, ansList1, time1);
        System.out.printf("leastCoinsCoinFinite2（面值压缩去重） testTimes:%d  ansList：%s  总耗时：%dms\n", testTimes, ansList2, time2);
        System.out.println("----------------------------------");
        System.out.printf("当存在大量重复面值，方案2比方案1更快！\n");
    }
    // 动态规划耗时对比
    private static void compareTimeConsumeDP() {
        System.out.println("\n动态规划耗时对比：");
        int testTimes = 10;
        int maxSize = 10000;
        int maxValue = 10;
        int maxAim = maxSize * maxValue;
        long startTime, endTime, time1 = 0, time2 = 0;
        LinkedList<Integer> ansList1 = new LinkedList<>(), ansList2 = new LinkedList<>();
        for (int i = 0; i < testTimes; i++) {
            int[] coins = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);  // 有重复面值
            int aim = (int) (Math.random() * maxAim + 1);
            startTime = System.currentTimeMillis(); ansList1.add(leastCoinsCoinFinite3_1(coins, aim)); endTime = System.currentTimeMillis();
            time1 += endTime - startTime;
            startTime = System.currentTimeMillis(); ansList2.add(leastCoinsCoinFinite3_2(coins, aim)); endTime = System.currentTimeMillis();
            time2 += endTime - startTime;
        }
        System.out.printf("maxCoinsSize：%d  maxCoinValue：%d\n", maxSize, maxValue);
        System.out.printf("leastCoinsCoinFinite3_1（迭代）          testTimes:%d  ansList：%s  总耗时：%dms\n", testTimes, ansList1, time1);
        System.out.printf("leastCoinsCoinFinite3_2（窗口结构最小值） testTimes:%d  ansList：%s  总耗时：%dms\n", testTimes, ansList2, time2);
        System.out.println("----------------------------------------");
        System.out.println("货币重复面值越多，窗口结构最小值的方式更快！");
    }
}
