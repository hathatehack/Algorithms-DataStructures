package C01_random;

public class C01_RandToRand {
    static public void main(String[] args) {
//        testRandom();
//        testXPower2();
//        testXPower3();
        gTest();
    }

    static public void testRandom() {
        // Math.random() -> double -> [0,1)   小于x的概率为x，x范围[0,1)。
        int testTimes = 100000;
        int count = 0;
        for (double n = 0.1; n <= 1; n+=0.1) {
            for (int i = 0; i < testTimes; i++) {
                if (Math.random() < n) {
                    count++;
                }
            }

            System.out.format("<%.1f: %f\n", n, (double) count / testTimes);
            count = 0;
        }

        System.out.println("=========================");

        // 每个区间出现次数基本相同
        int[] counts = new int[10];
        int K = 10;
        for (int i = 0; i < testTimes; i++) {
            int num = (int)(Math.random() * K);  //[0,K) -> [0,9]
            counts[num]++;
        }
        for (int i = 0; i < K; i++) {
            System.out.format("%d这个数出现了%d次\n", i, counts[i]);
        }
    }

    static public void testXPower2() {
        int testTimes = 100000;
        int count = 0;
        double x = 0.1;
        for (int i = 0; i < testTimes; i++) {
            // 将[0,1)范围内[0,x)的出现概率有原来的x调整成x的平方
            if (Math.max(Math.random(), Math.random()) < x) {
                count++;
            }
        }
        System.out.println((double)count / testTimes);
        System.out.println(Math.pow(x, 2));
    }

    static public void testXPower3() {
        int testTimes = 100000;
        int count = 0;
        double x = 0.1;
        for (int i = 0; i < testTimes; i++) {
            // 将[0,1)范围内[0,x)的出现概率有原来的x调整成x的3次方
            if (Math.max(Math.random(), Math.max(Math.random(), Math.random())) < x) {
                count++;
            }
        }
        System.out.println((double)count / testTimes);
        System.out.println(Math.pow(x, 3));
    }



    // 给定函数等概率返回[0,5]
    static public int r1() {
        return (int)(Math.random() * 5) + 1;
    }
    // 改造成等概率返回0,1
    static public int f1() {
        int result = 0;
        do {
            result = r1();
        } while(result == 3);
        return result < 3 ? 0 : 1;
    }
    // 改造成等概率返回[0,7]，通过组装成b000~b111，每一bit的0和1都是等概率的。
    static public int g1() {
        return (f1()<<2) + (f1()<<1) + f1();
    }
    // 改造成等概率返回[0,6]
    static public int g2() {
        int result = 0;
        do {
            result = g1();
        } while (result > 6);
        return result;
    }
    // 改造成等概率返回[1,7]
    static public int g3() {
        return g2() + 1;
    }

    // 给定函数不等概率返回0,1
    static public int r2() {
        return Math.random() < 0.8 ? 0 : 1;
    }
    // 改造成等概率返回0,1
    static public int f2() {
        int result = 0;
        do {
            result = r2();
        } while (result == r2());
        return result;
    }

    static public void gTest() {
        int testTimes = 100000;
        int[] counts = new int[10];
        int K = 10;
        for (int i = 0; i < testTimes; i++) {
            counts[g3()]++;
        }
        for (int i = 0; i < K; i++) {
            System.out.format("%d这个数出现了%d次\n", i, counts[i]);
        }
    }
}
