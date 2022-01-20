package C01_random;

public class C02_ReservoirSampling {
    // 给定一个袋子只能装下C个球，现有N个球，如何做到每个球都等概率被放进袋子。
    // 蓄水池算法
    public static class RandomBag {
        public int[] bag;
        private int capacity;
        private int count;  // 有多少个球尝试入袋

        public RandomBag(int _capacity) {
            capacity = _capacity;
            bag = new int[_capacity];
            count = 0;
        }

        private int random(int max) {
            return (int)(Math.random() * (max + 1));
        }

        public void put(int who) {
            if (count < capacity) {
                bag[count] = who;
            } else if (random(count) < capacity) {
                bag[random(capacity - 1)] = who;
            }
            count++;
        }
    }


    public static void main(String[] args) {
        int testTimes = 1000000;
        int capacity = 5;
        int balls = 20;
        int[] statistics = new int[balls];
        for (int i = 0; i < testTimes; i++) {
            RandomBag bag = new RandomBag(capacity);
            for (int j = 0; j < balls; j++) {
                bag.put(j);
            }
            // 统计这轮袋子内放了哪些球
            for (int k = 0; k < bag.bag.length; k++) {
                statistics[bag.bag[k]]++;
            }
        }
        // 打印testTimes次试验后每个球的入袋次数
        for (int i = 0; i < statistics.length; i++) {
            System.out.printf("ball%02d 入袋%d次\n", i, statistics[i]);
        }
    }
}
