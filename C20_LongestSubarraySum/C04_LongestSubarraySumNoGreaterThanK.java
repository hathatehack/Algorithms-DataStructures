package C20_LongestSubarraySum;

import C01_random.GenerateRandomArray;

import java.util.NavigableMap;
import java.util.TreeMap;

public class C04_LongestSubarraySumNoGreaterThanK {
    // 给定一个整数组成的无序数组arr和一个整数值K，找到累加和小于等于K的最长子数组，返回其长度。
    // 分析技巧：
    //  有两种尝试方案：
    //   1)以i位置做开头能右扩多远，预处理结构minSum缓存表
    //   2)以i位置做结尾能左扩多远，预处理结构preSum缓存表
    //
    // 以i位置做开头的尝试方案， 遍历+minSum缓存表，时间复杂度O(N)
    public static int longestSubarraySumNoGreaterThanK(int[] array, int K) {
        if (array == null || array.length == 0) {
            return 0;
        }
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
        if (minSumEnd[0] == array.length - 1 && minSum[0] <= K) { // 例如数组{0, 0, 0}找累加和<=0的最长子数组
            return array.length;
        }
        // 从左往右利用minSum缓存表凑出尽可能长的达标数组
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
            if (end > i) {
                sum -= array[i]; // 更新[i+1,end)的累加和
            } else {
                end++;  //右边界得大于左边界
            }
        }
        return maxLen;
    }


    // 以i位置做结尾的尝试方案， 遍历+preSum缓存表，时间复杂度O(N*(logN+M))
    public static int longestSubarraySumNoGreaterThanK2(int[] array, int K) {
        TreeMap<Integer, Integer> preSumTreeMap = new TreeMap<>(); // key:前缀和 value:最早出现的位置， 记录累加和使窗口不回退！
        preSumTreeMap.put(0, -1);  // 边界
        int maxLen = 0;
        int sum = 0;
        // 遍历每个位置做结尾找最长达标子数组
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
            // 以i位置做结尾，尝试尽可能左扩
            int leastPreSum = sum - K; // 找大于等于sum-K的最左前缀和
            NavigableMap<Integer, Integer> results = preSumTreeMap.subMap(leastPreSum, true, Integer.MAX_VALUE, true); // O(logN)
            if (results.size() != 0) {
                // 例如数组{10, -1, -5}找累加和<=0的最长子数组
                int mostLeft = results.values().stream().min((i1, i2) -> i1 - i2).get();  // 遍历找最左前缀和， O(M)
                // 结算i位置结尾的最长达标子数组长度
                maxLen = Math.max(maxLen, i - mostLeft);
            }
            // 记录当前位置前缀和，重复出现的前缀和只取最早出现的位置
            if (!preSumTreeMap.containsKey(sum)) {
                preSumTreeMap.put(sum, i);
            }
        }
        return maxLen;
    }



    // 以i位置做结尾的尝试方案， 遍历+缓存表，时间复杂度O(N*logN)
    public static int longestSubarraySumNoGreaterThanK3(int[] arr, int k) {
        int[] h = new int[arr.length + 1];
        int sum = 0;
        h[0] = sum;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            h[i + 1] = Math.max(sum, h[i]);
        }
        sum = 0;
        int res = 0;
        int pre = 0;
        int len = 0;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            pre = getLessIndex(h, sum - k);
            len = pre == -1 ? 0 : i - pre + 1;
            res = Math.max(res, len);
        }
        return res;
    }
    private static int getLessIndex(int[] arr, int num) {
        int low = 0;
        int high = arr.length - 1;
        int mid = 0;
        int res = -1;
        while (low <= high) {
            mid = (low + high) / 2;
            if (arr[mid] >= num) {
                res = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }



    // 以i位置做开头，暴力解， 时间复杂度O(N^3)
    public static int longestSubarraySumNoGreaterThanK4(int[] array, int K) {
        int maxLen = 0;
        for (int L = 0; L < array.length; L++) {
            for (int R = L; R < array.length; R++) {
                int sum = 0;
                // 累加L到R
                for (int i = L; i <= R; i++) {   // 每次R尝试右扩，R就发生回退遍历！
                    sum += array[i];
                }
                if (sum <= K)
                    maxLen = Math.max(maxLen, R - L + 1);
            }
        }
        return maxLen;
    }


    public static void main(String[] args) {
//        int ans1 = longestSubarraySumNoGreaterThanK(new int[] {0, 0, 0}, 0);
//        int ans2 = longestSubarraySumNoGreaterThanK2(new int[] {10, -1, -5}, 0);
        int testTimes = 100000;
        int maxSize = 50;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int K = (int)(Math.random() * maxValue) + 1;
            int ans1 = longestSubarraySumNoGreaterThanK(array, K);
            int ans2 = longestSubarraySumNoGreaterThanK2(array, K);
            int ans3 = longestSubarraySumNoGreaterThanK3(array, K);
            int ans4 = longestSubarraySumNoGreaterThanK4(array, K);
            if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d, ans3=%d, ans4=%d  K=%d array: ", ans1, ans2, ans3, ans4, K);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
