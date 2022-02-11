package C13_DP.C01_StackModel;

import C01_random.GenerateRandomStringArray;

public class C02_TransformNumbersToLetters {
    // 规定1=>A、2=>B .. 26=>Z，数字串111可以转化为AAA、KA、AK，给定只含数字字符串string，返回有多少种转化结果。
    // 暴力递归
    public static int numbersToLetters1(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        return process1(string.toCharArray(), 0);
    }
    // chars为数字字符串，从index位置开始转换，返回全部成功的转法。
    private static int process1(char[] chars, int index) {
        // base case
        if (index == chars.length) {  // 全部字符处理完毕，得到1种转法。
            return 1;
        }
        if (chars[index] == '0') {  // 数字0开头没有对应转换，结束分支。
            return 0;
        }
        // 尝试一次转换1个字符、一次转换相邻2个字符
        int p1 = process1(chars, index + 1);  // 一次转换1个字符
        int p2 = (index + 1 == chars.length) || ((chars[index] - '0') * 10 + chars[index + 1] - '0' >= 27) ? 0 : // 相邻2个字符无法转成一个字母
                process1(chars, index + 2);  // 一次转换相邻2个字符
        return p1 + p2;
    }




    // 动态规划
    public static int numbersToLetters2(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        char[] chars = string.toCharArray();
        if (chars[0] == '0')
            return 0;
        int[] dp = new int[chars.length + 1];
        // base case
        dp[chars.length] = 1;
        // 尝试一次转换1个字符、一次转换相邻2个字符
        for (int i = chars.length - 1; i >= 0; i--) {  // 从右往左填
            if (chars[i] != '0') {  //
                int p1 = dp[i + 1]; // 一次转换1个字符
                int p2 = (i + 1 == chars.length) || ((chars[i] - '0') * 10 + chars[i + 1] - '0' >= 27) ? 0 : // 剪枝！
                        dp[i + 2];  // 一次转换相邻2个字符
                dp[i] = p1 + p2;
            }
        }
        return dp[0];
    }





    public static void main(String[] args) {
        int testTimes = 100000;
        int maxStrLen = 10;
        for (int i = 0; i <= testTimes; i++) {
            String string = GenerateRandomStringArray.generateRandomString(maxStrLen, '0', '9');
            int ans1 = numbersToLetters1(string);
            int ans2 = numbersToLetters2(string);
            if (ans1 != ans2) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d  string: %s\n", ans1, ans2, string);
            }
        }
    }
}
