package C04_Sort;

import C01_random.GenerateRandomArray;
import java.util.Stack;

public class quickSort {
    static public void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 30;
        int maxValue = 50;
        for (int i = 0; i < testTimes; i++) {
            int[] array = GenerateRandomArray.generateRandomArray(maxSize, maxValue);
            int[] ans1 = GenerateRandomArray.copyArray(array);
            quickSortRecursive1(ans1);
            int[] ans2 = GenerateRandomArray.copyArray(array);
            quickSortRecursive2(ans2);
            int[] ans3 = GenerateRandomArray.copyArray(array);
            quickSortRecursive3(ans3);
            int[] ans3_1 = GenerateRandomArray.copyArray(array);
            quickSortRecursive3_1(ans3_1);
            int[] ans4 = GenerateRandomArray.copyArray(array);
            quickSortIterative(ans4);
            if (!GenerateRandomArray.isEqual(ans1, ans2) || !GenerateRandomArray.isEqual(ans1, ans3) &&
                    !GenerateRandomArray.isEqual(ans1, ans3_1) || !GenerateRandomArray.isEqual(ans1, ans4)) {
                System.out.printf("Oops! i=%d\n", i);
                System.out.print("array: "); GenerateRandomArray.printArray(array);
                System.out.print("ans1: "); GenerateRandomArray.printArray(ans1);
                System.out.print("ans2: "); GenerateRandomArray.printArray(ans2);
                System.out.print("ans3: "); GenerateRandomArray.printArray(ans3);
                System.out.print("ans3_1: "); GenerateRandomArray.printArray(ans3_1);
                System.out.print("ans4: "); GenerateRandomArray.printArray(ans4);
            }
        }
    }

    static public void quickSortRecursive1(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        process1(array, 0, array.length - 1);
    }
    static private void process1(int[] array, int left, int right) {
        if (left < right) {
            int pivot_idx = partition1(array, left, right);
            process1(array, left, pivot_idx - 1);
            process1(array, pivot_idx + 1, right);
        }
    }
    // 左右同时相向搜索逼近中轴 >.....< ，中轴值为array[right]
    static private int partition1(int[] array, int left, int right) {
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
    static private void swap(int[] array, int i, int j) {
        if (i == j)
            return;
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
    static private void process2(int[] array, int left, int right) {
        if (right - left >= 3) {
            int pivot_idx = partition2(array, left, right);
            process2(array, left, pivot_idx - 1);
            process2(array, pivot_idx + 1, right);
        } else {
            // 小于3个数直接快排
            for (int i = left + 1; i <= right; i++) {
                for (int j = i - 1; j >= left && array[j] > array[j + 1]; j--) {
                    swap(array, j, j + 1);
                }
            }
        }
    }
    static private int partition2(int[] array, int left, int right) {
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
        /*第一个不小于pivot的值和末尾的pivot交换位置*/
        swap(array, L, right-1);
        return L;
    }
    // 三数中值法选基准
    static private int medianPivot(int[] array, int left, int right) {
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
    static private void process3(int[] array, int left, int right) {
        if (left < right) {
            int[] pivot_bound = partition3(array, left, right);
            process3(array, left, pivot_bound[0] - 1);
            process3(array, pivot_bound[1] + 1, right);
        }
    }
    // 从左向右搜索至中轴 >...... ，中轴值为array[right]
    static private int[] partition3(int[] array, int left, int right) {
        int pivot = array[right];
        int lessR = left - 1;  // 小于pivot且最右的值的位置
        int greaterL = right;  // 大于pivot且最左的值的位置
        int i = left;
        while (i < greaterL) {
            if (array[i] < pivot) {         // 小值放到pivot区前面
                swap(array, ++lessR, i++);
            } else if (array[i] > pivot) {  // 大值放到pivot区后面，i不变下次继续判断被交换到前面的值大小。
                swap(array, --greaterL, i);
            } else {                        // 等于pivot，lessR不变，继续处理下一个值
              i++;
            }
        }
        swap(array, greaterL, right);  // 第一个大于pivot的值和末尾pivot交换位置，保证<=>三个区间。
        return new int[] {lessR + 1, greaterL};
    }


    static public void quickSortRecursive3_1(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        process3_1(array, 0, array.length - 1);
    }
    static private void process3_1(int[] array, int left, int right) {
        if (left < right) {
            int[] pivotBound = partition3_1(array, left, right);
            process3_1(array, left, pivotBound[0] - 1);
            process3_1(array, pivotBound[1] + 1, right);
        }
    }
    // 从左向右搜索至中轴 >...... ，中轴值随机选array[left, right]
    static private int[] partition3_1(int[] array, int left, int right) {
        int pivot = array[left + (int)(Math.random() * (right - left + 1))];
        int lessR = left - 1;      // 小于pivot且最右的值的位置
        int greaterL = right + 1;  // 大于pivot且最左的值的位置
        int i = left;
        while (i < greaterL) {
            if (array[i] < pivot) {         // 小值放到pivot区前面
                swap(array, ++lessR, i++);
            } else if (array[i] > pivot) {  // 大值放到pivot区后面，i不变下次继续判断被交换到前面的值大小。
                swap(array, --greaterL, i);
            } else {                        // 等于pivot，lessR不变，继续处理下一个值
                i++;
            }
        }
        return new int[] {lessR + 1, greaterL - 1};
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
