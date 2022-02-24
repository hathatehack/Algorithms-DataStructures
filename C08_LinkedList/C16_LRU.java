package C08_LinkedList;

import C01_random.GenerateRandomArray;
import C08_LinkedList.C15_DoublyLinkedList.*;
import java.util.*;

public class C16_LRU {
    // HashMap + DoubleLinkedList，get操作时间复杂度O(1)，put操作时间复杂度O(1)。
    public static class LRU1<K, V> {
        private HashMap<K, Node<K, V>> map;  // 时间复杂度O(1)
        private DoublyLinkedList<K, V> list; // 时间复杂度O(1)
        private int capacity;

        public LRU1(int _capacity) {
            map = new HashMap<>();
            list = new DoublyLinkedList<>();
            capacity = _capacity;
        }

        public V get(Integer key) {
            if (!map.containsKey(key)) {
                return null;
            }
            Node<K, V> node = map.get(key);
            // 将被访问节点移到尾部
            list.removeNode(node);
            list.addLast(node);
            return node.value;
        }

        public void put(K key, V value) {
            if (map.containsKey(key)) {  // 已存在，先删除再更新。
                list.removeNode(map.get(key));
            } else if (list.size() == capacity) {  // 缓存已满，删除最久未使用的节点。
                Node<K, V> first = list.removeFirst();
                map.remove(first.key);
            }
            Node<K, V> node = new Node<>(key, value);
            map.put(key, node);
            // 将被访问节点移到尾部
            list.addLast(node);
        }

        public Collection<V> values() {
            return list.values();
        }

        public int size() {
            return list.size();
        }
    }



    // HashMap + LinkedList，get操作时间复杂度O(1)，put操作时间复杂度O(N)。
    public static class LRU2<K, V> {
        private HashMap<K, V> map;       // 时间复杂度O(1)
        private LinkedList<K> keyList;   // 时间复杂度O(N)
        private LinkedList<V> valueList; // 时间复杂度O(N)
        private int capacity;

        public LRU2(int _capacity) {
            map = new HashMap<>();
            keyList = new LinkedList<>();
            valueList = new LinkedList<>();
            capacity = _capacity;
        }

        public V get(K key) {
            if (!map.containsKey(key)) {
                return null;
            }
            V value = map.get(key);
            // 将被访问节点移到尾部
            keyList.remove(key);
            keyList.addLast(key);
            valueList.remove(value);
            valueList.addLast(value);
            return value;
        }

        public void put(K key, V value) {
            if (map.containsKey(key)) {  // 已存在，先删除再更新。
                keyList.remove(key);
                valueList.remove(value);
            } else if (valueList.size() == capacity) {  // 缓存已满，删除最久未使用的节点。
                map.remove(keyList.getFirst());
                keyList.removeFirst();
                valueList.removeFirst();
            }
            map.put(key, value);
            // 将被访问节点移到尾部
            keyList.addLast(key);
            valueList.addLast(value);
        }

        public Collection<V> values() {
            return valueList;
        }

        public int size() {
            return valueList.size();
        }
    }



    // LinkedList，get操作时间复杂度O(N)，put操作时间复杂度O(N)。
    public static class LRU3<K, V> {
        private LinkedList<K> keyList;   // 时间复杂度O(N)
        private LinkedList<V> valueList; // 时间复杂度O(N)
        private int capacity;

        public LRU3(int _capacity) {
            keyList = new LinkedList<>();
            valueList = new LinkedList<>();
            capacity = _capacity;
        }

        public V get(K key) {
            int idx = keyList.indexOf(key);
            if (idx == -1) {
                return null;
            }
            V value = valueList.get(idx);
            // 将被访问节点移到尾部
            keyList.remove(key);
            keyList.addLast(key);
            valueList.remove(value);
            valueList.addLast(value);
            return value;
        }

        public void put(K key, V value) {
            int idx = keyList.indexOf(key);
            if (idx != -1) {  // 已存在，先删除再更新。
                keyList.remove(idx);
                valueList.remove(idx);
            } else if (valueList.size() == capacity) {  // 缓存已满，删除最久未使用的节点。
                keyList.removeFirst();
                valueList.removeFirst();
            }
            // 将被访问节点移到尾部
            keyList.addLast(key);
            valueList.addLast(value);
        }

        public Collection<V> values() {
            return valueList;
        }

        public int size() {
            return valueList.size();
        }
    }



    // checker
    public static class LRUChecker<K, V> extends LinkedHashMap<K, V> {
        private int capacity;

        public LRUChecker(int _capacity) {
            super(_capacity, 0.75F, true);
            capacity = _capacity;
        }

        public V get(Object key) {
            return super.getOrDefault(key, null);
        }

        public V put(K key, V value) {
            return super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }





