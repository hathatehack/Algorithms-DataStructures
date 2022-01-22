package C15_CatalanNumber;

public class C01_CountDifferentBT {
    // 给定N个节点，能组成多少种二叉树？
    //      1        头占用一个节点
    //   /     \
    // 0~n-1  n-1~0  左树0个节点则右树n-1个节点、左树1个节点则右树n-2个节点。。。
    // k(0) = 1, k(1) = 1
    // k(n) = k(0)*k(n-1) + k(1)*k(n-2) + ... + k(n-2)*k(1) + k(n-1)*k(0)
    // Catalan范式1
    public static long count1(int N) {
        if (N < 0) {
            return 0;
        }
        if (N < 2) {
            return 1;
        }
        long[] k = new long[N + 1];
        k[0] = 1;
        k[1] = 1;
        for (int n = 2; n <= N; n++) {
            for (int left = 0; left < n; left++) {
                k[n] += k[left] * k[n - 1 - left];
            }
        }
        return k[N];
    }


    // Catalan范式2，k(n) = c(2n, n) - c(2n, n-1)
    public static long count2(int N) {
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


    // Catalan范式3，k(n) = c(2n, n) / (n + 1)
    public static long count3(int N) {
        if (N < 0) {
            return 0;
        }
        if (N < 2) {
            return 1;
        }
        return combination(N * 2, N) / (N + 1);
    }


    public static void main(String[] args) {
        for (int n = 0; n <= 10; n++) {
            long ans1 = count1(n);
            long ans2 = count2(n);
            long ans3 = count3(n);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.printf("Oops! n=%d\n", n);
                System.out.printf("ans1=%d, ans2=%d, ans3=%d\n", ans1, ans2, ans3);
            }
        }
    }
}
