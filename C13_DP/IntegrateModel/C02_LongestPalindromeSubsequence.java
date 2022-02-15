package C13_DP.IntegrateModel;

import C01_random.GenerateRandomStringArray;

public class C02_LongestPalindromeSubsequence {
    // 给定一个字符串string，返回这个字符串的最长回文子序列长度，例如"a1b2c.d2e1f"的最长回文子序列是"12c21"或者"12.21"。
    // 等价于生成倒序字符串和原字符串找最长公共子序列。
    // 暴力递归
    public static int longestPalindromeSubsequence(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        char[] chars1 = string.toCharArray();
        char[] chars2 = reverse(chars1);
        return longestCommonSubsequence(chars1, chars2, chars1.length - 1, chars2.length - 1);
    }
    // 返回chars1[0..i1]和chars2[0..i2]的最长公共子序列长度
    private static int longestCommonSubsequence(char[] chars1, char[] chars2, int i1, int i2) {
        // base case
        if (i1 == 0 && i2 == 0) {  // 双方都只剩一个字符了
            return chars1[0] == chars2[0] ? 1 : 0;
        }
        if (i1 == 0) {  // chars1只剩一个字符了
            return chars1[0] == chars2[i2] ? 1 : longestCommonSubsequence(chars1, chars2, 0, i2 - 1);
        }
        if (i2 == 0) {  // chars2只剩一个字符了
            return chars1[i1] == chars2[0] ? 1 : longestCommonSubsequence(chars1, chars2, i1 - 1, 0);
        }
        // 查找相同字符可能存在的以下子序列范围
        // 可能相同字符存在于以下子序列内：1.chars1[0..i1]和chars2[0..i2-1]、2.chars1[0..i1-1]和chars2[0..i2]、3.chars1[0..i1]和chars2[0..i2]。
        int p1 = longestCommonSubsequence(chars1, chars2, i1, i2 - 1);
        int p2 = longestCommonSubsequence(chars1, chars2, i1 - 1, i2);
        if (chars1[i1] != chars2[i2]) {  // chars1[i1]!=chars2[i2]，可能性3跳过
            return Math.max(p1, p2);
        }
        int p3 = 1 + longestCommonSubsequence(chars1, chars2, i1 - 1, i2 - 1);  // 结算。chars1[i1]==chars2[i2]那么至少存在1对相同字符，剩余相同字符可能存在于：chars1[0..i1-1]和chars2[0..i2-1]。
        return Math.max(p1, Math.max(p2, p3));
    }
    private static char[] reverse(char[] chars) {
        char[] r = new char[chars.length];
        for (int i = chars.length - 1, j = 0; i >= 0; i--, j++) {
            r[i] = chars[j];
        }
        return r;
    }



    // 动态规划
    public static int longestPalindromeSubsequence2(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        char[] chars1 = string.toCharArray();
        char[] chars2 = reverse(chars1);
        int N1 = chars1.length;
        int N2 = chars2.length;
        int[][] dp = new int[N1][N2];
        // base case
        dp[0][0] = chars1[0] == chars2[0] ? 1 : 0;  // 双方都只有一个字符
        for (int i2 = 1; i2 < N2; i2++) {  // chars1只有一个字符
            dp[0][i2] = chars1[0] == chars2[i2] ? 1 : dp[0][i2 - 1];
        }
        for (int i1 = 1; i1 < N1; i1++) {  // chars2只有一个字符
            dp[i1][0] = chars1[i1] == chars2[0] ? 1 : dp[i1 - 1][0];
        }
        // 查找相同字符可能存在的以下子序列范围
        // 可能相同字符存在于以下子序列内：1.chars1[0..i1]和chars2[0..i2-1]、2.chars1[0..i1-1]和chars2[0..i2]、3.chars1[0..i1]和chars2[0..i2]。
        // 位置依赖：左、上、左上。
        for (int i1 = 1; i1 < N1; i1++) {  // 从上往下
            for (int i2 = 1; i2 < N2; i2++) { // 从左往右填
                int p1 = dp[i1][i2 - 1];
                int p2 = dp[i1 - 1][i2];
                if (chars1[i1] != chars2[i2]) {  // chars1[i1]!=chars2[i2]，可能性3跳过
                    dp[i1][i2] = Math.max(p1, p2);
                    continue;
                }
                int p3 = 1 + dp[i1 - 1][i2 - 1];  // 结算。chars1[i1]==chars2[i2]那么至少存在1对相同字符，剩余相同字符可能存在于：chars1[0..i1-1]和chars2[0..i2-1]。
                dp[i1][i2] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[N1 - 1][N2 - 1];
    }




    public static void main(String[] args) {
        int testTimes = 10000;
        int maxStrLen = 10;
        int maxCharKind = 5;
        for (int i = 0; i < testTimes; i++) {
            String string = GenerateRandomStringArray.generateRandomString(maxStrLen, maxCharKind);
            int ans1 = longestPalindromeSubsequence(string);
            int ans2 = longestPalindromeSubsequence2(string);
            int ans2_1 = C13_DP.WindowModel.C02_LongestPalindromeSubsequence.longestPalindromeSubsequence2_1(string);
            if (ans1 != ans2 || ans1 != ans2_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans2_1=%d   string: %s\n", ans1, ans2, ans2_1, string);
            }
        }
    }
}