    public static void main(String[] args) {
        checkResult();
        compareTimeConsume();
    }
    // 验证结果
    private static void checkResult() {
        System.out.println("验证结果...");
        int testTimes = 10000;
        int maxCapacity = 3;
        int maxValue = 5;
        int operations = 100;
        for (int i = 0; i <= testTimes; i++) {
            int capacity = (int)(Math.random() * maxCapacity + 1);
            LRU1<Integer, Integer> lru1 = new LRU1<>(capacity);
            LRU2<Integer, Integer> lru2 = new LRU2<>(capacity);
            LRU3<Integer, Integer> lru3 = new LRU3<>(capacity);
            LRUChecker<Integer, Integer> checker = new LRUChecker<>(capacity);
            // LRU的get、put操作批量测试
            for (int t = operations; t >= 0; t--) {
                int value = (int)(Math.random() * maxValue + 1);
                if (Math.random() < 0.8) {
                    lru1.put(value, value);
                    lru2.put(value, value);
                    lru3.put(value, value);
                    checker.put(value, value);
                } else {
                    lru1.get(value);
                    lru2.get(value);
                    lru3.get(value);
                    checker.get(value);
                }
            }
            // 比较操作结果
            int size_lru1 = lru1.size();
            int size_lru2 = lru2.size();
            int size_lru3 = lru3.size();
            int size_checker = checker.size();
            if (size_lru1 != size_checker || size_lru2 != size_checker || size_lru3 != size_checker) {
                System.err.printf("Oops! i=%d  size不一致！\n", i);
                System.err.printf(" size_checker=%d, size_lru1=%d, size_lru2=%d, size_lru3=%d\n", size_checker, size_lru1, size_lru2, size_lru3);
            }
            Object[] values_lru1 = lru1.values().toArray();
            Object[] values_lru2 = lru2.values().toArray();
            Object[] values_lru3 = lru3.values().toArray();
            Object[] values_checker = checker.values().toArray();
            if (!Arrays.equals(values_lru1, values_checker) || !Arrays.equals(values_lru2, values_checker) || !Arrays.equals(values_lru3, values_checker)) {
                System.err.printf("Oops! i=%d  keys不一致！\n", i);
                System.err.print(" values_lru1:    "); GenerateRandomArray.printArray(values_lru1);
                System.err.print(" values_lru2:    "); GenerateRandomArray.printArray(values_lru2);
                System.err.print(" values_lru3:    "); GenerateRandomArray.printArray(values_lru3);
                System.err.print(" values_checker: "); GenerateRandomArray.printArray(values_checker);
            }
//            System.out.printf("Oops! i=%d  capacity=%d", i, capacity); System.out.print("  values_lru1: "); GenerateRandomArray.printArray(values_lru1);
        }
    }
    // 耗时对比
    private static void compareTimeConsume() {
        System.out.println("\n耗时对比：");
        int testTimes = 10;
        int capacity = 100;
        int maxValue = capacity * 10;
        int operations = 300000;
        long startTimeLRU1, endTimeLRU1, startTimeLRU2, endTimeLRU2, startTimeLRU3, endTimeLRU3;
        long timeLRU1 = 0, timeLRU2 = 0, timeLRU3 = 0;
        for (int i = 0; i <= testTimes; i++) {
            LRU1<Integer, Integer> lru1 = new LRU1<>(capacity);
            LRU2<Integer, Integer> lru2 = new LRU2<>(capacity);
            LRU3<Integer, Integer> lru3 = new LRU3<>(capacity);
            int[] array = GenerateRandomArray.generateRandomMatrixPositive(1, 1, operations, operations, maxValue)[0];
            // LRU1
            startTimeLRU1 = System.currentTimeMillis();
            for (int value : array)
                lru1.put(value, value);
            endTimeLRU1 = System.currentTimeMillis();
            timeLRU1 = endTimeLRU1 - startTimeLRU1;
            // LRU2
            startTimeLRU2 = System.currentTimeMillis();
            for (int value : array)
                lru2.put(value, value);
            endTimeLRU2 = System.currentTimeMillis();
            timeLRU2 = endTimeLRU2 - startTimeLRU2;
            // LRU3
            startTimeLRU3 = System.currentTimeMillis();
            for (int value : array)
                lru3.put(value, value);
            endTimeLRU3 = System.currentTimeMillis();
            timeLRU3 = endTimeLRU3 - startTimeLRU3;
        }
        System.out.printf("maxValue：%d  capacity：%d  operations：%d\n", maxValue, capacity, operations);
        System.out.printf("LRU1(HashMap + DoubleLinkedList)  testTimes:%d  总耗时：%dms\n", testTimes, timeLRU1);
        System.out.printf("LRU2(HashMap + LinkedList)        testTimes:%d  总耗时：%dms\n", testTimes, timeLRU2);
        System.out.printf("LRU3(LinkedList)                  testTimes:%d  总耗时：%dms\n", testTimes, timeLRU3);
        System.out.println("----------------------------------------------------------");
        System.out.println("capacity和operations越大，则LRU1相对越快、LRU3越慢！");
    }
}