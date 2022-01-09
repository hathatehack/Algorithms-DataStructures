package C03_BinarySearch;

import C01_random.GenerateRandomArray;
import java.util.Arrays;

public class C02_FirstNoLessInSortedArray {
    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxsize = 10;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxsize, maxValue);
            Arrays.sort(array);
            int search = (int)((maxValue + 1) * Math.random()) - (int)((maxValue + 1) * Math.random());
            if (firstNoLess(array, search) != firstNoLess2(array, search)) {
                System.out.printf("Oops! i=%d\n", i);
            }
        }
    }

    // 在有序数组中找>=某个数的最左位置
    public static int firstNoLess(int[] sortedArray, int comparison) {
        int L = 0;
        int R = sortedArray.length - 1;
        int mid = 0;
        int index = -1;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (sortedArray[mid] < comparison) {
                L = mid + 1;
            } else {  // mid >= comparison
                index = mid;
                R = mid - 1;
            }
        }
        return index;
    }


    public static int firstNoLess2(int[] sortedArray, int comparison) {
        for (int i = 0; i < sortedArray.length; i++) {
            if (sortedArray[i] >= comparison)
                return i;
        }
        return -1;
    }
}
