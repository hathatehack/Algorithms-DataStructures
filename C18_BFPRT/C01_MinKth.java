package C18_BFPRT;

import C01_random.GenerateRandomArray;

import java.util.Arrays;
import java.util.PriorityQueue;

public class C01_MinKth {
    // 给定一个整数组成的无序数组arr和一个正数值K，找到在数组中第K小的数，返回其值。
    // 利用bfprt算法，时间复杂度O(N)
    public static Integer minKth(int[] array, int K) {
        if (array == null || K < 1 || K > array.length) {
            return null;
        }
        return bfprt(array, 0, array.length - 1, K - 1);
    }
    // 返回有序array[L,R]中第K位置的值
    private static int bfprt(int[] array, int L, int R, int K) {
        if (L == R) {
            return array[L];
        }
        int pivot = medianOfMedians(array, L, R);  //
        int[] pivotBound = partition(array, L, R, pivot);  // 以pivot值为中轴将数组划分为3个区
        // 根据K位置选择一个区间进入
        if (K < pivotBound[0]) {         // K位置在左区间
            return bfprt(array, L, pivotBound[0] - 1, K);
        } else if (K > pivotBound[1]) {  // K位置在右区间
            return bfprt(array, pivotBound[1] + 1, R, K);
        } else {                         // pivotBound[0] <= K <= pivotBound[1]
            return pivot;
        }
    }
    // 5个数一组，小组内排序，每组的中位数组成新数组，返回新数组的中位数
    private static int medianOfMedians(int[] array, int L, int R) {
        int[] medians = new int[(R - L + 5) / 5];
        for (int i = 0; i < medians.length; i++) {
            int start = L + i * 5;
            medians[i] = getMedian(array, start, Math.min(R, start + 4));
        }
        return bfprt(medians, 0, medians.length - 1, medians.length / 2);
    }
    private static int getMedian(int[] array, int L, int R) {
        insertSort(array, L, R);
        return array[(L + R) / 2];
    }
    private static void insertSort(int[] array, int L, int R) {
        for (int i = L + 1; i <= R; i++) {
            for (int j = i - 1; j >= L && array[j] > array[j + 1]; j--) {
                swap(array, j, j + 1);
            }
        }
    }
    private static int[] partition(int[] array, int L, int R, int pivot) {
        int lessR = L - 1;     // 小于pivot且最右的值的位置
        int greaterL = R + 1;  // 大于pivot且最左的值的位置
        int i = L;
        while (i < greaterL) {
            if (array[i] < pivot) {         // 小值放到pivot区前面
                swap(array, ++lessR, i++);
            } else if (array[i] > pivot) {  // 大值放到pivot区后面，i不变下次继续判断被交换到前面的值大小。
                swap(array, --greaterL, i);
            } else {                        // 等于pivot，lessR不变，继续处理下一个值
                i++;
            }
        }
        return new int[] {lessR + 1, greaterL - 1};
    }
    private static void swap(int[] array, int i, int j) {
        if (i == j)
            return;
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }



    // 改写快排（递归版），时间复杂度O(N)，额外空间O(logN)
    public static Integer minKth2(int[] array, int K) {
        if (array == null || K < 1 || K > array.length) {
            return null;
        }
        array = GenerateRandomArray.copyArray(array);
        return process(array, 0, array.length - 1, K - 1);
    }
    private static int process(int[] array, int L, int R, int K) {
        if (L == R) {
            return array[L];
        }
        int pivot = array[L + (int)(Math.random() * (R - L + 1))];  // array[L,R]随机选一个值做轴
        int[] pivotBound = partition(array, L, R, pivot);  // 以pivot值为中轴将数组划分为<=>三个区
        // 根据K位置选择一个区间进入
        if (pivotBound[0] > K){         // K位置在左区间
            return process(array, L, pivotBound[0] - 1, K);
        } else if (pivotBound[1] < K){  // K位置在右区间
            return process(array, pivotBound[1] + 1, R, K);
        } else {                        // pivotBound[0] <= K <= pivotBound[1]
            return pivot;
        }
    }

    // 改写快排（迭代版），时间复杂度O(N)，额外空间O(1)
    public static Integer minKth2_1(int[] array, int K) {
        if (array == null || K < 1 || K > array.length) {
            return null;
        }
        K--;
        array = GenerateRandomArray.copyArray(array);
        int L = 0;
        int R = array.length - 1;
        while (L < R) {
            int pivot = array[L + (int)(Math.random() * (R - L + 1))];
            int[] pivotBound = partition(array, L, R, pivot);
            // 根据K位置选择一个区间进入
            if (pivotBound[0] > K) {         // K位置在左侧
                R = pivotBound[0] - 1;
            } else if (pivotBound[1] < K) {  // K位置在右侧
                L = pivotBound[1] + 1;
            } else {                         // pivotBound[0] <= K <= pivotBound[1]
                return pivot;
            }
        }
        return array[L];
    }



    // 利用大根堆，时间复杂度O(N*logK)
    public static Integer minKth3(int[] array, int K) {
        if (array == null || K < 1 || K > array.length) {
            return null;
        }
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> o2 - o1);
        for (int i = 0; i < K; i++) {
            maxHeap.add(array[i]);
        }
        for (int i = K; i < array.length; i++) {
            if (array[i] < maxHeap.peek()) {
                maxHeap.poll();
                maxHeap.add(array[i]);
            }
        }
        return maxHeap.peek();
    }



    // 排序，时间复杂度O(N*logN)
    public static Integer minKth4(int[] array, int K) {
        if (array == null || K < 1 || K > array.length) {
            return null;
        }
        K--;
        array = GenerateRandomArray.copyArray(array);
        Arrays.sort(array);
        return array[K];
    }


    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 30;
        int maxValue = 50;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int K = (int)(Math.random() * array.length) + 1;
            Integer ans1 = minKth(array, K);
            Integer ans2 = minKth2(array, K);
            Integer ans2_1 = minKth2_1(array, K);
            Integer ans3 = minKth3(array, K);
            Integer ans4 = minKth4(array, K);
            if (!(ans1 == null && ans2 == null && ans2_1 == null && ans3 == null && ans4 == null) &&
                    ((ans1 == null || ans2 == null || ans2_1 == null || ans3 == null || ans4 == null) ||
                            (!ans1.equals(ans2) || !ans1.equals(ans2_1) || !ans1.equals(ans3) || !ans1.equals(ans4)))) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d, ans2_1=%d, ans3=%d, ans4=%d  K=%d array:", ans1, ans2, ans2_1, ans3, ans4, K);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
