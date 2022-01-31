package C11_Graph;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class C11_XtoYShortestDistance {
    // 给定邻接矩阵matrix，出发节点x，目的节点y，返回x到y节点的最短距离是多少。
    // Dijkstra算法求解
    public static int x2yShortestDistance(int[][] matrix, int x, int y) {
        boolean[] confirmedNodes = new boolean[matrix.length];
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((d1, d2) -> d1[1] - d2[1]);  // 距离短优先
        minHeap.add(new int[] {x, 0});
        while (!minHeap.isEmpty()) {
            int[] shortest = minHeap.poll();  // 在已解锁未确认的路径中挑一个最短距离的（贪心）
            int node = shortest[0];
            int distance = shortest[1];
            if (y == node) {  // 找到目的地，返回最短距离
                return distance;
            }
            if (!confirmedNodes[node]) {  // node节点还未确认，则确认并解锁其所有相连的边
                confirmedNodes[node] = true;  // 确认已得到start到node的最短距离
                for (int to = matrix.length - 1; to >= 0; to--) {  // 遍历所有节点，解锁node节点所有相连的边
                    if (!confirmedNodes[to] && matrix[node][to] != Integer.MAX_VALUE) {  // 与to节点相连且还未确认，则将该点距入堆进行比较
                        minHeap.add(new int[] {to, distance + matrix[node][to]});
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }


    // DFS暴力递归
    public static int x2yShortestDistance2(int[][] matrix, int x, int y) {
        boolean[] pathNodes = new boolean[matrix.length];
        return process(matrix, pathNodes, x, y);
    }
    // 给定邻接矩阵matrix，已走过节点有pathNodes，返回从当前cur节点走到目的地aim节点最少还需要多远。
    private static int process(int[][] matrix, boolean[] pathNodes, int cur, int aim) {
        if (cur == aim) {  // 当前到目的地了，不用再走了
            return 0;
        }
        pathNodes[cur] = true;  // 标记当前走cur节点
        int shortest = Integer.MAX_VALUE;
        for (int next = matrix.length - 1, nextStepDistance; next >= 0; next--) {  // 尝试以每个节点作为下一步
            if (!pathNodes[next] && (nextStepDistance = matrix[cur][next]) != Integer.MAX_VALUE) {  // next节点是连通的且还没走过
                // 看看next能否走到目的地，若可以则比较是不是最短距离的
                int rest = process(matrix, pathNodes, next, aim);
                if (rest != Integer.MAX_VALUE) {
                    shortest = Math.min(shortest, nextStepDistance + rest);
                }
            }
        }
        pathNodes[cur] = false;  // 撤销标记
        return shortest;
    }




    public static int[][] generateRandomPaths(int maxNodeNum, int maxDistance) {
        int nodeNum = Math.random() < 0.001 ? 0 : (int)(Math.random() * maxNodeNum + 1);  // [0, maxNodeNum]
        int maxPathNum = nodeNum * (nodeNum - 1) / 2;
        int pathNum = (int)(Math.random() * (maxPathNum + 1));  // [0, maxPathNum]
        int[][] matrix = new int[nodeNum][nodeNum];  // 邻接矩阵
        // 初始化所有节点不相连
        for (int i = nodeNum - 1; i >= 0; i--) {
            for (int j = nodeNum - 1; j >= 0; j--) {
                matrix[i][j] = Integer.MAX_VALUE;
            }
        }
        // 连接节点
        LinkedList<int[]> paths = new LinkedList<>();
        for (int i = pathNum; i > 0; i--) {
            int from = (int)(Math.random() * nodeNum);
            int to = (int)(Math.random() * nodeNum);
            int distance = (int)(Math.random() * (maxDistance + 1));
            if (from == to) {
                i++;
                continue;
            }
            matrix[from][to] = distance;
            matrix[to][from] = distance;
            paths.add(new int[] {from, to, distance});
        }
        System.out.printf("nodeNum=%d pathNum=%d  [", nodeNum, pathNum);
        paths.forEach(arr -> System.out.printf("%d<->%d#%d, ", arr[0], arr[1], arr[2]));
        System.out.println("]");
        return matrix;
    }

    public static void main(String[] args) {
        int testTimes = 100000;
        int maxNodeNum = 10;
        int maxDistance = 10;
        for (int i = 0; i < testTimes; i++) {
            int[][] matrix = generateRandomPaths(maxNodeNum, maxDistance);
            int maxNode = matrix.length - 1;
            int x = (int)(Math.random() * (maxNode + 1));  // [0, maxNode]
            int y = (int)(Math.random() * (maxNode + 1));  // [0, maxNode]
            int ans1 = x2yShortestDistance(matrix, x, y);
            int ans2 = x2yShortestDistance2(matrix, x, y);
            if (ans1 != ans2) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.printf("ans1=%d, ans2=%d  x:%d y:%d\n", ans1, ans2, x, y);
                break;
            }
        }
    }
}
