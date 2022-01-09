package C07_LinkedList;

import java.util.Stack;

public class C06_GetMinStack {
    public static void main(String[] args) {
        MyStack1 stack1 = new MyStack1();
        stack1.push(3);
        System.out.println(stack1.getMin());
        stack1.push(4);
        System.out.println(stack1.getMin());
        stack1.push(1);
        System.out.println(stack1.getMin());
        System.out.println(stack1.pop());
        System.out.println(stack1.getMin());

        System.out.println("=============");

        MyStack2 stack2 = new MyStack2();
        stack2.push(3);
        System.out.println(stack2.getMin());
        stack2.push(4);
        System.out.println(stack2.getMin());
        stack2.push(1);
        System.out.println(stack2.getMin());
        System.out.println(stack2.pop());
        System.out.println(stack2.getMin());
    }

    //实现一个特殊的栈，在基本功能的基础上，再实现返回栈中最小元素的功能
    //1）pop、push、getMin操作的时间复杂度都是 O(1)。
    //2）设计的栈类型可以使用现成的栈结构。
    static public class MyStack1 {
        private Stack<Integer> stackData;  // 保存所有数据
        private Stack<Integer> stackMin;  // 记录最小数据，stackMin长度可能比stackData小。

        public MyStack1() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        public void push(Integer value) {
            // 保存value
            stackData.push(value);
            // 如果value是新的最小值，才记录当前栈最小值
            if (stackMin.isEmpty() || value <= getMin()) {
                stackMin.push(value);
            }
        }

        public Integer pop() {
            if (!stackData.isEmpty()) {
                Integer value = stackData.pop();
                if (value == getMin()) {
                    stackMin.pop();
                }
                return value;
            }
            throw new RuntimeException("Stack is empty!");
        }

        public Integer getMin() {
            if (!stackMin.isEmpty()) {
                return stackMin.peek();
            }
            throw new RuntimeException("Stack is empty!");
        }
    }


    static public class MyStack2 {
        private Stack<Integer> stackData;  // 保存所有数据
        private Stack<Integer> stackMin;  // 记录最小数据，每保存一个data就记录一个min。

        public MyStack2() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        public void push(Integer value) {
            // 保存value
            stackData.push(value);
            // 记录当前栈最小值
            if (stackMin.isEmpty()) {
                stackMin.push(value);
            } else if (value < getMin()) {
                stackMin.push(value);
            } else {
                stackMin.push(getMin());
            }
        }

        public Integer pop() {
            if (!stackData.isEmpty()) {
                stackMin.pop();
                return stackData.pop();
            }
            throw new RuntimeException("Stack is empty!");
        }

        public Integer getMin() {
            if (!stackMin.isEmpty()) {
                return stackMin.peek();
            }
            throw new RuntimeException("Stack is empty!");
        }
    }
}
