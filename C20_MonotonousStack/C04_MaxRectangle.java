package C20_MonotonousStack;

import java.util.Stack;

public class C04_MaxRectangle {
    // 测试链接：https://leetcode.com/problems/maximal-rectangle/
    // 给定一个二维数组matrix，其中的值不是0就是1，返回全部由1组成的最大子矩形内部有多少个1（面积）。
    // 分析技巧：
    //  不同的高度所能形成的矩形宽度取决于以该高度能扩宽多大范围，即求以某高度做最小值的区间范围，高度乘区间即是面积；
    //  以第0~N行分别做地基，累加每行的高度且遇0则清零，求出从每行地基到顶部由'1'堆出的矩形的面积;
    //  最大矩形必定在某行地基上。
    // 时间复杂度O(N^2)
    public static int maximalRectangle(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int max = 0;
        int[] height = new int[matrix[0].length];
        // 遍历每行做为地基
        for (int i = 0; i < matrix.length; i++) {
            // 生成以第i行做底的直方图
            for (int j = 0; j < matrix[0].length; j++) {
                height[j] = matrix[i][j] == '0' ? 0 : (height[j] + 1);  // 累加每行的高度，遇0则清零
            }
            // 第i行做底直方图是否有整体最大矩形
            max = Math.max(maxRect(height), max);
        }
        return max;
    }

    private static int maxRect(int[] height) {
        int max = 0;
        Stack<Integer> incStack = new Stack<>(); // 递增栈找每个高度的最小值区间
        // 遍历每个高度
        for (int i = 0; i < height.length; i++) {
            while (!incStack.isEmpty() && height[incStack.peek()] >= height[i]) { // 清理栈内不平衡元素，保证栈内单调性
                // 单调性被破坏，说明出现了更小值，结算前个最小值及其区间的乘积
                int idx = incStack.pop();
                if (height[idx] != height[i]) { // 如果出现重复值就推迟结算，让后一个重复值算对即可！
                    int area = (!incStack.isEmpty() ? (i - incStack.peek() - 1) : i) * height[idx];
                    max = Math.max(max, area);
                }
            }
            incStack.push(i);  // 入栈新元素的下标！！
        }
        // 结算剩余栈，栈内单调意味着剩余元素的右边界为数组末尾
        while (!incStack.isEmpty()) {
            int idx = incStack.pop();
            int area = (!incStack.isEmpty() ? (height.length - incStack.peek() - 1) : height.length) * height[idx];
            max = Math.max(max, area);
        }
        return max;
    }



    public static void main(String[] args) {
        int testTimes = 1;
        char[][] matrix = new char[3][3];
        matrix[0] = new char[] {'0', '0', '1'};
        matrix[1] = new char[] {'1', '1', '0'};
        matrix[2] = new char[] {'1', '1', '1'};
        System.out.println(maximalRectangle(matrix));
//        int ans1 = maximalRectangle(matrix);
//        int ans2 = maximalRectangle2(matrix);
//        for (int i = 0; i < testTimes; i++) {
//            if (ans1 != ans2) {
//                System.out.printf("Oops! i=%d\n", i);
//            }
//        }
    }
}
