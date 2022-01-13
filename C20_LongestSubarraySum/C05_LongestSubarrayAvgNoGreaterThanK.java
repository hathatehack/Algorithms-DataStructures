package C20_LongestSubarraySum;

import C01_random.GenerateRandomArray;
import java.util.TreeMap;

public class C05_LongestSubarrayAvgNoGreaterThanK {
    // 给定一个整数组成的无序数组arr和一个整数值K，找到平均值小于等于K的最长子数组，返回其长度。
    // 分析技巧：
    //  等价于把原数组每个位置的值都减掉K，找累加和小于等于0的最长子数组。
    // 以i位置做开头的尝试方案，遍历+minSum缓存表，时间复杂度O(N)
    public static int longestSubarrayAvgNoGreaterThanK(int[] array, int K) {
        if (array == null || array.length == 0) {
            return 0;
        }
        // 预处理
        array = GenerateRandomArray.copyArray(array);
        for (int i = 0; i < array.length; i++)
            array[i] -= K;
        K = 0;
        // 从右往左遍历每个位置求右扩所能得到的更小累加和、结束位置
        int[] minSum = new int[array.length];
        int[] minSumEnd = new int[array.length];
        minSum[array.length - 1] = array[array.length - 1];
        minSumEnd[array.length - 1] = array.length - 1;
        for (int i = array.length - 2; i >= 0; i--) {  // i依赖i+1位置的信息，所以先处理右边
            if (minSum[i + 1] <= 0) {  // 右扩能使累加和更小
                minSum[i] = array[i] + minSum[i + 1];
                minSumEnd[i] = minSumEnd[i + 1];
            } else {
                minSum[i] = array[i];
                minSumEnd[i] = i;
            }
        }
        if (minSumEnd[0] == array.length - 1 && minSum[0] <= K) {  // 整个数组的累加和小于等于K
            return array.length;
        }
        // 从左往右利用minSum凑出尽可能长的达标数组
        int maxLen = 0;
        int sum = 0;
        int end = 0;  // 达标子数组的最右边界，开区间
        // 遍历每个位置做开头找最长达标子数组
        for (int i = 0; i < array.length; i++) {
            // 以i位置做开头，尝试尽可能右扩
            while (end < array.length && sum + minSum[end] <= K) {
                sum += minSum[end];
                end = minSumEnd[end] + 1;  // 右边界，开区间
            }
            // 结算i位置开头的最长达标子数组长度
            maxLen = Math.max(maxLen, end - i);
            // 初始化i+1位置
            if (end > i) {  // 更新[i+1, end)的累加和
                sum -= array[i];
            } else {
                end++;  // 右边界得大于左边界
            }
        }
        return maxLen;
    }



    // 以i位置做结尾的尝试方案， 遍历+缓存表，时间复杂度O(N*logN)
    public static int longestSubarrayAvgNoGreaterThanK2(int[] arr, int v) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        TreeMap<Integer, Integer> origins = new TreeMap<>();
        int ans = 0;
        int modify = 0;
        for (int i = 0; i < arr.length; i++) {
            int p1 = arr[i] <= v ? 1 : 0;
            int p2 = 0;
            int query = -arr[i] - modify;
            if (origins.floorKey(query) != null) {
                p2 = i - origins.get(origins.floorKey(query)) + 1;
            }
            ans = Math.max(ans, Math.max(p1, p2));
            int curOrigin = -modify - v;
            if (origins.floorKey(curOrigin) == null) {
                origins.put(curOrigin, i);
            }
            modify += arr[i] - v;
        }
        return ans;
    }


    // 以i位置做开头，暴力解， 时间复杂度O(N^3)
    public static int longestSubarraySumNoGreaterThanK3(int[] array, int K) {
        int maxLen = 0;
        for (int L = 0; L < array.length; L++) {
            for (int R = L; R < array.length; R++) {
                int sum = 0;
                // 累加L到R
                for (int i = L; i <= R; i++) {   // 每次R尝试右扩，R就发生回退遍历！
                    sum += array[i];
                }
                double avg = (double)sum / (R - L + 1);  // double防止结果被向下取整
                if (avg <= K)
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
            int ans1 = longestSubarrayAvgNoGreaterThanK(array, K);
            int ans2 = longestSubarrayAvgNoGreaterThanK2(array, K);
            int ans3 = longestSubarraySumNoGreaterThanK3(array, K);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d, ans3=%d  K=%d array: ", ans1, ans2, ans3, K);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
