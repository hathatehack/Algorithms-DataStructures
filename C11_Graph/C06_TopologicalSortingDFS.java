package C11_Graph;

import C11_Graph.C01_Graph.*;
import java.util.ArrayList;
import java.util.HashMap;

public class C06_TopologicalSortingDFS {
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
            createDeepRecord(node, recordMap);
        }
        ArrayList<DirectedGraphNode> sortedList = new ArrayList<>();
        recordMap.values().stream().sorted((r1, r2) -> r2.deep - r1.deep).forEach(r -> sortedList.add(r.node));  // deep大的排在前面
        return sortedList;
    }

    private static class Record {
        public DirectedGraphNode node;
        public int deep;

        public Record(DirectedGraphNode n, int d) {
            node = n;
            deep = d;
        }
    }

    private static Record createDeepRecord(DirectedGraphNode node, HashMap<DirectedGraphNode, Record> recordMap) {
        if (recordMap.containsKey(node)) {
            return recordMap.get(node);
        }
        int deep = 0;
        for (DirectedGraphNode n : node.neighbors) {
            deep += Math.max(deep, createDeepRecord(n, recordMap).deep);  // 找当前节点下路径最大深度
        }
        Record r = new Record(node, deep + 1);  // 当前深度要加1
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
