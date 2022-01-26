package C10_UnionFindSet;

import java.util.LinkedList;
import C01_random.GenerateRandomArray;
import C10_UnionFindSet.C01_UnionFindSet.UnionFindSet;

public class C04_NumberOfIslands {
    // 测试链接：https://leetcode.com/problems/number-of-islands
    // 给定一个二维数组board，里面的值不是1就是0，上、下、左、右相邻的1认为是一片岛，返回board中岛的数量。
    // 递归感染法，时间复杂度O(N)
    public static int numberOfIslands(char[][] board) {
        board = GenerateRandomArray.copyArray(board);
        int islands = 0;
        // 遍历所有节点，尝试感染周围节点
        int row = board.length;
        int col = board[0].length;
        for (int r = row - 1; r >= 0; r--) {
            for (int c = col - 1; c >= 0; c--) {
                if (board[r][c] == '1') {
                    inflect(board, r, c);
                    islands++;
                }
            }
        }
        return islands;
    }
    // 尝试把board[row][col]节点及其上下左右为'1'的节点感染为2，被感染节点继续感染其上下左右，所以每个节点会进入5次。
    private static void inflect(char[][] matrix, int row, int col) {
        if (row < 0 || row >= matrix.length ||
                col < 0 || col >= matrix.length ||
                matrix[row][col] != '1') {
            return;
        }
        // 感染标记为2
        matrix[row][col] = 2;
        // 必须上下左右都看，否则会导致岛被割裂
        inflect(matrix, row - 1, col);
        inflect(matrix, row + 1, col);
        inflect(matrix, row, col - 1);
        inflect(matrix, row, col + 1);
    }




    // 并查集（数组实现），时间复杂度O(N)，常数时间较大
    public static int numberOfIslands2(char[][] board) {
        int row = board.length;
        int col = board[0].length;
        UnionFindSet2 ufs = new UnionFindSet2(board);
        // 单独处理第一行，每个节点看能否与左边节点合并
        for (int c = col - 1; c >= 1; c--) {
            if (board[0][c - 1] == '1' && board[0][c] == '1') {
                ufs.union(0, c - 1, 0, c);
            }
        }
        // 单独处理第一列，每个节点看能否与上边节点合并
        for (int r = row - 1; r >= 1; r--) {
            if (board[r - 1][0] == '1' && board[r][0] == '1') {
                ufs.union(r - 1, 0, r, 0);
            }
        }
        // 处理剩余节点，每个节点同时看能否与上边节点、左边节点合并
        for (int r = row - 1; r >= 1; r--) {
            for (int c = col - 1; c >= 1; c--) {
                if (board[r - 1][c] == '1' && board[r][c] == '1') {
                    ufs.union(r - 1, c, r, c);
                }
                if (board[r][c - 1] == '1' && board[r][c] == '1') {
                    ufs.union(r, c - 1, r, c);
                }
            }
        }
        return ufs.size();
    }

    // 并查集（数组实现），构造参数为二维数组
    public static class UnionFindSet2 {
        private int[] sizeMap;  // 每个节点及其所属集合大小的映射
        private int[] parentMap;  // 每个节点及其父亲的映射
        private int[] nodesPath;  // 寻父过程记录沿途节点
        private int size;  // 有多少集合
        private int column;

