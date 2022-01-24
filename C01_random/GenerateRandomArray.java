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

    static public int[] generateRandomArrayNonRepeat(int maxSize) {
        int size = (int)(Math.random() * maxSize) + 1;
        int[] array = new int[size];
        // 生成递增序列
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        // 打乱顺序
        for (int i = 0; i < size; i++) {
            int swapIndex = (int)(Math.random() * size);
            int tmp = array[swapIndex];
            array[swapIndex] = array[i];
            array[i] = tmp;
        }
        return array;
    }

    static public int[] generateRandomArrayPositive(int maxSize, int maxValue) {
        int size = (int)(Math.random() * (maxSize + 1));  // [0, maxSize]
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int)(Math.random() * maxValue) + 1; // [1, maxValue]
        }
        return array;
    }

    static public char[] generateRandomArrayChar(int maxSize, char start, char end) {
        char[] array = new char[(int)(Math.random() * (maxSize + 1))];  // [0, maxSize]
        int range = end - start + 1;
        for (int i = 0; i < array.length; i++) {
            array[i] = (char)((int)(Math.random() * range) + start);  // [start, end]
        }
        return array;
    }

    static public char[][] generateRandomMatrixChar(int maxRow, int minRow, int maxCol, int minCol, char start, char end) {
        int rowRange = maxRow - minRow + 1;
        int colRange = maxCol - minCol + 1;
        char[][] matrix = new char[minRow + (int)(Math.random() * rowRange)][minCol + (int)(Math.random() * colRange)];
        int range = end - start + 1;
        for (int r = matrix.length - 1; r >= 0; r--) {
            for (int c = matrix[0].length - 1; c >= 0; c--)
                matrix[r][c] = (char)((int)(Math.random() * range) + start);  // [start, end]
        }
        return matrix;
    }
    static public char[][] generateRandomMatrixChar(int maxRow, int maxCol, char start, char end) {
        return generateRandomMatrixChar(maxRow, 0, maxCol, 0, start, end);
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

    static public int[][] generateRandomLinesPositive(int maxSize, int maxValue) {
        int size = (int)(Math.random() * (maxSize + 1));  // [0, maxSize]
        int[][] lines = new int[size][2];
        for (int i = 0; i < size; i++) {
            int a = (int)(Math.random() * (maxValue + 1));  // [0, maxValue]
            int b = (int)(Math.random() * (maxValue + 1));  // [0, maxValue]
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
        int row = arr.length;
        int col = arr[0].length;
        int[][] copy = new int[arr.length][col];
        for (int r = row - 1; r >= 0; r--) {
            for (int c = col - 1; c >= 0; c--)
                copy[r][c] = arr[r][c];
        }
        return copy;
    }

    static public char[][] copyArray(char[][] arr){
        if (arr == null) {
            return null;
        }
        int row = arr.length;
        int col = arr[0].length;
        char[][] copy = new char[arr.length][col];
        for (int r = row - 1; r >= 0; r--) {
            for (int c = col - 1; c >= 0; c--)
                copy[r][c] = arr[r][c];
        }
        return copy;
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
            System.out.format("%s, ", arr[i]);
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
