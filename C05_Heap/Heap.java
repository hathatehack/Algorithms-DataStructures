package C05_Heap;

import java.util.PriorityQueue;

// 堆是一种CBT/完全二叉树。堆是弱序的（只保证根到叶的每一条路径内有序），不支持直接按序遍历，但可以快速移除根节点、插入新节点。
public class Heap {
    static public void main(String[] args) {
        int testTimes = 100;
        int limit = 100;
        int maxValue = 100;
        for (int i = 0; i < testTimes; i++) {
            int curLimit = (int) (Math.random() * limit) + 1;
            // 小根堆
            MinHeap minHeap1 = new MinHeap(curLimit);
            PriorityQueue<Integer> minHeap2 = new PriorityQueue<>();
            // 大根堆
            MaxHeap maxHeap1 = new MaxHeap(curLimit);
            PriorityQueue<Integer> maxHeap2 = new PriorityQueue<>((o1, o2) -> o2 - o1);

            _Heap heap1 = minHeap1;
            PriorityQueue<Integer> heap2 = minHeap2;
//            _Heap heap1 = maxHeap1;
//            PriorityQueue<Integer> heap2 = maxHeap2;
            int operateTimes = (int)(Math.random() * limit);
            for (int j = 0; j < operateTimes; j++) {
                if (heap1.isEmpty() != heap2.isEmpty()) {
                    System.out.println("Oops!");
                    return;
                }
                if (heap1.isEmpty()) {
                    int value = (int)(Math.random() * maxValue);
                    heap1.push(value);
                    heap2.add(value);
                } else if (heap1.isFull()) {
                    if (heap1.pop() != heap2.poll()) {
                        System.out.println("Oops!");
                        return;
                    }
                } else {
                    if (Math.random() < 0.5) {
                        int value = (int)(Math.random() * maxValue);
                        heap1.push(value);
                        heap2.add(value);
                    } else {
                        if (heap1.pop() != heap2.poll()) {
                            System.out.println("Oops!");
                            return;
                        }
                    }
                }
            }
        }
    }

    static public abstract class _Heap {
        protected int[] heap;
        protected final int limit;
        protected int heapSize;

        public _Heap(int limit) {
            heap = new int[limit];
            this.limit = limit;
            heapSize = 0;
        }

        public boolean isEmpty() {
            return heapSize == 0;
        }

        public boolean isFull() {
            return heapSize == limit;
        }

        public void push(int value) {
            heap[heapSize] = value;
            heapInsert(heapSize++);
        }

        public int pop() {
            int root = heap[0];
            swap(0, --heapSize);
            heapify(0);
            return root;
        }

        public int peek() {
            return heap[0];
        }

        protected void heapInsert(int index) {
            throw new RuntimeException("heapInsert() doesn't be implemented!");
        }

        protected void heapify(int index) {
            throw new RuntimeException("heapify() doesn't be implemented!");
        }

        protected void swap(int i, int j) {
            int tmp = heap[i];
            heap[i] = heap[j];
            heap[j] = tmp;
        }
    }


    // 小根堆
    static public class MinHeap extends _Heap {
        public MinHeap(int limit) {
            super(limit);
        }

        // 从尾向头遍历
        protected void heapInsert(int index) {
            // 比父节点大则停、移动到0位置则停
            while (heap[index] < heap[(index - 1) / 2]) {  // index-1为负数时不能>>1只能/2
                swap(index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

        // 从头向尾遍历
        protected void heapify(int index) {
            int left = (index << 1) + 1;
            while (left < heapSize) {  // 先判断是否存在左孩子，后面得再判断是否存在右孩子!
                int least = (left + 1 < heapSize) && (heap[left + 1] < heap[left]) ? left + 1 : left;
                least = heap[least] < heap[index] ? least : index;
                if (least == index) {
                    break;
                }
                swap(least, index);
                index = least;
                left = (index << 1) + 1;
            }
        }
    }



    // 大根堆
    static public class MaxHeap extends _Heap {
        public MaxHeap(int limit) {
            super(limit);
        }

        // 从尾向头遍历
        protected void heapInsert(int index) {
            // 比父节点小则停、移动到0位置则停
            while (heap[index] > heap[(index - 1) / 2]) {  // index-1为负数时不能>>1只能/2
                swap(index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

        // 从头向尾遍历
        protected void heapify(int index) {
            int left = (index << 1) + 1;
            while (left < heapSize) {  // 先判断是否存在左孩子，后面得再判断是否存在右孩子!
                int largest = (left + 1 < heapSize) && (heap[left + 1] > heap[left]) ? left + 1 : left;
                largest = heap[largest] > heap[index] ? largest : index;
                if (largest == index) {
                    break;
                }
                swap(index, largest);
                index = largest;
                left = (index << 1) + 1;
            }
        }
    }
}
