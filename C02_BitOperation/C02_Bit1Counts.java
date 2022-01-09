package C02_BitOperation;

public class C02_Bit1Counts {
    static public void main(String[] args) {
        System.out.println(bit1Counts(0xfe));
    }

    static public int bit1Counts(int num) {
        int count = 0;
        while (num != 0) {
            int rightBit1 = num & (~num + 1);  // num & -num
            count++;
            num ^= rightBit1;
        }
        return count;
    }
}
