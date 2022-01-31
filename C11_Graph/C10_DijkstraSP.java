package C11_Graph;

import java.util.*;
import C11_Graph.C01_Graph.*;
import C05_Heap.HeapEnhance;

public class C10_DijkstraSP {
    // 无负边的图，指定一个出发点，求该出发点到每个节点的最短距离（权重和最小）。
    // Dijkstra算法：在已解锁未确认的路径中挑最短距离的（贪心），确认该节点并解锁其所有相连的边。
    public static HashMap<Node, Integer> dijkstra(Node start) {
        HashMap<Node, Integer> distanceMap = new HashMap<>();  // 点距表
        distanceMap.put(start, 0);
        HashSet<Node> confirmedNodes = new HashSet<>();  // 确认集
        Node node;
        while ((node = getShortestDistanceAndUnselectedNode(distanceMap, confirmedNodes)) != null) {  // 在已解锁未确认的路径中挑一个最短距离的（贪心）
            confirmedNodes.add(node);  // 确认已得到start到node的最短距离
            int distance = distanceMap.get(node);
            for (Edge edge : (Collection<Edge>)node.edges) {  // 解锁node节点所有相连的边
                Node to = edge.to;
                if (!confirmedNodes.contains(to)) {  // to节点还未确认
                    if (!distanceMap.containsKey(to)) {  // to节点是第一次到达的，直接更新点距
                        distanceMap.put(to, distance + edge.weight);
                    } else { // to节点非第一次到达，需比较取最短距离！
                        distanceMap.put(to, Math.min(distanceMap.get(to), distance + edge.weight));
                    }
                }
            }
        }
        return distanceMap;
    }
    // 返回一个已解锁还未确认的最短距离路径（贪心）
    private static Node getShortestDistanceAndUnselectedNode(HashMap<Node, Integer> distanceMap, HashSet<Node> confirmedNodes) {
        Node shortestNode = null;
        int shortestDistance = Integer.MAX_VALUE;
        for (Map.Entry<Node, Integer> entry : distanceMap.entrySet()) {  // 挑一个最短点距需要遍历比较所有已解锁路径，其中的已确认路径是不必要的，导致增加耗时！
            Node to = entry.getKey();
            int distance = distanceMap.get(to);
            if (!confirmedNodes.contains(to) && distance < shortestDistance) {
                shortestNode = to;
                shortestDistance = distance;
            }
        }
        return shortestNode;
    }



    // 确认集+系统堆优化
    public static HashMap<Node, Integer> dijkstra2(Node start) {
        HashMap<Node, Integer> distanceMap = new HashMap<>(); // 点距表
        HashSet<Node> confirmedNodes = new HashSet<>();  // 确认集
        PriorityQueue<Object[]> minHeap = new PriorityQueue<>((d1, d2) -> (int)d1[1] - (int)d2[1]);  // 距离短优先
        minHeap.add(new Object[] {start, 0});
        while (!minHeap.isEmpty()) {
            Object[] shortest = minHeap.poll();  // 在已解锁未确认的路径中挑一个最短距离的（贪心）
            Node node = (Node)shortest[0];
            int distance = (int)shortest[1];
            if (!confirmedNodes.contains(node)) {  // node节点还未确认，则确认并解锁其所有相连的边
                confirmedNodes.add(node);  // 确认已得到start到node的最短距离
                distanceMap.put(node, distance);
                for (Edge edge : (Collection<Edge>)node.edges) {  // 解锁node节点所有相连的边
                    Node to = edge.to;
                    if (!confirmedNodes.contains(to)) {  // to节点还未确认，则将该点距入堆进行比较
                        minHeap.add(new Object[] {to, distance + edge.weight});
                    }
                }
            }
        }
        return distanceMap;
    }


    
    // 加强堆优化，挑最短点距只遍历已解锁未确认的路径，不需遍历所有已解锁路径。
    public static HashMap<Node, Integer> dijkstra3(Node start, int nodeNum) {
        HashMap<Node, Integer> distanceMap = new HashMap<>();
        NodeDistanceHeap minHeap = new NodeDistanceHeap(nodeNum);  // 距离短优先
        minHeap.addOrUpdateOrIgnore(start, 0);
        while (!minHeap.isEmpty()) {
            Object[] shortest = minHeap.pop();  // 在已解锁未确认的路径中挑一个最短距离的（贪心）
            Node node = (Node)shortest[0];
            int distance = (int)shortest[1];
            distanceMap.put(node, distance);
            for (Edge edge : (Collection<Edge>)node.edges) {  // 解锁node节点所有相连的边
                minHeap.addOrUpdateOrIgnore(edge.to, distance + edge.weight);  // 尝试更新start到to节点的最短距离
            }
        }
        return distanceMap;
    }
    // 加强堆（小根），支持更新、确认点距。
    private static class NodeDistanceHeap {
        private int heapSize;  // 已添加多少堆元素
        private Node[] nodes;  // 存放堆元素的结构
        private HashMap<Node, Integer> indexMap;  // 元素在堆中的位置
        private HashMap<Node, Integer> distanceMap;
        private Comparator<Integer> distanceComp;

