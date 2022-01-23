package C21_LongestSubarraySum;

import C01_random.GenerateRandomArray;
import java.util.HashMap;

public class C02_LongestSubarraySumEqualToK {
    // 给定一个整数组成的无序数组arr和一个整数值K，找到累加和等于K的最长子数组，返回其长度。
    // 分析技巧：
    //  1）整数包含负数、0，导致累加和没有单调性，以i位置做开头找子数组的话需要每次都遍历到末尾才知道结果，时间复杂度大；
    //  2）显然每遍历到一个位置，必然知道当前、之前位置的每个累加和，所以对于i位置结尾只需要搜索之前是否存在累加和等于preSum[i]-K。
    // 以i位置做结尾的尝试方案， 利用preSum缓存表，时间复杂度O(N)
    public static int longestSubarraySumEqualToK(int[] array, int K) {
        if (array == null || array.length == 0) {
            return 0;
        }
        HashMap<Integer, Integer> preSumMap = new HashMap<>(); // key:前缀和 value:最早出现的位置， 窗口不回退查找O(1)！
        preSumMap.put(0, -1);  // 边界
        int maxLen = 0;
        int sum = 0;
        for (int i = 0; i < array.length; i++) {  // 遍历每个位置做结尾找达标最长子数组
            sum += array[i];
            // 是否存在一个前缀和加上K等于当前和
            int preSum = sum - K;
            if (preSumMap.containsKey(preSum)) {
                maxLen = Math.max(maxLen, i - preSumMap.get(preSum));
            }
            // 记录当前位置前缀和，重复出现只保留最左位置
            if (!preSumMap.containsKey(sum)) {
                preSumMap.put(sum, i);
            }
        }
        return maxLen;
    }



    // 以i位置做开头，暴力解， 时间复杂度O(N^3)
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
            int[] array = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
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
