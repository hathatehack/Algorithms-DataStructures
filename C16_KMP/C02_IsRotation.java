package C16_KMP;

import C01_random.GenerateRandomArray;

public class C02_IsRotation {
    // 判断str1和str2是否互为旋转字符串，例如"abc"、"bca"、"cab"互为旋转字符串。
    public static boolean isRotation(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() != str2.length()) {
            return false;
        }
        // str1拼接自己，包含了全部可能性，使用KMP判断str2是否是str1+str1的子串
        String string = str1 + str1;
        return indexOf(string, str2) != -1;
    }
    // KMP，时间复杂度O(N)
    private static int indexOf(String string, String match) {
        if (string == null || match == null || match.length() == 0 || string.length() < match.length()) {
            return -1;
        }
        char[] s = string.toCharArray();
        char[] m = match.toCharArray();
        int[] preNext = generatePreNextArray(m);  // 生成match字符串的前缀匹配表
        int sIndex = 0;
        int mIndex = 0;
        while (sIndex < s.length && mIndex < m.length) {  // 找到一个match即可
            if (s[sIndex] == m[mIndex]) {
                sIndex++;
                mIndex++;
            } else if (preNext[mIndex] > -1) {
                mIndex = preNext[mIndex];  // 尝试用m[0,mIndex)内与后缀串相同的前缀串去接着匹配string
            } else {  // 如果s[sIndex]和match的首个字符都不相等，那sIndex++
                sIndex++;
            }
        }
        return mIndex == m.length ? sIndex - mIndex : -1;
    }
    private static int[] generatePreNextArray(char[] chars) {
        if (chars.length == 1) {
            return new int[] {-1};
        }
        int[] preNext = new int[chars.length];  // 在每个位置记录前个子串内与后缀串相同的最长前缀串结束位置（开区间）
        preNext[0] = -1;  // 0位置前不可达
        preNext[1] = 0;   // 1位置不匹配只能去0位置从头开始匹配
        int matchIndex = 0;  // 前缀串待匹配位置
        int i = 2;
        while (i < chars.length) {
            if (chars[i - 1] == chars[matchIndex]) {  // 前个子串存在相同的后缀串和前缀串
                preNext[i++] = ++matchIndex;  // 在当前位置记录前个子串的相应前缀串结束位置（开区间），当i位置与目标字符串匹配失败后可以从该前缀后面接着匹配，而不需要完全从头匹配
            } else if (matchIndex > 0) {
                matchIndex = preNext[matchIndex];  // 尝试前个子串的相应前缀串后面接着匹配
            } else {
                preNext[i++] = 0;  // 前个子串不存在相应的前缀串，只能从头开始匹配
            }
        }
        return preNext;
    }



    // 暴力解，时间复杂度O(N^3)
    public static boolean isRotation2(String str1, String str2) {
        for (int i = 0; i < str1.length(); i++) {
            String string = str1.substring(i) + str1.substring(0, i);
            if (string.matches(str2)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        int testTimes = 100000;
        int stringSize = 20;
        int matchSize = 5;
        char start = 'a';
        char end = 'f';
        for (int i = 0; i < testTimes; i++) {
            String string = String.valueOf(GenerateRandomArray.generateRandomArrayChar(stringSize, start, end));
            String match = String.valueOf(GenerateRandomArray.generateRandomArrayChar(matchSize, start, end));
            boolean ans1 = isRotation(string, match);
            boolean ans2 = isRotation2(string, match);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%b, ans2=%b  string: %s match: %s\n", ans1, ans2, string, match);
            }
        }
    }
}
