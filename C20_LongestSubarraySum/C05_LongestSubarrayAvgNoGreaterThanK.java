package C20_LongestSubarraySum;

import C01_random.GenerateRandomArray;

import java.util.Map;
import java.util.TreeMap;

public class C05_LongestSubarrayAvgNoGreaterThanK {
    // 给定一个整数组成的无序数组arr和一个整数值K，找到平均值小于等于K的最长子数组，返回其长度。
    // 分析技巧：
    //  等价于把原数组每个位置的值都减掉K，找累加和小于等于0的最长子数组。
    // 以i位置做开头的尝试方案，利用minSum缓存表，时间复杂度O(N)
    public static int longestSubarrayAvgNoGreaterThanK(int[] array, int K) {
        if (array == null || array.length == 0) {
            return 0;
        }
        // 预处理，把原数组每个位置的值都减掉K，转化为找累加和小于等于0的最长子数组。
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
        // 利用minSum缓存表向右扩出尽可能长的达标数组
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



    // 以i位置做结尾的尝试方案， 利用优化preSum缓存表，时间复杂度O(N*logN)
    public static int longestSubarrayAvgNoGreaterThanK2(int[] array, int K) {
        if (array == null || array.length == 0) {
            return 0;
        }
        // 利用preSumTreeMap向左扩出尽可能长的达标数组
        TreeMap<Integer, Integer> preSumTreeMap = new TreeMap<>();
        preSumTreeMap.put(0, -1);  // 边界
        int maxLen = 0;
        int sum = 0;
        int maxSum = sum;  // 记录当前位置凑到的最大累加和，舍弃降序的key，优化preSum缓存表
        for (int i = 0; i < array.length; i++) {  // 遍历每个位置做结尾找最长达标子数组
            // 把原数组每个位置的值都减掉K，转化为找累加和小于等于0的最长子数组！
            sum += array[i] - K;
            // 以i位置做结尾，尝试尽可能左扩
            int leastPreSum = sum - 0;  // 是否存在前缀和>=sum
            Map.Entry<Integer, Integer> mostLeft = preSumTreeMap.ceilingEntry(leastPreSum);
            if (mostLeft != null) {  // 能左扩，结算i位置结尾的最长达标子数组长度
                maxLen = Math.max(maxLen, i - mostLeft.getValue());
            }
            // 记录当前位置累加和，除了重复出现、更小累加和
            // 如果[i,j]...[i,j+n]前缀和都是递减的且都>=K，那么更小的前缀和会使左边界右缩到j+n，而不是最左的j ！！
            if (sum > maxSum) {
                preSumTreeMap.put(sum, i);
                maxSum = sum;
            }
        }
        return maxLen;
    }

    // 以i位置做结尾的尝试方案，利用优化preSum缓存表（数组版），时间复杂度O(N*logN)
    public static int longestSubarrayAvgNoGreaterThanK2_1(int[] array, int K) {
        if (array == null || array.length == 0) {
            return 0;
        }
        // 从左往右遍历每个位置记录该位置前面凑到的最大累加和
        int[] preSumArray = new int[array.length + 1];
        preSumArray[0] = 0;  // 边界
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i] - K;
            preSumArray[i + 1] = Math.max(preSumArray[i], sum);  // 记录当前位置前面凑到的最大累加和，降序的优化为前一个的值，保证升序记录！
        }
        // 利用preSumArray缓存表向左扩尽可能长的达标数组
        sum = 0;
        int maxLen = 0;
        for (int i = 0; i < array.length; i++) {  // 遍历每个位置做结尾找最长达标子数组
            sum += array[i] - K;
            // 以i位置做结尾，尝试尽可能左扩
            int leastPreSum = sum - 0;  // 是否存在前缀和>=sum
            int mostLeft = firstNoLessIndex(preSumArray, leastPreSum);
            if (mostLeft != -1) {  // 能左扩，结算i位置做结尾的最长达标子数组长度
                maxLen = Math.max(maxLen, i - mostLeft + 1);
            }
        }
        return maxLen;
    }
    private static int firstNoLessIndex(int[] ascendingArray, int comparison) {
        int ans = -1;
        int left = 0;
        int right = ascendingArray.length - 1;
        int mid = 0;
        while (left <= right) {
            mid = left + ((right - left) >> 1);
            if (ascendingArray[mid] < comparison) {
                left = mid + 1;
            } else {  // >=comparison，记录位置
                right = mid - 1;
                ans = mid;
            }
        }
        return ans;
    }



    // 以i位置做开头，暴力解， 时间复杂度O(N^3)
    public static int longestSubarrayAvgNoGreaterThanK3(int[] array, int K) {
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
            int ans2_1 = longestSubarrayAvgNoGreaterThanK2_1(array, K);
            int ans3 = longestSubarrayAvgNoGreaterThanK3(array, K);
            if (ans1 != ans2 || ans1 != ans2_1 || ans1 != ans3) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d, ans2_1=%d, ans3=%d  K=%d array: ", ans1, ans2, ans2_1, ans3, K);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
