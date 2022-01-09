package C03_Sort;

import C01_random.GenerateRandomArray;

import java.util.Arrays;

public class heapSort {
    static public void main(String[] args) {
        int testTime = 100000;
        int maxSize = 100;
        int maxValue = 100;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int[] arr2 = GenerateRandomArray.copyArray(arr1);
//            GenerateRandomArray.printArray(arr1);
            heapSort(arr1);
            Arrays.sort(arr2);
            if (!GenerateRandomArray.isEqual(arr1, arr2)) {
                System.out.format("%d出错了！\n", i);
                GenerateRandomArray.printArray(arr1);
                GenerateRandomArray.printArray(arr2);
                break;
            }
        }
    }

    // O(N*logN)
    static public void heapSort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        // 1.先调整成大根堆
//        // O(N*logN)
////        for (int i = 0; i < array.length; i++) {
////            heapInsert(array, i);
////        }
        // O(N)
        for (int i = array.length - 1; i >= 0; i--) {
            heapify(array, i, array.length);
        }
        // 2.进行N次首尾交换，大值置于末尾。
        int heapSize = array.length;
        swap(array, 0, --heapSize);
        while (heapSize > 1) {
            heapify(array, 0, heapSize);  // O(logN)
            swap(array, 0, --heapSize);   // O(1)
        }
    }

    // 从尾向头遍历
    static public void heapInsert(int[] array, int index) {
        // 比父节点大则停、移动到0位置则停
        while (array[index] > array[(index - 1) / 2]) {  // index-1为负数时不能>>1只能/2
            swap(array, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    // 从头向尾遍历
    static public void heapify(int[] array, int index, int heapSize) {
        int left = (index << 1) + 1;
        while (left < heapSize) {  // 先判断是否存在左孩子，后面得再判断是否存在右孩子!
            int largest = left + 1 < heapSize && array[left + 1] > array[left] ? left + 1 : left;
            largest = array[largest] > array[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(array, largest, index);
            index = largest;
            left = (index << 1) + 1;
        }
    }

    static public void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
