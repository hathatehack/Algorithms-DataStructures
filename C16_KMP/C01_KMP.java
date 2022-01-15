package C16_KMP;

import C01_random.GenerateRandomArray;

public class C01_KMP {
    // 返回string中第一个match的起始位置。
    // KMP，时间复杂度O(N)
    public static int indexOf(String string, String match) {
        if (string == null || match == null || match.length() == 0 || match.length() > string.length()) {
            return -1;
        }
        char[] s = string.toCharArray();
        char[] m = match.toCharArray();
        int[] nextInMatch = getNextArray(m);  // 生成match字符串的前缀表
        int sIndex = 0;
        int mIndex = 0;
        while (sIndex < s.length && mIndex < m.length) {  // 找到一个match即可
            if (s[sIndex] == m[mIndex]) {
                sIndex++;
                mIndex++;
            } else if (nextInMatch[mIndex] > -1) {  //
                mIndex = nextInMatch[mIndex];
            } else {  // s[sIndex]和match的首字符都不相等，那sIndex++
                sIndex++;
            }
        }
        return mIndex == m.length ? sIndex - mIndex : -1;
    }
    private static int[] getNextArray(char[] chars) {
        if (chars.length == 1) {
            return new int[] {-1};
        }
        // abc..abc...abc..abcd
        // ---  ---
        // ---        ---
        // ---             ---
        int[] next = new int[chars.length];  //  开头到每个位置内，与后缀串相同的前缀串结束位置。。。。。每个位置匹配失败后往前找最可能的匹配位置
        next[0] = -1;  // 0位置不匹配没有再前位置
        next[1] = 0;   // 1位置不匹配只能去0位置从头开始匹配
        int matchedIndex = 0; // 已经匹配到哪里 ？？？？？
        int i = 2;
        while (i < chars.length) {
            if (chars[i - 1] == chars[matchedIndex]) {
                next[i++] = ++matchedIndex;  // 记录i位置左边前缀结束位置
            } else if (matchedIndex > 0) {  // 左边有相同前缀
                matchedIndex = next[matchedIndex];
            } else {
                next[i++] = 0;
            }
        }
        return next;
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
            int ans1 = indexOf(string, match);
            int ans2 = string.indexOf(match);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d  string: %s match: %s\n", ans1, ans2, string, match);
            }
        }
    }
}
