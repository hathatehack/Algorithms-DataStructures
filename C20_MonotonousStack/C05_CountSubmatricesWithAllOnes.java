package C20_MonotonousStack;

import java.util.Stack;

public class C05_CountSubmatricesWithAllOnes {
    // 测试链接：https://leetcode.com/problems/count-submatrices-with-all-ones
    // 给定一个由0和1组成二维数组matrix，返回全部由1组成的子矩形数量。
    // 分析技巧：
    //  以第0~N行分别做地基，累加每行的高度且遇0则清零，求出从每行地基到顶部由'1'堆出的子矩形数量：
    //  对于全1的单列矩阵，[0,i-1]行已算出的矩形仅是[0,i]行大矩形内的子矩形，且第i地基比i-1地基上矩形增加i+1个（先前每个矩形高变大、新增高1矩形）。
    //  地基0: 1 0   地基0上就只有1个矩形；
    //  地基1: 1 1   地基1之前的矩形已结算，所以只需计算新增的矩形即可，有新增高度为2的矩形1个，新增高度为1的矩形3个，
    //               所以总共累加有5个子矩形。
    //  计算高度为1的C列矩形有多少子矩形：(C*(C+1))/2， 计算高度H的C列矩形内高度在(L,H]且L<H的子矩形有多少：(H-L)*((C*(C+1))/2)
    // 时间复杂度O(N^2)
    public static int numSubmat(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int counts = 0;
        int[] height = new int[matrix[0].length];
        // 遍历每行做为地基
        for (int i = 0; i < matrix.length; i++) {
            // 生成以第i行做底的直方图
            for (int j = 0; j < matrix[0].length; j++) {
                height[j] = matrix[i][j] == 0 ? 0 : (height[j] + 1);  // 累加每行的高度，遇0则清零
            }
            // 累加地基扩展一行后的新增子矩形数量
            counts += countNewSubmatFromNewBase(height);
        }
        return counts;
    }

    // 计算地基扩展一行后的新增子矩形数量
    private static int countNewSubmatFromNewBase(int[] height) {
        int counts = 0;
        Stack<Integer> incStack = new Stack<>();
        // 遍历height[]计算每个高度的矩形有多少个
        for (int i = 0; i < height.length; i++) {
            while (!incStack.isEmpty() && height[incStack.peek()] >= height[i]) { // 出现非等高矩形
                // 单调性被破坏，说明出现了更低的矩形，结算前个高度的矩形数量
                int idx = incStack.pop();
                if (height[idx] != height[i]) {  // 如果出现重复值就推迟计算，让后一个重复值算对即可
                    int left = !incStack.isEmpty() ? incStack.peek() : -1;
                    int columns = i - left - 1;  // 当前矩形宽
                    int heightOverlap = Math.max(left != -1 ? height[left] : 0, height[i]); // 与左右相邻矩形重合的最大高度
                    // 当前高矩形只计算未重合高度的矩形数，重合部分由低矩形计算
                    counts += (height[idx] - heightOverlap) * ((columns * (columns + 1)) >> 1);
                }
            }
            incStack.push(i);  // 入栈新元素的下标！！
        }
        // 结算剩余栈，栈内单调意味着剩余元素的右边界为数组末尾
        while (!incStack.isEmpty()) {
            int idx = incStack.pop();
            int left = !incStack.isEmpty() ? incStack.peek() : -1;
            int columns = height.length - left - 1;  // 当前矩形宽
            int heightOverlap = left != -1 ? height[left] : 0; // 与左右相邻矩形重合的最大高度
            // 当前高矩形只计算未重合高度的矩形数，重合部分由低矩形计算
            counts += (height[idx] - heightOverlap) * ((columns * (columns + 1)) >> 1);
        }
        return counts;
    }



    public static void main(String[] args) {
        int[][] matrix = new int[3][3];
        matrix[0] = new int[] {1, 0, 0};
        matrix[1] = new int[] {1, 1, 0};
        matrix[2] = new int[] {1, 1, 1};
        System.out.println(numSubmat(matrix));
//        int testTimes = 1;
//        int ans1 = numSubmat(matrix);
//        int ans2 = numSubmat2(matrix);
//        for (int i = 0; i < testTimes; i++) {
//            if (ans1 != ans2) {
//                System.out.printf("Oops! i=%d\n", i);
//                System.out.printf("ans1=%d, ans2=%d\n", ans1, ans2);
//            }
//        }
    }
}
