package C07_LinkedList;

import java.util.HashMap;

public class C14_CopyListWithRandomPointer {
    static public class Node {
        public int value;
        public Node next;
        public Node randomNode;  // randomNode可能指向链表中的任意一个节点，也可能指向null
        public Node(int val) { value = val;}
    }

    // https://leetcode.com/problems/copy-list-with-random-pointer
    // rand指针是单链表节点结构中新增的指针，rand可能指向链表中的任意一个节点，也可能指向null。
    // 给定一个由Node节点类型组成的无环单链表的头节点 head，请实现一个函数完成这个链表的复制，并返回复制的新链表的头节点。
    // 使用指针。时间复杂度O(N)，额外空间复杂度O(1)
    static public Node copyList(Node head) {
        if (head == null) return null;
        // 生成全部新节点
        // 1 -> 1' -> 2 -> 2' -> 3 -> 3' -> ... -> null
        Node cur = head;
        Node next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = new Node(cur.value);
            cur.next.next = next;
            cur = next;
        }
        // 设置新节点的randomNode
        Node newNode = null;
        cur = head;
        while (cur != null) {
            newNode = cur.next;
            newNode.randomNode = cur.randomNode != null ? cur.randomNode.next : null;
            cur = cur.next.next;
        }
        // 调整新老节点的next
        Node newHead = head.next;
        cur = head;
        while (cur != null) {
            next = cur.next.next;
            newNode = cur.next;
            cur.next = next;
            newNode.next = next != null ? next.next : null;
            cur = next;
        }
        return newHead;
    }


    // 使用容器。时间复杂度O(N)，额外空间复杂度O(N)
    static public Node copyList2(Node head) {
        if (head == null) return null;
        HashMap<Node, Node> map = new HashMap<>();  // key=old, value=new
        // 生成全部新节点
        Node cur = head;
        for (; cur != null; cur = cur.next) {
            map.put(cur, new Node(cur.value));
        }
        // 设置新节点的next、randomNode
        cur = head;
        for (; cur != null; cur = cur.next) {
            Node newNode = map.get(cur);
            newNode.next = map.get(cur.next);
            newNode.randomNode = map.get(cur.randomNode);
        }
        return map.get(head);
    }





    public static void printLinkedList(Node head) {
        Node node = head;
        System.out.print("Linked List: ");
        while (node != null) {
            System.out.print(node.value + " ");
            node = node.next;
        }
        node = head;
        System.out.print(",  ");
        while (node != null) {
            System.out.print(node.toString() + " ");
            node = node.next;
        }
        System.out.println();
    }
    public static void main(String[] args) {
        Node head1 = new Node(0);
        head1.next = new Node(1);
        head1.next.next = new Node(2);
        head1.next.next.randomNode = head1.next;

        Node ans1 = copyList(head1);
        Node ans2 = copyList2(head1);

        printLinkedList(head1);
        printLinkedList(ans1);
        printLinkedList(ans2);
        printLinkedList(head1);
    }
}
