package C14_Greedy;

import C01_random.GenerateRandomArray;
import java.util.Arrays;

public class C03_BestArrange {
    // 一个会议室不能同时容纳两个项目的宣讲，给你每一个项目开始的时间和结束的时间，要求安排会议室进行的宣讲的场次最多，返回最多的宣讲场次。
    // 贪心解，结束时间早的优先安排
    public static int bestArrange(int[][] timelines) {
        Arrays.sort(timelines, (t1, t2) -> t1[1] - t2[1]);  // 按会议结束时间升序排序，结束时间早的优先安排
        int endTime = Integer.MIN_VALUE;
        int passMeetings = 0;
        for (int[] timeline : timelines) {
            if (timeline[0] >= endTime) {
                passMeetings++;
                endTime = timeline[1];
            }
        }
        return passMeetings;
    }


    // 暴力解
    public static int bestArrange2(int[][] timelines) {
        if (timelines == null || timelines.length == 0) {
            return 0;
        }
        return process(timelines, 0, Integer.MIN_VALUE);
    }
    // 当前已安排好会议passMeetings个，结束时间endTime，剩余会议有restTimelines，返回最终能安排的最多会议数。
    private static int process(int[][] restTimelines, int passMeetings, int endTime) {
        // 安排完，返回成功会议数
        if (restTimelines.length == 0) {
            return passMeetings;
        }
        // 安排剩下的会议
        int maxMeetings = passMeetings;
        for (int i = 0; i < restTimelines.length; i++) {
            if (restTimelines[i][0] >= endTime) {
                int[][] rest = copyExcept(restTimelines, i);
                maxMeetings = Math.max(maxMeetings, process(rest, passMeetings + 1, restTimelines[i][1]));
            }
        }
        return maxMeetings;
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
        int maxSize = 10;
        int maxTime = 30;
        for (int i = 0; i < testTimes; i++) {
            int[][] timelines = GenerateRandomArray.generateRandomLinesPositive(maxSize, maxTime);
            int ans1 = bestArrange(timelines);
            int ans2 = bestArrange2(timelines);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);

            }
        }
    }
}
