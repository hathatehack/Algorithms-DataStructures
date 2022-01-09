package C03_BinarySearch;

public class C04_FindAnyLocalMinimum {
    public static void main(String[] args) {
        int[] array = new int[] {10, 5, 10, 5 ,10};
        int index = localMinimum(array);
        System.out.printf("index=%d  %s\n", index, check(array, index));
    }

    // 给定一个数组arr，任两个相邻的数都不相等，找到任意一个局部最小位置返回
    public static int localMinimum(int[] array) {
        if (array == null || array.length == 0) {
            return -1;
        }
        if (array[0] < array[1]) {
            return 0;  // 0位置局部最小
        }
        if (array[array.length - 1] < array[array.length - 2]) {
            return array.length - 1; // 最后一个是局部最小
        }
        // 以上情况都不符合，说明局部最小在内部，二分查找时以右下降做左边界、左下降做右边界。
        // \ ??? /
        // 0 ... N
        int L = 1;
        int R = array.length - 2;
        int mid = 0;
        while (L < R) {
            mid = L + ((R - L) >> 1);
            if (array[mid] > array[mid + 1]) {  // 右下降做左边界
                L = mid + 1;
            } else if (array[mid] < array[mid - 1]) { // 左下降做右边界
                R = mid - 1;
            } else {
                return mid;
            }
        }
        return L;
    }

    private static boolean check(int[] array, int index) {
        if (index == 0 && (array[0] < array[1])) {
            return true;
        }
        if ((index == array.length - 1) && (array[array.length - 1] < array[array.length - 2])) {
            return true;
        }
        return array[index - 1] > array[index] && array[index] < array[index + 1];
    }
}
