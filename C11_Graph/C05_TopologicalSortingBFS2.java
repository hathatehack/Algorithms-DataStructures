package C11_Graph;

import C11_Graph.C01_Graph.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class C05_TopologicalSortingBFS2 {
    public static class DirectedGraphNode {
        public String label;
        public ArrayList<DirectedGraphNode> neighbors;

        public DirectedGraphNode(String val) {
            label = val;
            neighbors = new ArrayList<>();
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // 测试链接：https://www.lintcode.com/problem/topological-sorting
    // 给定一DAG的全部节点，返回该DAG的任一拓扑序（BFS）。
    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> nodes) {
        HashMap<DirectedGraphNode, Integer> inMap = new HashMap<>();
        for (DirectedGraphNode node : nodes) {  // 初始化所有节点入度
            inMap.put(node, 0);
        }
        for (DirectedGraphNode node : nodes) {
            for (DirectedGraphNode toNode : node.neighbors) {  // 子节点的入度都加1
                inMap.put(toNode, inMap.get(toNode) + 1);
            }
        }
        Queue<DirectedGraphNode> zeroInQueue = new LinkedList<>();
        for (DirectedGraphNode node : inMap.keySet()) {
            if (inMap.get(node) == 0) {  // 把入度为0的子节点入队zeroInQueue
                zeroInQueue.add(node);
            }
        }
        ArrayList<DirectedGraphNode> sortedList = new ArrayList<>();
        while (!zeroInQueue.isEmpty()) {
            DirectedGraphNode node = zeroInQueue.poll();
            sortedList.add(node);  // 记录遍历节点
            for (DirectedGraphNode toNode : node.neighbors) {
                inMap.put(toNode, inMap.get(toNode) - 1);  // 子节点入度减1
                if (inMap.get(toNode) == 0) {  // 把入度为0的子节点入队zeroInQueue
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
        HashMap<Node, DirectedGraphNode> map = new HashMap<>();
        graph.nodes.values().forEach(node -> map.put(node, new DirectedGraphNode(node.value)));
        graph.nodes.values().forEach(node -> { DirectedGraphNode dgn = map.get(node); node.toNodes.forEach(n -> dgn.neighbors.add(map.get(n))); });
        ArrayList<DirectedGraphNode> ans = topSort(new ArrayList<>(map.values()));
        System.out.println(ans);
    }
}
