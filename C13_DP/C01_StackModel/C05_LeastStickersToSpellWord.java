package C13_DP.C01_StackModel;

import C01_random.GenerateRandomStringArray;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class C05_LeastStickersToSpellWord {
    // 测试链接：https://leetcode.com/problems/stickers-to-spell-word
    // 给定一个只含a~z的字符串数组stickers表示有多种贴纸，每种贴纸都有无限张，可以把每张贴纸剪开使用，返回拼出指定字符串word至少需要多少张贴纸。
    // 例如：word="babac"，stickers={"ba", "c", "abcd"}，有“ba,ba,c”3张、”ba,abcd“2张、”abcd,abcd“2张，所以需要最少2张。
    // 有两种试法：
    //   2. 找到一种贴纸能消除掉word的部分字符，使用1张该贴纸消除word，剩余word字符串和全部贴纸进入下一消除流程，周而复始直至消完。
    //   1. 枚举贴纸能使用的次数消除word，剩余word字符消除不再使用该贴纸，剩余word字符串和剩余贴纸进入下一消除流程，周而复始直至消完。
    //   方案1只有1个可变参数，设计递归的原则：减少可变参数，配合词频表优化+贪心剪枝，换取缓存结构的命中率，不变的数据能被缓存更久。
    //   方案2有2个可变参数，尝试的贴纸种类是递减的，后面的剩余word的消除流程只会越来越快。
    // 暴力递归（方案1）
    // 找到一种贴纸能消除掉word的部分字符，使用1张该贴纸消除word，剩余word字符串和全部贴纸进入下一消除流程，周而复始直至消完。
    public static int leastStickersToSpellWord1_1(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        return process1_1(stickers, word);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process1_1(String[] stickers, String word) {
        // base case
        if (word.equals("")) {
            return 0;
        }
        // 尝试每种贴纸使用1张消除word里的字符
        int min = Integer.MAX_VALUE;
        for (String sticker : stickers) {
            String restWord = strip1_1(sticker, word);
            if (restWord.length() != word.length()) {  // 若某种贴纸能够消除word的部分字符
                int p1 = process1_1(stickers, restWord);
                if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前1张+后续p1张。
                    p1 = 1 + p1;
                    min = Math.min(min, p1);  // 取最少贴纸张数
                }
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }
    // 用给定贴纸消除word里的字符，返回word剩余字符串。
    private static String strip1_1(String sticker, String word) {
        if ("".equals(sticker))
            return word;
        char[] stickerChars = sticker.toCharArray(), wordChars = word.toCharArray();
        int[] bucket = new int[26];
        for (char c : wordChars) bucket[c - 'a']++;
        for (char c : stickerChars) bucket[c - 'a']--;
        StringBuilder restWord = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            for (int n = bucket[i]; n > 0; n--)
                restWord.append((char)(i + 'a'));
        }
        return restWord.toString();
    }


    // 暴力递归（方案1） + 词频表优化 + 贪心剪枝
    // 找到一种贴纸能消除掉word的部分字符，使用1张该贴纸消除word，剩余word字符串和全部贴纸进入下一消除流程，周而复始直至消完。
    public static int leastStickersToSpellWord1_2(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        int[][] counts = new int[stickers.length][26];  // 词频表
        for (int i = stickers.length - 1; i >= 0; i--) {
            for (char c : stickers[i].toCharArray()) {
                counts[i][c - 'a']++;
            }
        }
        return process1_2(counts, word);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process1_2(int[][] stickerCounts, String word) {
        // base case
        if (word.equals("")) {
            return 0;
        }
        // 尝试每种贴纸使用1张消除word里的字符
        int min = Integer.MAX_VALUE;
        for (int[] stickerCount : stickerCounts) {
            String restWord = strip1_2(stickerCount, word);
            if (restWord.length() != word.length()) {  // 若某种贴纸能够消除word的部分字符
                int p1 = process1_2(stickerCounts, restWord);
                if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前1张+后续p1张。
                    p1 = 1 + p1;
                    min = Math.min(min, p1);  // 取最少贴纸张数
                }
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }
    // 用给定贴纸消除word里的字符，返回word剩余字符串。（贪心策略）
    private static String strip1_2(int[] stickerCount, String word) {
        char[] wordChars = word.toCharArray();
        // 剪枝。贪心策略，word的第一个字符迟早都需要消除，有可能当前贴纸就不含有word里的字符，或全部贴纸就不存在word[0]字符，
        // 强制先消除word[0]并不会影响全局解，且可以有效减少很多无谓尝试！
        if (stickerCount[wordChars[0] - 'a'] == 0)
            return word;
        // 消掉word存在于当前贴纸里的所有字符
        int[] bucket = new int[26];
        for (char c : wordChars)
            bucket[c - 'a']++;
        StringBuilder restWord = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (bucket[i] > 0) {  // 存在某个字符
                for (int n = bucket[i] - stickerCount[i]; n > 0; n--)
                    restWord.append((char)(i + 'a'));
            }
        }
        return restWord.toString();
    }


    // 缓存表记忆化搜索（方案1）
    // 找到一种贴纸能消除掉word的部分字符，使用1张该贴纸消除word，剩余word字符串和全部贴纸进入下一消除流程，周而复始直至消完。
    public static int leastStickersToSpellWord1_3(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        int[][] counts = new int[stickers.length][26];  // 词频表
        for (int i = stickers.length - 1; i >= 0; i--) {
            for (char c : stickers[i].toCharArray()) {
                counts[i][c - 'a']++;
            }
        }
        HashMap<String, Integer> dp = new HashMap<>();
        dp.put("", 0);
        return process1_3(counts, word, dp);
    }
    // 返回最少张数
    private static int process1_3(int[][] stickerCounts, String word, HashMap<String, Integer> dp) {
        if (dp.containsKey(word)) {  // 之前算过此情况，直接返回缓存。
            return dp.get(word);
        }
        // 尝试每种贴纸使用1张消除word里的字符
        int min = Integer.MAX_VALUE;
        for (int[] stickerCount : stickerCounts) {
            String restWord = strip1_2(stickerCount, word);
            if (restWord.length() != word.length()) {  // 若某种贴纸能够消除word的部分字符
                int p1 = process1_3(stickerCounts, restWord, dp);
                if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前1张+后续p1张。
                    p1 = 1 + p1;
                    min = Math.min(min, p1);  // 取最少贴纸张数
                }
            }
        }
        min = min == Integer.MAX_VALUE ? -1 : min;
        dp.put(word, min);  // 缓存记录
        return min;
    }


    // 字符串组合太多，不适合做严格表结构的动态规划。



    // 暴力递归（方案2），stickers可变。
    // 枚举贴纸能使用的次数消除word，剩余word字符消除不再使用该贴纸，剩余word字符串和剩余贴纸进入下一消除流程，周而复始直至消完。
    public static int leastStickersToSpellWord2_1_1(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        return process2_1_1(new ArrayList<>(Arrays.asList(stickers)), word);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process2_1_1(ArrayList<String> stickers, String word) {
        // base case
        if (word.equals("")) {
            return 0;
        }
        // 尝试分别使用每种贴纸消除word里的字符！
        int min = Integer.MAX_VALUE;
        for (int i = stickers.size() - 1; i >= 0; i--) {
            String sticker = stickers.get(i);
            String lastWord = word;
            String restWord;
            // 若当前种类贴纸能消除word字符，则从1开始枚举能使用的次数消除掉word里的字符。
            for (int curStickerPieces = 1; (restWord = strip1_1(sticker, lastWord)).length() != lastWord.length(); curStickerPieces++, lastWord = restWord) {
                String cur = stickers.remove(i);  // 不再使用当前种类贴纸
                int p1 = process2_1_1(stickers, restWord);
                if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前curStickerPieces张+后续p1张。
                    p1 = curStickerPieces + p1;
                    min = Math.min(min, p1);  // 取最少贴纸张数
                }
                stickers.add(i, cur);  // 恢复使用该贴纸！
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }


    // 缓存表记忆化搜索（方案2），stickers可变。
    // 枚举贴纸能使用的次数消除word，剩余word字符消除不再使用该贴纸，剩余word字符串和剩余贴纸进入下一消除流程，周而复始直至消完。
    public static int leastStickersToSpellWord2_1_2(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        HashMap<String, Integer> dp = new HashMap<>();
        dp.put("", 0);
        return process2_1_2(new ArrayList<>(Arrays.asList(stickers)), word, dp);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process2_1_2(ArrayList<String> stickers, String word, HashMap<String, Integer> dp) {
        if (dp.containsKey(word)) {  // 之前算过此情况，直接返回缓存。
            return dp.get(word);
        }
        // 尝试分别使用每种贴纸消除word里的字符！
        int min = Integer.MAX_VALUE;
        for (int i = stickers.size() - 1; i >= 0; i--) {
            String sticker = stickers.get(i);
            String lastWord = word;
            String restWord;
            // 若当前种类贴纸能消除word字符，则从1开始枚举能使用的次数消除掉word里的字符。
            for (int curStickerPieces = 1; (restWord = strip1_1(sticker, lastWord)).length() != lastWord.length(); curStickerPieces++, lastWord = restWord) {
                String cur = stickers.remove(i);  // 不再使用当前种类贴纸
                int p1 = process2_1_2(stickers, restWord, dp);
                if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前curStickerPieces张+后续p1张。
                    p1 = curStickerPieces + p1;
                    min = Math.min(min, p1);  // 取最少贴纸张数
                }
                stickers.add(i, cur);  // 恢复使用该贴纸！
            }
        }
        min = min == Integer.MAX_VALUE ? -1 : min;
        dp.put(word, min);  // 缓存记录
        return min;
    }


    // 暴力递归（方案2），stickers可变。
    // 枚举贴纸能使用的次数消除word，剩余word字符消除不再使用该贴纸，剩余word字符串和剩余贴纸进入下一消除流程，周而复始直至消完。
    public static int leastStickersToSpellWord2_2_1(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        return process2_2_1(new ArrayList<>(Arrays.asList(stickers)), word);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process2_2_1(ArrayList<String> stickers, String word) {
        // base case
        if (word.equals("")) {
            return 0;
        }
        if (stickers.size() == 0) {
            return -1;
        }
        // 枚举当前种类贴纸能使用的次数消除掉word里的字符（从0张贴纸开始枚举）
        int min = Integer.MAX_VALUE;
        int i = 0;
        String sticker = stickers.get(i);
        String lastWord = "";
        String restWord = word;
        for (int curStickerPieces = 0; restWord.length() != lastWord.length(); curStickerPieces++, lastWord = restWord, restWord = strip1_1(sticker, restWord)) {
            String cur = stickers.remove(i);  // 不再使用当前种类贴纸
            int p1 = process2_2_1(stickers, restWord);
            if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前curStickerPieces张+后续p1张。
                p1 = curStickerPieces + p1;
                min = Math.min(min, p1);  // 取最少贴纸张数
            }
            stickers.add(i, cur);  // 恢复使用该贴纸！
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }
    // 缓存表记忆化搜索（方案2），stickers可变，leastStickersToSpellWord2_2_1()的stickers和word组合做缓存，对stickers的处理太耗性能！
    // 可改成如下方式，stickers数组固定，startIndex整形可变。


    // 暴力递归（方案2），stickers固定，startIndex可变。
    // 枚举贴纸能使用的次数消除word，剩余word字符消除不再使用该贴纸，剩余word字符串和剩余贴纸进入下一消除流程，周而复始直至消完。
    public static int leastStickersToSpellWord2_3_1(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        return process2_3_1(stickers, 0, word);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process2_3_1(String[] stickers, int startIndex, String word) {
        // base case
        if (word.equals("")) {
            return 0;
        }
        if (startIndex == stickers.length) {
            return -1;
        }
        // 枚举当前种类贴纸能使用的次数消除掉word里的字符（从0张贴纸开始枚举）
        int min = Integer.MAX_VALUE;
        String sticker = stickers[startIndex];
        String lastWord = "";
        String restWord = word;
        for (int curStickerPieces = 0; restWord.length() != lastWord.length(); curStickerPieces++, lastWord = restWord, restWord = strip1_1(sticker, restWord)) {
            int p1 = process2_3_1(stickers, startIndex + 1, restWord);
            if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前curStickerPieces张+后续p1张。
                p1 = curStickerPieces + p1;
                min = Math.min(min, p1);  // 取最少贴纸张数
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }


    // 缓存表记忆化搜索（方案2），stickers固定，startIndex可变。
    // 枚举贴纸能使用的次数消除word，剩余word字符消除不再使用该贴纸，剩余word字符串和剩余贴纸进入下一消除流程，周而复始直至消完。
    public static int leastStickersToSpellWord2_3_2(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        HashMap<String, Integer> dp = new HashMap<>();
        return process2_3_2(stickers, 0, word, dp);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process2_3_2(String[] stickers, int startIndex, String word, HashMap<String, Integer> dp) {
        String status = String.format("%d,%s", startIndex, word);
        if (dp.containsKey(status)) {  // 之前算过此情况，直接返回缓存。
            return dp.get(status);
        }
        // base case
        if (word.equals("")) {
            return 0;
        }
        if (startIndex == stickers.length) {
            return -1;
        }
        // 枚举当前种类贴纸能使用的次数消除掉word里的字符（从0张贴纸开始枚举）
        int min = Integer.MAX_VALUE;
        String sticker = stickers[startIndex];
        String lastWord = "";
        String restWord = word;
        for (int curStickerPieces = 0; restWord.length() != lastWord.length(); curStickerPieces++, lastWord = restWord, restWord = strip1_1(sticker, restWord)) {
            int p1 = process2_3_2(stickers, startIndex + 1, restWord, dp);
            if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前curStickerPieces张+后续p1张。
                p1 = curStickerPieces + p1;
                min = Math.min(min, p1);  // 取最少贴纸张数
            }
        }
        min = min == Integer.MAX_VALUE ? -1 : min;
        dp.put(status, min);  // 缓存记录
        return min;
    }





    public static void main(String[] args) throws ClassNotFoundException {
        checkResult();
        compareTimeConsume();
    }
    // 验证结果
    private static void checkResult() {
        System.out.println("验证结果...");
        int testTimes = 10000;
        int maxWordLen = 10;//20;
        int maxStickerLen = 5;
        int maxStickersSize = 5;
        int maxCharKind = 10;//26;
        for (int i = 0; i <= testTimes; i++) {
            String word = GenerateRandomStringArray.generateRandomString(maxWordLen, maxCharKind);
            String[] stickers = GenerateRandomStringArray.generateRandomStringArray(maxStickersSize, maxStickerLen, maxCharKind);
//            System.out.printf("i=%d  word: %s  stickers: ", i, word); GenerateRandomStringArray.printArray(stickers);
            int ans1_1 = leastStickersToSpellWord1_1(stickers, word);
            int ans1_2 = leastStickersToSpellWord1_2(stickers, word);
            int ans1_3 = leastStickersToSpellWord1_3(stickers, word);
            int ans2_1_1 = leastStickersToSpellWord2_1_1(stickers, word);
            int ans2_1_2 = leastStickersToSpellWord2_1_2(stickers, word);
            int ans2_2_1 = leastStickersToSpellWord2_2_1(stickers, word);
            int ans2_3_1 = leastStickersToSpellWord2_3_1(stickers, word);
            int ans2_3_2 = leastStickersToSpellWord2_3_2(stickers, word);
            if (ans1_1 != ans1_2 || ans1_1 != ans1_3 || ans1_1 != ans2_1_1 || ans1_1 != ans2_1_2 || ans1_1 != ans2_2_1 || ans1_1 != ans2_3_1 || ans1_1 != ans2_3_2) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("word: %s   stickers: ", word); GenerateRandomStringArray.printArray(stickers);
                System.err.printf("ans1_1=%d, ans1_2=%d, ans1_3=%d, ans2_1_1=%d, ans2_1_2=%d, ans2_2_1=%d, ans2_3_1=%d, ans2_3_2=%d\n", ans1_1, ans1_2, ans1_3, ans2_1_1, ans2_1_2, ans2_2_1, ans2_3_1, ans2_3_2);
            }
        }
    }
    // 耗时对比
    private static void compareTimeConsume() throws ClassNotFoundException {
        System.out.print("\n耗时对比：");
        aa(10, 20, 5, 5, 3, new String[] {});
        aa(10, 40, 5, 5, 3, new String[] {"leastStickersToSpellWord1_1"});
    }
    private static void aa(int testTimes, int maxWordLen, int maxStickerLen, int maxStickersSize, int maxCharKind, String[] excludeMethods) throws ClassNotFoundException {
        System.out.printf("\nexcludeMethods: %s\n", Arrays.toString(excludeMethods));
        Object[][] testCases = new Object[testTimes][];
        for (int i = testTimes - 1; i >= 0; i--) {
            testCases[i] = new Object[] {
                    GenerateRandomStringArray.generateRandomStringArray(maxStickersSize, maxStickerLen, maxCharKind),
                    GenerateRandomStringArray.generateRandomString(maxWordLen, maxCharKind)
            };
        }
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : Class.forName(Thread.currentThread().getStackTrace()[1].getClassName()).getMethods()) {
            if (method.getName().startsWith("leastStickersToSpellWord")) {
                int i = excludeMethods.length - 1;
                for (; i >= 0 && !excludeMethods[i].equals(method.getName()); i--) {}
                if (i < 0)
                    methods.add(method);
            }
        }
        methods.sort((m1, m2) -> m1.getName().compareTo(m2.getName()));
        for (Method method : methods) {
            long time = 0;
            ArrayList<Integer> ansList = new ArrayList<>();
            for (Object[] testCase : testCases) {
                long startTime = System.currentTimeMillis();
                try {
                    ansList.add((int)method.invoke(null, testCase[0], testCase[1]));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                time += endTime - startTime;
            }
            System.out.printf("  %s%s   ansList=%s   testTimes: %d   总耗时：%dms\n", method.getName(), method.getName().length() == "leastStickersToSpellWord?_?_?".length() ? "" : "  ", ansList, testTimes, time);
        }
    }
}
