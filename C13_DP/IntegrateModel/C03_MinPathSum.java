package C13_DP.IntegrateModel;

import C01_random.GenerateRandomArray;

public class C03_MinPathSum {
    // 测试链接：https://leetcode-cn.com/problems/minimum-path-sum/
    // 给定一个二维数组matrix，一个人必须从左上角出发，最后到达右下角，沿途只可以向下或向右走，返回最小的沿途数值累加和。
    // 暴力递归
    public static int minPathSum(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
            return 0;
        }
        // 从终点到起点等价于从起点到终点
        return process(matrix, matrix.length - 1, matrix[0].length - 1);
    }
    // 返回从[curR, curC]位置通过左移或上移到[0, 0]位置沿途经过的最小数值累加和。
    private static int process(int[][] matrix, int curR, int curC) {
        // base case
        if (curR == 0 && curC == 0) {  // 到目的地了
            return matrix[0][0];
        }
        if (curR == 0) {  // 上边界
            return matrix[0][curC] + process(matrix, 0, curC - 1);
        }
        if (curC == 0) {  // 左边界
            return matrix[curR][0] + process(matrix, curR - 1, 0);
        }
        // 尝试左移或上移
        int p1 = process(matrix, curR, curC - 1);  // 尝试左移
        int p2 = process(matrix, curR - 1, curC);  // 尝试上移
        return matrix[curR][curC] + Math.min(p1, p2);  // 结算。当前节点数值+后续最小数值累加和。
    }



    // 动态规划
    public static int minPathSum2(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
            return 0;
        }
        int row = matrix.length;
        int col = matrix[0].length;
        int[][] dp = new int[row][col];
        // base case
        dp[0][0] = matrix[0][0];  // 目的地
        for (int c = 1; c < col; c++) { // 上边界
            dp[0][c] = matrix[0][c] + dp[0][c - 1];
        }
        for (int r = 1; r < row; r++) { // 左边界
            dp[r][0] = matrix[r][0] + dp[r - 1][0];
        }
        // 尝试左移或上移，位置依赖：左、上。
        for (int r = 1; r < row; r++) {  // 从上往下
            for (int c = 1; c < col; c++) { // 从左往右填
                int p1 = dp[r][c - 1];  // 尝试左
                int p2 = dp[r - 1][c];  // 尝试上
                dp[r][c] = matrix[r][c] + Math.min(p1, p2); // 结算。当前节点数值+沿途最小数值累加和。
            }
        }
        return dp[row - 1][col - 1];
    }

    // 动态规划优化。对于位置依赖：左、上，可优化为复用一个一维数组进行自我更新。
    public static int minPathSum2_1(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
            return 0;
        }
        int row = matrix.length;
        int col = matrix[0].length;
        int[] dp = new int[col];  // 所有行复用一个一维数组进行自我更新
        // base case
        dp[0] = matrix[0][0];  // 目的地
        for (int c = 1; c < col; c++) { // 上边界
            dp[c] = matrix[0][c] + dp[c - 1];
        }
        // 尝试左移或上移，位置依赖：左、上。
        for (int r = 1; r < row; r++) {  // 从上往下
            dp[0] = dp[0] + matrix[r][0];  // 左边界
            for (int c = 1; c < col; c++) { // 从左往右填
                int p1 = dp[c - 1];  // 尝试左
                int p2 = dp[c];  // 尝试上
                dp[c] = matrix[r][c] + Math.min(p1, p2); // 结算。当前节点数值+沿途最小数值累加和。
            }
        }
        return dp[col - 1];
    }




    public static void main(String[] args) {
        int testTimes = 100000;
        int maxRow = 10;
        int maxCol = 10;
        int maxValue = 50;
        for (int i = 0; i < testTimes; i++) {
            int[][] matrix = GenerateRandomArray.generateRandomMatrixPositive(maxRow, maxCol, maxValue);
            int ans1 = minPathSum(matrix);
            int ans2 = minPathSum2(matrix);
            int ans2_1 = minPathSum2_1(matrix);
            if (ans1 != ans2 || ans1 != ans2_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans2_1=%d   matrix: ", ans1, ans2, ans2_1);
                GenerateRandomArray.printArray(matrix);
            }
        }
    }
}
