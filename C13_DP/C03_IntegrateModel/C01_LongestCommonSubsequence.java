package C13_DP.C03_IntegrateModel;

import C01_random.GenerateRandomStringArray;

public class C01_LongestCommonSubsequence {
    // 测试链接：https://leetcode.com/problems/longest-common-subsequence
    // 给定两个字符串str1和str2，返回这两个字符串的最长公共子序列长度，如str1="1a2b3c"，str2=".123"，lcs是"123"。
    // 暴力递归
    public static int longestCommonSubsequence(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() == 0 || str2.length() == 0) {
            return 0;
        }
        return longestCS(str1.toCharArray(), str2.toCharArray(), str1.length() - 1, str2.length() - 1);
    }
    // 返回chars1[0..end1]和chars2[0..end2]的最长公共子序列长度
    private static int longestCS(char[] chars1, char[] chars2, int end1, int end2) {
        // base case
        if (end1 == 0 && end2 == 0) {  // 双方都只剩一个字符了
            return chars1[0] == chars2[0] ? 1 : 0;
        }
        if (end1 == 0) {  // chars1只剩一个字符了
            return chars1[0] == chars2[end2] ? 1 : longestCS(chars1, chars2, 0, end2 - 1);
        }
        if (end2 == 0) {  // chars2只剩一个字符了
            return chars1[end1] == chars2[0] ? 1 : longestCS(chars1, chars2, end1 - 1, 0);
        }
        // 查找相同字符可能存在的以下子序列范围
        // 可能相同字符存在于以下子序列内：1.chars1[0..end1]和chars2[0..end2-1]、2.chars1[0..end1-1]和chars2[0..end2]、3.chars1[0..end1]和chars2[0..end2]。
        int p1 = longestCS(chars1, chars2, end1, end2 - 1);
        int p2 = longestCS(chars1, chars2, end1 - 1, end2);
        if (chars1[end1] != chars2[end2]) {  // chars1[end1]!=chars2[end2]，可能性3跳过
            return Math.max(p1, p2);
        }
        int p3 = 1 + longestCS(chars1, chars2, end1 - 1, end2 - 1);  // 结算。chars1[end1]==chars2[end2]那么至少存在1对相同字符，剩余相同字符可能存在于：chars1[0..end1-1]和chars2[0..end2-1]。
        return Math.max(p1, Math.max(p2, p3));
    }



    // 缓存表记忆化搜索。子序列存在重复查找，可做缓存。
    public static int longestCommonSubsequence2(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() == 0 || str2.length() == 0) {
            return 0;
        }
        int[][] dp = new int[str1.length()][str2.length()];
        for (int end1 = str1.length() - 1; end1 >= 0; end1--) {
            for (int end2 = str2.length() - 1; end2 >= 0; end2--) {
                dp[end1][end2] = -1;
            }
        }
        return longestCS2(str1.toCharArray(), str2.toCharArray(), str1.length() - 1, str2.length() - 1, dp);
    }
    // 返回chars1[0..end1]和chars2[0..end2]的最长公共子序列长度
    private static int longestCS2(char[] chars1, char[] chars2, int end1, int end2, int[][] dp) {
        if (dp[end1][end2] != -1) {
            return dp[end1][end2];
        }
        // base case
        if (end1 == 0 && end2 == 0) {  // 双方都只剩一个字符了
            return dp[end1][end2] = chars1[0] == chars2[0] ? 1 : 0;
        }
        if (end1 == 0) {  // chars1只剩一个字符了
            return dp[end1][end2] = chars1[0] == chars2[end2] ? 1 : longestCS2(chars1, chars2, 0, end2 - 1, dp);
        }
        if (end2 == 0) {  // chars2只剩一个字符了
            return dp[end1][end2] = chars1[end1] == chars2[0] ? 1 : longestCS2(chars1, chars2, end1 - 1, 0, dp);
        }
        // 查找相同字符可能存在的以下子序列范围
        // 可能相同字符存在于以下子序列内：1.chars1[0..end1]和chars2[0..end2-1]、2.chars1[0..end1-1]和chars2[0..end2]、3.chars1[0..end1]和chars2[0..end2]。
        int p1 = longestCS2(chars1, chars2, end1, end2 - 1, dp);
        int p2 = longestCS2(chars1, chars2, end1 - 1, end2, dp);
        if (chars1[end1] != chars2[end2]) {  // chars1[end1]!=chars2[end2]，可能性3跳过
            return dp[end1][end2] = Math.max(p1, p2);
        }
        int p3 = 1 + longestCS2(chars1, chars2, end1 - 1, end2 - 1, dp);  // 结算。chars1[end1]==chars2[end2]那么至少存在1对相同字符，剩余相同字符可能存在于：chars1[0..end1-1]和chars2[0..end2-1]。
        return dp[end1][end2] = Math.max(p1, Math.max(p2, p3));
    }



    // 动态规划
    public static int longestCommonSubsequence3(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() == 0 || str2.length() == 0) {
            return 0;
        }
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int N1 = chars1.length;
        int N2 = chars2.length;
        int[][] dp = new int[N1][N2];
        // base case
        dp[0][0] = chars1[0] == chars2[0] ? 1 : 0;  // 双方都只有一个字符
        for (int end2 = 1; end2 < N2; end2++) {  // 上边界，chars1只有一个字符
            dp[0][end2] = chars1[0] == chars2[end2] ? 1 : dp[0][end2 - 1];
        }
        for (int end1 = 1; end1 < N1; end1++) {  // 左边界，chars2只有一个字符
            dp[end1][0] = chars1[end1] == chars2[0] ? 1 : dp[end1 - 1][0];
        }
        // 查找相同字符可能存在的以下子序列范围
        // 可能相同字符存在于以下子序列内：1.chars1[0..end1]和chars2[0..end2-1]、2.chars1[0..end1-1]和chars2[0..end2]、3.chars1[0..end1]和chars2[0..end2]。
        // 位置依赖：左、上、左上（即同行内依赖左，下行依赖上行）。
        for (int end1 = 1; end1 < N1; end1++) {  // 从上往下
            for (int end2 = 1; end2 < N2; end2++) { // 从左往右填
                int p1 = dp[end1][end2 - 1];  // 左
                int p2 = dp[end1 - 1][end2];  // 上
                if (chars1[end1] != chars2[end2]) {  // chars1[end1]!=chars2[end2]，可能性3跳过
                    dp[end1][end2] = Math.max(p1, p2);
                    continue;
                }
                // 1+左上
                int p3 = 1 + dp[end1 - 1][end2 - 1];  // 结算。chars1[end1]==chars2[end2]那么至少存在1对相同字符，剩余相同字符可能存在于：chars1[0..end1-1]和chars2[0..end2-1]。
                dp[end1][end2] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[N1 - 1][N2 - 1];
    }

    // 动态规划优化。对于位置依赖：左、上、左上（即同行内依赖左，下行依赖上行），可优化为复用一个一维数组进行自我更新、一个变量保存左上记录。
    public static int longestCommonSubsequence3_1(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() == 0 || str2.length() == 0) {
            return 0;
        }
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int N1 = chars1.length;
        int N2 = chars2.length;
        int[] dp = new int[N2];  // 所有行复用一个一维数组进行自我更新
        int topLeft;  // 所有行复用一个变量保存左上记录
        // base case
        dp[0] = chars1[0] == chars2[0] ? 1 : 0;  // 双方都只有一个字符
        for (int end2 = 1; end2 < N2; end2++) {  // 上边界，chars1只有一个字符
            dp[end2] = chars1[0] == chars2[end2] ? 1 : dp[end2 - 1];
        }
        // 查找相同字符可能存在的以下子序列范围
        // 可能相同字符存在于以下子序列内：1.chars1[0..end1]和chars2[0..end2-1]、2.chars1[0..end1-1]和chars2[0..end2]、3.chars1[0..end1]和chars2[0..end2]。
        // 位置依赖：左、上、左上（即同行内依赖左，下行依赖上行）。
        for (int end1 = 1; end1 < N1; end1++) {  // 从上往下
            topLeft = dp[0];  // 保存左上记录
            dp[0] = chars1[end1] == chars2[0] ? 1 : dp[0];  // 左边界，chars2只有一个字符
            for (int end2 = 1; end2 < N2; end2++) { // 从左往右填
                int p1 = dp[end2 - 1];  // 左
                int p2 = dp[end2];  // 上
                if (chars1[end1] != chars2[end2]) {  // chars1[end1]!=chars2[end2]，可能性3跳过
                    topLeft = dp[end2];  // 保存左上记录
                    dp[end2] = Math.max(p1, p2);
                    continue;
                }
                // 1+左上
                int p3 = 1 + topLeft;  // 结算。chars1[end1]==chars2[end2]那么至少存在1对相同字符，剩余相同字符可能存在于：chars1[0..end1-1]和chars2[0..end2-1]。
                topLeft = dp[end2];  // 保存左上记录
                dp[end2] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[N2 - 1];
    }



    public static void main(String[] args) {
        int testTimes = 100000;
        int maxStrLen = 10;
        int maxCharKind = 5;
        for (int i = 0; i < testTimes; i++) {
//            String str1 = "ab";
//            String str2 = "ba";
            String str1 = GenerateRandomStringArray.generateRandomString(maxStrLen, maxCharKind);
            String str2 = GenerateRandomStringArray.generateRandomString(maxStrLen, maxCharKind);
            int ans1 = longestCommonSubsequence(str1, str2);
            int ans2 = longestCommonSubsequence2(str1, str2);
            int ans3 = longestCommonSubsequence3(str1, str2);
            int ans3_1 = longestCommonSubsequence3_1(str1, str2);
            if (ans1 != ans2 || ans1 != ans3 || ans1 != ans3_1) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans3=%d, ans3_1=%d   str1:%s  str2:%s\n", ans1, ans2, ans3, ans3_1, str1, str2);
            }
        }
    }
}
