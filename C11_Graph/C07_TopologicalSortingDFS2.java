package C11_Graph;

import C11_Graph.C01_Graph.*;
import java.util.ArrayList;
import java.util.HashMap;

public class C07_TopologicalSortingDFS2 {
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
    // 给定一DAG的全部节点，返回该DAG的任一拓扑序（DFS）。
    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> nodes) {
        HashMap<DirectedGraphNode, Record> recordMap = new HashMap<>();
        for (DirectedGraphNode node : nodes) {
            createNodesRecord(node, recordMap);
        }
        ArrayList<DirectedGraphNode> sortedList = new ArrayList<>();
        recordMap.values().stream().sorted((r1, r2) -> r2.nodes - r1.nodes).forEach(r -> sortedList.add(r.node));  // 节点数多的排在前面
        return sortedList;
    }

    private static class Record {
        public DirectedGraphNode node;
        public int nodes;

        public Record(DirectedGraphNode n, int num) {
            node = n;
            nodes = num;
        }
    }

    private static Record createNodesRecord(DirectedGraphNode node, HashMap<DirectedGraphNode, Record> recordMap) {
        if (recordMap.containsKey(node)) {
            return recordMap.get(node);
        }
        int nodes = 0;
        for (DirectedGraphNode n : node.neighbors) {
            nodes += createNodesRecord(n, recordMap).nodes;  // 累加当前节点下全部路径节点数
        }
        Record r = new Record(node, nodes + 1);  // 当前节点数要加1
        recordMap.put(node, r);
        return r;
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
