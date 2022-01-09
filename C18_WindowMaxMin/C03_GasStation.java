package C18_WindowMaxMin;

import java.util.LinkedList;

public class C03_GasStation {
    public static void main(String[] args) {
        int[] gas =  new int[] {1, 1, 5, 1, 7, 1};
        int[] cost = new int[] {3, 4, 1, 3, 1, 2};
        System.out.println(canCompleteCircuit(gas, cost));
    }

    // 测试链接：https://leetcode.com/problems/gas-station
    // N个加油站组成一个环形(N>1)，给定两个非负数组gas[N]和cost[N]，gas表示每个加油站存的油可以跑多少千米，cost表示下个加油站有多少千米，请给出一个可以跑完一圈的良好出发点。
    // 时间复杂度O(N)，额外空间O(N)
    // 技巧分析：
    //  1.加工新数组gas-cost，表示每走一站的剩余油量情况，2N长度以支持环绕路线；
    //  2.前缀和，表示走任意多个站点的总油量使用情况；
    //  3.窗口结构遍历2N前缀和数组找出任意的N长度子数组内每个值>=0，即走到下一站油刚好用完或还有剩余。
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int N = gas.length;
        int _2N = N << 1;
        int[] preSum = new int[_2N];  // 表示走任意多个站点的总油量使用情况，2N长度以支持环绕路线
        for (int i = 0; i < N; i++) {
            preSum[i] = gas[i] - cost[i];
            preSum[i + N] = preSum[i];
        }
        for (int i = 1; i < _2N; i++) {
            preSum[i] += preSum[i - 1];
        }
        boolean[] isGoodArray = new boolean[N];
        LinkedList<Integer> minWindow = new LinkedList<>();  // 存放窗口内值的下标！
        // 孵化N窗口
        for (int R = 0; R < N; R++) {
            while (!minWindow.isEmpty() && preSum[minWindow.peekLast()] >= preSum[R]) { // 清理窗口内<=右窗口的值的下标，减少后续窗口的重复回溯数量且保证最左对应最大/最小
                minWindow.pollLast();
            }
            minWindow.addLast(R);  // 入队新元素的下标!!
        }
        // 滑动N窗口
        for (int lastPreSum = 0, L = 0, R = N; L < N; lastPreSum = preSum[L++], R++) {
            // 记录当前路线[L,R]是否能跑完
            if (preSum[minWindow.peekFirst()] - lastPreSum >= 0) {  // 当前子数组正确的preSum应当都减去左窗口前preSum
                isGoodArray[L] = true;
            }
            if (minWindow.peekFirst() == L) {  // 窗口每次右滑一格后需弹出在左窗口之前的下标
                minWindow.pollFirst();
            }
            while (!minWindow.isEmpty() && preSum[minWindow.peekLast()] >= preSum[R]) { // 清理窗口内<=右窗口的值的下标，减少后续窗口的重复回溯数量且保证最左对应最大/最小
                minWindow.pollLast();
            }
            minWindow.addLast(R);  // 入队新元素的下标!!
        }

//        // 打印所有良好站点
//        for (int i = 0; i < isGoodArray.length; i++) {
//            if (isGoodArray[i])
//                System.out.printf("%d ", i);
//            System.out.println();
//        }
        // 返回一个良好站点
        for (int i = 0; i < isGoodArray.length; i++) {
            if (isGoodArray[i]) {
                return i;
            }
        }
        return -1;
    }
}
