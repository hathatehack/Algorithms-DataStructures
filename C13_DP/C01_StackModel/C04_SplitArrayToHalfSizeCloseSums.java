package C13_DP.C01_StackModel;

import C01_random.GenerateRandomArray;

public class C04_SplitArrayToHalfSizeCloseSums {
    // 给定一个非负整数数组array，若个数为偶数则把array分成个数相同的两个集合，若个数为奇数则把array分成个数相差1的两个集合，要求两集合的累加和最接近并返回较小的集合累加和。
    // 暴力递归
    public static int splitArrayToHalfSizeCloseSums1(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        sum /= 2;
        if ((array.length & 1) == 0) {
            return process1(array, 0, array.length / 2, sum);
        } else {
            int p1 = process1(array, 0, array.length / 2, sum);
            int p2 = process1(array, 0, array.length / 2 + 1, sum);
            return Math.max(p1, p2);
        }
    }
    // 从非负整数数组array的第index个数开始挑够picks个数累加，返回最接近但不超过sum的累加和。
    private static int process1(int[] array, int index, int picks, int sum) {
        // base case
        if (sum < 0) {  // 返回-1结束无效分支
            return -1;
        }
        if (picks < 0) {  // 返回-1结束无效分支
            return -1;
        }
        if (index == array.length) {  // 没有数了
            return picks != 0 ? -1 : // 没挑够，返回-1结束无效分支。
                    0;  // 挑够了，结束分支。
        }
        // 尝试要、不要第index个数
        int p1 = process1(array, index + 1, picks, sum);  // 不要第index个数
        int p2 = process1(array, index + 1, picks - 1, sum - array[index]); // 要第index个数
        if (p2 != -1) {  // 若能要第index个数，则累加其值。
            p2 = array[index] + p2;
        }
        return Math.max(p1, p2);  // 取较大的累加和
    }


    // 暴力递归 + 剪枝
    public static int splitArrayToHalfSizeCloseSums1_1(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        sum /= 2;
        if ((array.length & 1) == 0) {
            return process1_1(array, 0, array.length / 2, sum);
        } else {
            int p1 = process1_1(array, 0, array.length / 2, sum);
            int p2 = process1_1(array, 0, array.length / 2 + 1, sum);
            return Math.max(p1, p2);
        }
    }
    // 从非负整数数组array的第index个数开始挑够picks个数累加，返回最接近但不超过sum的累加和。
    private static int process1_1(int[] array, int index, int picks, int sum) {
        // base case
        if (index == array.length) {  // 没有数了
            return picks != 0 ? -1 : // 没挑够，返回-1结束无效分支。
                    0;  // 挑够了，结束分支。
        }
        // 尝试要、不要第index个数
        int p1 = process1_1(array, index + 1, picks, sum);  // 不要第index个数
        int p2 = (picks == 0 || picks > array.length - index || sum < array[index]) ? -1 : // 剪枝！若能继续下去，则要第index个数。
                process1_1(array, index + 1, picks - 1, sum - array[index]);
        if (p2 != -1) {  // 若能要第index个数，则累加其值。
            p2 = array[index] + p2;
        }
        return Math.max(p1, p2);  // 取较大的累加和
    }



    // 动态规划
    public static int splitArrayToHalfSizeCloseSums2(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        sum /= 2;
        int N = array.length;
        int picks = (N + 1) / 2;
        int[][][] dp = new int[picks + 1][N + 1][sum + 1];
        // base case
        for (int s = sum; s >= 0; s--) {
            dp[0][N][s] = 0;  // 挑够了
            for (int p = picks; p >= 1; p--) {
                dp[p][N][s] = -1;  // 没挑够，-1表示无效分支。
            }
        }
        // 尝试要、不要第index个数
        // 位置依赖：顶层的i行依赖同层的i+1行、底层的i+1行
        for (int p = 0; p <= picks; p++) {  // 从底往顶
            for (int i = N - 1; i >= 0; i--) {  // 从下往上
                for (int s = sum; s >= 0; s--) {  // 从右往左填
                    int p1 = dp[p][i + 1][s];  // 不要第i个数
                    int p2 = (p == 0 || p > N - i || s < array[i]) ? -1 : // 剪枝，压缩表！
                            dp[p - 1][i + 1][s - array[i]];  // 若能继续下去，则要第i个数。
                    if (p2 != -1) {  // 若能要第index个数，则累加其值。
                        p2 = array[i] + p2;
                    }
                    dp[p][i][s] = Math.max(p1, p2);  // 取较大的累加和
                }
            }
        }
        if ((N & 1) == 0) {
            return dp[picks][0][sum];
        } else {
            int p1 = dp[array.length / 2][0][sum];
            int p2 = dp[array.length / 2 + 1][0][sum];
            return Math.max(p1, p2);
        }
    }


    // 动态规划
    public static int splitArrayToHalfSizeCloseSums2_1(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        sum /= 2;
        int N = array.length;
        int picks = (N + 1) / 2;
        int[][][] dp = new int[N + 1][picks + 1][sum + 1];
        // base case
        for (int s = sum; s >= 0; s--) {
            dp[N][0][sum] = 0;  // 挑够了
            for (int p = picks; p >= 1; p--) {
                dp[N][p][s] = -1;  // 没挑够，-1表示无效分支。
            }
        }
        // 尝试要、不要第index个数
        // 位置依赖：底层的p行依赖顶层的p行、p-1行
        for (int i = N - 1; i >= 0; i--) {  // 从顶往底
            for (int p = 0; p <= picks; p++) {  // 从上往下
                for (int s = sum; s >= 0; s--) {  // 从左往右填
                    int p1 = dp[i + 1][p][s];
                    int p2 = (p == 0 || p > N - i || s < array[i]) ? -1 : // 剪枝，压缩表！
                            dp[i + 1][p - 1][s - array[i]];  // 若能继续下去，则要第i个数。
                    if (p2 != -1) {  // 若能要第index个数，则累加其值。
                        p2 = array[i] + p2;
                    }
                    dp[i][p][s] = Math.max(p1, p2);  // 取较大的累加和
                }
            }
        }
        if ((N & 1) == 0) {
            return dp[0][picks][sum];
        } else {
            int p1 = dp[0][array.length / 2][sum];
            int p2 = dp[0][array.length / 2 + 1][sum];
            return Math.max(p1, p2);
        }
    }





    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 10;
        int maxValue = 10;
        for (int i = 0; i <= testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
//            System.out.printf("i=%d  array: ", i); GenerateRandomArray.printArray(array);
            int ans1 = splitArrayToHalfSizeCloseSums1(array);
            int ans1_1 = splitArrayToHalfSizeCloseSums1_1(array);
            int ans2 = splitArrayToHalfSizeCloseSums2(array);
            int ans2_1 = splitArrayToHalfSizeCloseSums2_1(array);
            if (ans1 != ans1_1 || ans1 != ans2 || ans1 != ans2_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans1_1=%d, ans2=%d, ans2_1=%d  array: ", ans1, ans1_1, ans2, ans2_1);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
