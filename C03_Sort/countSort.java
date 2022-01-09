package C03_Sort;

import java.util.Arrays;

public class countSort {
    static public void main(String[] args) {
        int[] arr = new int[]{0,1,0,1,1,2,3,4,5,4,3,2,6};
//        int[] result = countSort1(arr);
        int[] result = countSort2(arr);
        System.out.println(Arrays.toString(result));
    }

    static public int[] countSort1(int[] array) {
        int min = array[0];
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            } else if (min > array[i]) {
                min = array[i];
            }
        }

        int[] count = new int[max - min + 1];
        // 统计个数并填充到计数数组
        for (int i = 0; i < array.length; i++) {
            count[array[i] - min]++;
        }
        // 前缀和，表示小于等于i有多少个数。
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        int[] result = new int[array.length];
        // 从后往前遍历array并填充到result。
        for (int i = array.length - 1; i >= 0; i--) {
            result[count[array[i] - min] - 1] = array[i];
            count[array[i] - min]--;
        }

        return result;
    }


    static public int[] countSort2(int[] array) {
        int min = array[0];
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            } else if (min > array[i]) {
                min = array[i];
            }
        }

        int[] count = new int[max - min + 1 + 1];
        // 统计个数并填充到计数数组，count[0]保持为0
        for (int i = 0; i < array.length; i++) {
            count[array[i] - min + 1]++;
        }
        // 前缀和，表示小于等于i有多少个数。
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        int[] result = new int[array.length];
        // 从前往后遍历array并填充到result。
        for (int i = 0; i < array.length; i++) {
            result[count[array[i] - min]] = array[i];
            count[array[i] - min]++;
        }

        return result;
    }

    // https://www.cnblogs.com/xiaochuan94/p/11198610.html  一文弄懂计数排序算法！ - 程序员小川 - 博客园 (cnblogs.com)
}
