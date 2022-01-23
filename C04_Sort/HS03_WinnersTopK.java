package C04_Sort;

import C05_Heap.HeapEnhance;
import C01_random.GenerateRandomArray;

import java.util.*;

public class HS03_WinnersTopK {
    public static void main(String[] args) {
        int testTime = 100000;
        int maxSize = 50;
        int maxID = 10;
        int maxK = 6;
        for (int i = 0; i < testTime; i++) {
            int topK = (int)(Math.random() * maxK) + 1;
            Data data = generateRandomData(maxSize, maxID);
            int[][] ans1 = WinnersTopK.getWinners(data.customers, data.op, topK);
            int[][] ans2 = WinnersTopK2.getWinners(data.customers, data.op, topK);
            if (!GenerateRandomArray.isEqual(ans1, ans2)) {
                System.out.format("%d出错了, topK=%d\n", i, topK);
                GenerateRandomArray.printArray(ans1);
                GenerateRandomArray.printArray(ans2);
                break;
            }
        }
    }

    static public class Customer {
        public int id, buy, time;
        public Customer(int i, int b, int t) {
            id = i;
            buy = b;
            time = t;
        }
    }

    static public class CandidateComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer c1, Customer c2) {
            return (c1.buy != c2.buy) ? (c2.buy - c1.buy) : (c1.time - c2.time);
        }
    }
    static public class WinnerComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer c1, Customer c2) {
            return (c1.buy != c2.buy) ? (c1.buy - c2.buy) : (c1.time - c2.time);
        }
    }

    static public class WinnersTopK {
        static public int[][] getWinners(int[] customers, boolean[] operations, int topK) {
            HashMap<Integer, Customer> map = new HashMap<>();
            HeapEnhance<Customer> candidates = new HeapEnhance<Customer>(new CandidateComparator());
            HeapEnhance<Customer> winners = new HeapEnhance<Customer>(new WinnerComparator());
            List<int[]> result = new ArrayList<>();
            for (int i = 0; i < customers.length; i++) {
                int id = customers[i];
                boolean isBuy = operations[i];
                // 用户从未购买但发生退货
                if (!isBuy && !map.containsKey(id)) {
                    result.add(getCurWinnersId(winners));
                    continue;
                }
                // 用户第一次购买先建立信息
                Customer c;
                if (!map.containsKey(id)) {
                    c = new Customer(id, 1, 0);
                    map.put(id, c);
                } else { // 多次买、退
                    c = map.get(id);
                    c.buy += isBuy ? 1 : -1;
                }
                // 更新中奖区、候选区
                if (c.buy == 0) {  // 删除
                    map.remove(id);
                    if (!candidates.remove(c))
                        winners.remove(c);
                } else if (!candidates.contains(c) && !winners.contains(c)) { // 新加入
                    c.time = i;
                    if (winners.size() < topK) {
                        winners.push(c);
                    } else {
                        candidates.push(c);
                    }
                } else if (candidates.contains(c)) { // 更新heap
                    candidates.resign(c);
                } else if (winners.contains(c)) { // 更新heap
                    winners.resign(c);
                }
                // 重新选择中奖区
                reselect(candidates, winners, topK, i);
                result.add(getCurWinnersId(winners));
            }
            int[][] arr = new int[result.size()][];
            result.toArray(arr);
            return arr;
        }

        static private void reselect(HeapEnhance<Customer> candidates, HeapEnhance<Customer> winners, int topK, int time) {
            if (candidates.isEmpty()) {
                return;
            }
            if (winners.size() < topK) {  // 中奖区不满，直接从候选区选最合适者。
                Customer c = candidates.pop();
                c.time = time;
                winners.push(c);
            } else {  // 中奖区满了，需判断候选区是否有合适者。
                if (candidates.peek().buy > winners.peek().buy) {
                    Customer w = candidates.pop();
                    Customer c = winners.pop();
                    w.time = time;
                    c.time = time;
                    winners.push(w);
                    candidates.push(c);
                }
            }
        }

        static private int[] getCurWinnersId(HeapEnhance<Customer> winners) {
            List<Customer> list = winners.getAllElements();
            list.sort(new WinnerComparator());
            int size = list.size();
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = list.get(i).id;
            }
            return array;
        }
    }



    static public class WinnersTopK2 {
        static public int[][] getWinners(int[] customers, boolean[] operations, int topK) {
            HashMap<Integer, Customer> map = new HashMap<>();
            ArrayList<Customer> candidates = new ArrayList<>();
            ArrayList<Customer> winners = new ArrayList<>();
            List<int[]> result = new ArrayList<>();
            for (int i = 0; i < customers.length; i++) {
                int id = customers[i];
                boolean isBuy = operations[i];
                // 用户从未购买但发生退货
                if (!isBuy && !map.containsKey(id)) {
                    result.add(getCurWinnersId(winners));
                    continue;
                }
                // 用户第一次购买先建立信息
                Customer c;
                if (!map.containsKey(id)) {
                    c = new Customer(id, 1, 0);
                    map.put(id, c);
                } else { // 多次买、退
                    c = map.get(id);
                    c.buy += isBuy ? 1 : -1;
                }
                // 更新中奖区、候选区
                if (c.buy == 0) {  // 删除
                    map.remove(id);
                    if (!candidates.remove(c))
                        winners.remove(c);
                } else if (!candidates.contains(c) && !winners.contains(c)) {  // 新加入
                    c.time = i;
                    if (winners.size() < topK) {
                        winners.add(c);
                    } else {
                        candidates.add(c);
                    }
                }
                // 重新选择中奖区
                reselect(candidates, winners, topK, i);
                result.add(getCurWinnersId(winners));
            }
            int[][] arr = new int[result.size()][];
            result.toArray(arr);
            return arr;
        }

        static private void reselect(List<Customer> candidates, List<Customer> winners, int topK, int time) {
            if (candidates.isEmpty()) {
                return;
            }
            // 以下排序可以使用heapsort方式优化。
            candidates.sort(new CandidateComparator());
            winners.sort(new WinnerComparator());
            if (winners.size() < topK) {  // 中奖区不满，直接从候选区选最合适者。
                Customer c = candidates.get(0);
                c.time = time;
                winners.add(c);
                candidates.remove(c);
            } else {  // 中奖区满了，需判断候选区是否有合适者。
                if (candidates.get(0).buy > winners.get(0).buy) {
                    Customer w = candidates.get(0);
                    Customer c = winners.get(0);
                    candidates.remove(0);
                    winners.remove(0);
                    w.time = c.time = time;
                    candidates.add(c);
                    winners.add(w);
                }
            }
        }

        static private int[] getCurWinnersId(List<Customer> winners) {
            winners.sort(new WinnerComparator());
            int size = winners.size();
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = winners.get(i).id;
            }
            return array;
        }
    }



    static public class Data {
        public int[] customers;
        public boolean[] op;
        public Data(int[] arr, boolean[] o) {
            customers = arr;
            op = o;
        }
    }

    static public Data generateRandomData(int maxSize, int maxID) {
        int size = (int)(Math.random() * (maxSize + 1));
        int[] customers = new int[size];
        boolean[] op = new boolean[size];
        for (int i = 0; i < size; i++) {
            customers[i] = (int)(Math.random() * maxID);
            op[i] = Math.random() < 0.5;
        }
        return new Data(customers, op);
    }
}
