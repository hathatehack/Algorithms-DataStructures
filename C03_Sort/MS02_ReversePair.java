package C03_Sort;

import C01_random.GenerateRandomArray;

public class MS02_ReversePair {
    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 10;
        int maxValue = 100;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int[] arr2 = GenerateRandomArray.copyArray(arr1);
//            GenerateRandomArray.printArray(arr1);
            int ans1 = reversePairCount(arr1);
            int ans2 = reversePairCount2(arr2);
            if (ans1 != ans2) {
                System.out.format("%d出错了！\n", i);
                GenerateRandomArray.printArray(arr1);
                GenerateRandomArray.printArray(arr2);
                break;
            }
        }
    }

    // 一个数组中，任何一个数a比后面任何一个b大，即ab是降序的，就称为逆序对，返回数组中所有的逆序对数量。
    // 归并排序的思想，分而治之，把无序变有序，域内单调，多轮倍增步长从左到右进行域内处理，可以有效计算逆序对。
    static public int reversePairCount(int[] array) {
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
        int[] tmp = new int[right - left + 1];
        int l = left;
        int r = mid + 1;
        int t = 0;
        int count = 0;
        while (l <= mid && r <= right) {
            count += array[l] <= array[r] ? 0 : (mid - l + 1);  // array[l]>array[r]域内单调可知array[l~mid]>array[r]
            tmp[t++] = array[l] <= array[r] ? array[l++] : array[r++];
        }
        while (l <= mid) tmp[t++] = array[l++];
        while (r <= right) tmp[t++] = array[r++];
        for (int i = 0; i < tmp.length; i++) {
            array[left++] = tmp[i];
        }
        return count;
    }




    // For check with reversePairCount
    static public int reversePairCount2(int[] array) {
        int count = 0;
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < i; j++) {
                count += array[j] > array[i] ? 1 : 0;
            }
        }
        return count;
    }
}
