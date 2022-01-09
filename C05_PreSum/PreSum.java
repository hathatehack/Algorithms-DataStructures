package C05_PreSum;

public class PreSum {
    public int[] preSum;

    public PreSum(int[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        int len = array.length;
        preSum = new int[len];
        preSum[0] = array[0];
        for (int i = 1; i < len; i++) {
            preSum[i] = preSum[i - 1] + array[i];
        }
    }

    public int rangeSum(int left, int right) {
        return left == 0 ? preSum[right] : preSum[right] - preSum[left - 1];
    }
}
