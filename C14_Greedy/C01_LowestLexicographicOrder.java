package C14_Greedy;

import C01_random.GenerateRandomStringArray;
import C12_Recurse.C04_AllCombinations;
import java.util.Arrays;
import java.util.TreeSet;

public class C01_LowestLexicographicOrder {
    // 给定一个由字符串组成的数组array，必须拼接所有的字符串，返回拼接结果中字典序最小的拼接串。
    // 贪心解
    public static String lowestLexOrder(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        array = GenerateRandomStringArray.copyArray(array);
        Arrays.sort(array, (s1, s2) -> "".equals(s1) ? 1 : "".equals(s2) ? -1 : // 空字符要挪到后面！
                (s1 + s2).compareTo(s2 + s1));              // {dc,d}排序后是{dc,d}拼接得dcd，正确答案
//        Arrays.sort(array, (s1, s2) -> s1.compareTo(s2)); // {dc,d}排序后是{d,dc}拼接得ddc，不是正确答案
        StringBuilder ans = new StringBuilder();
        for (String str : array) {
            ans.append(str);
        }
        return ans.toString();
    }



    // 暴力解
    public static String lowestLexOrder2(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        array = GenerateRandomStringArray.copyArray(array);
        TreeSet<String> ans = process(array);
        return ans.first();
    }
    // 返回strings数组的所有拼接组合
    private static TreeSet<String> process(String[] array) {
        TreeSet<String> treeSet = new TreeSet<>();
        if (array.length == 0) {
            treeSet.add("");
            return treeSet;
        }
        // 每个元素做开头，去拼接剩余元素的全部组合
        for (int i = 0; i < array.length; i++) {
            String first = array[i];
            String[] rest = copyExcept(array, i);
            TreeSet<String> restCombinations = process(rest);
            for (String restCombination : restCombinations) {
                treeSet.add(first + restCombination);
            }
        }
        return treeSet;
    }
    private static String[] copyExcept(String[] array, int index) {
        String[] newArray = new String[array.length - 1];
        for (int i = 0, newIndex = 0; i < array.length; i++) {
            if (i != index) {
                newArray[newIndex++] = array[i];
            }
        }
        return newArray;
    }



    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 4;
        int maxStrLen = 5;
        int maxCharKind = 5;
        for (int i = 0; i < testTimes; i++) {
            String[] array = GenerateRandomStringArray.generateRandomStringArray(maxSize, maxStrLen, maxCharKind);
//            System.out.printf("i=%d  array: ", i); GenerateRandomStringArray.printArray(array);
            String ans1 = lowestLexOrder(array);
            String ans2 = lowestLexOrder2(array);
            if (!GenerateRandomStringArray.isEqual(ans1, ans2)) {
                System.err.printf("Oops! i=%d\n", i);
                System.err.print("array: "); GenerateRandomStringArray.printArray(array);
                System.err.printf("ans1=%s, ans2=%s\n", ans1, ans2);
            }
        }
    }
}
