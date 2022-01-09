package C08_BTree;

public class C04_PaperFolding {
    public static void main(String[] args) {
        printAllFolds(1);
        printAllFolds(2);
        printAllFolds(3);
        printAllFolds(4);
    }

    // 一段竖直的纸条从下向上逆时针对折多次，第1次对折展开得1条凹折痕，第2次有3条折痕（凹凹凸），第3次有7条折痕（凹凹凸凹凹凸凸），
    // 给定一个参数N，代表纸条对折N次，请从上到下打印所有折痕的方向。
    // 分析可知每一次对折，1.会以上一次的每个新增折痕为头节点新增凹凸节点；2.以第1条折痕为中心，上下折痕数一致。从上到下打印符合中序遍历的规律。
    static public void printAllFolds(int N) {
        process(1, N, true);
        System.out.println();
    }

    static private void process(int i, int N, boolean isDown) {
        if (i > N)
            return;
        process(i + 1, N, true);
        System.out.print(isDown ? "凹" : "凸");
        process(i + 1, N, false);
    }
}
