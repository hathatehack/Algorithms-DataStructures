package C08_LinkedList;

import java.util.Stack;
import C08_LinkedList.C09_LinkedListMid.Node;

public class C10_IsPalindrome {
    // 使用指针+反转。only O(1) extra space
    static public boolean isPalindrome(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        // 将右部链表反转
        Node right = reverseList(C09_LinkedListMid.midOrUpMidNode(head));  // 奇数中点，偶数上中点
        // 回文比较
        Node head1 = right;
        boolean isPalindrome = true;
        while (head != null && head1 != null) {
            if (head.value != head1.value) {
                isPalindrome = false;
                break;
            }
            head = head.next;  // left to mid
            head1 = head1.next;// right to mid
        }
        // 恢复链表
        reverseList(right);
        return isPalindrome;
    }
    static private Node reverseList(Node head) {
        Node pre = null;
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }


    // 使用指针+容器。need O(N/2) extra space
    static public boolean isPalindrome2(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        Node right = C09_LinkedListMid.midOrUpMidNode(head).next;  // 奇数中点，偶数上中点，该中点的所有后节点依次压栈后与head开始比对。
        Stack<Node> stack = new Stack<>();
        for (; right != null; right = right.next) {
            stack.push(right);
        }
        for (; !stack.isEmpty(); head = head.next) {
            if (head.value != stack.pop().value)
                return false;
        }
        return true;
    }


    // 完全使用容器。need O(N) extra space
    static public boolean isPalindrome3(Node head) {
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        for (; cur != null; cur = cur.next) {
            stack.push(cur);
        }
        for (; head != null; head = head.next) {
            if (head.value != stack.pop().value)
                return false;
        }
        return true;
    }






    public static void printLinkedList(Node node) {
        System.out.print("Linked List: ");
        while (node != null) {
            System.out.print(node.value + " ");
            node = node.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = null;
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");

        head = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(2);
        head.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");

        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(2);
        head.next.next.next.next = new Node(1);
        printLinkedList(head);
        System.out.print(isPalindrome(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        System.out.println("=========================");
    }
}
