package C14_Greedy;

import C01_random.GenerateRandomArray;
import java.util.PriorityQueue;

public class C05_EarnMoreMoney {
    // 给定二维正数数组projects表示每个项目的花费、利润(含花费)，正数M表示初始资金，正数K表示做多少个项目，项目只能串行完成，返回最多能获得多少钱。
    // 贪心解，每次都做资金能启动的项目里利润最大的项目
    public static int earnMoreMoney(int[][] projects, int M, int K) {
        PriorityQueue<int[]> costMinHeap = new PriorityQueue<>((a1, a2) -> a1[0] - a2[0]);   // 花费少的项目进入候选
        PriorityQueue<int[]> profitMaxHeap = new PriorityQueue<>((a1, a2) -> a2[1] - a1[1]); // 利润多的候选项目优先做
        for (int i = 0; i < projects.length; i++) {
            costMinHeap.add(projects[i]);
        }
        for (int i = K; i > 0; i--) {
            // 把花费小于当前资金的项目都放进profitMaxHeap
            while (!costMinHeap.isEmpty() && costMinHeap.peek()[0] <= M) {
                profitMaxHeap.add(costMinHeap.poll());
            }
            // 找一个项目做
            if (!profitMaxHeap.isEmpty()) {
                M += profitMaxHeap.poll()[1];
            } else { // 资金不足、或者没有项目了，只能提前结束！
                return M;
            }
        }
        return M;
    }


    // 暴力解
    public static int earnMoreMoney2(int[][] projects, int M, int K) {
        return process(projects, M, K);
    }
    // restProjects剩余项目的花费和利润，当前资金为M，最多做K个项目，返回最多能获得多少钱。
    private static int process(int[][] restProjects, int M, int K) {
        if (K == 0 || restProjects.length == 0) {
            return M;
        }
        int max = M;
        for (int i = 0; i < restProjects.length; i++) {
            if (restProjects[i][0] <= M) {
                max = Math.max(max, process(copyExcept(restProjects, i), M + restProjects[i][1], K - 1));
            }
        }
        return max;
    }
    private static int[][] copyExcept(int[][] array, int index) {
        int[][] newArray = new int[array.length - 1][];
        for (int i = 0, newIndex = 0; i < array.length; i++) {
            if (i != index) {
                newArray[newIndex++] = array[i];
            }
        }
        return newArray;
    }


    public static void main(String[] args) {
        int testTimes = 100000;
        int maxProjects = 5;
        int maxMoney = 30;
        for (int i = 0; i < testTimes; i++) {
            int[][] projects = GenerateRandomArray.generateRandomLinesPositive(maxProjects, maxMoney);
            int K = (int)(Math.random() * (maxProjects + 2));  // [0, maxProjects + 1]
            int M = (int)(Math.random() * (maxMoney + 1));  // [0, maxMoney]
            int ans1 = earnMoreMoney(projects, M, K);
            int ans2 = earnMoreMoney2(projects, M, K);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d  M:%d K:%d projects:", ans1, ans2, M, K);
                GenerateRandomArray.printArray(projects);
            }
        }
    }
}
