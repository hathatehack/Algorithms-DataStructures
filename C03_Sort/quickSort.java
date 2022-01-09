package C03_Sort;

import java.util.Arrays;
import java.util.Stack;

public class quickSort {
    static public void main(String[] args) {
        int[] arr = new int[] {7,3,2,6,8,1,9,5,4,6,10};
        quickSortRecursive1(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[] {7,3,2,6,8,1,9,5,4,6,10};
        quickSortRecursive2(arr);
        System.out.print(Arrays.toString(arr));
        arr = new int[] {7,3,2};
        quickSortRecursive2(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[] {7,3,2,6,8,1,9,5,4,6,10};
        quickSortRecursive3(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[] {7,3,2,6,8,1,9,5,4,6,10};
        quickSortIterative(arr);
        System.out.println(Arrays.toString(arr));
    }

    static public void quickSortRecursive1(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        process1(array, 0, array.length - 1);
    }

    static public void process1(int[] array, int left, int right) {
        if (left < right) {
            int pivot_idx = partition1(array, left, right);
            process1(array, left, pivot_idx - 1);
            process1(array, pivot_idx + 1, right);
        }
    }
    // 左右同时相向搜索逼近 >.....<
    static public int partition1(int[] array, int left, int right) {
        int pivot = array[right];
        int l = left;
        int r = right - 1;

        while (l <= r) {  // {1,2} 使用l<=r确保最后的swap(array, l, right)正确
            while (l <= r && array[l] <= pivot) l++;  // {1,2,3,5} 使用l<=r确保最后的swap(array, l, right)正确；{2,3,2} array[l]等于pivot要跳过避免死循环。
            while (l <= r && array[r] > pivot) r--;   // {5,6,1,7,3,4,3} 使用array[r]>pivot确保左边都是小于等于3，右边都是大于3
            // 左边大于pivot的跟右边小于等于pivot的交换
            if (l < r) {
                swap(array, l, r);
            }
        }

        swap(array, l, right);

        return l;
    }

    static public void swap(int[] array, int i, int j) {
        if (i == j) return;
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }






    static public void quickSortRecursive2(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        process2(array, 0, array.length - 1);
    }

    static public void process2(int[] array, int left, int right) {
        if (right - left >= 3) {
            int pivot_idx = partition2(array, left, right);
            process2(array, left, pivot_idx - 1);
            process2(array, pivot_idx + 1, right);
        } else {
            medianPivot(array, left, right);
        }
    }

    static public int partition2(int[] array, int left, int right) {
        int pivot = medianPivot(array, left, right);
        int L = left;
        int R = right - 1;
        for(;;)
        {
            /*L R分别向右和向左移动，先++因为medianPivot方法对首尾和倒数第二个元素已排序*/
            while(array[++L] < pivot) {}
            while(array[--R] > pivot) {}

            if(L < R) {
                swap(array, L, R);
            }
            /*交错时停止*/
            else {
                break;
            }
        }
        /*交换基准元素和i指向的元素*/
        swap(array, L, right-1);
        return L;
    }
    // 三数中值法选基准
    static public int medianPivot(int[] array, int left, int right) {
        int center = left + (right - left) / 2;
        // 对三数进行排序
        if (array[left] > array[center])
            swap(array, left, center);
        if (array[left] > array[right])
            swap(array, left, right);
        if (array[center] > array[right])
            swap(array, center, right);
        // 交换中值和倒数第二个元素
        swap(array, center, right - 1);

        return array[right - 1];
    }






    static public void quickSortRecursive3(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        process3(array, 0, array.length - 1);
    }

    static public void process3(int[] array, int left, int right) {
        if (left < right) {
            int[] pivot_bound = partition3(array, left, right);
            process3(array, left, pivot_bound[0] - 1);
            process3(array, pivot_bound[1] + 1, right);
        }
    }
    // 从左向右搜索 >......
    static public int[] partition3(int[] array, int left, int right) {
        int pivot = array[right];
        int lessR = left - 1;
        int moreL = right;
        int i = left;
        while (i < moreL) {
            if (array[i] < pivot) {         // 小值放到前面
                swap(array, ++lessR, i++);
            } else if (array[i] > pivot) {  // 大值放到后面，下次继续判断被交换的值是该往前放、往后放或跳过。
                swap(array, --moreL, i);
            } else {                        // 等于pivot，lessR不变，继续下一个值
              i++;
            }
        }
        swap(array, moreL, right);          // 第一个大于pivot的值和pivot交换位置，保证>=<三个区间。
        return new int[] {lessR + 1, moreL};
    }




    static public class Job {
        public int left;
        public int right;
        public Job(int l, int r) {
            left = l;
            right = r;
        }
    }

    static public void quickSortIterative(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        Stack<Job> stack = new Stack<>();
        stack.push(new Job(0, array.length - 1));
        while (!stack.isEmpty()) {
            Job job = stack.pop();
            int[] pivot_bound = partition3(array, job.left, job.right);
            if (pivot_bound[0] > job.left) {
                stack.push(new Job(job.left, pivot_bound[0] - 1));
            }
            if (pivot_bound[1] < job.right) {
                stack.push(new Job(pivot_bound[1] + 1, job.right));
            }
        }
    }
}
