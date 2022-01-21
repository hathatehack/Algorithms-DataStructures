package C13_Recurse;

import java.util.ArrayList;

public class C04_AllCombinations {
    // array里选unitSize个数一组，返回所有组合。
    // 递归版
    public static long allCombinations(int[] array, int unitSize) {
        if (array == null || array.length == 0 || unitSize <= 0) {
            return 0;
        }
        return combination(array, 0, array.length - 1, unitSize);
    }
    private static long combination(int[] array, int start, int end, int unitSize) {
        if (unitSize == 1) {
            return end - start + 1;
        }
        int all = 0;
        int limit = end - unitSize + 1;  // 避免重复组合
        for (int i = start; i <= limit; i++) {
            all += combination(array, i + 1, end, unitSize - 1);
        }
        return all;
    }
    private static ArrayList<ArrayList<Integer>> combination_(int[] array, int start, int end, int unitSize) {
        ArrayList<ArrayList<Integer>> all = new ArrayList<>();
        if (unitSize == 1) {
            for (int i = start; i <= end; i++) {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(array[i]);
                all.add(arr);
            }
            return all;
        }
        int limit = end - unitSize + 1;  // 避免重复组合
        for (int i = start; i <= limit; i++) {
            ArrayList<ArrayList<Integer>> group = combination_(array, i + 1, end, unitSize - 1);
            for (ArrayList<Integer> arr : group) {
                arr.add(0, array[i]);
            }
            all.addAll(group);
        }
        return all;
    }



    // 公式法
    public static long allCombinations2(int[] array, int unitSize) {
        if (array == null || array.length == 0 || unitSize <= 0) {
            return 0;
        }
        return combination2(array.length, unitSize);
    }
    // C(n, m) = n! / (m! * (n - m)!)
    private static long combination2(int n, int m) {
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




    private static int[] generateArrayNonRepeat(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    public static void main(String[] args) {
        int maxSize = 10;
        for (int n = 0; n <= maxSize; n++) {
            int[] array = generateArrayNonRepeat(n);
            for (int m = 0; m <= n; m++) {
                long ans1 = allCombinations(array, m);
                long ans2 = allCombinations2(array, m);
                if (ans1 != ans2) {
                    System.out.printf("Oops! ans1=%d, ans2=%d  n:%d m:%d\n", ans1, ans2, n, m);
                }
            }
        }
    }
}
