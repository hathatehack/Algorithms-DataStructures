package C08_LinkedList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class C04_DoubleEndsQueue {
    // 双向节点
    static private class Node<T> {
        public Node<T> pre;
        public Node<T> next;
        public T value;

        public Node(T val) {
            value = val;
        }
    }
    // 双端队列
    static public class DoubleEndsQueue<T> {
        Node<T> head;
        Node<T> tail;

        public void addToHead(T value) {
            Node<T> cur = new Node<T>(value);
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                cur.next = head;
                head.pre = cur;
                head = cur;
            }
        }

        public void addToTail(T value) {
            Node<T> cur = new Node<T>(value);
            if (tail == null) {
                head = cur;
                tail = cur;
            } else {
                cur.pre = tail;
                tail.next = cur;
                tail = cur;
            }
        }

        public T popFromHead() {
            if (head == null) {
                return null;
            }
            Node<T> cur = head;
            if (head == tail) {
                head = tail = null;
            } else {
                head = head.next;
                head.pre = null;
                cur.next = null;
            }
            return cur.value;
        }

        public T popFromTail() {
            if (tail == null) {
                return null;
            }
            Node<T> cur = tail;
            if (head == tail) {
                head = tail = null;
            } else {
                tail = tail.pre;
                tail.next = null;
                cur.pre = null;
            }
            return cur.value;
        }

        public boolean isEmpty() {
            return head == null;
        }
    }

    // 双向链表实现栈
    static public class MyStack<T> {
        private DoubleEndsQueue<T> queue;

        public MyStack() {
            queue = new DoubleEndsQueue<T>();
        }

        public void push(T value) {
            queue.addToHead(value);
        }

        public T pop() {
            return queue.popFromHead();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // 双向链表实现队列
    static public class MyQueue<T> {
        private DoubleEndsQueue<T> queue;

        public MyQueue() {
            queue = new DoubleEndsQueue<T>();
        }

        public void push(T value) {
            queue.addToHead(value);
        }

        public T poll() {
            return queue.popFromTail();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }




    public static boolean isEqual(Integer o1, Integer o2) {
        if (o1 == null && o2 != null) {
            return false;
        }
        if (o1 != null && o2 == null) {
            return false;
        }
        if (o1 == null && o2 == null) {
            return true;
        }
        return o1.equals(o2);
    }

    static public void main(String[] args) {
        int testTimes = 100000;
        int value = 10000;
        for (int i = 0; i < testTimes; i++) {
            MyStack<Integer> myStack = new MyStack<>();
            MyQueue<Integer> myQueue = new MyQueue<>();
            Stack<Integer> stack = new Stack<>();
            Queue<Integer> queue = new LinkedList<>();
            int val;
            for (int tries = 100; tries > 0; tries--) {
                // 测试栈
                val = (int) (Math.random() * value);
                if (stack.isEmpty()) {
                    myStack.push(val);
                    stack.push(val);
                } else {
                    if (Math.random() < 0.5) {
                        myStack.push(val);
                        stack.push(val);
                    } else {
                        if (!isEqual(myStack.pop(), stack.pop())) {
                            System.out.println("oops!");
                        }
                    }
                }
                // 测试队列
                val = (int) (Math.random() * value);
                if (queue.isEmpty()) {
                    myQueue.push(val);
                    queue.offer(val);
                } else {
                    if (Math.random() < 0.5) {
                        myQueue.push(val);
                        queue.offer(val);
                    } else {
                        if (!isEqual(myQueue.poll(), queue.poll())) {
                            System.out.println("oops!");
                        }
                    }
                }
            }
        }
        System.out.println("finish!");
    }
}