        public NodeDistanceHeap(int capacity) {
            heapSize = 0;
            nodes = new Node[capacity];
            indexMap = new HashMap<>();
            distanceMap = new HashMap<>();
            distanceComp = (i1, i2) -> distanceMap.get(nodes[i1]) - distanceMap.get(nodes[i2]);  // 距离短优先
        }

        public boolean isEmpty() {
            return heapSize == 0;
        }

        public boolean isAdded(Node node) {
            return indexMap.containsKey(node);
        }

        public boolean isInHeap(Node node) {
            return isAdded(node) && indexMap.get(node) != -1;
        }
        // 给定到某节点的距离，若该点不存在则添加，若已被确认则忽略，若是更小距离则更新
        public void addOrUpdateOrIgnore(Node toNode, int distance) {
            if (!isAdded(toNode)) {  // 从未添加，直接更新点距
                distanceMap.put(toNode, distance);
                nodes[heapSize] = toNode;
                indexMap.put(toNode, heapSize);
                insertHeapify(heapSize);
                heapSize++;
            } else if (isInHeap(toNode)) {  // 已添加但未确认，需比较取最短距离！
                distanceMap.put(toNode, Math.min(distanceMap.get(toNode), distance));
                insertHeapify(indexMap.get(toNode));
            } else {  // 已确认
                // ignore
            }
        }
        // 在已解锁未确认的路径中挑一个最短距离的（贪心）
        public Object[] pop() {
            Object[] root = new Object[] {nodes[0], distanceMap.get(nodes[0])};
            swap(0, --heapSize);
            indexMap.put(nodes[heapSize], -1);  // -1标记到该节点最短距离已被确认！保留不删除记录能保证到该节点若有多条路径时不会重复确认！
            distanceMap.remove(nodes[heapSize]);
            nodes[heapSize] = null;  // 解除引用，及时GC
            heapify(0);
            return root;
        }

        private void insertHeapify(int index) {
            while (distanceComp.compare(index, (index - 1) / 2) < 0) {
                swap(index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

        private void heapify(int index) {
            int left = index * 2 + 1;  // 左孩子
            while (left < heapSize) {  // 先判断是否存在左孩子，再判断是否存在右孩子！再在头左右节点选出最小节点
                int least = left + 1 < heapSize && distanceComp.compare(left, left + 1) < 0 ? left + 1 : left;
                least = distanceComp.compare(least, index) < 0 ? least : index;
                if (least == index) {
                    break;
                }
                swap(least, index);
                index = least;
                left = index * 2 + 1;
            }

        }

        private void swap(int i1, int i2) {
            Node o1 = nodes[i1];
            Node o2 = nodes[i2];
            // 交换两个堆元素
            nodes[i1] = o2;
            nodes[i2] = o1;
            // 更新位置索引
            indexMap.put(o2, i1);
            indexMap.put(o1, i2);
        }
    }


    
    public static void main(String[] args) {
        Graph<Integer> graph = C01_Graph.createGraph(new int[][]{
                new int[]{0, 1, 100},  //         0
                new int[]{0, 2, 600},  //     /.  |.  \.
                new int[]{0, 3, 200},  //    1 -. 2 -. 3
                new int[]{1, 2, 200},  //     \.  |.  /.
                new int[]{1, 4, 600},  //         4
                new int[]{2, 3, 500},
                new int[]{2, 4, 100},
                new int[]{3, 4, 1000}
        });
        Node start = graph.nodes.get(0);
        HashMap<Node, Integer> distanceMap1 = dijkstra(start);
        ArrayList<String> ans1 = new ArrayList<>();
        distanceMap1.entrySet().forEach(entry -> ans1.add(start + "->" + entry.getKey() + "#" + entry.getValue()));
        System.out.println(ans1);

        HashMap<Node, Integer> distanceMap2 = dijkstra2(start);
        ArrayList<String> ans2 = new ArrayList<>();
        distanceMap2.entrySet().forEach(entry -> ans2.add(start + "->" + entry.getKey() + "#" + entry.getValue()));
        System.out.println(ans2);

        HashMap<Node, Integer> distanceMap3 = dijkstra3(start, graph.nodes.size());
        ArrayList<String> ans3 = new ArrayList<>();
        distanceMap3.entrySet().forEach(entry -> ans3.add(start + "->" + entry.getKey() + "#" + entry.getValue()));
        System.out.println(ans3);
    }
}
