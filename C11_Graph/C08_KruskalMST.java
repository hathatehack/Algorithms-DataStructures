package C11_Graph;

import java.util.*;
import C11_Graph.C01_Graph.*;
import C10_UnionFindSet.C01_UnionFindSet.UnionFindSet;

public class C08_KruskalMST {
    // 无向图的最小生成树。选取某些边，使得所有点都连在一起，且所有边的整体权值最小。
    // kruskal算法：遍历所有的边，先选权重小且其节点未连通的边（贪心）。
    public static ArrayList<Edge> kruskalMST(Graph graph) {
        ArrayList<Edge> MST = new ArrayList<>();
        // 并查集，检查指定节点是否已连通
        UnionFindSet<Node> ufs = new UnionFindSet<Node>(graph.nodes.values());
        // 将所有边加入小根堆
        PriorityQueue<Edge> minEdgeHeap = new PriorityQueue<>((e1, e2) -> e1.weight - e2.weight);  // 权重小优先
        for (Edge edge : (HashSet<Edge>)graph.edges) {  // O(N*logN)
            minEdgeHeap.add(edge);
        }
        // 先选权重小且其节点未连通的边
        while (!minEdgeHeap.isEmpty()) {
            Edge edge = minEdgeHeap.poll();
            if (!ufs.isSameSet(edge.from, edge.to)) {  // 若两节点已连通则舍弃该edge，否则会成环！
                MST.add(edge);  // 记录MST
                ufs.union(edge.from, edge.to);
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
        ArrayList<Edge> mst = kruskalMST(graph);
        System.out.println(mst);
    }
}
