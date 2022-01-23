package C04_Sort;

import java.util.Arrays;

public class radixSort {
    static public void main(String[] args) {
        int[] arr = new int[] {11,1,43,55,100,200,111};
        int[] result = radixSort(arr);
        System.out.println(Arrays.toString(result));
    }

    // 输入需要为正整数，负数可以通过将数组减去该最小负数转化为非负数，追后输出时还原，但可能存在溢出风险。
    static public int[] radixSort(int[] array) {
        int[] count = new int[10];
        int[] result = new int[array.length];

        for (int r = 0; r < 3; r++) {
            int divisor = (int)Math.pow(10, r);

            // 统计个数并填充到计数数组
            for (int i = 0; i < array.length; i++) {
                int num = array[i] / divisor % 10;
                count[num]++;
            }
            // 前缀和，表示小于等于i有多少个数。
            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }

            for (int i = array.length - 1; i >= 0; i--) {
                int num = array[i] / divisor % 10;
                result[--count[num]] = array[i];
            }

            System.arraycopy(result, 0, array, 0, array.length);
            Arrays.fill(count, 0);
        }

        return result;
    }
}
