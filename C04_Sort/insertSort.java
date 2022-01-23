package C04_Sort;

import java.util.Arrays;

public class insertSort {
    static public void main(String[] args)
    {
        int[] arr = new int[]{1,3,5,2,6,4};
        insertSort1(arr);
        System.out.print(Arrays.toString(arr));
        insertSort2(arr);
        System.out.print(Arrays.toString(arr));
    }

    static public void insertSort1(int[] array)
    {
        for(int i = 1; i < array.length; i++)
        {
            int ele = array[i];
            // 把全部小于ele的元素右挪，最后在左边空位插入ele。
            int j = i - 1;
            for (; j >= 0; j--)
            {
                if(ele < array[j])
                    array[j+1] = array[j];
                else
                    break;
            }
            array[j+1] = ele;
        }
    }


    static public void insertSort2(int[] array) {
        if (array == null || array.length < 2) {
            return;
        }

        for (int end = 1; end < array.length; end++) {
            int i = end;
            // 从右往左依次比较交换。
            while (i - 1 >= 0 && array[i - 1] > array[i]) {
                swap(array, i - 1, i);
                i--;
            }
//            for (int j = i - 1; j >= 0 && array[j] > array[j + 1]; j--) {
//                swap(array, j , j + 1);
//            }
        }
    }

    static public void swap(int[] array, int i, int j) {
//        int temp = array[i];
//        array[i] = array[j];
//        array[j] = temp;
        array[i] ^= array[j];
        array[j] ^= array[i];
        array[i] ^= array[j];
    }
}
