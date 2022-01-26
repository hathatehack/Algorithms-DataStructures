package C10_UnionFindSet;

import java.util.LinkedList;
import C01_random.GenerateRandomArray;
import C10_UnionFindSet.C02_ConcurrentUnionFindSet.ConcurrentUnionFindSet;
import C10_UnionFindSet.C01_UnionFindSet.UnionFindSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class C05_NumberOfIslandsParallelization {
    // 岛问题的并行化计算
    // 数据一分为二，创建一个全局并发并查集U1，同时利用U1做各自区域内合并，最后对边界元素做最终合并，结果等于U1.size()。
    public static int numberOfIslandsParallelization(char[][] matrix) throws InterruptedException {
        // 找出处理区域内所有为'1'的节点，添加序号到并查集
        LinkedList<Integer> nodes = new LinkedList<>();
        int row = matrix.length;
        int col = matrix[0].length;
        for (int r = row - 1; r >= 0; r--) {
            for (int c = col - 1; c >= 0; c--) {
                if (matrix[r][c] == '1') {
                    nodes.addLast(r * col + c);
                }
            }
        }
        // 并行计算，两个线程
        ConcurrentUnionFindSet<Integer> ufs = new ConcurrentUnionFindSet<>(nodes);
        Thread[] threads = new Thread[] {
                new ThreadUnionIslands(matrix, 0, row - 1, 0, col / 2 - 1, ufs),
                new ThreadUnionIslands(matrix, 0, row - 1, col / 2, col - 1, ufs)};
        for (Thread t : threads) {
            t.start();
        }
        // 等待处理完毕
        for (Thread t : threads) {
            t.join();
        }
        // 合并边界节点
        int mid = col / 2 - 1;
        for (int r = row - 1; r >= 0; r--) {
            if (matrix[r][mid] == '1' && matrix[r][mid + 1] == '1') {
                ufs.union(r * col + mid, r * col + mid + 1);
            }
        }
        return ufs.size();
    }
    // 合并指定区域内的节点
    private static void unionIslands(char[][] matrix, int startRow, int endRow, int startCol, int endCol, ConcurrentUnionFindSet<Integer> ufs) {
        int col = matrix[0].length;  // !!
        // 单独处理第一行，每个节点看能否与左边节点合并
        for (int c = startCol + 1; c <= endCol; c++) {
            if (matrix[startRow][c - 1] == '1' && matrix[startRow][c] == '1') {
                ufs.union(startRow * col + c - 1, startRow * col + c);
            }
        }
        // 单独处理第一列，每个节点看能否与上边节点合并
        for (int r = startRow + 1; r <= endRow; r++) {
            if (matrix[r - 1][startCol] == '1' && matrix[r][startCol] == '1') {
                ufs.union((r - 1) * col + startCol, r * col + startCol);
            }
        }
        // 处理剩余节点，每个节点同时看能否与上边节点、左边节点合并
        for (int r = startRow + 1; r <= endRow; r++) {
            for (int c = startCol + 1; c <= endCol; c++) {
                if (matrix[r - 1][c] == '1' && matrix[r][c] == '1') {
                    ufs.union((r - 1) * col + c, r * col + c);
                }
                if (matrix[r][c - 1] == '1' && matrix[r][c] == '1') {
                    ufs.union(r * col + c - 1, r * col + c);
                }
            }
        }
    }
    // unionIslands线程
    private static class ThreadUnionIslands extends Thread {
        char[][] matrix;
        int startRow, endRow, startCol, endCol;
        ConcurrentUnionFindSet<Integer> ufs;

        public ThreadUnionIslands(char[][] _matrix, int _startRow, int _endRow, int _startCol, int _endCol, ConcurrentUnionFindSet<Integer> _ufs) {
            matrix = _matrix; startRow = _startRow; endRow = _endRow; startCol = _startCol; endCol = _endCol; ufs = _ufs;
        }

        @Override
        public void run() {
            unionIslands(matrix, startRow, endRow, startCol, endCol, ufs);
        }
    }




    // 数据一分为二，分别创建并查集U1、U2做各自区域内合并，最后收集U1、U2集合创建一个并查集U3对边界元素做最终合并，结果等于U3.size()。
    public static int numberOfIslandsParallelization2(char[][] matrix) throws InterruptedException, ExecutionException {
        int row = matrix.length;
        int col = matrix[0].length;
        // 并行计算，两个线程
        FutureTask<UnionFindSet<Integer>>[] futureTasks = new FutureTask[] {
                new FutureTask<>(new CallableUnionIslands(matrix, 0, row - 1, 0, col / 2 - 1)),
                new FutureTask<>(new CallableUnionIslands(matrix, 0, row - 1, col / 2, col - 1))
        };
        for (FutureTask t : futureTasks) {
            new Thread(t).start();
        }
        // 等待处理完毕
        UnionFindSet<Integer> ufs1 = futureTasks[0].get();
        UnionFindSet<Integer> ufs2 = futureTasks[1].get();
        // 合并边界节点
        LinkedList<Integer> nodes = new LinkedList<>();
        for (Set set : new Set[] {ufs1.set(), ufs2.set()}) {
            for (Object node : set) {
                nodes.add((Integer) node);
            }
        }
        UnionFindSet<Integer> ufs3 = new UnionFindSet<>(nodes);
        int mid = col / 2 - 1;
        for (int r = row - 1; r >= 0; r--) {
            if (matrix[r][mid] == '1' && matrix[r][mid + 1] == '1') {
                Integer n1 = ufs1.findParent(r * col + mid);
                Integer n2 = ufs2.findParent(r * col + mid + 1);
                ufs3.union(n1, n2);
            }
        }
        return ufs3.size();
    }
    // 合并指定区域内的节点
    private static UnionFindSet<Integer> unionIslands2(char[][] matrix, int startRow, int endRow, int startCol, int endCol) {
        // 指定区域内找出所有为'1'的节点，添加序号到并查集
        LinkedList<Integer> nodes = new LinkedList<>();
        int col = matrix[0].length;
        for (int r = endRow; r >= startRow; r--) {
            for (int c = endCol; c >= startCol; c--) {
                if (matrix[r][c] == '1') {
                    nodes.addLast(r * col + c);
                }
            }
        }
        UnionFindSet<Integer> ufs = new UnionFindSet<>(nodes);
        // 单独处理第一行，每个节点看能否与左边节点合并
        for (int c = startCol + 1; c <= endCol; c++) {
            if (matrix[startRow][c - 1] == '1' && matrix[startRow][c] == '1') {
                ufs.union(startRow * col + c - 1, startRow * col + c);
            }
        }
        // 单独处理第一列，每个节点看能否与上边节点合并
        for (int r = startRow + 1; r <= endRow; r++) {
            if (matrix[r - 1][startCol] == '1' && matrix[r][startCol] == '1') {
                ufs.union((r - 1) * col + startCol, r * col + startCol);
            }
        }
        // 处理剩余节点，每个节点同时看能否与上边节点、左边节点合并
        for (int r = startRow + 1; r <= endRow; r++) {
            for (int c = startCol + 1; c <= endCol; c++) {
                if (matrix[r - 1][c] == '1' && matrix[r][c] == '1') {
                    ufs.union((r - 1) * col + c, r * col + c);
                }
                if (matrix[r][c - 1] == '1' && matrix[r][c] == '1') {
                    ufs.union(r * col + c - 1, r * col + c);
                }
            }
        }
        return ufs;
    }
    // unionIslands线程
    private static class CallableUnionIslands implements Callable<UnionFindSet<Integer>> {
        char[][] matrix;
        int startRow, endRow, startCol, endCol;

        public CallableUnionIslands(char[][] _matrix, int _startRow, int _endRow, int _startCol, int _endCol) {
            matrix = _matrix; startRow = _startRow; endRow = _endRow; startCol = _startCol; endCol = _endCol;;
        }

        @Override
        public UnionFindSet<Integer> call() {
            return unionIslands2(matrix, startRow, endRow, startCol, endCol);
        }
    }




    public static void main(String[] args) throws InterruptedException, ExecutionException {
        numberOfIslandsParallelization(new char[][] {new char[] {'1', '1'}, new char[] {'1', '1'}});
        numberOfIslandsParallelization2(new char[][] {new char[] {'1', '1'}, new char[] {'1', '1'}});
        long startTime;
        long endTime;
        int[] rows = new int[] {10, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        int[] cols = new int[] {10, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        for (int i = 0; i < rows.length; i++) {
            int row = rows[i];
            int col = cols[i];
            char[][] matrix = GenerateRandomArray.generateRandomMatrixChar(row, col, row, col, '0', '1');
            int ans;
            System.out.printf("matrix规模：%d*%d\n", row, col);

            startTime = System.currentTimeMillis();
            ans = C04_NumberOfIslands.numberOfIslands3(matrix);
            endTime = System.currentTimeMillis();
            System.out.printf("并查集（1线程计算）的运行结果 ：%- 10d 运行时间： %dms\n", ans, endTime - startTime);

            startTime = System.currentTimeMillis();
            ans = numberOfIslandsParallelization(matrix);
            endTime = System.currentTimeMillis();
            System.out.printf("并查集（2线程计算）1的运行结果：%- 10d 运行时间： %dms\n", ans, endTime - startTime);

            startTime = System.currentTimeMillis();
            ans = numberOfIslandsParallelization2(matrix);
            endTime = System.currentTimeMillis();
            System.out.printf("并查集（2线程计算）2的运行结果：%- 10d 运行时间： %dms\n\n", ans, endTime - startTime);
        }
    }
}
