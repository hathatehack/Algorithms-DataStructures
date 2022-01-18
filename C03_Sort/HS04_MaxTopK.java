package C03_Sort;

import C01_random.GenerateRandomArray;
import java.util.Arrays;

public class HS04_MaxTopK {
    // 给定一个整数组成的无序数组arr和一个正数值K，返回topK个最大的数。
    // 改写快排，时间复杂度O(N + K*logK)
    public static int[] maxTopK(int[] array, int K) {
        if (array == null || array.length == 0) {
            return new int[] {};
        }
        K = Math.min(array.length, K);
        array = GenerateRandomArray.copyArray(array);
        // 找倒数第K小的值，时间复杂度O(N)
        int numK = minKth(array, array.length - K);
        // 取topK个最大数
        int[] topK = new int[K];
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > numK)  // 先不包含第K大的值，若numK出现次数大于1次会导致真正的topK进不来！
                topK[index++] = array[i];
        }
        do {
            topK[index++] = numK; // 填补topK重复值
        } while (index < K);
        // 返回topK降序数组，时间复杂度O(K*logK)
        Arrays.sort(topK);
        for (int L = 0, R = topK.length - 1; L < R; L++, R--) {
            swap(topK, L, R);
        }
        return topK;
    }
    private static int minKth(int[] array, int K) {
        int L = 0;
        int R = array.length - 1;
        while (L < R) {
            int pivot = array[L + (int) (Math.random() * (R - L + 1))];
            int[] pivotBound = partition(array, L, R, pivot);
            if (K < pivotBound[0]) {
                R = pivotBound[0] - 1;
            } else if (K > pivotBound[1]) {
                L = pivotBound[1] + 1;
            } else {
                return pivot;
            }
        }
        return array[L];
    }
    private static int[] partition(int[] array, int L, int R, int pivot) {
        int lessR = L - 1;
        int greaterL = R + 1;
        int i = L;
        while (i < greaterL) {
            if (array[i] < pivot) {
                swap(array, ++lessR, i++);
            } else if (array[i] > pivot) {
                swap(array, --greaterL, i);
            } else {
                i++;
            }
        }
        return new int[] {lessR + 1, greaterL - 1};
    }


    // 利用大根堆，时间复杂度O(N + K*logN)
    public static int[] maxTopK2(int[] array, int K) {
        if (array == null || array.length == 0) {
            return new int[] {};
        }
        K = Math.min(array.length, K);
        array = GenerateRandomArray.copyArray(array);
        // 从底向上大根堆化，时间复杂度O(N)
        for (int i = array.length - 1; i >= 0; i--) {
            heapify(array, i, array.length);
        }
        // 取最大的K个数放到array末尾，时间复杂度O(K*logN)
        int heapSize = array.length;
        swap(array, 0, --heapSize);  // 弹出max置于数组末尾
        int count = 1;
        while (heapSize > 1 && count < K) {
            heapify(array, 0, heapSize);
            swap(array, 0, --heapSize);  // 弹出max置于数组末尾
            count++;
        }
        // 返回topK降序数组
        int[] topK = new int[K];
        for (int i = 0, j = array.length - 1; i < K; i++, j--) {
            topK[i] = array[j];
        }
        return topK;
    }
    // 大根堆化
    private static void heapify(int[] array, int index, int heapSize) {
        int left = index * 2 + 1;  // array[index]的左节点位置
        while (left < heapSize) {  // 先判断是否存在左孩子，后面再判断是否存在右孩子！
            int largest = left + 1 < heapSize && array[left + 1] > array[left] ? left + 1 : left;
            largest = array[largest] > array[index] ? largest : index;
            if (largest != index) {
                swap(array, index, largest);
                index = largest;
                left = index * 2 + 1;
            } else {
                break;
            }
        }
    }
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


    // 排序，时间复杂度O(N*logN)
    public static int[] maxTopK3(int[] array, int K) {
        if (array == null || array.length == 0) {
            return new int[] {};
        }
        K = Math.min(array.length, K);
        array = GenerateRandomArray.copyArray(array);
        // 排序，时间复杂度O(N*logN)
        Arrays.sort(array);
        // 返回topK降序数组
        int[] topK = new int[K];
        for (int i = 0, j = array.length - 1; i < K; i++, j--) {
            topK[i] = array[j];
        }
        return topK;
    }


    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 30;
        int maxValue = 50;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int K = (int) (Math.random() * array.length) + 1;
            int[] ans1 = maxTopK(array, K);
            int[] ans2 = maxTopK2(array, K);
            int[] ans3 = maxTopK3(array, K);
            if (!GenerateRandomArray.isEqual(ans1, ans2) ||
                    !GenerateRandomArray.isEqual(ans2, ans3)) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("K=%d array: ", K); GenerateRandomArray.printArray(array);
                System.out.print("ans1: "); GenerateRandomArray.printArray(ans1);
                System.out.print("ans2: "); GenerateRandomArray.printArray(ans2);
                System.out.print("ans3: "); GenerateRandomArray.printArray(ans3);

            }
        }
    }
}
