package C02_BitOperation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class C04_KMTimes {
    static public void main(String[] args) {
        int testTimes = 100000;
        int maxKinds = 5;
        int maxValue = 10;
        int maxM = 4;
        for (int i = 0; i < testTimes; i++) {
            int[] r = {(int)(Math.random() * maxM + 1), (int)(Math.random() * maxM + 1)};
            int k = Math.min(r[0], r[1]);
            int m = Math.max(r[0], r[1]);
            if (k == m) {
                m++;
            }

            int[] array = randomArray(maxKinds, maxValue, k, m);
            int ans1 = KTimes(array, k, m);
            int ans2 = KTimes2(array, k, m);
            if (ans1 != ans2) {
                System.out.println("出错了！");
                System.out.printf("k=%d, m=%d, r=%s\n", k, m, Arrays.toString(r));
                System.out.println(Arrays.toString(array));
                System.out.println(ans1);
                System.out.println(ans2);
            }
        }
    }

    static public HashMap<Integer, Integer> bitIndexMap = new HashMap<>();

    static public void bitIndexMapCreater(HashMap<Integer, Integer> map) {
        int bit1 = 1;
        for (int i = 0; i < 32; i++) {
            map.put(bit1, i);
            bit1 <<= 1;
        }
    }

    // 一个数组中有一种数出现K次，其他数都出现M次，M>1且K<M，如果存在则返回K次的数，否则返回-1。
    static public int KTimes(int[] array, int k, int m) {
        if (bitIndexMap.size() == 0)
            bitIndexMapCreater(bitIndexMap);
        int[] bitCounts = new int[32];
        // 统计每个数每一bit为1的和
        for (int num : array) {
            while (num != 0) {
                int rightBit1 = num & -num;
                int idx = bitIndexMap.get(rightBit1);
                bitCounts[idx]++;
                num ^= rightBit1;
            }
        }
        int ans = 0;
        // 提取相应bitCounts[i]%m为k的bit
        for (int i = 0; i < 32; i++) {
            if (bitCounts[i] % m == 0) {
                continue;
            } else if (bitCounts[i] % m == k) {
                ans |= (1 << i);
            } else {
                return -1;
            }
        }
        if (ans == 0) {
            int count = 0;
            for (int num : array) {
                if (num == 0) {
                    count++;
                }
            }
            if (count != k) {
                return -1;
            }
        }

        return ans;
    }

    static public int KTimes2(int[] array, int k, int m) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : array) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }
        for (int num : map.keySet()) {
            if (map.get(num) == k) {
                return num;
            }
        }
        return -1;
    }





    static public int[] randomArray(int maxKinds, int maxValue, int k, int m) {
        int kNumber = randomNumber(maxValue);
        int kTimes = Math.random() < 0.5 ? k : (int)(Math.random() * (m - 1) + 1);  // k随机化，模拟出现不符合规则的序列
        int numKinds = Math.max((int)(Math.random() * (maxKinds + 1)), 2);
        int[] array = new int[kTimes + (numKinds - 1) * m];
        int index = 0;
        for (; index < kTimes; index++) {
            array[index] = kNumber;
        }
        numKinds--;
        HashSet<Integer> kinds = new HashSet<>();
        kinds.add(kNumber);
        while (numKinds != 0) {
            int number = 0;
            do {
                number = randomNumber(maxValue);
            } while (kinds.contains(number));
            kinds.add(number);
            numKinds--;
            for (int i = 0; i < m; i++) {
                array[index++] = number;
            }
        }
        // Disrupt the order
        for (int i = 0; i < array.length; i++) {
            int j = (int)(Math.random() * array.length);
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }

        return array;
    }

    static public int randomNumber(int maxValue) {
        // [-maxValue , maxValue]
        return (int)(Math.random() * (maxValue + 1)) - (int)(Math.random() * (maxValue + 1));
    }
}
