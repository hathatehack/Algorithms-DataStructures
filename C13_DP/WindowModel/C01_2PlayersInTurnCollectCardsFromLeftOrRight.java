package C13_DP.WindowModel;

import C01_random.GenerateRandomArray;

public class C01_2PlayersInTurnCollectCardsFromLeftOrRight {
    // 给定一个非负数数组，代表一排纸牌，玩家A和玩家B轮流从最左或最右拿一张牌，若玩家A先拿且两个玩家都绝顶聪明，返回最后获胜者的分数。
    // 暴力递归
    public static int win(int[] cards) {
        if (cards == null || cards.length == 0) {
            return 0;
        }
        // 偶数张牌时总能成功，但是奇数张牌时可能不是全局最大得分。
        if ((cards.length & 1) == 0) {
            return first(cards, 0, cards.length - 1);
        } else {
            int A = first(cards, 0, cards.length - 1);
            int B = second(cards, 0, cards.length - 1);
            return Math.max(A, B);
        }
    }
    // 先手轮可以主动选最有利的得分，偶数张牌时总能成功，但是奇数张牌时可能不是全局最大得分，如下：
    // [10, 100, 200, 20]偶数张能正确解； [10, 20, 100]奇数张能正确解； [10, 100, 20]奇数张且由于只能选最左或最右，先手不可能赢！
    private static int first(int[] cards, int L, int R) {
        if (L == R) {
            return cards[L];
        }
        int score1 = cards[L] + second(cards, L + 1, R);  // 尝试取最左牌的最终得分
        int score2 = cards[R] + second(cards, L, R - 1);  // 尝试取最右牌的最终得分
        return Math.max(score1, score2);  // 先手可以主动选走最有利的得分
    }
    // 后手轮只能被动接受不利的得分
    private static int second(int[] cards, int L, int R) {
        if (L == R) {
            return 0;
        }
        int score1 = first(cards, L + 1, R);  // 先手对手尝试取最左牌的最终得分
        int score2 = first(cards, L, R - 1);  // 先手对手尝试取最右牌的最终得分
        return Math.min(score1, score2);  // 后手只能被动接受不利的得分，因为先手对手会选走对其有利的得分。
    }



    // 缓存表记忆化搜索
    public static int win2(int[] cards) {
        if (cards == null || cards.length == 0) {
            return 0;
        }
        int[][] firstMap = new int[cards.length][cards.length];
        int[][] secondMap = new int[cards.length][cards.length];
        for (int i = cards.length - 1; i >= 0; i--) {
            for (int j = cards.length - 1; j >= 0; j--) {
                firstMap[i][j] = -1;
                secondMap[i][j] = -1;
            }
        }
        // 偶数张牌时总能成功，但是奇数张牌时可能不是全局最大得分。
        if ((cards.length & 1) == 0) {
            return first2(cards, 0, cards.length - 1, firstMap, secondMap);
        } else {
            int A = first2(cards, 0, cards.length - 1, firstMap, secondMap);
            int B = second2(cards, 0, cards.length - 1, firstMap, secondMap);
            return Math.max(A, B);
        }
    }
    // 先手轮可以主动选最有利的得分，偶数张牌时总能成功，但是奇数张牌时可能不是全局最大得分。
    private static int first2(int[] cards, int L, int R, int[][] firstMap, int[][] secondMap) {
        if (firstMap[L][R] != -1) {  // 之前算过此情况，直接返回缓存
            return firstMap[L][R];
        }
        int score;
        if (L == R) {
            score = cards[L];
        } else {
            int score1 = cards[L] + second2(cards, L + 1, R, firstMap, secondMap);  // 尝试取最左牌的最终得分
            int score2 = cards[R] + second2(cards, L, R - 1, firstMap, secondMap);  // 尝试取最右牌的最终得分
            score = Math.max(score1, score2);  // 先手可以主动选走最有利的得分
        }
        firstMap[L][R] = score;  // 缓存记录
        return score;
    }
    // 后手轮只能被动接受不利的得分
    private static int second2(int[] cards, int L, int R, int[][] firstMap, int[][] secondMap) {
        if (secondMap[L][R] != -1) {
            return secondMap[L][R];
        }
        int score;
        if (L == R) {
            score = 0;
        } else {
            int score1 = first2(cards, L + 1, R, firstMap, secondMap);  // 先手对手尝试取最左牌的最终得分
            int score2 = first2(cards, L, R - 1, firstMap, secondMap);  // 先手对手尝试取最右牌的最终得分
            score = Math.min(score1, score2);  // 后手只能被动接受不利的得分，因为先手对手会选走对其有利的得分。
        }
        secondMap[L][R] = score;  // 缓存记录
        return score;
    }



    // 动态规划
    public static int win3(int[] cards) {
        if (cards == null || cards.length == 0) {
            return 0;
        }
        int N = cards.length;
        int[][] firstMap = new int[N][N];
        int[][] secondMap = new int[N][N];
        // base case
        for (int L = N - 1; L >= 0; L--) {
            firstMap[L][L] = cards[L];
            secondMap[L][L] = 0;
        }
        // [L..R]选最左或最右，R不会越过L左边（即R>=L），所以只计算右上半三角区，位置依赖：左、下。
        for (int L = N - 2; L >= 0; L--) {  // 从下往上
            for (int R = L + 1; R < N; R++) { // 从左往右填
                firstMap[L][R] = Math.max(  // 先手可以主动选走最有利的得分
                        cards[L] + secondMap[L + 1][R],
                        cards[R] + secondMap[L][R - 1]
                );
                secondMap[L][R] = Math.min(  // 后手只能被动接受不利的得分，因为先手对手会选走对其有利的得分。
                        firstMap[L + 1][R],
                        firstMap[L][R - 1]
                );
            }
        }
        // 偶数张牌时总能成功，但是奇数张牌时可能不是全局最大得分。
        return Math.max(firstMap[0][N - 1], secondMap[0][N - 1]);
    }




    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 10;
        int maxValue = 50;
        for (int i = 0; i < testTimes; i++) {
            int[] cards = GenerateRandomArray.generateRandomArrayPositive(maxSize, maxValue);
//            System.out.printf("i=%d  cards: ", i); GenerateRandomArray.printArray(cards);
            int ans1 = win(cards);
            int ans2 = win2(cards);
            int ans3 = win3(cards);
            if (ans1 != ans2 || ans1 != ans3) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.printf("ans1=%d, ans2=%d, ans2=%d   cards: ", ans1, ans2, ans3);
                GenerateRandomArray.printArray(cards);
            }
        }
    }
}
