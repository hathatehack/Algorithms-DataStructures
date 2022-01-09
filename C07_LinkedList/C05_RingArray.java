package C07_LinkedList;

public class C05_RingArray {
    static public class MyQueue {
        private int[] array;
        private int size;
        private int pushi;
        private int popi;
        private final int limit;

        public MyQueue(int limit) {
            array = new int[limit];
            size = 0;
            pushi = popi = 0;
            this.limit = limit;
        }

        public void push(int value) {
            if (size == limit) {
                throw new RuntimeException("队列已满，添加失败！");
            }
            size++;
            array[pushi] = value;
            pushi = nextIndex(pushi);
        }

        public int pop() {
            if (size == limit) {
                throw new RuntimeException("队列已空，获取失败！");
            }
            size--;
            int value = array[popi];
            popi = nextIndex(popi);
            return value;
        }

        private int nextIndex(int i) {
            return (i < (limit - 1)) ? i + 1 : 0;
        }
    }
}
