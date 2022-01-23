package C04_Sort;

import java.util.Arrays;

import C05_Heap.HeapEnhance;
import C01_random.GenerateRandomArray;

public class HS01_SortArrayDistanceLessK {
    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 100;
        int maxValue = 100;
        for (int i = 0; i < testTime; i++) {
            int K = (int)(Math.random() * maxSize);
            int[] arr = GenerateRandomArray.generateRandomArraySortDistanceNoMoreK(maxSize, maxValue, K);
            int[] arr1 = GenerateRandomArray.copyArray(arr);
            int[] arr2 = GenerateRandomArray.copyArray(arr);
//            System.out.println(Arrays.toString(arr));
//            System.out.println("K=" + K);
            sortArrayDistanceLessK(arr1, K);
            Arrays.sort(arr2);
            if (!GenerateRandomArray.isEqual(arr1, arr2)) {
                System.out.format("%d出错了, K=%d！\n", i, K);
                GenerateRandomArray.printArray(arr);
                GenerateRandomArray.printArray(arr1);
                break;
            }
        }
    }

    // 有一个几乎有序的数组，排序时每个元素移动的距离不会超过K，K相对于数组长度是比较小于的，请选择一个合适的排序策略。
    static public void sortArrayDistanceLessK(int[] array, int k) {
        if (array == null || array.length <= 1 || k == 0) {
            return;
        }
//        PriorityQueue<Integer> heap = new PriorityQueue<>();  // 默认小根堆
        HeapEnhance<Integer> heap = new HeapEnhance<Integer>((o1, o2) -> o1 - o2);
        int index = 0;
        int limit = Math.min(array.length - 1, k - 1);
        for (; index <= limit; index++) {
            heap.push(array[index]);
        }
        // K窗口每次弹出最小值并向右压入一个值，直至抵达末尾。
        int i = 0;
        for (; index < array.length; i++, index++) {
            heap.push(array[index]);
            array[i] = heap.pop();
        }
        while (!heap.isEmpty()) {
            array[i++] = heap.pop();
        }
    }
}
