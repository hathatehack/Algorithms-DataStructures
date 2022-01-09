package C18_WindowMaxMin;

import C01_random.GenerateRandomArray;
import java.util.LinkedList;

public class C01_SlidingWindowMax {
    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxsize = 10;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxsize, maxValue);
            int w = (int)((array.length + 1) * Math.random());
            int[] ans1 = slidingWindowMax(array, w);
            int[] ans2 = slidingWindowMax2(array, w);
            if (!GenerateRandomArray.isEqual(ans1, ans2)) {
                System.out.printf("Oops! i=%d\n", i);
            }
        }
    }

    // 假设一个固定大小为W的窗口，从左到右滑过arr，返回每一次滑动窗口内最大值。
    // 例如，arr = [4,3,5,4,3,3,6,7], W = 3, 返回：[5,5,5,4,6,7]
    // 时间复杂度O(N)
    // 技巧分析：R只递增不回退，
    public static int[] slidingWindowMax(int[] array, int w) {
        if (array == null || w < 1 || w > array.length) {
            return null;
        }
        LinkedList<Integer> maxWindow = new LinkedList<>();  // 存放窗口内值的下标！
        int[] maxArray = new int[array.length - w + 1];
        int ansIndex = 0;
        // 孵化w窗口
        for (int R = 0; R < w - 1; R++) {
            while (!maxWindow.isEmpty() && array[maxWindow.peekLast()] <= array[R]) { // 清理窗口内<=右窗口的值的下标，减少后续窗口的重复回溯数量且保证最左对应最大/最小。
                maxWindow.pollLast();
            }
            maxWindow.addLast(R);  // 入队新元素的下标!!
        }
        // 滑动w窗口
        for (int R = w - 1; R < array.length; R++) {
            while (!maxWindow.isEmpty() && array[maxWindow.peekLast()] <= array[R]) { // 清理窗口内<=右窗口的值的下标，减少后续窗口的重复回溯数量且保证最左对应最大/最小。
                maxWindow.pollLast();
            }
            maxWindow.addLast(R);  // 入队新元素的下标!!
            if (maxWindow.peekFirst() == R - w) {  // 窗口每次右滑一格后需弹出在左窗口之前的下标
                maxWindow.pollFirst();
            }
            // 获取窗口每次右滑新窗口的max
            maxArray[ansIndex++] = array[maxWindow.peekFirst()];
        }
        return maxArray;
    }


    public static int[] slidingWindowMax2(int[] array, int w) {
        if (array == null || w < 1 || w > array.length) {
            return null;
        }
        int[] maxArray = new int[array.length - w + 1];
        int L = 0;
        int R = w - 1;
        for (; R < array.length; L++, R++) {
            int max = array[L];
            for (int i = L + 1; i <= R; i++) {  // 右窗口回退再遍历
                if (max <array[i])
                    max = array[i];
            }
            maxArray[L] = max;
        }
        return maxArray;
    }
}
