package C11_Graph;

import java.util.*;
import C11_Graph.C01_Graph.*;

public class C09_PrimMST {
    // 无向图的最小生成树。选取某些边，使得所有点都连在一起，且所有边的整体权值最小。
    // prim算法：由节点解锁边，先选权重小的边（贪心）。
    public static ArrayList<Edge> primMST(Graph graph) {
        ArrayList<Edge> MST = new ArrayList<>();
        // 已解锁边的小根堆
        PriorityQueue<Edge> minEdgeHeap = new PriorityQueue<>((e1, e2) -> e1.weight - e2.weight);  // 权重小优先
        // 已处理节点的集合
        HashSet<Node> nodeSet = new HashSet<>();
        // 由节点解锁边，先选权重小的边
        for (Node node : (Collection<Node>)graph.nodes.values()) {  // 挑一个节点
            // 解锁node节点所有相连的边
            if (!nodeSet.contains(node)) {
                nodeSet.add(node);
                for (Edge edge : (Collection<Edge>)node.edges) {
                    minEdgeHeap.add(edge);
                }
            }
            // 先选权重小的边（贪心）
            while (!minEdgeHeap.isEmpty()) {
                Edge edge = minEdgeHeap.poll();
                Node n = edge.to;
                if (!nodeSet.contains(n)) {  // to节点若未处理过，则解锁该节点所有相连的边
                    nodeSet.add(n);
                    MST.add(edge);  // 记录MST
                    for (Edge e : (Collection<Edge>)n.edges) {
                        minEdgeHeap.add(e);
                    }
                }
            }
            // break;  // 如果给定的图包含多个子图，则不应break
        }
        return MST;
    }



    public static ArrayList<int[]> primMST2(int[][] graph) {
        ArrayList<int[]> MST = new ArrayList<>();
        int size = graph.length;
        int[][] weights = new int[size][2];    // weights[to] = [weight, from]
        boolean[] visited = new boolean[size]; // 节点处理标记
        int from = 0;
        for (int i = size - 1; i >= 0; i--) {  // 解锁from节点所有相连的边
            weights[i] = new int[] {graph[from][i], from};
        }
        visited[from] = true;
        for (int n = 1; n < size; n++) {
            int minWeight = Integer.MAX_VALUE;
            int minTo = -1;
            for (int i = size - 1; i >= 0; i--) {
                // 选一条from节点的最小权值边（贪心）
                if (!visited[i] && weights[i][0] < minWeight) {
                    minWeight = weights[i][0];
                    minTo = i;
                }
            }
            if (minTo != -1) {  // 记录边，并解锁minTo节点所有相连的边
                MST.add(new int[]{weights[minTo][0], weights[minTo][1], minTo});  // 记录MST
                visited[minTo] = true;
                from = minTo;  // 解锁minTo节点所有相连的边
                for (int i = size - 1; i >= 0; i--) {
                    if (!visited[i] && graph[from][i] < weights[i][0]) {  // 若新解锁的边有更小权值，则更新相应映射
                        weights[i] = new int[]{graph[from][i], from};
                    }
                }
            } else {  // from节点所属的子图已处理完，尝试找下一个子图
                for (from = size - 1; from >= 0 && visited[from]; from--) {}
                if (from == -1) {
                    break;  // 全部节点都处理过了，结束遍历
                }
                for (int i = size - 1; i >= 0; i--) {  // 解锁from节点所有相连的边
                    if (!visited[i]) {
                        weights[i] = new int[] {graph[from][i], from};
                    }
                }
                visited[from] = true;
            }
        }
        return MST;
    }



    public static void main(String[] args) {
        Graph<Integer> graph = C01_Graph.createGraph(new int[][] {
                new int[] {0, 1, 100},
                new int[] {0, 3, 200},
                new int[] {1, 2, 300},
                new int[] {2, 3, 400},
                new int[] {4, 5, 500},
                // from和to交换，生成无向图
                new int[] {1, 0, 100},
                new int[] {3, 0, 200},
                new int[] {2, 1, 300},
                new int[] {3, 2, 400},
                new int[] {5, 4, 500},
        });
        ArrayList<Edge> mst = primMST(graph);
        System.out.println(mst);

        ArrayList<int[]> mst2 = primMST2(new int[][] {
                new int[] {Integer.MAX_VALUE, 100, Integer.MAX_VALUE, 200, Integer.MAX_VALUE, Integer.MAX_VALUE},
                new int[] {100, Integer.MAX_VALUE, 300, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE},
                new int[] {Integer.MAX_VALUE, 300, Integer.MAX_VALUE, 400, Integer.MAX_VALUE, Integer.MAX_VALUE},
                new int[] {200, Integer.MAX_VALUE, 400, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE},
                new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 500},
                new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 500, Integer.MAX_VALUE}
        });
        ArrayList<String> list = new ArrayList<>();
        mst2.forEach(arr -> list.add(arr[1] + "->" + arr[2] + "#" + arr[0]));
        System.out.println(list);
    }
}
