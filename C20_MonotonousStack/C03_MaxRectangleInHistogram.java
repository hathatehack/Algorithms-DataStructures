package C20_MonotonousStack;

import C01_random.GenerateRandomArray;
import java.util.Stack;

public class C03_MaxRectangleInHistogram {
    // 测试链接：https://leetcode.com/problems/largest-rectangle-in-histogram
    // 给定一个正整数数组arr，代表直方图，返回直方图的最大长方形面积。
    // 分析技巧：
    //  不同的高度所能形成的矩形宽度取决于以该高度能扩宽多大范围，即求以某高度做最小值的区间范围。
    //  单调栈元素及其做最小值区间宽度的乘积即是面积。
    //  以下直方图可利用递增栈从左往右遍历每个高度的最小值区间
    //       1
    //     1 1 1
    //   1 1 1 1 1
    // 时间复杂度O(N)
    public static int maxRectangleArea(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        int max = 0;
        Stack<Integer> incStack = new Stack<>();  // 递增栈找每个高度的最小值区间，存放数组值的下标！！
        // 遍历每个高度
        for (int i = 0; i < height.length; i++) {
            while (!incStack.isEmpty() && height[incStack.peek()] >= height[i]) { // 清理栈内不平衡元素，保证栈内单调性
                // 单调性被破坏，说明出现了更小值，结算前个最小值及其区间的乘积
                int idx = incStack.pop();
                if (height[idx] != height[i]) { // 如果出现重复值就推迟结算，让后一个重复值算对即可！
                    int area = (!incStack.isEmpty() ? (i - incStack.peek() - 1) : i) * height[idx];
                    max = Math.max(max, area);
                }
            }
            incStack.push(i);  // 入栈新元素的下标！！
        }
        // 结算剩余栈，栈内单调意味着剩余元素的右边界为数组末尾
        while (!incStack.isEmpty()) {
            int idx = incStack.pop();
            int area = (!incStack.isEmpty() ? (height.length - incStack.peek() - 1) : height.length) * height[idx];
            max = Math.max(max, area);
        }
        return max;
    }

    // 数组版单调栈
    public static int maxRectangleArea2(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        int max = 0;
        int[] incStack = new int[height.length];  // 递增栈找每个高度的最小值区间，存放数组值的下标！！
        int stackIdx = -1;
        // 遍历每个高度
        for (int i = 0; i < height.length; i++) {
            while (stackIdx != -1 && height[incStack[stackIdx]] >= height[i]) { // 清理栈内不平衡元素，保证栈内单调性
                // 单调性被破坏，说明出现了更小值，结算前个最小值及其区间的乘积
                int idx = incStack[stackIdx--];
                if (height[idx] != height[i]) { // 如果出现重复值就推迟计算，让后一个重复值算对即可！
                    int area = (stackIdx != -1 ? (i - incStack[stackIdx] - 1) : i) * height[idx];
                    max = Math.max(max, area);
                }
            }
            incStack[++stackIdx] = i;  // 入栈新元素的下标！！
        }
        // 结算剩余栈，栈内单调意味着剩余元素的右边界为数组末尾
        while (stackIdx != -1) {
            int idx = incStack[stackIdx--];
            int area = (stackIdx != -1 ? (height.length - incStack[stackIdx] - 1) : height.length) * height[idx];
            max = Math.max(max, area);
        }
        return max;
    }

    // O(N^3)
    public static int maxRectangleArea3(int[] array) {
        int max = 0;
        for (int L = 0; L < array.length; L++) {
            for (int R = L; R < array.length; R++) {
                int width = R - L + 1;
                int minHeight = Integer.MAX_VALUE;
                for (int i = L; i <= R; i++) {  // 每次R尝试右扩，R就发生回退遍历！
                    if (minHeight > array[i])
                        minHeight = array[i];
                }
                max = Math.max(max, width * minHeight);
            }
        }
        return max;
    }


    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 50;
        int maxValue = 30;
        for (int i = 0; i < testTimes; i++) {
            int[] height = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
            int ans1 = maxRectangleArea(height);
            int ans2 = maxRectangleArea2(height);
            int ans3 = maxRectangleArea3(height);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d, ans3=%d, height: ", ans1, ans2, ans3);
                GenerateRandomArray.printArray(height);
            }
        }
    }
}
