package C10_UnionFindSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class C02_ConcurrentUnionFindSet {
    // 并查集（HashMap实现）
    public static class ConcurrentUnionFindSet<T> {
        private ConcurrentHashMap<T, Integer> sizeMap;  // 每个节点及其所属集合大小的映射；由于多线程执行remove操作，使用ConcurrentHashMap防止计数出错！
        private HashMap<T, T> parentMap;  // 每个节点及其父亲的映射
        private ThreadLocal<LinkedList<T>> nodesPathThreadLocal;  // 寻父过程记录沿途节点；由于多线程更新节点父亲，使用ThreadLocal防止互相干扰！

        // 构造函数，一次性添加完节点
        public ConcurrentUnionFindSet(List<T> nodes) {
            sizeMap = new ConcurrentHashMap<>();
            parentMap = new HashMap<>();
            for (T node : nodes) {
                sizeMap.put(node, 1);  // 初始化每个节点所属集合大小只有自己一个
                parentMap.put(node, node);  // 初始化每个节点的父亲为自己
            }
            nodesPathThreadLocal = new ThreadLocal<>();
        }

        public T findParent(T node) {
            LinkedList<T> nodesPath;
            while ((nodesPath = nodesPathThreadLocal.get()) == null) {  // 多线程必须使用ThreadLocal！
                nodesPathThreadLocal.set(new LinkedList<>());
            }
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
                sizeMap.remove(lesser);  // 多线程必须使用ConcurrentHashMap！
            }
        }

        public int size() {
            return sizeMap.size();
        }

        public Set<T> set() {
            return sizeMap.keySet();
        }
    }
}
