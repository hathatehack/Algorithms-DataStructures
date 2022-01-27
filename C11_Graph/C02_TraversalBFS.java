package C11_Graph;

import C11_Graph.C01_Graph.*;
import java.util.*;

public class C02_TraversalBFS {
    // 图遍历（BFS）：同一层内部节点无顺序要求，所以图遍历不唯一。
    public static ArrayList<Node> bfs(Node node) {
        ArrayList<Node> traversalList = new ArrayList<>();
        if (node == null) {
            return traversalList;
        }
        Queue<Node> queue = new LinkedList<>();
        HashSet<Node> set = new HashSet<>();  // 图可能有环，用于限制每节点只处理一次
        queue.add(node);
        set.add(node);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            traversalList.add(cur);  // 记录遍历节点，每个节点只会入队一次
            for (Node n : (List<Node>)cur.toNodes) {
                if (!set.contains(n)) {  // 图可能有环，防止死循环遍历！
                    set.add(n);
                    queue.add(n);
                }
            }
        }
        return traversalList;
    }


    public static void main(String[] args) {
        Graph<String> graph = C01_Graph.createGraph(new String[][] {                        //        a
                new String[] {"a", "b"}, new String[] {"a", "c"}, new String[] {"a", "d"},  //    /.  |.  \.
                new String[] {"b", "e"}, new String[] {"b", "c"},                           //   b -. c -. d
                new String[] {"c", "d"}, new String[] {"c", "e"},                           //    \.  |.  /'
                new String[] {"e", "d"}});                                                  //        e
        List<Node> ans = bfs(graph.nodes.get("a"));
        System.out.println(ans);
    }
}
