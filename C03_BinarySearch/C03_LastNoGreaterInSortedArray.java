package C03_BinarySearch;

import C01_random.GenerateRandomArray;
import java.util.Arrays;

public class C03_LastNoGreaterInSortedArray {
    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxsize = 10;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxsize, maxValue);
            Arrays.sort(array);
            int search = (int)((maxValue + 1) * Math.random()) - (int)((maxValue + 1) * Math.random());
            int ans1 = lastNoGreater(array, search);
            int ans2 = lastNoGreater2(array, search);
            int ans3 = lastNoGreater3(array, search);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.printf("Oops! i=%d\n", i);
            }
        }
    }

    // 在有序数组中找<=某个数的最右位置
    public static int lastNoGreater(int[] sortedArray, int comparison) {
        int L = 0;
        int R = sortedArray.length - 1;
        int mid = 0;
        int index = -1;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (sortedArray[mid] <=comparison) {
                index = mid;
                L = mid + 1;
            } else {
                R = mid - 1;
            }
        }
        return index;
    }



    public static int lastNoGreater2(int[] sortedArray, int comparison) {
        for (int i = sortedArray.length - 1; i >= 0; i--) {
            if (sortedArray[i] <= comparison)
                return i;
        }
        return -1;
    }

    public static int lastNoGreater3(int[] sortedArray, int comparison) {
        for (int i = 0; i < sortedArray.length; i++) {
            if (sortedArray[i] > comparison)
                return i - 1;
        }
        return sortedArray.length - 1;
    }
}