        public UnionFindSet2(char[][] M) {
            int row = M.length;
            int col = M[0].length;
            column = col;
            sizeMap = new int[row * col];
            parentMap = new int[row * col];
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    if (M[r][c] == '1') {  // 只添加'1'节点
                        int index = index(r, c);
                        sizeMap[index] = 1;  // 初始化每个节点所属集合大小只有自己一个
                        parentMap[index] = index;  // 初始化每个节点的父亲为自己
                        size++;
                    }
                }
            }
            nodesPath = new int[row * col];
        }

        private int index(int row, int col) {
            return row * column + col;
        }

        private int findParent(int node) {
            int parent;
            int i = -1;
            while (node != (parent = parentMap[node])) {
                nodesPath[++i] = node;
                node = parent;
            }
            if (i >= 0) {
                i--;  // 父节点的直接子节点不重复设置
                while (i >= 0) {  // 更新沿途节点的最终父亲
                    parentMap[nodesPath[i--]] = parent;
                }
            }
            return parent;
        }

        public void union(int r1, int c1, int r2, int c2) {
            int p1 = findParent(index(r1, c1));
            int p2 = findParent(index(r2, c2));
            if (p1 != p2) {
                int p1SetSize = sizeMap[p1];
                int p2SetSize = sizeMap[p2];
                // 小集合并入大集合
                int bigger = p1SetSize >= p2SetSize ? p1 : p2;
                int lesser = bigger == p1 ? p2 : p1;
                parentMap[lesser] = bigger;  // 仅更新小集合代表节点的父亲，剩余节点在下次触发findParent()时懒更新
                sizeMap[bigger] = p1SetSize + p2SetSize;
                size--;
            }
        }

        public int size() {
            return size;
        }
    }




    // 并查集（HashMap实现），时间复杂度O(N)，常数时间很大
    public static int numberOfIslands3(char[][] board) {
        // 找出所有为'1'的节点，添加序号到并查集
        LinkedList<Integer> nodes = new LinkedList<>();
        int row = board.length;
        int col = board[0].length;
        for (int r = row - 1; r >= 0; r--) {
            for (int c = col - 1; c >= 0; c--) {
                if (board[r][c] == '1') {
                    nodes.addLast(r * col + c);
                }
            }
        }
        UnionFindSet<Integer> ufs = new UnionFindSet<>(nodes);
        // 单独处理第一行，每个节点看能否与左边节点合并
        for (int c = col - 1; c >= 1; c--) {
            if (board[0][c - 1] == '1' && board[0][c] == '1') {
                ufs.union(c - 1, c);
            }
        }
        // 单独处理第一列，每个节点看能否与上边节点合并
        for (int r = row - 1; r >= 1; r--) {
            if (board[r - 1][0] == '1' && board[r][0] == '1') {
                ufs.union((r - 1) * col, r * col);
            }
        }
        // 处理剩余节点，每个节点同时看能否与上边节点、左边节点合并
        for (int r = row - 1; r >= 1; r--) {
            for (int c = col - 1; c >= 1; c--) {
                if (board[r - 1][c] == '1' && board[r][c] == '1') {
                    ufs.union((r - 1) * col + c, r * col + c);
                }
                if (board[r][c - 1] == '1' && board[r][c] == '1') {
                    ufs.union(r * col + c - 1, r * col + c);
                }
            }
        }
        return ufs.size();
    }




    public static void main(String[] args) {
        long startTime;
        long endTime;
        int[] rows = new int[] {10, 100, 1000, 5000};
        int[] cols = new int[] {10, 100, 1000, 5000};
        for (int i = 0; i < rows.length; i++) {
            int row = rows[i];
            int col = cols[i];
            char[][] matrix = GenerateRandomArray.generateRandomMatrixChar(row, col, row, col, '0', '1');
            int ans;
            System.out.printf("board规模：%d*%d\n", row, col);

            startTime = System.currentTimeMillis();
            ans = numberOfIslands(matrix);
            endTime = System.currentTimeMillis();
            System.out.printf("递归感染法的运行结果      ：%- 10d 运行时间： %dms\n", ans, endTime - startTime);

            startTime = System.currentTimeMillis();
            ans = numberOfIslands2(matrix);
            endTime = System.currentTimeMillis();
            System.out.printf("并查集(数组实现)的运行结果：%- 10d 运行时间： %dms\n", ans, endTime - startTime);

            startTime = System.currentTimeMillis();
            ans = numberOfIslands3(matrix);
            endTime = System.currentTimeMillis();
            System.out.printf("并查集(Map实现)的运行结果 ：%- 10d 运行时间： %dms\n\n", ans, endTime - startTime);
        }
    }
}
