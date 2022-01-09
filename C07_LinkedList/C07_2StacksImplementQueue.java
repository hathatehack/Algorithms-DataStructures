package C07_LinkedList;

import java.util.Stack;

public class C07_2StacksImplementQueue {
    public static void main(String[] args) {
        TwoStacksQueue test = new TwoStacksQueue();
        test.add(1);
        test.add(2);
        test.add(3);
        System.out.println(test.peek());
        System.out.println(test.poll());
        System.out.println(test.peek());
        System.out.println(test.poll());
        System.out.println(test.peek());
        System.out.println(test.poll());
        
        System.out.println(test.poll());
    }

    static public class TwoStacksQueue {
        private Stack<Integer> stackPush;
        private Stack<Integer> stackPop;

        public TwoStacksQueue() {
            stackPush = new Stack<>();
            stackPop = new Stack<>();
        }

        private void stackPushToStackPop() {
            // stackPush向stackPop倒放元素，且只有当stackPop为空才能执行此操作！
            if (stackPop.isEmpty()) {
                while (!stackPush.isEmpty()) {
                    stackPop.push(stackPush.pop());
                }
            }
        }

        public void add(int value) {
            stackPush.push(value);
            stackPushToStackPop();
        }

        public int poll() {
            stackPushToStackPop();
            return stackPop.pop();
        }

        public int peek() {
            stackPushToStackPop();
            return stackPop.peek();
        }
    }
}
