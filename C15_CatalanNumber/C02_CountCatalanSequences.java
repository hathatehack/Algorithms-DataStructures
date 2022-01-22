package C15_CatalanNumber;

import java.util.LinkedList;

public class C02_CountCatalanSequences {
    // 假设给你N个0和N个1，你必须用全部数字拼接序列，返回有多少个序列满足要求：任何前缀串0的数量都不少于1的数量。
    // Catalan范式3，k(n) = c(2n, n) / (n + 1)
    public static long countCatalanSequences(int N) {
        if (N < 0) {
            return 0;
        }
        if (N < 2) {
            return 1;
        }
        return combination(N * 2, N) / (N + 1);
    }

    // Catalan范式2，k(n) = c(2n, n) - c(2n, n-1)
    public static long countCatalanSequences2(int N) {
        if (N < 0) {
            return 0;
        }
        if (N < 2) {
            return 1;
        }
        return combination(N * 2, N) - combination(N * 2, N - 1);
    }
    // C(n, m) = n! / (m! * (n - m)!)
    private static long combination(int n, int m) {
        long _n = 1;
        long _m = 1;
        for (int i = 1, j = (n - m) + 1; i <= m; i++, j++) {
            _n *= j;
            _m *= i;
            // 防止溢出
            long gcd = gcd(_n, _m);
            _n /= gcd;
            _m /= gcd;
        }
        return _n / _m;
    }
    // 最大公约数
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }


    // 暴力解
    public static long countCatalanSequences3(int N) {
        LinkedList<LinkedList<Integer>> all = new LinkedList<>();
        process(N, N, new LinkedList<Integer>(), all);
        long count = 0;
        for (LinkedList<Integer> seq : all) {
            int status = 0;
            for (Integer ele : seq) {
                if (ele == 0) {
                    status++;
                } else {
                    status--;
                }
                if (status < 0) {
                    break;
                }
            }
            if (status == 0) {
                count++;
            }
        }
        return count;
    }
    private static void process(int restZero, int restOne, LinkedList<Integer> sequence, LinkedList<LinkedList<Integer>> all) {
        if (restZero == 0 && restOne == 0) {
            all.add(new LinkedList<>(sequence));
            return;
        }
        // 尝试当前位置拼接0
        if (restZero != 0) {
            sequence.addLast(0);
            process(restZero - 1, restOne, sequence, all);
            sequence.removeLast();
        }
        // 尝试当前位置拼接1
        if (restOne != 0) {
            sequence.addLast(1);
            process(restZero, restOne - 1, sequence, all);
            sequence.removeLast();
        }
    }



    public static void main(String[] args) {
        for (int n = 0; n <= 10; n++) {
            long ans1 = countCatalanSequences(n);
            long ans2 = countCatalanSequences2(n);
            long ans3 = countCatalanSequences3(n);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.printf("Oops! n=%d\n", n);
                System.out.printf("ans1=%d, ans2=%d, ans3=%d\n", ans1, ans2, ans3);
            }
        }
    }
}
