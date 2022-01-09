package C02_BitOperation;

public class C03_EvenTimeOddTimes {
    static public void main(String[] args) {
        printOddTimesNum1(new int[] {1,1,2,2,3,3,3,4,4});

        printOddTimesNum2(new int[] {1,1,2,2,3,3,3,4,4,4});
    }

    // 一个数组中有一种数出现了奇数次，其他数都出现了偶数次，找到并打印这种数。
    static public void printOddTimesNum1(int[] array) {
        int eor = 0;
        for (int i = 0; i < array.length; i++) {
            eor ^= array[i];  // 一个数异或偶数次等于0，最后只剩奇数次的数。
        }

        System.out.println(eor);
    }

    // 一个数组中有两种数出现了奇数次，其他数都出现了偶数次，找到并打印这两种数。
    static public void printOddTimesNum2(int[] array) {
        int eor = 0;
        for (int i = 0; i < array.length; i++) {
            eor ^= array[i];  // 得到两种奇数次的数的异或值
        }
        int rightBit1 = eor & (~eor + 1);  // 提取这两种奇数次的数的等于1的最右bit，用于识别这两种数。

        int eorNum1 = 0;
        for (int i = 0; i < array.length; i++) {
            if ((rightBit1 & array[i]) != 0) {
                eorNum1 ^= array[i];
            }
        }
        System.out.println(eorNum1 + "," + (eorNum1 ^ eor));
    }
}
