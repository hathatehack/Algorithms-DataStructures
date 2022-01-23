package C04_Sort;

import C01_random.GenerateRandomArray;

public class MS03_BiggerThanRightTwice {
    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 10;
        int maxValue = 100;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int[] arr2 = GenerateRandomArray.copyArray(arr1);
//            GenerateRandomArray.printArray(arr1);
            int ans1 = biggerTwice(arr1);
            int ans2 = biggerTwice2(arr2);
            if (ans1 != ans2) {
                System.out.format("%d出错了！\n", i);
                GenerateRandomArray.printArray(arr1);
                GenerateRandomArray.printArray(arr2);
                break;
            }
        }
    }

    // 一个数组中，一个数大于后面任何一个数*2，求这种数的总个数。
    // 归并排序的思想，分而治之，把无序变有序，域内单调，多轮倍增步长从左到右进行域内处理，可以方便地求解。
    static public int biggerTwice(int[] array) {
        if (array == null || array.length <= 1) {
            return 0;
        }
        return process(array, 0, array.length - 1);
    }

    static public int process(int[] array, int left, int right) {
        if (left == right) {
            return 0;
        }
        int mid = left + ((right - left) >> 1);
        return process(array, left, mid) +
                process(array, mid + 1, right) +
                mergeAndSort(array, left, mid, right);
    }

    static public int mergeAndSort(int[] array, int left, int mid, int right) {
        int count = 0;
        int windowR = mid + 1;
        for (int l = left; l <= mid; l++) {
            // array[l]>array[windowR]*2域内单调可知array[l]>array(mid~windowR]*2
            while (windowR <= right && array[l] > (array[windowR]*2)) {
                windowR++;
            }
            count += windowR - (mid + 1);
        }

        int[] tmp = new int[right - left + 1];
        int l = left;
        int r = mid + 1;
        int t = 0;
        while (l <= mid && r <= right) {
            tmp[t++] = array[l] <= array[r] ? array[l++] : array[r++];
        }
        while (l <= mid) tmp[t++] = array[l++];
        while (r <= right) tmp[t++] = array[r++];
        for (int i = 0; i < tmp.length; i++) {
            array[left++] = tmp[i];
        }

        return count;
    }




    // For check with biggerTwice
    static public int biggerTwice2(int[] array) {
        int count = 0;
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < i; j++) {
                count += array[j] > (array[i] << 1) ? 1 : 0;
            }
        }
        return count;
    }
}
