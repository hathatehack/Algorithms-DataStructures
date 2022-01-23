package C04_Sort;

import java.util.Arrays;

public class bubbleSort {
    static public void main(String[] args)
    {
        int[] arr = new int[]{1,3,5,2,6,4};
        bubbleSort1(arr);
        System.out.print(Arrays.toString(arr));
        bubbleSort2(arr);
        System.out.print(Arrays.toString(arr));
    }

    static public void bubbleSort1(int[] array)
    {
        if (array == null || array.length < 2)
            return;

        for(int i = 0; i < array.length; i++)
        {
            boolean flag = false;
            for (int j = 0; j < array.length - i - 1; j++)
            {
                if (array[j] > array[j+1])
                {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j+1] = temp;
                    flag = true;
                }
            }

            if (!flag)
                break;
        }
    }


    static public void bubbleSort2(int[] array) {
        if (array == null || array.length < 2)
            return;

        for (int end = array.length - 1; end >= 0; end--) {
            boolean flag = false;
            for (int second = 1; second <= end; second++) {
                if (array[second - 1] > array[second]) {
                    swap(array, second - 1, second);
                    flag = true;
                }
            }

            if (!flag)
                break;
        }
    }

    static public void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
