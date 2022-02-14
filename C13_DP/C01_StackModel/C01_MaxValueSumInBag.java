package C13_DP.C01_StackModel;

import C01_random.GenerateRandomArray;

public class C01_MaxValueSumInBag {
    // 给定两个长度相等的一维正整数数组weights和values，分别表示每个物品的重量和价值，给定一个正数bag表示袋子的载重，返回袋子能装下的物品总价最多是多少。
    // 暴力递归
    public static int maxValueSumInBag1(int[] weights, int[] values, int bag) {
        if (weights == null || values == null || weights.length != values.length || weights.length == 0 || bag < 0)
            return 0;
        return process1(weights, values, 0, bag);
    }
    // weights和values分别表示每个物品的重量和价值，restLoad表示袋子还能装多少重量，从第index号物品开始挑，返回袋子能装下的最大物品总价。
    private static int process1(int[] weights, int[] values, int index, int restLoad) {
        // base case
        if (restLoad < 0)  // 返回-1结束无效分支。
            return -1;
        if (index == weights.length)  // 没有物品了，结束分支。
            return 0;
        // 尝试要、不要第index号物品
        int p1 = process1(weights, values, index + 1, restLoad);  // 不要第index号物品
        int p2 = process1(weights, values, index + 1, restLoad - weights[index]); // 要第index号物品
        if (p2 != -1)  // 能装下第index号物品，则累加其物品价值。
            p2 = values[index] + p2;
        return Math.max(p1, p2);  // 取能装下的最大总价
    }


    // 暴力递归 + 剪枝
    public static int maxValueSumInBag1_1(int[] weights, int[] values, int bag) {
        if (weights == null || values == null || weights.length != values.length || weights.length == 0 || bag < 0) {
            return 0;
        }
        return process1_1(weights, values, 0, bag);
    }
    // weights和values分别表示每个物品的重量和价值，restLoad表示袋子还能装多少重量，从第index号物品开始挑，返回袋子能装下的最大物品总价。
    private static int process1_1(int[] weights, int[] values, int index, int restLoad) {
        // base case
        if (index == weights.length) {  // 没有物品了，结束分支。
            return 0;
        }
        // 尝试要、不要第index号物品
        int p1 = process1_1(weights, values, index + 1, restLoad);  // 不要第index号物品
        int p2 = restLoad < weights[index] ? 0 :  // 剪枝！如果装得下的话，则要第index号物品。
                (values[index] + process1_1(weights, values, index + 1, restLoad - weights[index]));
        return Math.max(p1, p2);  // 取能装下的最大总价
    }





    // 缓存表记忆化搜索。剩余物品和剩余载重组合存在重复查找，可做缓存。
    public static int maxValueSumInBag2(int[] weights, int[] values, int bag) {
        if (weights == null || values == null || weights.length != values.length || weights.length == 0 || bag < 0) {
            return 0;
        }
        int[][] dp = new int[weights.length + 1][bag + 1];
        for (int[] row : dp) {
            for (int b = bag; b >= 0; b--)
                row[b] = -1;
        }
        return process2(weights, values, 0, bag, dp);
    }
    // weights和values分别表示每个物品的重量和价值，restLoad表示袋子还能装多少重量，从第index号物品开始挑，返回袋子能装下的最大物品总价。
    private static int process2(int[] weights, int[] values, int index, int restLoad, int[][] dp) {
        if (dp[index][restLoad] != -1) {  // 之前算过此情况，直接返回缓存。
            return dp[index][restLoad];
        }
        // base case
        if (index == weights.length) {  // 没有物品了，结束分支。
            return dp[index][restLoad] = 0;
        }
        // 尝试要、不要第index号物品
        int p1 = process2(weights, values, index + 1, restLoad, dp);  // 不要第index号物品
        int p2 = restLoad < weights[index] ? 0 :  // 剪枝！如果装得下的话，则要第index号物品。
                (values[index] + process2(weights, values, index + 1, restLoad - weights[index], dp));
        return dp[index][restLoad] = Math.max(p1, p2);
    }





    // 动态规划
    public static int maxValueSumInBag3(int[] weights, int[] values, int bag) {
        if (weights == null || values == null || weights.length != values.length || weights.length == 0 || bag < 0) {
            return 0;
        }
        int[][] dp = new int[weights.length + 1][bag + 1];
        // 尝试要、不要第index号物品
        // 位置依赖：上行依赖下行，同行内无依赖。
        for (int i = weights.length - 1; i >= 0; i--) {  // 从下往上
            for (int restLoad = bag; restLoad >= 0; restLoad--) { // 从右往左填
                int p1 = dp[i + 1][restLoad];  // 不要第index号物品
                int p2 = restLoad < weights[i] ? 0 : // 剪枝，压缩表！如果装得下的话，则要第index号物品。
                        (values[i] + dp[i + 1][restLoad - weights[i]]);
                dp[i][restLoad] = Math.max(p1, p2);  // 取能装下的最大总价
            }
        }
        return dp[0][bag];
    }


    // 动态规划优化。位置依赖：上行依赖下行，同行内无依赖，可优化为复用两个一维数组进行交替更新，减少空间占用。
    public static int maxValueSumInBag3_1(int[] weights, int[] values, int bag) {
        if (weights == null || values == null || weights.length != values.length || weights.length == 0 || bag < 0) {
            return 0;
        }
        int[][] dp = new int[2][bag + 1];  // 优化为复用两个一维数组进行轮流更新
        int[] upperRow = dp[0];
        int[] lowerRow = dp[1];
        int[] up = null;
        // 尝试要、不要第index号物品
        // 位置依赖：上行依赖下行，同行内无依赖
        for (int i = weights.length - 1; i >= 0; i--) {  // 从下往上
            for (int restLoad = bag; restLoad >= 0; restLoad--) { // 从右往左填
                int p1 = lowerRow[restLoad];  // 不要第index号物品
                int p2 = restLoad < weights[i] ? 0 :  // 剪枝，压缩表！如果装得下的话，要第index号物品。
                        (values[i] + lowerRow[restLoad - weights[i]]);
                upperRow[restLoad] = Math.max(p1, p2);  // 取能装下的最大总价
            }
            up = upperRow;
            upperRow = lowerRow;
            lowerRow = up;
        }
        return up[bag];
    }





    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 10;
        int maxValue = 10;
        for (int i = 0; i <= testTimes; i++) {
            int[][] matrix = GenerateRandomArray.generateRandomMatrixPositive(2, 2, 0, maxSize, maxValue);
            int[] weights = matrix[0];
            int[] values = matrix[1];
            int bag = (int)(weights.length * maxValue * Math.random());
            int ans1 = maxValueSumInBag1(weights, values, bag);
            int ans1_1 = maxValueSumInBag1_1(weights, values, bag);
            int ans2 = maxValueSumInBag2(weights, values, bag);
            int ans3 = maxValueSumInBag3(weights, values, bag);
            int ans3_1 = maxValueSumInBag3_1(weights, values, bag);
            if (ans1 != ans1_1 || ans1 != ans2 || ans1 != ans3 || ans1 != ans3_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.print("weights: "); GenerateRandomArray.printArray(weights);
                System.err.print("values: "); GenerateRandomArray.printArray(values);
                System.err.printf("bag=%d\n", bag);
                System.err.printf("ans1=%d, ans1_1=%d, ans2=%d, ans3=%d, ans3_1=%d\n", ans1, ans1_1, ans2, ans3, ans3_1);
            }
        }
    }
}
