package C13_DP.C01_StackModel;

import C01_random.GenerateRandomArray;

public class C03_SplitArrayToTwoCloseSums {
    // 给定一个非负整数数组array，把array分成两个集合，要求两集合的累加和最接近并返回较小的集合累加和。
    // 暴力递归
    public static int splitArrayToTwoCloseSums1(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        return process1(array, 0, sum / 2);
    }
    // 从非负整数数组array的第index个数开始挑数累加，返回最接近但不超过sum的累加和。
    private static int process1(int[] array, int index, int sum) {
        // base case
        if (sum < 0) {  // 返回-1结束无效分支
            return -1;
        }
        if (index == array.length) {  // 没有数了，结束分支。
            return 0;
        }
        // 尝试要、不要第index个数
        int p1 = process1(array, index + 1, sum);  // 不要第index个数
        int p2 = process1(array, index + 1, sum - array[index]); // 要第index个数
        if (p2 != -1) {  // 若能要第index个数，则累加其值。
            p2 = array[index] + p2;
        }
        return Math.max(p1, p2);  // 取较大的累加和
    }


    // 暴力递归 + 剪枝
    public static int splitArrayToTwoCloseSums1_1(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        return process1_1(array, 0, sum / 2);
    }
    // 从非负整数数组array的第index个数开始累加，返回最接近但不超过sum的累加和。
    private static int process1_1(int[] array, int index, int sum) {
        // base case
        if (index == array.length) {  // 没有数了，结束分支。
            return 0;
        }
        // 尝试要、不要第index个数
        int p1 = process1_1(array, index + 1, sum);  // 不要第index个数
        int p2 = array[index] > sum ? 0 :  // 剪枝！若能要，则累加第index个数。
                (array[index] + process1_1(array, index + 1, sum - array[index]));
        return Math.max(p1, p2);  // 取较大的累加和
    }



    // 动态规划
    public static int splitArrayToTwoCloseSums2(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        sum /= 2;
        int[][] dp = new int[array.length + 1][sum + 1];
        // 尝试要、不要第index个数
        // 位置依赖：上行依赖下行，同行内无依赖。
        for (int i = array.length - 1; i >= 0; i--) {  // 从下往上
            for (int s = sum; s >= 0; s--) {  // 从右往左填
                int p1 = dp[i + 1][s];  // 不要第index个数
                int p2 = array[i] > s ? 0 :  // 剪枝，压缩表！
                        (array[i] + dp[i + 1][s - array[i]]); // 若能要，则累加第index个数。
                dp[i][s] = Math.max(p1, p2);  // 取较大的累加和
            }
        }
        return dp[0][sum];
    }


    // 动态规划优化。位置依赖：上行依赖下行，同行内无依赖，可优化为复用两个一维数组进行交替更新。
    public static int splitArrayToTwoCloseSums2_1(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        sum /= 2;
        int[][] dp = new int[2][sum + 1];  // 优化为复用两个一维数组进行轮流更新
        int[] upperRow = dp[0];
        int[] lowerRow = dp[1];
        int[] up = null;
        // 尝试要、不要第index个数
        // 位置依赖：上行依赖下行，同行内无依赖。
        for (int i = array.length - 1; i >= 0; i--) {  // 从下往上
            for (int s = sum; s >= 0; s--) {  // 从右往左填
                int p1 = lowerRow[s];  // 不要第index个数
                int p2 = array[i] > s ? 0 :  // 剪枝，压缩表！
                        (array[i] + lowerRow[s - array[i]]); // 若能要，则累加第index个数。
                upperRow[s] = Math.max(p1, p2);  // 取较大的累加和
            }
            up = upperRow;
            upperRow = lowerRow;
            lowerRow = up;
        }
        return up[sum];
    }





    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 10;
        int maxValue = 10;
        for (int i = 0; i <= testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
//            System.out.printf("i=%d  array: ", i); GenerateRandomArray.printArray(array);
            int ans1 = splitArrayToTwoCloseSums1(array);
            int ans1_1 = splitArrayToTwoCloseSums1_1(array);
            int ans2 = splitArrayToTwoCloseSums2(array);
            int ans2_1 = splitArrayToTwoCloseSums2_1(array);
            if (ans1 != ans1_1 || ans1 != ans2 || ans1 != ans2_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans1_1=%d, ans2=%d, ans2_1=%d  array: ", ans1, ans1_1, ans2, ans2_1);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
