package C04_Sort;

import C01_random.GenerateRandomArray;

public class MS01_SmallSum {
    public static void main(String[] args) {
        int testTime = 100;
        int maxSize = 10;
        int maxValue = 100;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int[] arr2 = GenerateRandomArray.copyArray(arr1);
//            GenerateRandomArray.printArray(arr1);
            int ans1 = smallSum(arr1);
            int ans2 = smallSum2(arr2);
            if (ans1 != ans2) {
                System.out.format("%d出错了！\n", i);
                GenerateRandomArray.printArray(arr1);
                GenerateRandomArray.printArray(arr2);
                break;
            }
        }
    }

    // 一个数组中，一个数左边比它小的所有数之和，叫数的小和；所有数的小和累加，叫数组小和。
    // 归并排序的思想，分而治之，把无序变有序，域内单调，多轮倍增步长从左到右进行域内处理，可以有效处理小和计算。
    static public int smallSum(int[] array) {
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
        int sum = 0;
        while (l <= mid && r <= right) {
            sum += array[l] < array[r] ? array[l] * (right - r + 1) : 0;  // array[l]<array[r]域内单调可知array[l]<array[r~right]
            tmp[t++] = array[l] < array[r] ? array[l++] : array[r++];  // 与稳定版归并排序不同，左右有等值时先取出右值，防止干扰小和计算。
        }
        while (l <= mid) tmp[t++] = array[l++];
        while (r <= right) tmp[t++] = array[r++];
        for (int i = 0; i < tmp.length; i++) {
            array[left++] = tmp[i];
        }
        return sum;
    }




    // for check with smallSum
    static public int smallSum2(int[] array) {
        if (array == null && array.length <= 1) {
            return 0;
        }
        int sum = 0;
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < i; j++) {
                sum += array[j] < array[i] ? array[j] : 0;
            }
        }
        return sum;
    }
}
