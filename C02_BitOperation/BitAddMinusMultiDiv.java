package C02_BitOperation;

public class BitAddMinusMultiDiv {
    static public void main(String[] args) {
        System.out.println(multi(9, -3));
        System.out.println(div(10, -2));
    }

    static public int add(int a, int b) {
        int sum = b == 0 ? a : 0;
        while (b != 0) {
            sum = a ^ b;       // 无进位相加
            b = (a & b) << 1;  // 进位信息
            a = sum;
        }
        return sum;
    }

    static public int negNum(int n) {
        return add(~n, 1);  // 补码
    }

    static public int minus(int a, int b) {
        return add(a, negNum(b));
    }

    static public int multi(int a, int b) {
        int res = 0;
        while (b != 0) {
            if ((b & 1) != 0) {
                res = add(a, res);
            }
            a <<= 1;
            b >>>= 1;
        }
        return res;
    }


    static public boolean isNeg(int n) {
        return n < 0;
    }

    static public int div(int a, int b) {
        int x = isNeg(a) ? negNum(a) : a;
        int y = isNeg(b) ? negNum(b) : b;
        int res = 0;
        // x和y已转为非负数，符号位不参与计算
        // 右移x去比较y，可以避免y左移到符号位的bug
        for (int i = 30; i >= 0; i = minus(i, 1)) {
            if ((x >> i) >= y) {
                res |= (1 << i);
                x = minus(x, y << i);
            }
        }
        // 结果值还原符号位
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }

    static public int divide(int a, int b) {
        if (a == Integer.MIN_VALUE && b == Integer.MIN_VALUE) {
            return -1;
        } else if (b == Integer.MIN_VALUE) {
            return 0;
        } else if (a == Integer.MIN_VALUE) {
            if (b == -1) {
                return Integer.MAX_VALUE;
            }
            else {
                // Int负数最小值没有对应的正值，所以先加1保证有对应最大正值并求得商，再加上补偿商，得到最终商。
                int c = div(add(a, 1), b);
                int d = minus(a, multi(b, c));
                int e = div(d, b);
                return add(c, e);
            }
        } else {
            return div(a, b);
        }
    }
}
