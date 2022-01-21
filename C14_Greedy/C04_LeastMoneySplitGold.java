package C14_Greedy;

import C01_random.GenerateRandomArray;
import java.text.SimpleDateFormat;
import java.util.*;

public class C04_LeastMoneySplitGold {
    // 一块金条切成两块，是需要花费和长度数值一样的铜板的。一群人想整分整块金条，怎么分最省铜板?
    // 例如,给定数组{10,20,30}，代表一共三个人，整块金条长度为60，金条要分成10，20，30三个部分。
    // 如果先把长度60的金条分成10和50，花费60; 再把长度50的金条分成20和30，花费50; 一共花费110铜板。
    // 但若先把长度60的金条分成30和30，花费60; 再把长度30的金条分成10和20，花费30; 一共花费90铜板。
    // 给定一个数组array，表示将金条切成相应的多个金块，返回分割的最小代价。
    // 贪心解，使用小根堆模拟将多个金块合并还原为一个金条
    public static int leastMoneySplitGold(int[] array) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int len : array) {
            minHeap.add(len);
        }
        // 每次从小根堆弹出两出个小值，合成一个数再放回去，周而复始。例如数组{10,20,30}切割过程如下，
        //      60'
        //     /   \
        //    30'   30
        //   /  \
        //  10  20
        int money = 0;
        while (minHeap.size() > 1) {
            int cur =  minHeap.poll() + minHeap.poll();  // 每次切割的花费
            money += cur;
            minHeap.add(cur);
        }
        return money;
    }


    // 暴力解，将多个金块合并还原为一个金条
    public static int leastMoneySplitGold2(int[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        return process2(array, 0);
    }
    // restArray剩余待合并的金块，moneyUsed前面的合并花费，返回完成的最少总花费。
    private static int process2(int[] restArray, int moneyUsed) {
        if (restArray.length == 1) {
            return moneyUsed;
        }
        int leastMoney = Integer.MAX_VALUE;
        for (int i = 0; i < restArray.length; i++) {
            for (int j = i + 1; j < restArray.length; j++) {
                leastMoney = Math.min(leastMoney,
                        process2(copyAndMergeTwo(restArray, i, j), moneyUsed + restArray[i] + restArray[j]));
            }
        }
        return leastMoney;
    }
    private static int[] copyAndMergeTwo(int[] array, int i, int j) {
        int[] newArray = new int[array.length - 1];
        int newIndex = 0;
        for (int index = 0; index < array.length; index++) {
            if (index != i && index != j) {
                newArray[newIndex++] = array[index];
            }
        }
        newArray[newIndex] = array[i] + array[j];
        return newArray;
    }



    // 暴力解，将金条分割为相应多个金块
    public static int leastMoneySplitGold3(int[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        return process3(copy2List(array));
    }
    // restList待分割成相应金块，返回完成的最少总花费
    private static int process3(ArrayList<Integer> restList) {
        if (restList.size() == 1) {
            return 0;
        }
        int leastMoney = Integer.MAX_VALUE;
        int curMoney = sum(restList);
        int limit = restList.size() / 2;  // 分割方式限制，避免重复尝试
        for (int unitSize = 1; unitSize <= limit; unitSize++) {
            ArrayList<ArrayList<Integer>> splitWays = combination(restList, 0, restList.size() - 1, unitSize);
            for (ArrayList<Integer> leftHalf : splitWays) {
                int leftHalfMoney = process3(leftHalf);
                int rightHalfMoney = process3(copyExcept(restList, leftHalf));
                leastMoney = Math.min(leastMoney, curMoney + leftHalfMoney + rightHalfMoney);
            }
        }
        return leastMoney;
    }
    // list里选unitSize个数一组，返回所有组合
    private static ArrayList<ArrayList<Integer>> combination(ArrayList<Integer> list, int start, int end, int unitSize) {
        ArrayList<ArrayList<Integer>> all = new ArrayList<>();
        if (unitSize == 1) {
            for (int i = start; i <= end; i++) {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(list.get(i));
                all.add(arr);
            }
            return all;
        }
        int limit = end - unitSize + 1;  // 避免重复组合
        for (int i = start; i <= limit; i++) {
            ArrayList<ArrayList<Integer>> group = combination(list, i + 1, end, unitSize - 1);
            for (ArrayList<Integer> arr : group) {
                arr.add(0, list.get(i));
            }
            all.addAll(group);
        }
        return all;
    }
    private static ArrayList<Integer> copy2List(int[] array) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int ele : array) {
            list.add(ele);
        }
        return list;
    }
    private static ArrayList<Integer> copyExcept(ArrayList<Integer> list, ArrayList<Integer> excludes) {
        ArrayList<Integer> newList = new ArrayList<>();
        for (int i = 0, end = list.size() - 1; i <= end; i++) {
            Integer ele = list.get(i);
            int idx = excludes.indexOf(ele);
            if (idx != -1) {
                excludes.remove(idx);
            } else {
                newList.add(ele);
            }
        }
        return newList;
    }
    private static int sum(ArrayList<Integer> list) {
        int sum = 0;
        for (int ele : list) {
            sum += ele;
        }
        return sum;
    }


    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("leastMoneySplitGold2");
        System.out.printf("start %s\n", sdf.format(new Date()));
        System.out.println(leastMoneySplitGold2(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}));
        System.out.printf("end   %s\n", sdf.format(new Date()));
        System.out.println("leastMoneySplitGold3");
        System.out.printf("start %s\n", sdf.format(new Date()));
        System.out.println(leastMoneySplitGold3(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}));
        System.out.printf("end   %s\n", sdf.format(new Date()));

        System.out.println("===========================================\ntest..");
        int testTimes = 100000;
        int maxSize = 6;//3;
        int maxLen = 10;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxLen);
            int ans1 = leastMoneySplitGold(array);
            int ans2 = leastMoneySplitGold2(array);
            int ans3 = leastMoneySplitGold3(array);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d, ans2=%d  array: ", ans1, ans2, ans3);
                GenerateRandomArray.printArray(array);
            }
        }
    }
}
