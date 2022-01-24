package C10_UnionFindSet;

public class C02_FriendCircles {
    // 测试链接：https://leetcode.com/problems/friend-circles
    // 给定一个行列数相等的二维数组matrix，里面的值不是1就是0，表示一群人中任意两个人认识或不认识，返回共有几个无关系的圈子。
    public static int friendCircles(int[][] matrix) {
        UnionFindSet ufs = new UnionFindSet(matrix.length);
        int row = matrix.length;
        int col = matrix[0].length;
        for (int i = row - 1; i >= 0; i--) {
            for (int j = col - 1; j > i; j--) {  // 倒序处理matrix上三角的数据，下三角和上三角重复所以不处理
                if (matrix[i][j] == 1) {
                    ufs.union(i, j);
                }
            }
        }
        return ufs.size();
    }

    private static class UnionFindSet {
        private int[] sizeMap;  // 每个节点及其所属集合大小的映射
        private int[] parentMap;  // 每个节点及其父亲的映射
        private int[] nodesPath;  // 寻父过程记录沿途节点
        private int size;  // 有多少集合

        public UnionFindSet(int N) {
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
                sizeMap[bigger]=  p1SetSize + p2SetSize;
                size--;
            }
        }

        public int size() {
            return size;
        }
    }



    public static void main(String[] args) {
        int[][] matrix = new int[][] {
                new int[] {1, 0, 0, 0},
                new int[] {0, 1, 0, 0},
                new int[] {0, 0, 1, 0},
                new int[] {0, 0, 0, 1},
        };
        int ans = friendCircles(matrix);
        if (ans != 4) {
            System.out.printf("test1 failed! ans=%d\n", ans);
        }

        matrix = new int[][] {
                new int[] {1, 1, 0, 0},
                new int[] {1, 1, 0, 0},
                new int[] {0, 0, 1, 0},
                new int[] {0, 0, 0, 1},
        };
        ans = friendCircles(matrix);
        if (ans != 3) {
            System.out.printf("test2 failed! ans=%d\n", ans);
        }

        matrix = new int[][] {
                new int[] {1, 1, 1, 0},
                new int[] {1, 1, 0, 0},
                new int[] {1, 0, 1, 0},
                new int[] {0, 0, 0, 1},
        };
        ans = friendCircles(matrix);
        if (ans != 2) {
            System.out.printf("test2 failed! ans=%d\n", ans);
        }

        matrix = new int[][] {
                new int[] {1, 1, 1, 0},
                new int[] {1, 1, 0, 1},
                new int[] {1, 0, 1, 0},
                new int[] {0, 1, 0, 1},
        };
        ans = friendCircles(matrix);
        if (ans != 1) {
            System.out.printf("test2 failed! ans=%d\n", ans);
        }
    }
}
