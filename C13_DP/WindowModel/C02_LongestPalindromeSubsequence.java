package C13_DP.WindowModel;

import C01_random.GenerateRandomStringArray;

public class C02_LongestPalindromeSubsequence {
    // 给定一个字符串string，返回这个字符串的最长回文子序列长度，例如"a1b2c.d2e1f"的最长回文子序列是"12c21"或者"12.21"。
    // 暴力递归
    public static int longestPalindromeSubsequence(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        char[] chars = string.toCharArray();
        return longestPS(chars, 0, chars.length - 1);
    }
    // 返回chars[L..R]内的最长回文子序列长度
    private static int longestPS(char[] chars, int L, int R) {
        // base case
        if (L == R) {  // 只剩一个字符了
            return 1;
        }
        if (L == R - 1) { // 只剩两个字符了
            return chars[L] == chars[R] ? 2 : 1;
        }
        // 回文子序列可能存在于以下子序列内：1.以L开头以R-1结尾、2.以L+1开头以R结尾、3.以L开头以R结尾、4.以L+1开头以R-1结尾。
        int p1 = longestPS(chars, L, R - 1);  // 查找L开头R-1结尾的子串内最长回文长度
        int p2 = longestPS(chars, L + 1, R);  // 查找L+1开头R结尾的子串内最长回文长度
        int p3 = (chars[L] != chars[R] ? 0 : 2)  // 结算。若首尾相同字符，[L..R]至少有长度为2的回文；若首尾不同字符，不成回文
                + longestPS(chars, L + 1, R - 1); // 查找去除首尾的子串内最长回文长度
        return Math.max(p1, Math.max(p2, p3));
    }



    // 动态规划
    public static int longestPalindromeSubsequence2(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        char[] chars = string.toCharArray();
        int N = chars.length;
        int[][] dp = new int[N][N];
        // base case
        dp[N - 1][N - 1] = 1;
        for (int L = N - 2; L >= 0; L--) {
            dp[L][L] = 1;  // 只剩一个字符时
            dp[L][L + 1] = chars[L] == chars[L + 1] ? 2 : 1; // 只剩两个字符时
        }
        // [L..R]范围内查找，R不会越过L左边（即R>=L），所以只计算右上半三角区，位置依赖：2+左下、左下、左、下。
        for (int L = N - 3; L >= 0; L--) {  // 从下往上
            for (int R = L + 2; R < N; R++) { // 从左往右填
                // 回文子序列可能存在于以下子序列内：1.以L开头以R-1结尾、2.以L+1开头以R结尾、3.以L开头以R结尾、4.以L+1开头以R-1结尾。
                int p1 = dp[L][R - 1];  // 查找左
                int p2 = dp[L + 1][R];  // 查找下
                int p3 = (chars[L] != chars[R] ? 0 : 2)  // 结算。若首尾相同字符，[L..R]至少有长度为2的回文；若首尾不同字符，不成回文
                        + dp[L + 1][R - 1];  // 查找左下
                dp[L][R] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[0][N - 1];
    }

    // 动态规划优化。对于位置依赖：2+左下、左下、左、下，因为左会依赖左下，下也会依赖左下，所以单独左下可以不参与比较。
    public static int longestPalindromeSubsequence2_1(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        char[] chars = string.toCharArray();
        int N = chars.length;
        int[][] dp = new int[N][N];
        // base case
        dp[N - 1][N - 1] = 1;
        for (int L = N - 2; L >= 0; L--) {
            dp[L][L] = 1;  // 只剩一个字符时
            dp[L][L + 1] = chars[L] == chars[L + 1] ? 2 : 1; // 只剩两个字符时
        }
        // [L..R]范围内查找，R不会越过L左边（即R>=L），所以只计算右上半三角区，位置依赖：左、下、2+左下。
        for (int L = N - 3; L >= 0; L--) {  // 从下往上
            for (int R = L + 2; R < N; R++) { // 从左往右填
                // 回文子序列可能存在于以下子序列内：1.以L开头以R-1结尾、2.以L+1开头以R结尾、3.以L开头以R结尾、4.以L+1开头以R-1结尾。
                int p1 = dp[L][R - 1];  // 查找左
                int p2 = dp[L + 1][R];  // 查找下
                if (chars[L] != chars[R]) {  // 优化，因为左会依赖左下，下也会依赖左下，所以单独左下可以不参与比较。
                    dp[L][R] = Math.max(p1, p2);
                    continue;
                }
                int p3 = 2 + dp[L + 1][R - 1];  // 结算。首尾相同字符，[L..R]至少有长度为2的回文；剩余回文子序列查找左下。
                dp[L][R] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[0][N - 1];
    }




    public static void main(String[] args) {
        int testTimes = 10000;
        int maxStrLen = 10;
        int maxCharKind = 5;
        for (int i = 0; i < testTimes; i++) {
            String string = GenerateRandomStringArray.generateRandomString(maxStrLen, maxCharKind);
            int ans1 = longestPalindromeSubsequence(string);
            int ans2 = longestPalindromeSubsequence2(string);
            int ans2_1 = longestPalindromeSubsequence2_1(string);
            if (ans1 != ans2 || ans1 != ans2_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans2_1=%d   string: %s\n", ans1, ans2, ans2_1, string);
            }
        }
    }
}
