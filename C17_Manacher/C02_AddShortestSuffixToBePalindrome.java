package C17_Manacher;

import C01_random.GenerateRandomArray;

public class C02_AddShortestSuffixToBePalindrome {
    // 给定一个字符串string，只能在后面添加字符，使string变成回文串，返回至少要添加几个字符。
    // 分析：找出包含最后一个字符的最长回文串，把左边剩余字符逆序添加到末尾就构成回文了。
    // Manacher算法，时间复杂度O(N)
    public static String addShortestSuffix(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        // 找出包含最后一个字符的最长回文串
        char[] mChars = generateManacherChars(string);  // 转化为奇数长度的特殊字符串，如"121" -> "#1#2#1#"
        int[] pRadiusArray = new int[mChars.length];  // 每个位置做中心的最长回文半径
        int containEndCharMaxLen = 0;  // 含最后字符的最长回文长度
        int C = 0;  // 最右回文串的中心位置
        int R = 0;  // 以C为中心位置，右扩出的最长回文结束位置
        for (int i = 1, l = 1, r = 1; i < mChars.length; i++) {  // 遍历每个位置做中心找最长回文串
            // i位于以C为中心的回文右半串(C,R]内，有i左对称点i'位于[L,C)内，对于i'的4种回文可能性分析：
            // 1）i'有回文，且回文串整体在[L,R]内，那么i'在[L,R]有完整的回文半径pRadiusArray[i']；
            // 2）i'有回文，但回文左半串部分在L边界外，则i'在[L,R]回文半径被截断为i'-L；
            // 3）i'有回文，i'==L，那么i'在[L,R]回文半径为0；
            // 4）i'无回文，那么i'在[L,R]回文半径为0；
            // 综上若当前位置还在上个回文串里，则i位置回文半径更新为min(i'完整半径，i'截断半径)，否则为0。
            if (i < R) {
                pRadiusArray[i] = Math.min(pRadiusArray[C - (i - C)], R - i); // i位置只需O(1)查找前面i'的信息就得到最小回文半径，减少重复匹配字符，做到R不回退！
            }
            // 以i做中心点向左右两侧扩找回文
            while ((l = i - pRadiusArray[i] - 1) > -1 &&
                    (r = i + pRadiusArray[i] + 1) < mChars.length) {
                if (mChars[l] == mChars[r]) {
                    pRadiusArray[i]++;
                } else {
                    break;
                }
            }
            // i位置的回文结束位置突破了上个回文串范围
            if ((r = i + pRadiusArray[i]) > R) {
                R = r;
                C = i;
            }
            // 回文半径扩到字符串末尾了，结束搜索
            if (R == mChars.length - 1) {
                containEndCharMaxLen = pRadiusArray[i];  // 记录含最后字符的最长回文长度，mChars的回文半径就是string的回文长度！
                break;  // 结束
            }
        }
        // 左边剩余字符逆序返回
        char[] shortestSuffix = new char[string.length() - containEndCharMaxLen];
        for (int i = shortestSuffix.length - 1, j = 1; i >= 0; i--, j+=2) {
            shortestSuffix[i] = mChars[j];
        }
        return String.valueOf(shortestSuffix);
    }
    // 在每个字符间隙插入一个特殊字符，总是返回一个奇数长度的新字符串，如"121" -> "#1#2#1#"
    private static char[] generateManacherChars(String string) {
        char[] chars = string.toCharArray();
        char[] manacherChars = new char[string.length() * 2 + 1];
        for (int i = 0, j = 0; i < manacherChars.length; i++) {
            manacherChars[i] = (i & 1) == 0 ? '#' : chars[j++];
        }
        return manacherChars;
    }



    // 暴力解，时间复杂度O(N^2)
    public static String addShortestSuffix2(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        char[] mChars = generateManacherChars(string);  // 转化为奇数长度的特殊字符串，如"121" -> "#1#2#1#"
        int containEndCharMaxLen = 0;  // 含最后字符的最长回文长度
        for (int i = 0; i < mChars.length; i++) {
            // 以i做中心点向左右两侧扩找回文
            int L = i - 1;
            int R = i + 1;
            while (L > -1 && R < mChars.length && mChars[L] == mChars[R]) {
                L--;
                R++;
            }
            // 回文半径扩到字符串末尾了，结束搜索
            if (R == mChars.length) {
                containEndCharMaxLen = R - i - 1;  // 记录含最后字符的最长回文长度，mChars的回文半径就是string的回文长度！
                break;  // 结束
            }
        }
        // 左边剩余字符逆序返回
        char[] shortestSuffix = new char[string.length() - containEndCharMaxLen];
        for (int i = shortestSuffix.length - 1, j = 1; i >= 0; i--, j+=2) {
            shortestSuffix[i] = mChars[j];
        }
        return String.valueOf(shortestSuffix);
    }



    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxSize = 10;
        char start = 'a';
        char end = 'f';
        for (int i = 0; i < testTimes; i++) {
            String string = String.valueOf(GenerateRandomArray.generateRandomArrayChar(maxSize, start, end));
            String ans1 = addShortestSuffix(string);
            String ans2 = addShortestSuffix2(string);
            if ((ans1 != null && ans2 != null) && !ans1.equals(ans2) || ans1 == null ^ ans2 == null) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%s, ans2=%s  string: %s\n", ans1, ans2, string);
            }
        }
    }
}
