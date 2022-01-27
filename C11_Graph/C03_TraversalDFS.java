package C11_Graph;

import C11_Graph.C01_Graph.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class C03_TraversalDFS {
    // 图遍历（DFS）：同一层内部节点无顺序要求，所以图遍历不唯一。
    public static ArrayList<Node> dfs(Node node) {
        ArrayList<Node> traversalList = new ArrayList<>();
        if (node == null) {
            return traversalList;
        }
        Stack<Node> stack = new Stack<>();
        HashSet<Node> set = new HashSet<>();  // 图可能有环，用于限制每节点只处理一次
        set.add(node);
        stack.push(node);
        traversalList.add(node);  // 记录遍历节点
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            for (Node n : (List<Node>)cur.toNodes) {  // 挑一个cur的子节点
                if (!set.contains(n)) {  // 图可能有环，防止死循环遍历！
                    set.add(n);
                    stack.push(cur);  // 保存父节点待后续处理其剩余子节点！
                    stack.push(n);  // 待处理的一个子节点
                    traversalList.add(n);  // 记录遍历节点，遍历过程中每个节点作为子节点只有一次，但可能多次作为父节点！
                    break;  // 暂停cur节点，进行toNode节点DFS
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
        List<Node> ans = dfs(graph.nodes.get("a"));
        System.out.println(ans);
    }
}
