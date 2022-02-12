package C01_random;

public class GenerateRandomStringArray {
    public static String generateRandomString(int maxStrLen, int maxCharKind, char start) {
        char[] array = new char[(int) (Math.random() * maxStrLen) + 1];
        for (int i = 0; i < array.length; i++) {
            int value = (int) (Math.random() * maxCharKind);
            array[i] = (char) (start + value);
        }
        return String.valueOf(array);
    }

    public static String generateRandomString(int maxStrLen, char start, char end) {
        return generateRandomString(maxStrLen, end - start + 1, start);
    }

    public static String generateRandomString(int maxStrLen, int maxCharKind) {
        return generateRandomString(maxStrLen, maxCharKind, 'a');
    }

    public static String[] generateRandomStringArray(int maxSize, int maxStrLen, int maxCharKind, char start) {
        String[] array = new String[(int) (Math.random() * maxSize) + 1];
        for (int i = 0; i < array.length; i++) {
            array[i] = generateRandomString(maxStrLen, maxCharKind, start);
        }
        return array;
    }

    public static String[] generateRandomStringArray(int maxSize, int maxStrLen, int maxCharKind) {
        return generateRandomStringArray(maxSize, maxStrLen, maxCharKind, 'a');
    }

    public static String[] copyArray(String[] arr) {
        if (arr == null) {
            return null;
        }
        String[] res = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = String.valueOf(arr[i]);
        }
        return res;
    }

    public static boolean isEqual(String s1, String s2) {
        if (s1 == null && s2 == null) return true;
        if (s1 == null || s2 == null) return false;
        return s1.equals(s2);
    }

    public static boolean isEqual(String[] array1, String[] array2) {
        if (array1 == null && array2 == null) return true;
        if (array1 == null || array2 == null) return false;
        if (array1.length != array2.length) return false;
        for (int i = 0; i < array1.length; i++) {
            if (!isEqual(array1[i], array2[i])) {
                return false;
            }
        }
        return true;
    }

    public static void printArray(String[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.format("%s, ", arr[i]);
        }
        System.out.println();
    }
}
