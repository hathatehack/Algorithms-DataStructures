package C13_DP.C01_StackModel;

import C01_random.GenerateRandomArray;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

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
    // 从非负整数数组array的第index个数开始挑够picks个数进行累加，返回最接近但不超过sum的累加和。
    private static int process1(int[] array, int index, int picks, int sum) {
        // base case
        if (sum < 0) {  // 返回-1结束无效分支
            return -1;
        }
        if (index == array.length) {  // 没有数了
            return picks != 0 ? -1 : // 挑少或挑多了，返回-1结束无效分支。
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
    // 从非负整数数组array的第index个数开始挑够picks个数进行累加，返回最接近但不超过sum的累加和。
    private static int process1_1(int[] array, int index, int picks, int sum) {
        // base case
        if (picks == 0) {  // 挑够了，结束分支。
            return 0;
        }
        if (index == array.length) {  // 没有数挑了，返回-1结束无效分支。
            return -1;  // picks == 0 ? 0 : -1;
        }
        // 剪枝
        if (picks > array.length - index) {  // 不够数挑了，返回-1结束无效分支。
            return -1;
        }
        // 尝试要、不要第index个数
        int p1 = process1_1(array, index + 1, picks, sum);  // 不要第index个数
        int p2 = sum < array[index] ? -1 : // 剪枝！若能继续下去，则要第index个数。
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
        for (int p = picks; p >= 1; p--) {  // 没有数挑了，返回-1结束无效分支。
            Arrays.fill(dp[p][N], -1);
        }
        // 尝试要、不要第index个数
        // 位置依赖：顶层的i行依赖同层的i+1行、底层的i+1行
        for (int p = 1; p <= picks; p++) {  // 从底往顶
            for (int i = N - 1; i >= 0; i--) {  // 从下往上
                if (p > N - i) {  // 剪枝。不够数挑了，返回-1结束无效分支。
                    Arrays.fill(dp[p][i], -1);
                } else {
                    for (int s = sum; s >= 0; s--) {  // 从右往左填
                        int p1 = dp[p][i + 1][s];  // 不要第i个数
                        int p2 = s < array[i] ? -1 : // 剪枝，压缩表！
                                dp[p - 1][i + 1][s - array[i]];  // 若能继续下去，则要第i个数。
                        if (p2 != -1) {  // 若能要第index个数，则累加其值。
                            p2 = array[i] + p2;
                        }
                        dp[p][i][s] = Math.max(p1, p2);  // 取较大的累加和
                    }
                }
            }
        }
        if ((N & 1) == 0) {
            return dp[array.length / 2][0][sum];
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
        for (int p = picks; p >= 1; p--) {  // 没有数挑了，返回-1结束无效分支。
            Arrays.fill(dp[N][p], -1);
        }
        // 尝试要、不要第index个数
        // 位置依赖：底层的p行依赖顶层的p行、p-1行
        for (int i = N - 1; i >= 0; i--) {  // 从顶往底
            for (int p = 1; p <= picks; p++) {  // 从上往下
                if (p > N - i) {  // 剪枝。不够数挑了，返回-1结束无效分支。
                    Arrays.fill(dp[i][p], -1);
                } else {
                    for (int s = sum; s >= 0; s--) {  // 从右往左填
                        int p1 = dp[i + 1][p][s];
                        int p2 = s < array[i] ? -1 : // 剪枝，压缩表！
                                dp[i + 1][p - 1][s - array[i]];  // 若能继续下去，则要第i个数。
                        if (p2 != -1) {  // 若能要第index个数，则累加其值。
                            p2 = array[i] + p2;
                        }
                        dp[i][p][s] = Math.max(p1, p2);  // 取较大的累加和
                    }
                }
            }
        }
        if ((N & 1) == 0) {
            return dp[0][array.length / 2][sum];
        } else {
            int p1 = dp[0][array.length / 2][sum];
            int p2 = dp[0][array.length / 2 + 1][sum];
            return Math.max(p1, p2);
        }
    }



    // 动态规划优化。位置依赖：底层的p行依赖顶层的p行、p-1行，可优化为复用两个二维数组进行交替更新，减少空间占用。
    public static int splitArrayToHalfSizeCloseSums2_2(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        sum /= 2;
        int N = array.length;
        int picks = (N + 1) / 2;
        // 优化为复用两个二维数组进行交替更新。
        int[][][] dp = new int[2][picks + 1][sum + 1];
        int[][] topLevel = dp[0];
        int[][] bottomLevel = dp[1];
        int[][] bottom = null;
        // base case
        for (int p = picks; p >= 1; p--) {  // 没有数挑了，返回-1结束无效分支。
            Arrays.fill(topLevel[p], -1);
        }
        // 尝试要、不要第index个数
        // 位置依赖：底层的p行依赖顶层的p行、p-1行
        for (int i = N - 1; i >= 0; i--) {  // 从顶往底
            for (int p = 1; p <= picks; p++) {  // 从上往下
                if (p > N - i) {  // 剪枝。不够数挑了，返回-1结束无效分支。
                    Arrays.fill(bottomLevel[p], -1);
                } else {
                    for (int s = sum; s >= 0; s--) {  // 从右往左填
                        int p1 = topLevel[p][s];
                        int p2 = s < array[i] ? -1 : // 剪枝，压缩表！
                                topLevel[p - 1][s - array[i]];  // 若能继续下去，则要第i个数。
                        if (p2 != -1) {  // 若能要第index个数，则累加其值。
                            p2 = array[i] + p2;
                        }
                        bottomLevel[p][s] = Math.max(p1, p2);  // 取较大的累加和
                    }
                }
            }
            // 顶层和底层交换角色
            bottom = bottomLevel;
            bottomLevel = topLevel;
            topLevel = bottom;
        }
        if ((N & 1) == 0) {
            return bottom[array.length / 2][sum];
        } else {
            int p1 = bottom[array.length / 2][sum];
            int p2 = bottom[array.length / 2 + 1][sum];
            return Math.max(p1, p2);
        }
    }



    // 动态规划优化。位置依赖：底层的p行依赖顶层的p行、p-1行，可优化为所有层复用一个二维数组进行自我更新、两个一维数组临时保存行记录，减少空间占用。
    public static int splitArrayToHalfSizeCloseSums2_3(int[] array) {
        if (array == null || array.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : array)
            sum += num;
        sum /= 2;
        int N = array.length;
        int picks = (N + 1) / 2;
        // 优化为所有层复用一个二维数组进行自我更新、两个一维数组临时保存行记录。
        int[][] dp = new int[picks + 1][sum + 1];
        int[] topLevelPrePick = new int[sum + 1], curPick = new int[sum + 1];
        // base case
        for (int p = picks; p >= 1; p--) {
            Arrays.fill(dp[p], -1);  // 没有数挑了，返回-1结束无效分支。
        }
        // 尝试要、不要第index个数
        // 位置依赖：底层的p行依赖顶层的p行、p-1行
        for (int i = N - 1; i >= 0; i--) {  // 从顶往底
            // base case
            int[] temp = topLevelPrePick;
            topLevelPrePick = dp[0];
            dp[0] = temp;
            Arrays.fill(dp[0], 0);  // 挑够了，结束分支。
            // 尝试要、不要第index个数
            for (int p = 1; p <= picks; p++) {  // 从上往下
                if (p > N - i) {  // 剪枝。不够数挑了，返回-1结束无效分支。
                    Arrays.fill(dp[p], -1);
                } else {
                    for (int s = sum; s >= 0; s--) {  // 从右往左填
                        int p1 = dp[p][s];
                        int p2 = s < array[i] ? -1 : // 剪枝，压缩表！
                                topLevelPrePick[s - array[i]];
                        if (p2 != -1) {  // 若能要第index个数，则累加其值。
                            p2 = array[i] + p2;
                        }
                        curPick[s] = Math.max(p1, p2);  // 取较大的累加和
                    }
                    System.arraycopy(dp[p], 0, topLevelPrePick, 0, sum + 1);
                    System.arraycopy(curPick, 0, dp[p], 0, sum + 1);
                }
            }
        }
        if ((N & 1) == 0) {
            return dp[array.length / 2][sum];
        } else {
            int p1 = dp[array.length / 2][sum];
            int p2 = dp[array.length / 2 + 1][sum];
            return Math.max(p1, p2);
        }
    }





    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
        checkResult();
        compareConsume();  // 比较消耗。（建议设置-Xms200m以上！）
    }
    // 验证结果
    private static void checkResult() {
        System.out.println("验证结果...");
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        for (int i = 0; i <= testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
//            System.out.printf("i=%d  array: ", i); GenerateRandomArray.printArray(array);
            int ans1 = splitArrayToHalfSizeCloseSums1(array);
            int ans1_1 = splitArrayToHalfSizeCloseSums1_1(array);
            int ans2 = splitArrayToHalfSizeCloseSums2(array);
            int ans2_1 = splitArrayToHalfSizeCloseSums2_1(array);
            int ans2_2 = splitArrayToHalfSizeCloseSums2_2(array);
            int ans2_3 = splitArrayToHalfSizeCloseSums2_3(array);
            if (ans1 != ans1_1 || ans1 != ans2 || ans1 != ans2_1 || ans1 != ans2_2 || ans1 != ans2_3) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans1_1=%d, ans2=%d, ans2_1=%d, ans2_2=%d, ans2_3=%d  array: ", ans1, ans1_1, ans2, ans2_1, ans2_2, ans2_3);
                GenerateRandomArray.printArray(array);
            }
        }
    }
    // 比较消耗。（建议设置-Xms200m以上！）
    private static void compareConsume() throws InterruptedException, ClassNotFoundException {
        // 获取进程内存使用情况 https://blog.csdn.net/weixin_34391445/article/details/91807877
        System.out.print("\n内存占用、耗时对比（建议测试设置-Xms200m以上！）：");
        int maxValue = 100;
        Object[][] testCases = new Object[][] {
                {50, "splitArrayToHalfSizeCloseSums2"},
                {30, "splitArrayToHalfSizeCloseSums"}
        };
        for (Object[] testCase : testCases) {
            int size = (int)testCase[0];
            String methodPrefix = (String)testCase[1];
            int[][] arrays = GenerateRandomArray.generateRandomMatrixPositive(1, 1, size, size, maxValue);
            System.out.printf("\narraySize=%d：\n", size);
            Runtime rt = Runtime.getRuntime();
            ArrayList<Method> methods = new ArrayList<>();
            for (Method method : Class.forName(Thread.currentThread().getStackTrace()[1].getClassName()).getMethods()) {
                if (method.getName().startsWith(methodPrefix))
                    methods.add(method);
            }
            methods.sort((m1, m2) -> m1.getName().compareTo(m2.getName()));
            for (Method method : methods) {
                Thread thread = new Thread(() -> {
                    long startFreeMem = rt.freeMemory();
                    long startTime = System.currentTimeMillis();
                    int ans = 0;
                    try {
                        ans = (int) method.invoke(null, arrays[0]);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    long endTime = System.currentTimeMillis();
                    long endFreeMem = rt.freeMemory();
                    System.out.printf("  %s%s   ans=%d   耗时：% 5dms   内存占用：%dKB%s\n", method.getName(), method.getName().length() == 32 ? "" : "  ", ans, endTime - startTime, (startFreeMem - endFreeMem) / 1024, endFreeMem > startFreeMem ? "(内存占用过大触发过GC！)" : "");
                });
                thread.start();
                thread.join();
            }
        }
    }
}
