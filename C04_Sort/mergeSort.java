package C04_Sort;

import java.util.Arrays;

import static java.lang.Integer.min;

public class mergeSort {
    static public void main(String[] args)
    {
        int[] arr = new int[]{1,3,5,7,2,4,6};
        mergeSortRecursive(arr);
        System.out.print(Arrays.toString(arr));

        arr = new int[]{1,3,5,7,2,4,6};
        mergeSortIterative(arr);
        System.out.print(Arrays.toString(arr));

        arr = new int[]{1,3,5,7,2,4,6};
        mergeSortIterative1(arr);
        System.out.print(Arrays.toString(arr));

        mergeSortTest();
    }

    static public void mergeSortRecursive(int[] array)
    {
        if (array == null || array.length <= 1)
            return;
        int[] temp = new int[array.length];  // 预先分配临时数组，避免递归中频繁创建销毁
        process(array, 0, array.length - 1, temp);
    }

    static public void process(int[] array, int left, int right, int[] temp)
    {
        if (left < right) {
            int mid = left + (right - left) / 2;
            process(array, left, mid, temp);            // 左半部归并排序
            process(array, mid + 1, right, temp);  // 右半部归并排序
            mergeAndSort(array, left, mid, right, temp);  // 左右两个有序数组归并排序
        }
    }

    static public void mergeAndSort(int[] array, int left, int mid, int right, int[] temp)
    {
        int i = left;
        int j = mid + 1;
        int k = 0;

        while(i <= mid && j <= right) {
            if (array[i] <= array[j]) {  // <=保证排序稳定，值相等的元素保持原本顺序！
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while(i <= mid) temp[k++] = array[i++];
        while(j <= right) temp[k++] = array[j++];

        k = 0;
        while(left <= right) {
            array[left++] = temp[k++];
        }
    }





    static public void mergeSortIterative(int[] array)
    {
        if (array == null || array.length <= 1)
            return;
        int[] a = array;
        int[] b = new int[array.length];

        for (int step = 1; step < array.length; step += step) {  // 没考虑start+step可能溢出，请参考mergeSortIterative1
            for (int start = 0; start < array.length; start += step + step) {  // 没考虑start+step可能溢出，请参考mergeSortIterative1
                int left = start;
                int mid = min(start + step, array.length);
                int right = min(start + step + step, array.length);

                int i = left;
                int j = mid;
                int k = left;
                while(i < mid && j < right) {
                    if(a[i] <= a[j]) {
                        b[k++] = a[i++];
                    } else {
                        b[k++] = a[j++];
                    }
                }
                while (i < mid) b[k++] = a[i++];;
                while (j < right) b[k++] = a[j++];
            }
            // 数组b经过一轮归并排序后做为下一轮的输入！
            int[] tmp = a;
            a = b;
            b = tmp;

            // step等于2的30次方时，step*2将溢出，循环不能结束。
            if (step > (array.length / 2))
                break;
        }

        if (a != array) {
            for (int i = 0; i < array.length; i++) {
                array[i] = a[i];
            }
        }
    }

    static public void mergeSortIterative1(int[] array) {
        if (array == null && array.length <= 1) {
            return;
        }
        int[] a = array;
        int[] b = new int[array.length];

        int N = array.length;
        int step = 1;
        while (step < N) {
            int left = 0;
            int mid = 0;
            int right = 0;
            while (N - left > step) {  // 确保下面的left+step不会溢出，保证存在左右两部分。
                mid = left + step;
                right = mid + Math.min(step, N - mid);
                // merger sort
                int i = left;
                int j = mid;
                int k = left;
                while(i < mid && j < right) {
                    if(a[i] <= a[j]) {
                        b[k++] = a[i++];
                    } else {
                        b[k++] = a[j++];
                    }
                }
                while (i < mid) b[k++] = a[i++];;
                while (j < right) b[k++] = a[j++];
                // next round
                left = right;
            }
            for (; right < N; right++) {
                b[right] = a[right];
            }
            // 数组b经过一轮归并排序后做为下一轮的输入！
            int[] tmp = a;
            a = b;
            b = tmp;

            // step等于2的30次方时，step*2将溢出，循环不能正常结束。
            if (step > (N / 2)) {
                break;
            }
            step <<= 1;
        }

        if (a != array) {
            for (int i = 0; i < array.length; i++) {
                array[i] = a[i];
            }
        }
    }





    static public void mergeSortTest()
    {
        int[] array = new int[]{
                1,3,5,7,  /*左右部都是有序的，mergeSort只需左右部依次取元素比较，较小的先出队*/
                2,4,6
        };

        int mid = array.length / 2;
        int[] temp = new int[array.length];

        int i = 0;
        int j = mid + 1;
        int k = 0;

        while(i <= mid && j < array.length) {
            if (array[i] <= array[j]) {  // <=保证排序稳定，值相等的元素保持原本顺序！
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }
        while(i <= mid) temp[k++] = array[i++];
        while(j < array.length) temp[k++] = array[j++];

        System.out.print(Arrays.toString(array));
    }
}