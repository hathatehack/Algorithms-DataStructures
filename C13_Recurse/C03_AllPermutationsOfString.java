package C13_Recurse;

import java.util.ArrayList;
import java.util.List;

// 打印给定字符串的全部排列组合，每个字符都要。
public class C03_AllPermutationsOfString {
    public static void main(String[] args) {
        String test = "abc";
        List<String> ans1 = allPermutations(test);
        List<String> ans2 = allPermutations2(test);
        for (String s : ans1) System.out.printf("%s ", s);
        System.out.println();
        for (String s : ans2) System.out.printf("%s ", s);

        System.out.println("\n==================");
        test = "acc";
        List<String> ans3 = allPermutations(test);
        List<String> ans4 = allPermutationsNoRepeat(test);
        for (String s : ans3) System.out.printf("%s ", s);
        System.out.println();
        for (String s : ans4) System.out.printf("%s ", s);
    }


    public static List<String> allPermutations(String string) {
        List<String> all = new ArrayList<>();
        if (string == null || string.length() == 0)
            return all;
        process(string.toCharArray(), 0, all);
        return all;
    }
    // chars为所有字符，startIndex为开始处理位置，将所有排列组合记录到all
    private static void process(char[] chars, int startIndex, List<String> all) {
        if (startIndex == chars.length) {
            all.add(String.valueOf(chars));
            return;
        }
        // 轮流将startIndex到结束位置的每个字符做开头
        for (int i = startIndex; i < chars.length; i++) {
            swap(chars, startIndex, i);  // i位置的字符做开头
            process(chars, startIndex + 1, all);
            swap(chars, startIndex, i);  // 恢复
        }
    }

    private static void swap(char[] array, int i, int j) {
        if (i == j)
            return;
        char tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }





    public static List<String> allPermutations2(String string) {
        List<String> all = new ArrayList<>();
        if (string == null || string.length() == 0)
            return all;
        char[] str = string.toCharArray();
        ArrayList<Character> rest = new ArrayList<Character>();
        for (char c : str)
            rest.add(c);
        process2("", rest, all);
        return all;
    }
    // path为已排列串，rest为剩余待排列字符，将rest的全部排列组合追加到path后面并记录到all
    private static void process2(String path, List<Character> rest, List<String> all) {
        if (rest.isEmpty()) {
            all.add(path);
            return;
        }
        // 对n个字符轮流进行头排列组合
        for (int n = rest.size(), i = 0; i < n; i++) {
            char cur = rest.remove(i);  // 第i字符做头；剩余待排列去除i字符。
            // 对剩余n-1个字符轮流进行头排列组合
            process2(path + cur, // 新路径为旧路径加上当前i字符
                    rest,  // 剩余需排列字符
                    all);
            rest.add(i, cur);  // 恢复第i字符，进行下个字符头排列
        }
    }





    public static List<String> allPermutationsNoRepeat(String string) {
        List<String> all = new ArrayList<>();
        if (string == null || string.length() == 0)
            return all;
        processNoRepeat(string.toCharArray(), 0, all);
        return all;
    }

    private static void processNoRepeat(char[] str, int index, List<String> all) {
        if (index == str.length) {
            all.add(String.valueOf(str));
            return;
        }
        boolean[] visited = new boolean[256];
        for (int i = index; i < str.length; i++) {
            if (!visited[str[i]]) {  // 剪枝，避免无谓重复！
                visited[str[i]] = true;
                swap(str, index, i);
                processNoRepeat(str, index + 1, all);
                swap(str, index, i);
            }
        }
    }
}
