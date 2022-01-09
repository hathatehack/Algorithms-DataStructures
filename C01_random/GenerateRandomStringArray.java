package C01_random;

public class GenerateRandomStringArray {
    public static String generateRandomString(int strLen, int kind) {
        char[] array = new char[(int) (Math.random() * strLen) + 1];
        for (int i = 0; i < array.length; i++) {
            int value = (int) (Math.random() * kind);
            array[i] = (char) ('a' + value);
        }
        return String.valueOf(array);
    }

    public static String[] generateRandomStringArray(int maxSize, int maxStrLen, int kind) {
        String[] array = new String[(int) (Math.random() * maxSize) + 1];
        for (int i = 0; i < array.length; i++) {
            array[i] = generateRandomString(maxStrLen, kind);
        }
        return array;
    }
}
