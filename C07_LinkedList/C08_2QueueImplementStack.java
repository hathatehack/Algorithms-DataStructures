package C07_LinkedList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class C08_2QueueImplementStack {
    static public class TwoQueueStack<T> {
        private Queue<T> queue;
        private Queue<T> help;

        public TwoQueueStack() {
            queue = new LinkedList<>();
            help = new LinkedList<>();
        }

        public void push(T value) {
            queue.offer(value);
        }

        public T pop() {
            // 把除最后一个元素的其他元素转移到另一队列，弹出该最后一个元素。
            while (queue.size() > 1) {
                help.offer(queue.poll());
            }
            T value = queue.poll();
            // queue is empty, and swap queue with help.
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return value;
        }

        public T peek() {
            T value = pop();
            queue.offer(value);
            return value;
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }


    public static void main(String[] args) {
        System.out.println("test begin");
        TwoQueueStack<Integer> myStack = new TwoQueueStack<>();
        Stack<Integer> test = new Stack<>();
        int testTime = 1000000;
        int max = 1000000;
        for (int i = 0; i < testTime; i++) {
            if (myStack.isEmpty()) {
                if (!test.isEmpty()) {
                    System.out.println("Oops");
                }
                int num = (int) (Math.random() * max);
                myStack.push(num);
                test.push(num);
            } else {
                if (Math.random() < 0.25) {
                    int num = (int) (Math.random() * max);
                    myStack.push(num);
                    test.push(num);
                } else if (Math.random() < 0.5) {
                    if (!myStack.peek().equals(test.peek())) {
                        System.out.println("Oops");
                    }
                } else if (Math.random() < 0.75) {
                    if (!myStack.pop().equals(test.pop())) {
                        System.out.println("Oops");
                    }
                } else {
                    if (myStack.isEmpty() != test.isEmpty()) {
                        System.out.println("Oops");
                    }
                }
            }
        }
        System.out.println("test finish!");
    }
}
