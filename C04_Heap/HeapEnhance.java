package C04_Heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// 堆是一种CBT/完全二叉树。堆是弱序的（只保证根到叶的每一条路径内有序），不支持直接按序遍历，但可以快速移除根节点、插入新节点。
// 默认小根堆
public class HeapEnhance<T> {
    private ArrayList<T> heap = new ArrayList<>();
    private HashMap<T, Integer> indexMap = new HashMap<>();  // T如果是基础类型，会发生覆盖！
    private int heapSize = 0;
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
        heapInsert(heapSize++);
    }

    public T pop() {
        T obj = heap.get(0);
        swap(0, --heapSize);
        indexMap.remove(obj);
        heap.remove(heapSize);
        heapify(0);
        return obj;

    }

    public T peek() {
        return heap.get(0);
    }

    public boolean contains(T obj) {
        return indexMap.containsKey(obj);
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
            resign(index);
        }
        return true;
    }

    public T get(int index) {
        return heap.get(index);
    }

    public List<T> getAllElements() {
        List<T> list = new ArrayList<>();
        for (T o : heap) {
            list.add(o);
        }
        return list;
    }

    public void resign(T obj) {
        resign(indexMap.get(obj));
    }

    private void resign(int index) {
        heapInsert(index);
        heapify(index);
    }

    private void heapInsert(int index) {
        // 比父节点大则停、移动到0位置则停
        while (comp.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {  // index-1为负数时不能>>1只能/2
            swap(index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    private void heapify(int index) {
        int left = (index << 1) + 1;
        while (left < heapSize) {  // 先判断是否存在左孩子，后面得再判断是否存在右孩子!
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

    private void swap(int i, int j) {
        T o1 = heap.get(i);
        T o2 = heap.get(j);
        heap.set(i, o2);
        heap.set(j, o1);
        indexMap.put(o2, i);
        indexMap.put(o1, j);
    }


    public class Inner<T> {
        public T value;

        public Inner(T v) {
            value = v;
        }
    }
}
