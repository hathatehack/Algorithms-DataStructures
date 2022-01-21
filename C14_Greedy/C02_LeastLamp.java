package C14_Greedy;

import java.util.HashSet;

public class C02_LeastLamp {
    // 给定一个字符串str，只由‘X’和‘.’两种字符构成。‘X’表示墙，不能放灯，也不需要点亮；‘.’表示居民点，可以放灯，需要点亮。
    // 如果灯放在i位置，可以让i-1，i和i+1三个位置被点亮，返回如果点亮str中所有需要点亮的位置，至少需要几盏灯？
    // 贪心解
    public static int leastLamp(String string) {
        char[] chars = string.toCharArray();
        int lamps = 0;
        int index = 0;
        while (index < chars.length) {
            if (chars[index] == 'X') {
                index++;
            } else {
                // 三个连续的'.'只用一个灯就够
                lamps++;
                if (index + 1 == chars.length) {
                    break;
                } else {
                    if (chars[index + 1] == 'X') {
                        index += 2;
                    } else {
                        index += 3;
                    }
                }
            }
        }
        return lamps;
    }



    // 暴力解
    public static int leastLamp2(String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        return process(string.toCharArray(), 0, new HashSet<Integer>());
    }
    private static int process(char[] chars, int index, HashSet<Integer> lampsSet) {
        // 遍历完chars，做结算
        if (index == chars.length) {
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '.') {
                    if (!lampsSet.contains(i) && !lampsSet.contains(i - 1) && !lampsSet.contains(i + 1))
                        return Integer.MAX_VALUE;
                }
            }
            return lampsSet.size();
        }
        // 尝试index位置不放灯、放灯
        int noLamp = process(chars, index + 1, lampsSet);
        int putLamp = Integer.MAX_VALUE;
        if (chars[index] == '.') {
            lampsSet.add(index);
            putLamp = process(chars, index + 1, lampsSet);
            lampsSet.remove(index);
        }
        return Math.min(noLamp, putLamp);
    }



    private static String generateRandomString(int maxLen) {
        char[] chars = new char[(int)(Math.random() * (maxLen + 1))];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = Math.random() < 0.5 ? 'X' : '.';
        }
        return String.valueOf(chars);
    }
    public static void main(String[] args) {
        int testTimes = 1000000;
        int maxLen = 10;
        for (int i = 0; i < testTimes; i++) {
            String string = generateRandomString(maxLen);
            int ans1 = leastLamp(string);
            int ans2 = leastLamp2(string);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d  string:%s\n", ans1, ans2, string);
            }
        }
    }
}
