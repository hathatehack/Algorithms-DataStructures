package C20_LongestSubarraySum;

import C01_random.GenerateRandomArray;

public class C01_LongestSubarraySumEqualToKOfPositiveArray {
    // 给定一个正整数组成的无序数组arr和一个正整数值K，找到累加和等于K的最长子数组，返回其长度。
    // 分析技巧：
    //  以第i位置开头的正数子数组越长累加和只会越大，不可能变小，当累加和小于K就继续右扩，当累加和等于或大于K则结束i位置匹配，开始i+1位置匹配。
    //  通过L、R、sum完成窗口不回退遍历。
    // 窗口移动，时间复杂度O(N)
    public static int longestSubarraySumEqualToK(int[] array, int K) {
        if (array == null || array.length == 0 || K <0) {
            return 0;
        }
        int L = 0;
        int R = 0;
        int maxLen = 0;
        int sum = array[0];  // 记录累加和使窗口不回退！
        while (R < array.length) {
            if (sum < K) {  // 累加和小于K，则R右扩
                if (++R == array.length) {
                    break;
                }
                sum += array[R];
            } else if (sum > K) { // 累加和大于K，则L右缩
                sum -= array[L++];
            } else {  // 累加和等于K，则L右缩
                maxLen = Math.max(maxLen, R - L + 1);
                sum -= array[L++];
            }
        }
        return maxLen;
    }


    // O(N^3)
    public static int longestSubarraySumEqualToK2(int[] array, int K) {
        int maxLen = 0;
        for (int L = 0; L < array.length; L++) {
            for (int R = L; R < array.length; R++) {
                int sum = 0;
                // 累加L到R
                for (int i = L; i <= R; i++) {   // 每次R尝试右扩，R就发生回退遍历！
                    sum += array[i];
                }
                if (sum == K)
                    maxLen = Math.max(maxLen, R - L + 1);
            }
        }
        return maxLen;
    }


    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxSize = 30;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
            int K = (int)(Math.random() * maxValue) + 1;
            int ans1 = longestSubarraySumEqualToK(array, K);
            int ans2 = longestSubarraySumEqualToK2(array, K);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d  K=%d array: ", ans1, ans2, K);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
