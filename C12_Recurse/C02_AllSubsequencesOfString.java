package C12_Recurse;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

// 打印一个字符串的全部子序列，每个字符可选，相对顺序不可变。
public class C02_AllSubsequencesOfString {
    public static void main(String[] args) {
        String test = "abcd";
        List<String> ans1 = allSequences(test);
        List<String> ans2 = allSequences2(test);
        for (String s : ans1) System.out.printf("%s ", s);
        System.out.println();
        for (String s : ans2) System.out.printf("%s ", s);
        HashSet<String> set = new HashSet<>(ans1);
        if (ans1.size() != ans2.size()) {
            System.out.println("\nOops!");
        } else {
            for (String s : ans2) {
                if (!set.contains(s)) {
                    System.out.println("\nOops!");
                    break;
                }
            }
        }

        System.out.println("\n==================");
        test = "acc";
        List<String> ans3 = allSequences(test);
        HashSet<String> ans4 = allSequencesNoRepeat(test);
        for (String s : ans3) System.out.printf("%s ", s);
        System.out.println();
        for (String s : ans4) System.out.printf("%s ", s);
    }



    // 自顶向下组合，二分
    public static List<String> allSequences(String string) {
        char[] str = string.toCharArray();
        LinkedList<String> all = new LinkedList<>();
        process(str, 0, "", all);
        all.remove(all.getFirst() == "" ? 0 : (all.size() - 1)); // remove "".
        return all;
    }
    // 整体思想：先组合除最后一个外的前半部，再与最后一个组合，得到的组合path作为下一轮的起始path。
    // 每个字符只有2种组合方式：要、不要。以输入路径含或不含当前字符作为新输入，从顶向下周而复始遍历所有组合。
    private static void process(char[] str, int curIndex, String path, List<String> all) {
        if (curIndex == str.length) {  // 已遍历全部字符，返回路径
            all.add(path);
            return;
        }
        // 不含当前字符的path
        process(str, curIndex + 1, path, all);
        // 含当前字符的path
        process(str, curIndex + 1, path + str[curIndex], all);
    }




    // 自底向上组合，汇总
    public static List<String> allSequences2(String string) {
        char[] str = string.toCharArray();
        return process2(str, 0);
    }
    // 整体思想：先得到除第一个外的后半部组合，再与第一个组合，最后返回组合结果做为上一轮的后半部组合结果。
    // 先组合剩余，再组合全部（当前 、剩余、当前x剩余），从底向上周而复始返回组合结果。
    private static List<String> process2(char[] str, int curIndex) {
        if (curIndex == str.length) {
            return new LinkedList<>();
        }
        String cur = String.valueOf(str[curIndex]);
        // 1.先组合除当前字符的剩余字符
        List<String> sub = process2(str, curIndex + 1);
        // 2.再组合两部分字符，有3种组合方法：只要当前、不要当前、全要
        List<String> all = new LinkedList<>();
        all.add(cur);  // 只要当前字符；
        all.addAll(sub);  // 不含当前字符的组合；
        for (String s : sub) {
            all.add(cur + s);  // 全部字符组合。
        }
        return all;
    }







    // 自顶向下组合，二分
    public static HashSet<String> allSequencesNoRepeat(String string) {
        char[] str = string.toCharArray();
        HashSet<String> all = new HashSet<>();
        processNoRepeat(str, 0, "", all);
        return all;
    }
    // 整体思想：先组合除最后一个外的前半部，再与最后一个组合，得到的组合path作为下一轮的起始path。
    // 每个字符只有2种组合方式：要、不要。以输入路径含或不含当前字符作为新输入，从顶向下周而复始遍历所有组合。
    private static void processNoRepeat(char[] str, int curIndex, String path, HashSet<String> all) {
        if (curIndex == str.length) {  // 已遍历全部字符，返回路径
            all.add(path);
            return;
        }
        // 不含当前字符的path
        processNoRepeat(str, curIndex + 1, path, all);
        // 含当前字符的path
        processNoRepeat(str, curIndex + 1, path + str[curIndex], all);
    }
}
