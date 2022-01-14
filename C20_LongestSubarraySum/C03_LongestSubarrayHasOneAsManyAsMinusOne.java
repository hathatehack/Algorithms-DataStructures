package C20_LongestSubarraySum;

import C01_random.GenerateRandomArray;

import java.util.HashMap;

public class C03_LongestSubarrayHasOneAsManyAsMinusOne {
    // 给定一个整数组成的无序数组arr，找到1和-1数量一样多的最长子数组，返回其长度。
    // 分析技巧：
    //  1和-1数量一样多的子数组，忽略剩余的整数，那累加和就等于0.
    // 以i位置做结尾的尝试方案， 利用preSum缓存表，时间复杂度O(N)
    public static int maxLen(int[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        HashMap<Integer, Integer> preSumMap = new HashMap<>(); // key:前缀和 value:最早出现的位置， 记录累加和使窗口不回退！
        preSumMap.put(0, -1);  // 边界
        int curSum = 0;
        int maxLen = 0;
        // 遍历每个位置做结尾找达标最长子数组
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1 || array[i] == -1) {
                curSum += array[i];
            }
            // 查找前缀和
            if (preSumMap.containsKey(curSum)) {
                maxLen = Math.max(maxLen, i - preSumMap.get(curSum));
            } else { // 记录当前位置前缀和，重复出现只保留最左位置
                preSumMap.put(curSum, i);
            }
        }
        return maxLen;
    }



    // O(N^3)
    public static int maxLen2(int[] array) {
        int maxLen = 0;
        for (int L = 0; L < array.length; L++) {
            for (int R = L; R < array.length; R++) {
                int one = 0;
                int minusOne = 0;
                // 累加L到R
                for (int i = L; i <= R; i++) {   // 每次R尝试右扩，R就发生回退遍历！
                    if (array[i] == 1)
                        one++;
                    if (array[i] == -1)
                        minusOne++;
                }
                if (one == minusOne)
                    maxLen = Math.max(maxLen, R - L + 1);
            }
        }
        return maxLen;
    }


    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 30;
        int maxValue = 2;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int K = (int)(Math.random() * maxValue) + 1;
            int ans1 = maxLen(array);
            int ans2 = maxLen2(array);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d  K=%d array: ", ans1, ans2, K);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
