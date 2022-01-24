package C10_UnionFindSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class C01_UnionFindSet {
    // 并查集（HashMap实现）
    public static class UnionFindSet<T> {
        private HashMap<T, Integer> sizeMap;  // 每个节点及其所属集合大小的映射
        private HashMap<T, T> parentMap;  // 每个节点及其父亲的映射
        private LinkedList<T> nodesPath;  // 寻父过程记录沿途节点

        public UnionFindSet(List<T> nodes) {
            sizeMap = new HashMap<>();
            parentMap = new HashMap<>();
            for (T node : nodes) {
                sizeMap.put(node, 1);  // 初始化每个节点所属集合大小只有自己一个
                parentMap.put(node, node);  // 初始化每个节点的父亲为自己
            }
            nodesPath = new LinkedList<>();
        }

        private T findParent(T node) {
            T parent;
            while (node != (parent = parentMap.get(node))) {
                nodesPath.push(node);
                node = parent;
            }
            if (!nodesPath.isEmpty()) {
                nodesPath.pop();  // 父节点的直接子节点不重复设置
                while (!nodesPath.isEmpty()) {  // 更新沿途节点的最终父亲
                    parentMap.put(nodesPath.pop(), parent);
                }
            }
            return parent;
        }

        public void union(T n1, T n2) {
            if (!parentMap.containsKey(n1) || !parentMap.containsKey(n2)) {
                return;
            }
            T p1 = findParent(n1);
            T p2 = findParent(n2);
            if (p1 != p2) {
                int p1SetSize = sizeMap.get(p1);
                int p2SetSize = sizeMap.get(p2);
                // 小集合并入大集合
                T bigger = p1SetSize >= p2SetSize ? p1 : p2;
                T lesser = bigger == p1 ? p2 : p1;
                parentMap.put(lesser, bigger);  // 仅更新小集合代表节点的父亲，剩余节点在下次触发findParent()时懒更新
                sizeMap.put(bigger, p1SetSize + p2SetSize);
                sizeMap.remove(lesser);
            }
        }

        public boolean isSameSet(T n1, T n2) {
            if (!parentMap.containsKey(n1) || !parentMap.containsKey(n2)) {
                return false;
            }
            return findParent(n1) == findParent(n2);
        }

        public int size() {
            return sizeMap.size();
        }
    }



    // 并查集（数组实现），构造参数为0~N个数
    public static class UnionFindSet2 {
        private int[] sizeMap;  // 每个节点及其所属集合大小的映射
        private int[] parentMap;  // 每个节点及其父亲的映射
        private int[] nodesPath;  // 寻父过程记录沿途节点
        private int size;  // 有多少集合

        public UnionFindSet2(int N) {
            sizeMap = new int[N];
            parentMap = new int[N];
            for (int i = 0; i < N; i++) {
                sizeMap[i] = 1;  // 初始化每个节点所属集合大小只有自己一个
                parentMap[i] = i;  // 初始化每个节点的父亲为自己
            }
            nodesPath = new int[N];
            size = N;
        }

        private int findParent(int node) {
            int parent;
            int i = -1;
            while (node != (parent = parentMap[node])) {
                nodesPath[++i] = node;
                node = parent;
            }
            if (i >= 0) {
                i--;  // 父节点的直接子节点不重复设置
                while (i >= 0) {  // 更新沿途节点的最终父亲
                    parentMap[nodesPath[i--]] = parent;
                }
            }
            return parent;
        }

        public void union(int n1, int n2) {
            if (n1 >= parentMap.length || n2 >= parentMap.length) {
                return;
            }
            int p1 = findParent(n1);
            int p2 = findParent(n2);
            if (p1 != p2) {
                int p1SetSize = sizeMap[p1];
                int p2SetSize = sizeMap[p2];
                // 小集合并入大集合
                int bigger = p1SetSize >= p2SetSize ? p1 : p2;
                int lesser = bigger == p1 ? p2 : p1;
                parentMap[lesser] = bigger;  // 仅更新小集合代表节点的父亲，剩余节点在下次触发findParent()时懒更新
                sizeMap[bigger] = p1SetSize + p2SetSize;
                size--;
            }
        }

        public boolean isSameSet(int n1, int n2) {
            if (n1 >= parentMap.length || n2 >= parentMap.length) {
                return false;
            }
            return findParent(n1) == findParent(n2);
        }

        public int size() {
            return size;
        }
    }




    private static LinkedList<Integer> generateListNonRepeat(int size) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            list.addLast(i);
        }
        return list;
    }
    public static void main(String[] args) {
        int size = 30;
        LinkedList<Integer> nodes = generateListNonRepeat(size);
        UnionFindSet<Integer> ufs1 = new UnionFindSet<>(nodes);
        UnionFindSet2 ufs2 = new UnionFindSet2(nodes.size());
        int testTimes = size * size * 3;
        for (int i = 0; i < testTimes; i++) {
            int n1 = (int)(Math.random() * (size + 1));
            int n2 = (int)(Math.random() * (size + 1));
            if (Math.random() < 0.5) {
                ufs1.union(n1, n2);
                ufs2.union(n1, n2);
//                System.out.printf("i=%02d  ufs1.union(%d, %d)\n", i, n1, n2);
            } else if (Math.random() < 0.7) {
                boolean ans1 = ufs1.isSameSet(n1, n2);
                boolean ans2 = ufs2.isSameSet(n1, n2);
                if (ans1 != ans2) {
                    System.out.printf("Oops! i=%d  ans1=%b, ans2=%b\n", i, ans1, ans2);
                }
            } else {
                int ans1 = ufs1.size();
                int ans2 = ufs2.size();
                if (ans1 != ans2) {
                    System.out.printf("Oops! i=%d  ans1=%d, ans2=%d\n", i, ans1, ans2);
                }
            }
        }
    }
}