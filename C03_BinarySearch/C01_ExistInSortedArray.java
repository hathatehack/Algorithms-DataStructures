package C03_BinarySearch;

import C01_random.GenerateRandomArray;
import java.util.Arrays;

public class C01_ExistInSortedArray {
    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxsize = 10;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxsize, maxValue);
            Arrays.sort(array);
            int search = (int)((maxValue + 1) * Math.random()) - (int)((maxValue + 1) * Math.random());
            if (exist(array, search) != exist2(array, search)) {
                System.out.printf("Oops! i=%d\n", i);
            }
        }
    }

    public static boolean exist(int[] sortedArray, int search) {
        if (sortedArray == null || sortedArray.length == 0) {
            return false;
        }
        int L = 0;
        int R = sortedArray.length - 1;
        int mid = 0;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (sortedArray[mid] == search) {
                return true;
            } else if (sortedArray[mid] < search) {
                L = mid + 1;
            } else {
                R = mid - 1;
            }
        }
        return false;
    }


    public static boolean exist2(int[] sortedArray, int search) {
        for (int val : sortedArray) {
            if (val == search)
                return true;
        }
        return false;
    }
}
