package C03_Sort;

import java.util.Arrays;

import C04_Heap.HeapEnhance;
import C01_random.GenerateRandomArray;

public class HS02_CoverCountMax {
    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 20;
        int maxValue = 20;
        for (int i = 0; i < testTimes; i++) {
            int[][] lines = GenerateRandomArray.generateRandomLines(maxSize, maxValue);
            int ans1 = coverCountMax(lines);
            int ans2 = coverCountMax2(lines);
            int ans3 = coverCountMax3(lines);
            if (ans1 != ans2 || ans2 != ans3) {
                GenerateRandomArray.printArray(lines);
                System.out.format("%d出错了, ans1=%d, ans2=%d, ans3=%d！\n", i, ans1, ans2, ans3);
                break;
            }
        }
    }

    static public int coverCountMax(int[][] lines) {
        lines = GenerateRandomArray.copyArray(lines);
        Arrays.sort(lines, (l1, l2) -> l1[0] - l2[0]);
//        PriorityQueue<Integer> heap = new PriorityQueue<>();  // 默认小根堆
        HeapEnhance<Integer> heap = new HeapEnhance<Integer>((end1, end2) -> end1 - end2);
        int count = 0;
        for (int[] line : lines) {
            // 弹出heap里结束位置小于等于当前line起始的线段，lines已按起始位置排好序，被弹出的线段永远不会跟后面的线段相交。
            while (!heap.isEmpty() && heap.peek() <= line[0]) {
                heap.pop();
            }
            heap.push(line[1]);  // push end
            count = Math.max(count, heap.size());
        }
        return count;
    }




    static public int coverCountMax2(int[][] lines) {
        int count = 0;
        for (int i = 0; i < lines.length; i++) {
            for (int j = i; j < lines.length; j++) {
                if (lines[i][0] < lines[j][1] && lines[j][0] < lines[i][1]) {  // 两线段必须是相交的！ {{-13, 7}, {-3, 6}, {4, 7}, {6, 8}}
                    int L = Math.max(lines[i][0], lines[j][0]);
                    int R = Math.min(lines[i][1], lines[j][1]);
                    int cur = 0;
                    for (int[] line : lines) {
                        if (line[0] <= L && line[1] >= R) {
                            cur++;
                        }
                    }
                    count = Math.max(count, cur);
                }
            }
        }
        return count;
    }



    
    static public int coverCountMax3(int[][] lines) {
        if (lines.length == 0) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int[] line : lines) {
            min = Math.min(min, line[0]);
            max = Math.max(max, line[1]);
        }
        int count = 0;
        for (int L = min, R = min + 1; R <= max; L++, R++) {
            int cur = 0;
            for (int[] line : lines) {
                if (line[0] <= L && line[1] >= R) {
                    cur++;
                }
            }
            count = Math.max(count, cur);
        }
        return count;
    }
}
