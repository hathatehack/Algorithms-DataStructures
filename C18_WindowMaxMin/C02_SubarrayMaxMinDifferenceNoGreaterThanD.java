package C18_WindowMaxMin;

import C01_random.GenerateRandomArray;

import java.util.LinkedList;

public class C02_SubarrayMaxMinDifferenceNoGreaterThanD {
    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxsize = 10;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxsize, maxValue);
            int D = (int)((maxValue + 1) * Math.random());
            int ans1 = count(array, D);
            int ans2 = count2(array, D);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d  D:%d  array:", ans1, ans2, D);
                GenerateRandomArray.printArray(array);
            }
        }
    }


    // 给定一个整型数组arr，和一个整数D，要求子数组中最大值–最小值 <= D，返回达标子数组的数量。
    // 时间复杂度O(N)
    // 分析技巧：[10, 9, 8, 7], D=1
    //  1.假设[L,R)范围内max-min<=D，则必定有R-L个子数组全都符合要求！
    //  2.如果R右扩后不达标，意味着以[L,R]为前缀的子数组只会更加不达标,因为min<=curMin、max>=curMax，max-min>D !!
    //    所以R右扩后不达标，则停止尝试L开头，应从[L+1,R)继续开始整个流程。
    public static int count(int[] array, int D) {
        if (array == null || array.length == 0 || D < 0) { // max-min不会小于0
            return 0;
        }
        int count = 0;
        LinkedList<Integer> maxWindow = new LinkedList<>();  // 存放窗口内值的下标！
        LinkedList<Integer> minWindow = new LinkedList<>();  // 存放窗口内值的下标！
        for (int L = 0, R = 0; L < array.length; L++) {
            while (R < array.length) {  // 尝试右扩R，并且[L,R)窗口内max-min<=D
                // max
                while (!maxWindow.isEmpty() && array[maxWindow.peekLast()] <= array[R]) { // 清理窗口内<=右窗口的值的下标，减少后续窗口的重复回溯数量且保证最左对应最大/最小
                    maxWindow.pollLast();
                }
                maxWindow.addLast(R);  // 入队新元素的下标!!
                // min
                while (!minWindow.isEmpty() && array[minWindow.peekLast()] >= array[R]) { // 清理窗口内>=右窗口的值的下标，减少后续窗口的重复回溯数量且保证最左对应最大/最小
                    minWindow.pollLast();
                }
                minWindow.addLast(R);  // 入队新元素的下标!!
                // 判断窗口要不要右扩
                if (array[maxWindow.peekFirst()] - array[minWindow.peekFirst()] <= D) {
                    R++;
                } else {
                    break;
                }
            }
            count += R - L;  // 获取累加达标子数组数量
            // 窗口每次右滑一格后需弹出在左窗口之前的下标
            if (maxWindow.peekFirst() == L)
                maxWindow.pollFirst();
            if (minWindow.peekFirst() == L)
                minWindow.pollFirst();
        }
        return count;
    }


    public static int count2(int[] array, int D) {
        if (array == null || array.length == 0 || D < 0) {
            return 0;
        }
        int count = 0;
        for (int L = 0; L < array.length; L++) {
            for (int R = L; R < array.length; R++) {
                int min = array[L];
                int max = array[L];
                for (int i = L + 1; i <= R; i++) {  // 右窗口回退再遍历
                    if (min > array[i])
                        min = array[i];
                    else if (max < array[i])
                        max = array[i];
                }
                if (max - min <= D) {
                    count++;
                }
            }
        }
        return count;
    }
}
