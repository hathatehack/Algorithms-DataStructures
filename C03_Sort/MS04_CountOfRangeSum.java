package C03_Sort;

import C05_PreSum.PreSum;
import C01_random.GenerateRandomArray;

public class MS04_CountOfRangeSum {
    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 100;
        int maxValue = 100;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int[] arr2 = GenerateRandomArray.copyArray(arr1);
            int[] r = {(int)(Math.random() * maxValue + 1), (int)(Math.random() * maxValue + 1)};
            int lower = Math.min(r[0], r[1]);
            int upper = Math.max(r[0], r[1]);
//            System.out.printf("lower=%d,upper=%d,arr=", lower,upper); GenerateRandomArray.printArray(arr1);
            int ans1 = countRangeSum(arr1, lower, upper);
            int ans2 = countRangeSum2(arr2, lower, upper);
            if (ans1 != ans2) {
                System.out.format("%d出错了！ lower=%d, upper=%d\n", i, lower, upper);
                GenerateRandomArray.printArray(arr1);
                GenerateRandomArray.printArray(arr2);
                break;
            }
            System.out.println(ans1);
        }


        int ans1 = countRangeSum(new int[]{-2, 5, -1}, -2, 2);
        int ans2 = countRangeSum2(new int[]{-2, 5, -1}, -2, 2);
        if (ans1 != ans2) {
            System.out.println("出错了！");
            System.out.println(ans1);
            System.out.println(ans2);
        }
    }

    // https://leetcode.com/problems/count-of-range-sum
    // 一个数组中，连续区间内的和sum(i,j)位于范围[lower,upper]，i<=j，求这种区间的总个数。
    // 例如[-2, 5, -1],lower=-2,upper=2   存在3个区间：[0,0]=-2、[2,2]=-1、[0,2]=2
    // 求区间和可以先转化为前缀和，再利用归并排序的思想，分而治之，把无序变有序，域内单调，多轮倍增步长从左到右进行域内处理，可以方便地求解。
    static public int countRangeSum(int[] array, int lower, int upper) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int[] preSum = new PreSum(array).preSum;
        return process(preSum, 0, array.length - 1, lower, upper);
    }

    static public int process(int[] preSum, int left, int right, int lower, int upper) {
        if (left == right) {
            return (preSum[left] >= lower && preSum[left] <= upper) ? 1 : 0;
        }
        int mid = left + ((right - left) >> 1);
        return process(preSum, left, mid, lower, upper) +
                process(preSum, mid + 1, right, lower, upper) +
                mergeAndSort(preSum, left, mid, right, lower, upper);
    }

    static public int mergeAndSort(int[] preSum, int left, int mid, int right, int lower, int upper) {
        int count = 0;
        int windowL = left;
        int windowR = left;
        for (int r = mid + 1; r <= right; r++) {
            // 利用区间和范围可以反推出一个前缀和的子前缀和的区间范围，对于归并排序即根据右部确定左部的值范围。
            int min = preSum[r] - upper;
            int max = preSum[r] - lower;
            // left[1,2,4,6] right[8,10] range[2,5]， left、right有序递增，min/max随着right向右遍历只会变大；
            // 域内单调递增，所以min/max也单调增大，所以窗口指针只会前进不会回退。
            while (windowR <= mid && preSum[windowR] <= max) windowR++;
            while (windowL <= mid && preSum[windowL] < min) windowL++;
            count += windowR - windowL;
        }

        int[] tmp = new int[right - left + 1];
        int l = left;
        int r = mid + 1;
        int t = 0;
        while (l <= mid && r <= right) {
            tmp[t++] = preSum[l] <= preSum[r] ? preSum[l++] : preSum[r++];
        }
        while (l <= mid) tmp[t++] = preSum[l++];
        while (r <= right) tmp[t++] = preSum[r++];
        for (int i = 0; i < tmp.length; i++) {
            preSum[left++] = tmp[i];
        }

        return count;
    }




    // For check with countRangeSum
    public static int countRangeSum2(int[] array, int lower, int upper) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int count = 0;
        PreSum preSum = new PreSum(array);
        for (int l = 0; l < preSum.preSum.length; l++) {
            for (int r = l; r < preSum.preSum.length; r++) {
                int sum = preSum.rangeSum(l, r);
                if (sum >= lower && sum <= upper) {
                    count += 1;
                }
            }
        }
        return count;
    }
}
