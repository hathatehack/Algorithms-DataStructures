package C19_MonotonousStack;

import C01_random.GenerateRandomArray;
import java.util.Stack;

public class C02_MaxProductOfSubarraySumTimesMinimum {
    // 给定一个只包含正整数的数组arr，arr中任何一个子数组中的累加和乘最小值，得到的乘积最大是多少？
    // 分析技巧：
    //  最大乘积必定是以某个位置的值做最小值得来的，需遍历以每个位置做最小值时的最好答案。
    //  单调栈每个元素进出栈各一次，并在出栈时可以获取该元素的某种区间范围，此特性可利用：
    //   当前元素出递增栈意味着以当前元素做最小值的区间结束了，求最大乘积就等价于找出最大的某最小值与其最小值区间乘积！
    // 时间复杂度O(N)
    public static int maxProduct(int[] array) {
        int max = Integer.MIN_VALUE;
        if (array == null || array.length == 0) {
            return max;
        }
        int size = array.length;
        // 生成前缀和，O(N)
        int[] preSum = new int[size];
        preSum[0] = array[0];
        for (int i = 1; i < size; i++) {
            preSum[i] = preSum[i - 1] + array[i];
        }
        // 单调栈找最小值所在区间，O(N)
        // 遍历处理每个元素
        Stack<Integer> incStack = new Stack<>();  // 存放数组值的下标！！
        for (int i = 0; i < size; i++) {
            while (!incStack.isEmpty() && array[incStack.peek()] >= array[i]) {  // 清理栈内不平衡元素，保证栈内单调性
                // 单调性被破坏，说明出现了更小值，结算前个最小值及其区间的乘积
                int idx = incStack.pop();
                if (array[idx] != array[i]) {  // 如果出现重复值就推迟结算，让后一个重复值算对即可！
                    int product = (!incStack.isEmpty() ? (preSum[i - 1] - preSum[incStack.peek()]) : preSum[i - 1]) * array[idx];
                    max = Math.max(max, product);
                }
            }
            incStack.push(i);  // 入栈新元素的下标！！
        }
        // 结算剩余栈，栈内单调意味着剩余元素的右边界为数组末尾
        int sum = preSum[size - 1];
        while (!incStack.isEmpty()) {
            int idx = incStack.pop();
            int product = (!incStack.isEmpty() ? (sum - preSum[incStack.peek()]) : sum) * array[idx];
            max = Math.max(max, product);
        }
        return max;
    }


    // O(N^3)
    public static int maxProduct2(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int L = 0; L < array.length; L++) {
            for (int R = L; R < array.length; R++) {
                int sum = 0;
                int min = Integer.MAX_VALUE;
                for (int i = L; i <= R; i++) {  // 每次R尝试右扩，R就发生回退遍历！
                    sum += array[i];
                    if (min > array[i])
                        min = array[i];
                }
                max = Math.max(max, sum * min);
            }
        }
        return max;
    }


    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 10;
        int maxValue = 20;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
            int ans1 = maxProduct(array);
            int ans2 = maxProduct2(array);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d  array: ", ans1, ans2);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
