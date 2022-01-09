package C13_Recurse;

import java.util.Stack;

public class C04_ReverseStackByRecurse {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Reverse...");
        reverseStack(stack);
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }


    // 栈反转，不使用额外数据结构，只使用递归。
    // 整体思想：取出当前栈底元素，反转剩余栈，压入栈底元素到栈顶。
    public static void reverseStack(Stack<Integer> stack) {
        if (!stack.isEmpty()) {
            int bottom = popStackBottom(stack);
            reverseStack(stack);
            stack.push(bottom);
        }
    }
    // 返回移除的栈底元素，剩余元素保持原样。
    // 整体思想：假设栈里两个元素，先弹出当前栈顶元素，在弹出栈底元素，压回当前元素。
    private static int popStackBottom(Stack<Integer> stack) {
        int pop = stack.pop();
        if (stack.isEmpty()) {
            return pop;
        }
        int bottom = popStackBottom(stack);
        stack.push(pop);
        return bottom;
    }
}
