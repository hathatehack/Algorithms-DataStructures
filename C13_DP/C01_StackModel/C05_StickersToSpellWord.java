package C13_DP.C01_StackModel;

import C01_random.GenerateRandomStringArray;
import java.util.HashMap;

public class C05_StickersToSpellWord {
    // 测试链接：https://leetcode.com/problems/stickers-to-spell-word
    // 给定一个只含a~z的字符串数组stickers表示有多种贴纸，每种贴纸都有无限张，可以把每张贴纸剪开使用，返回拼出指定字符串word至少需要多少张贴纸。
    // 例如：word="babac"，stickers={"ba", "c", "abcd"}，有“ba,ba,c”3张、”ba,abcd“2张、”abcd,abcd“2张，所以需要最少2张。
    // 暴力递归
    public static int stickersToSpellWord1(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        return process1(stickers, word);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process1(String[] stickers, String word) {
        // base case
        if (word.equals("")) {
            return 0;
        }
        // 尝试使用1张贴纸消除word里的字符，每种贴纸都尝试一次。
        int min = Integer.MAX_VALUE;
        for (String sticker : stickers) {
            String restWord = strip1(sticker, word);
            if (restWord.length() != word.length()) {  // 若某种贴纸能够消除word的部分字符
                int p1 = process1(stickers, restWord);
                if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前1张+后续p1张。
                    p1 = 1 + p1;
                    min = Math.min(min, p1);
                }
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }
    // 用给定贴纸消除word里的字符，返回word剩余字符串。
    private static String strip1(String sticker, String word) {
        if ("".equals(sticker))
            return word;
        char[] stickerChars = sticker.toCharArray();
        char[] wordChars = word.toCharArray();
        int[] bucket = new int[26];
        for (char c : wordChars)
            bucket[c - 'a']++;
        for (char c : stickerChars)
            bucket[c - 'a']--;
        StringBuilder restWord = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            for (int n = bucket[i]; n > 0; n--) {
                restWord.append((char)(i + 'a'));
            }
        }
        return restWord.toString();
    }


    // 暴力递归 + 贪心剪枝
    public static int stickersToSpellWord1_1(String[] stickers, String word) {
        if (stickers == null || word == null) {
            return -1;
        }
        int[][] counts = new int[stickers.length][26];  // 词频表
        for (int i = stickers.length - 1; i >= 0; i--) {
            for (char c : stickers[i].toCharArray()) {
                counts[i][c - 'a']++;
            }
        }
        return process1_1(counts, word);
    }
    // 用给定几种贴纸stickers消除word里的字符，返回使用贴纸的最少张数。
    private static int process1_1(int[][] stickerCounts, String word) {
        // base case
        if (word.equals("")) {
            return 0;
        }
        // 尝试使用1张贴纸消除word里的字符，每种贴纸都尝试一次。
        int min = Integer.MAX_VALUE;
        for (int[] stickerCount : stickerCounts) {
            String restWord = strip1_1(stickerCount, word);
            if (restWord.length() != word.length()) {  // 若某种贴纸能够消除word的部分字符
                int p1 = process1_1(stickerCounts, restWord);
                if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前1张+后续p1张。
                    p1 = 1 + p1;
                    min = Math.min(min, p1);
                }
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }
    // 用给定贴纸消除word里的字符，返回word剩余字符串。（贪心策略）
    private static String strip1_1(int[] stickerCount, String word) {
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
                for (int n = bucket[i] - stickerCount[i]; n > 0; n--) {
                    restWord.append((char)(i + 'a'));
                }
            }
        }
        return restWord.toString();
    }



    // 缓存表记忆化搜索
    public static int stickersToSpellWord2(String[] stickers, String word) {
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
        return process2(counts, word, dp);
    }
    // 返回最少张数
    private static int process2(int[][] stickerCounts, String word, HashMap<String, Integer> dp) {
        if (dp.containsKey(word)) {  // 之前算过此情况，直接返回缓存。
            return dp.get(word);
        }
        // 尝试使用1张贴纸消除word里的字符，每种贴纸都尝试一次。
        int min = Integer.MAX_VALUE;
        for (int[] stickerCount : stickerCounts) {
            String restWord = strip1_1(stickerCount, word);
            if (restWord.length() != word.length()) {  // 若某种贴纸能够消除word的部分字符
                int p1 = process2(stickerCounts, restWord, dp);
                if (p1 != -1) {  // 若剩余字符串也能够被消除，则总贴纸数为当前1张+后续p1张。
                    p1 = 1 + p1;
                    min = Math.min(min, p1);
                }
            }
        }
        min = min == Integer.MAX_VALUE ? -1 : min;
        dp.put(word, min);  // 缓存记录
        return min;
    }


    // 字符串组合太多，不适合做严格表结构的动态规划。





    public static void main(String[] args) {
        int testTimes = 10000;
        int maxWordLen = 15;
        int maxStickerLen = 5;
        int maxStickersSize = 5;
        int maxCharKind = 10;//26;
        for (int i = 0; i <= testTimes; i++) {
            String word = GenerateRandomStringArray.generateRandomString(maxWordLen, maxCharKind);
            String[] stickers = GenerateRandomStringArray.generateRandomStringArray(maxStickersSize, maxStickerLen, maxCharKind);
//            System.out.printf("i=%d  word: %s  stickers: ", i, word); GenerateRandomStringArray.printArray(stickers);
            int ans1 = stickersToSpellWord1(stickers, word);
            int ans1_1 = stickersToSpellWord1_1(stickers, word);
            int ans2 = stickersToSpellWord2(stickers, word);
            if (ans1 != ans1_1 || ans1 != ans2) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("word: %s   stickers: ", word); GenerateRandomStringArray.printArray(stickers);
                System.err.printf("ans1=%d, ans1_1=%d, ans2=%d\n", ans1, ans1_1, ans2);
            }
        }
    }
}
