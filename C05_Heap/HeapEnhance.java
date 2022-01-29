package C05_Heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

// 堆是一种CBT/完全二叉树。堆是弱序的（只保证根到叶的每一条路径内有序），不支持直接按序遍历，但可以快速移除根节点、尾插新节点。
// 加强堆除了支持push、pop、peek操作，还支持删除、更新操作。默认为小根堆。
public class HeapEnhance<T> {
    private ArrayList<T> heap = new ArrayList<>();  // 存放堆元素的结构
    private HashMap<T, Integer> indexMap = new HashMap<>();  // 元素在堆中的位置。T如果是基础类型，会发生覆盖！
    private int heapSize = 0;  // 已添加多少堆元素
    private Comparator<? super T> comp;

    public HeapEnhance(Comparator<T> c) {
        comp = c;
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }

    public int size() {
        return heapSize;
    }

    public void push(T obj) {
        heap.add(obj);
        indexMap.put(obj, heapSize);
        insertHeapify(heapSize++);
    }

    public T pop() {
        T root = heap.get(0);
        swap(0, --heapSize);
        indexMap.remove(root);
        heap.remove(heapSize);
        heapify(0);
        return root;

    }

    public T peek() {
        return heap.get(0);
    }

    public boolean contains(T obj) {
        return indexMap.containsKey(obj);
    }

    // 更新堆元素的属性
    public void update(T obj, Consumer<? super T> action) {
        if (indexMap.containsKey(obj)) {
            action.accept(obj);
            reHeapify(obj);
        }
    }

    public boolean remove(T obj) {
        if (!indexMap.containsKey(obj)) {
            return false;
        }
        int index = indexMap.get(obj);
        indexMap.remove(obj);
        T replace = heap.remove(--heapSize);  // 末尾元素
        // obj是末尾元素直接删除即可
        if (replace != obj) {
            heap.set(index, replace);
            indexMap.put(replace, index);
            reHeapify(index);
        }
        return true;
    }

    public List<T> getAllElements() {
        List<T> list = new ArrayList<>();
        for (T o : heap) {
            list.add(o);
        }
        return list;
    }

    public void reHeapify(T obj) {
        reHeapify(indexMap.get(obj));
    }

    private void reHeapify(int index) {
        insertHeapify(index);
        heapify(index);
    }

    private void insertHeapify(int index) {
        // 比父节点大则停、移动到0位置则停
        while (comp.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {  // index-1为负数时不能>>1只能/2
            swap(index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    private void heapify(int index) {
        int left = (index << 1) + 1;
        while (left < heapSize) {  // 先判断是否存在左孩子，再判断是否存在右孩子！再在头左右节点选出最小节点
            int least = (left + 1 < heapSize) && comp.compare(heap.get(left + 1), heap.get(left)) < 0 ? left + 1 : left;
            least = comp.compare(heap.get(least), heap.get(index)) < 0 ? least : index;
            if (least == index) {
                break;
            }
            swap(least, index);
            index = least;
            left = (index << 1) + 1;
        }
    }

    private void swap(int i1, int i2) {
        T o1 = heap.get(i1);
        T o2 = heap.get(i2);
        // 交换两个堆元素
        heap.set(i1, o2);
        heap.set(i2, o1);
        // 更新位置索引
        indexMap.put(o2, i1);
        indexMap.put(o1, i2);
    }


    public class Inner<T> {
        public T value;

        public Inner(T v) {
            value = v;
        }
    }
}
