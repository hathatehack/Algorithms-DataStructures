package C11_Graph;

import java.util.*;
import C11_Graph.C01_Graph.*;

public class C04_TopologicalSortingBFS {
    // 对于DAG：a -> b、c -> b，拓扑序有acb、cab。
    // 拓扑序：
    //   对于BFS方式，先排入度为0的节点，同一层内部节点无顺序要求，所以拓扑序不唯一；
    //   对于DFS方式，先排节点路径深度大的或者先排子节点数多的，同一层内部节点无顺序要求，所以拓扑序不唯一；
    // 给定一DAG，返回该DAG的任一拓扑序（BFS）。
    public static List<Node> topologicalSorting(Graph graph) {
        HashMap<Node, Integer> imMap = new HashMap<>();  // 记录每个节点的入度大小
        Queue<Node> zeroInQueue = new LinkedList<>();  // 剩余入度为0的节点
        for (Node node : (Collection<Node>)graph.nodes.values()) { // 记录所有节点的入度，并找出入度为0的开始节点入队zeroInQueue
            imMap.put(node, node.in);
            if (node.in == 0) {
                zeroInQueue.add(node);
            }
        }
        List<Node> sortedList = new LinkedList<>();
        while (!zeroInQueue.isEmpty()) {
            Node cur = zeroInQueue.poll();
            sortedList.add(cur);  // 记录遍历节点
            for (Node toNode : (List<Node>)cur.toNodes) {
                imMap.put(toNode, imMap.get(toNode) - 1);  // 子节点入度减1
                if (imMap.get(toNode) == 0) {  // 把入度为0的子节点入队zeroInQueue
                    zeroInQueue.add(toNode);
                }
            }
        }
        return sortedList;
    }


    public static void main(String[] args) {
        Graph<String> graph = C01_Graph.createGraph(new String[][] {                        //        a
                new String[] {"a", "b"}, new String[] {"a", "c"}, new String[] {"a", "d"},  //    /.  |.  \.
                new String[] {"b", "e"}, new String[] {"b", "c"},                           //   b -. c -. d
                new String[] {"c", "d"}, new String[] {"c", "e"},                           //    \.  |.  /'
                new String[] {"e", "d"}});                                                  //        e
        List<Node> ans = topologicalSorting(graph);
        System.out.println(ans.toString());
    }
}
