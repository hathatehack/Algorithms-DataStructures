package C01_random;

import java.util.Arrays;

public class GenerateRandomArray {
    static public void main(String[] args) {
        for (int i = 10; i > 0; i--) {
            int[] array = generateRandomArray(3, 1);
            System.out.println(Arrays.toString(array));
        }
    }

    static public int[] generateRandomArray(int maxSize, int maxValue) {
        int size = (int)(Math.random() * (maxSize + 1));  // [0, maxSize]
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            int value = (int)(Math.random() * (maxValue + 1)) - (int)(Math.random() * (maxValue + 1));  // [-maxValue, maxValue]
            array[i] = value;
        }
        return array;
    }
    static public int[] generateRandomArraySortDistanceNoMoreK(int maxSize, int maxValue, int K) {
        int[] array = generateRandomArray(maxSize, maxValue);
        Arrays.sort(array);
        boolean[] isSwapped = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            int j = Math.min(i + (int)(Math.random() * (K + 1)), array.length - 1);
            if (!isSwapped[i] && !isSwapped[j]) {
                isSwapped[i] = isSwapped[j] = true;
                int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }
        return array;
    }

    static public int[][] generateRandomLines(int maxSize, int maxValue) {
        int size = (int)(Math.random() * (maxSize + 1));  // [0, maxSize]
        int[][] lines = new int[size][2];
        for (int i = 0; i < size; i++) {
            int a = (int)(Math.random() * (maxValue + 1)) - (int)(Math.random() * (maxValue + 1));  // [-maxValue, maxValue]
            int b = (int)(Math.random() * (maxValue + 1)) - (int)(Math.random() * (maxValue + 1));  // [-maxValue, maxValue]
            if (a == b) {
                b++;
            }
            lines[i][0] = Math.min(a, b);
            lines[i][1] = Math.max(a, b);
        }
        return lines;
    }





    static public int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    static public int[][] copyArray(int[][] arr) {
        if (arr == null) {
            return null;
        }
        int[][] res = new int[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    static public boolean isEqual(int[] array1, int[] array2) {
        if (array1 == null && array2 == null) return true;
        if (array1 == null || array2 == null) return false;
        if (array1.length != array2.length) return false;
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    static public boolean isEqual(int[][] array1, int[][] array2) {
        if (array1 == null && array2 == null) return true;
        if (array1 == null || array2 == null) return false;
        if (array1.length != array2.length) return false;
        for (int i = 0; i < array1.length; i++) {
            if(!isEqual(array1[i], array2[i])) {
                return false;
            }
        }
        return true;
    }

    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.format("%s ", arr[i]);
        }
        System.out.println();
    }

    public static void printArray(int[][] arr) {
        if (arr == null) {
            return;
        }
        System.out.printf("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.format("%s, ", Arrays.toString(arr[i]));
        }
        System.out.println("]");
    }
}
