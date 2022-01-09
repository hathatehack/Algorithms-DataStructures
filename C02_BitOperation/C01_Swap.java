package C02_BitOperation;

public class C01_Swap {
    static public void main(String[] args) {
        int[] array = {1,2};

        swap(array, 0, 1);
        System.out.format("array[0]=%d, array[1]=%d\n", array[0], array[1]);

        // i和j相等交换出错，异或总是为0
        int i = 0;
        int j = 0;
        array[i] ^= array[j];
        array[j] ^= array[i];
        array[i] ^= array[j];
        System.out.format("array[%d]=%d, array[%d]=%d\n",i, j, array[i], array[j]);
    }

    static public void swap(int[] array, int i, int j) {
        // i和j相等交换出错，异或总是为0
        if (i == j) {
            return;
        }
        array[i] ^= array[j];
        array[j] ^= array[i];
        array[i] ^= array[j];
    }
}
