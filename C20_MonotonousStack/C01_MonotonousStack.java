package C20_MonotonousStack;

import C01_random.GenerateRandomArray;

import java.util.LinkedList;
import java.util.Stack;

public class C01_MonotonousStack {
    // 求非重复数组中每个元素左右比当前值小的最近位置。
    // 单调栈，时间复杂度O(N)
    // [1, 2, 4, 3]                     ---  V  破坏了栈单调性
    //i:0  1  2  3                     ----
    //  i lLess rLess                    --  ^
    //  0  -1    -1                       -  |
    //  1   0    -1
    //  2   1     3
    //  3   0    -1
    public static int[][] nearestLessNonRepeat(int[] array) {
        int[][] ans = new int[array.length][2]; // 保存n个元素的两侧位置信息
        Stack<Integer> incStack = new Stack<>();  // 存放数组值的下标！！
        // 遍历处理每个元素
        for (int i = 0; i < array.length; i++) {
            while (!incStack.isEmpty() && array[incStack.peek()] > array[i]) { // 清理栈内不平衡元素，保证栈内单调性，减少后续入栈的重复回溯数量。
                int idx = incStack.pop();  // 单调性被破坏，栈顶元素的两侧位置信息已形成
                // 结算两侧的位置信息
                ans[idx][0] = !incStack.isEmpty() ? incStack.peek() : -1; // 左侧位置信息
                ans[idx][1] = i;   // 右侧位置信息
            }
            incStack.push(i);  // 入栈新元素的下标！！
        }
        // 结算剩余栈，栈内单调意味着剩余元素的右边界为数组末尾
        while (!incStack.isEmpty()) {
            int idx = incStack.pop();
            ans[idx][0] = !incStack.isEmpty() ? incStack.peek() : -1; // 左侧位置信息
            ans[idx][1] = -1;  // 右侧不存在合理位置
        }
        return ans;
    }

    // 求重复数组中每个元素左右比当前小的最近位置。
    // 单调栈，时间复杂度O(N)
    public static int[][] nearestLess(int[] array) {
        int[][] ans = new int[array.length][2]; // 保存n个元素的两侧位置信息
        Stack<LinkedList<Integer>> incStack = new Stack<>();  // 存放数组值的下标！！使用链表保存重复元素
        // 遍历处理每个元素
        for (int i = 0; i < array.length; i++) {
            while (!incStack.isEmpty() && array[incStack.peek().get(0)] > array[i]) { // 清理栈内不平衡元素，保证栈内单调性，减少后续入栈的重复回溯数量。
                LinkedList<Integer> repeatList = incStack.pop();  // 单调性被破坏，栈顶元素的两侧位置信息已形成
                // 结算两侧位置信息
                int leftNearestLess = !incStack.isEmpty() ? incStack.peek().get(0) : -1;
                for (Integer idx : repeatList) {
                    ans[idx][0] = leftNearestLess;
                    ans[idx][1] = i;
                }
            }
            // 入栈新元素的下标！！
            if (!incStack.isEmpty() && array[incStack.peek().get(0)] == array[i]) {
                incStack.peek().addFirst(i);  // 入栈新元素的下标！！重复元素使用头插法
            } else {
                LinkedList<Integer> list = new LinkedList<>();
                list.addFirst(i);  // 入栈新元素的下标！！
                incStack.push(list);
            }
        }
        // 结算剩余栈，栈内单调意味着剩余元素的右边界为数组末尾
        while (!incStack.isEmpty()) {
            LinkedList<Integer> repeatList = incStack.pop();
            int leftNearestLess = !incStack.isEmpty() ? incStack.peek().get(0) : -1;
            for (Integer idx : repeatList) {
                ans[idx][0] = leftNearestLess; // 左侧位置信息
                ans[idx][1] = -1; // 右侧不存在合理位置
            }
        }
        return ans;
    }

    // 暴力解，时间复杂度O(N^2)
    public static int[][] checker(int[] array) {
        int[][] ans = new int[array.length][2];
        for (int i = 0; i < array.length; i++) {
            int leftNearestLess = -1;
            int rightNearestLess = -1;
            // 在当前位置向左遍历一遍，向右遍历一遍，总共最多可能O(N)
            for (int L = i - 1, cur = array[i]; L >= 0; L--) {
                if (array[L] < cur) {
                    leftNearestLess = L;
                    break;
                }
            }
            for (int R = i + 1, cur = array[i]; R < array.length; R++) {
                if (array[R] < cur) {
                    rightNearestLess = R;
                    break;
                }
            }
            ans[i][0] = leftNearestLess;
            ans[i][1] = rightNearestLess;
        }
        return ans;
    }


    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxSize = 10;
        int maxValue = 20;
        System.out.println("test nearestLessNonRepeat...");
        for (int i = 0; i < testTimes; i++) {
            int[] arrayNonRepeat = GenerateRandomArray.generateRandomArrayNonRepeat(maxSize);
            int[][] ans1 = nearestLessNonRepeat(arrayNonRepeat);
            int[][] ans2 = checker(arrayNonRepeat);
            if (!GenerateRandomArray.isEqual(ans1, ans2)) {
                System.out.printf("Oops! i=%d\n", i);
                GenerateRandomArray.printArray(arrayNonRepeat);
                GenerateRandomArray.printArray(ans1);
                GenerateRandomArray.printArray(ans2);
            }
        }

        System.out.println("test nearestLess..");
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int[][] ans1 = nearestLess(array);
            int[][] ans2 = checker(array);
            if (!GenerateRandomArray.isEqual(ans1, ans2)) {
                System.out.printf("Oops! i=%d\n", i);
                GenerateRandomArray.printArray(array);
                GenerateRandomArray.printArray(ans1);
                GenerateRandomArray.printArray(ans2);
            }
        }
    }
}
