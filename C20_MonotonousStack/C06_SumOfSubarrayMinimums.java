package C20_MonotonousStack;

import C01_random.GenerateRandomArray;

public class C06_SumOfSubarrayMinimums {
    // 测试链接：https://leetcode.com/problems/sum-of-subarray-minimums/
    // 给定一个数组arr，返回所有子数组最小值的累加和，结果%1000000007。
    // 分析技巧：
    //  单调栈遍历每个位置求其最小值区间，必定能找出所有的子数组。
    //  比arr[i]小且最近的左位置是l，右位置是r，含arr[i]的子数组有多少个：(i-l)*(r-i)
    // 时间复杂度O(N^2)
    public static int sumSubarrayMins(int[] array) {
        int sum = 0;
        int[] incStack = new int[array.length];  // 递增栈找每个值的最小值区间，存放数组值的下标！！
        int stackIdx = -1;
        // 遍历每个元素
        for (int i = 0; i < array.length; i++) {
            while (stackIdx != -1 && array[incStack[stackIdx]] >= array[i]) { // 清理栈内不平衡元素，保证栈内单调性
                // 单调性被破坏，说明出现了更小值，结算前个最小值所形成的子数组
                int idx = incStack[stackIdx--];
                int leftLessIdx = stackIdx != -1 ? incStack[stackIdx] : -1;  // 左边比idx元素小的位置
                int subarrayCounts = (idx - leftLessIdx) * (i - idx); // 以idx位置做最小值的子数组数量
                sum += subarrayCounts * array[idx];
                sum %= 1000000007; // 题目要求
            }
            incStack[++stackIdx] = i;  // 入栈新元素的下标！！
        }
        // 结算剩余栈，栈内单调意味着剩余元素的右边界为数组末尾
        while (stackIdx != -1) {
            int idx = incStack[stackIdx--];
            int leftLessIdx = stackIdx != -1 ? incStack[stackIdx] : -1;  // 左边比idx元素小的位置
            int subarrayCounts = (idx - leftLessIdx) * (array.length - idx); // 以idx位置做最小值的子数组数量
            sum += subarrayCounts * array[idx];
            sum %= 1000000007; // 题目要求
        }
        return sum;
    }


    // 暴力解，时间复杂度O(N^3)
    public static int sumSubarrayMins2(int[] array) {
        int sum = 0;
        for (int L = 0; L < array.length; L++) {
            for (int R = L; R < array.length; R++) {
                int min = array[L];
                for (int r = L + 1; r <= R; r++) {
                    if (min > array[r])
                        min = array[r];
                }
                sum += min;
                sum %= 1000000007; // 题目要求
            }
        }
        return sum;
    }


    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxSize = 30;
        int maxValue = 20;
        for (int i = 0; i < testTimes; i++) {
//          int[] array = new int[] {2, 1, 1};
            int[] array = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int ans1 = sumSubarrayMins(array);
            int ans2 = sumSubarrayMins2(array);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d,  array: ", ans1, ans2);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
