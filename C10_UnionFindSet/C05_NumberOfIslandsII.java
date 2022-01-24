package C10_UnionFindSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class C05_NumberOfIslandsII {
    // 测试链接：https://leetcode.com/problems/number-of-islands-ii
    // 给定区域长宽分别为m、n，给定一个二维数组positions表示某个岛的某个位置，位置可能重复，返回每读取一个位置后岛数量的集合。
    public static List<Integer> numberOfIslandsII(int m, int n, int[][] positions) {
        LinkedList<Integer> ans = new LinkedList<>();
        UnionFindSet ufs = new UnionFindSet(m, n);
        for (int[] position : positions) {
            ans.add(ufs.connect(position[0], position[1]));
        }
        return ans;
    }
    // 并查集（数组实现）
    public static class UnionFindSet {
        private int[] sizeMap;  // 每个节点及其所属集合大小的映射
        private int[] parentMap;  // 每个节点及其父亲的映射
        private int[] nodesPath;  // 寻父过程记录沿途节点
        private int size;  // 有多少集合
        private int row;
        private int col;

        public UnionFindSet(int _row, int _col) {
            row = _row;
            col = _col;
            sizeMap = new int[row * col];
            parentMap = new int[sizeMap.length];
            nodesPath = new int[sizeMap.length];
            size = 0;
        }

        private int index(int r, int c) {
            return r * col + c;
        }

        private int findParent(int node) {
            int parent;
            int i = -1;
            while (node != (parent = findParent(node))) {
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

        public void union(int r1, int c1, int r2, int c2) {
            if (r1 < 0 || r1 >= row || c1 < 0 || c1 >= col ||
                    r2 < 0 || r2 >= row || c2 < 0 || c2 >= col) {
                return;
            }
            int n1 = index(r1, c1);
            int n2 = index(r2, c2);
            if (sizeMap[n1] == 0 || sizeMap[n2] == 0) {  // 有节点不存在，无法合并
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

        public int add(int r, int c) {
            int node = index(r, c);
            if (sizeMap[node] == 0) {  // 节点不存在
                sizeMap[node] = 1;
                parentMap[node] = node;
                size++;
                return node;  // 返回节点
            } else {
                return -1;  // 已存在
            }
        }

        public int connect(int r, int c) {
            int node = add(r, c);
            if (node != -1) {  // 节点添加成功，则尝试与上下左右节点合并
                union(r - 1, c, r, c);;
                union(r + 1, c, r, c);
                union(r, c - 1, r, c);
                union(r, c + 1, r, c);
            }
            return size;
        }
    }



    // 如果m*n比较大，但positions数量少，map实现空间占用少于数组实现
    public static List<Integer> numberOfIslandsII2(int m, int n, int[][] positions) {
        LinkedList<Integer> ans = new LinkedList<>();
        UnionFindSet2 ufs = new UnionFindSet2(m, n);
        for (int[] position : positions) {
            ans.add(ufs.connect(position[0], position[1]));
        }
        return ans;
    }
    // 并查集（HashMap实现）
    public static class UnionFindSet2 {
        private HashMap<Integer, Integer> sizeMap;  // 每个节点及其所属集合大小的映射
        private HashMap<Integer, Integer> parentMap;  // 每个节点及其父亲的映射
        private LinkedList<Integer> nodesPath;  // 寻父过程记录沿途节点
        private int size;  // 有多少集合
        private int row;
        private int col;

        public UnionFindSet2(int _row, int _col) {
            row = _row;
            col = _col;
            sizeMap = new HashMap<>();
            parentMap = new HashMap<>();
            nodesPath = new LinkedList<>();
            size = 0;
        }

        private int index(int r, int c) {
            return r * col + c;
        }

        private int findParent(int node) {
            int parent;
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

        public void union(int r1, int c1, int r2, int c2) {
            int n1 = index(r1, c1);
            int n2 = index(r2, c2);
            if (!parentMap.containsKey(n1) || !parentMap.containsKey(n2)) {  // 有节点不存在，无法合并
                return;
            }
            int p1 = findParent(n1);
            int p2 = findParent(n2);
            if (p1 != p2) {
                int p1SetSize = sizeMap.get(p1);
                int p2SetSize = sizeMap.get(p2);
                // 小集合并入大集合
                Integer bigger = p1SetSize >= p2SetSize ? p1 : p2;
                Integer lesser = bigger == p1 ? p2 : p1;
                parentMap.put(lesser, bigger);  // 仅更新小集合代表节点的父亲，剩余节点在下次触发findParent()时懒更新
                sizeMap.put(bigger, p1SetSize + p2SetSize);
                size--; // sizeMap.remove(lesser);
            }
        }

        public int add(int r, int c) {
            Integer node = index(r, c);
            if (!sizeMap.containsKey(node)) {  // 节点不存在
                sizeMap.put(node, 1);
                parentMap.put(node, node);
                size++;
                return node;  // 返回节点
            } else {
                return -1;  // 已存在
            }
        }

        public int connect(int r, int c) {
            int node = add(r, c);
            if (node != -1) {  // 节点添加成功，则尝试与上下左右节点合并
                union(r - 1, c, r, c);;
                union(r + 1, c, r, c);
                union(r, c - 1, r, c);
                union(r, c + 1, r, c);
            }
            return size;
        }
    }
}
