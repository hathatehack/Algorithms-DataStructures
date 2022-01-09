package C07_LinkedList;

import C07_LinkedList.C11_FindLoopNode.Node;

import java.util.HashSet;

public class C12_FindIntersectNode {
    // 使用指针。
    // 给定两个可能有环也可能无环的单链表头节点head1和head2，如果两个链表相交，返回相交的第一个节点。
    static public Node findIntersectNode(Node head1, Node head2) {
        if (head1 == null || head2 == null || head1 == head2) {
            return head1;
        }
        Node loop1 = C11_FindLoopNode.findLoopNode(head1);
        Node loop2 = C11_FindLoopNode.findLoopNode(head2);
        if (loop1 == null && loop2 == null) {
            return findIntersectNodeBothNoLoop(head1, head2);
        }
        if (loop1 !=null && loop2 != null) {
            return findIntersectNodeBothLoop(head1, loop1, head2, loop2);
        }
        // 单链表一个有环，一个无环，肯定不相交！
        return null;
    }
    // 两个单链表都是无环的，找第一个相交节点。
    static private Node findIntersectNodeBothNoLoop(Node head1, Node head2) {
        Node cur1 = head1;
        Node cur2 = head2;
        int n = 0;
        // 两个单链表的尾节点不是同一个，说明肯定无相交节点，返回null。
        for (; cur1.next != null; cur1 = cur1.next, n++) {}
        for (; cur2.next != null; cur2 = cur2.next, n--) {}
        if (cur1 != cur2)
            return null;
        // 两个单链表的尾节点是同一个，那就从头开始找出第一个相交节点。
        for (cur1 = head1; n > 0; cur1 = cur1.next, n--) {}  // 二者取短者，跳过多余的头部节点
        for (cur2 = head2; n < 0; cur2 = cur2.next, n++) {}  // 二者取短者，跳过多余的头部节点
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
    }
    // 两个单链表都是有环的，找第一个相交节点。
    static private Node findIntersectNodeBothLoop(Node head1, Node loop1, Node head2, Node loop2) {
        // 有3中情况：两个不同的环、入环节点是同一个环的同一节点>O、入环节点是同一个环的不同节点=O
        // 入环节点是同一个环的同一节点>O
        if (loop1 == loop2) {
            Node cur1 = head1;
            Node cur2 = head2;
            int n = 0;
            for (; cur1.next != loop1; cur1 = cur1.next, n++) {}
            for (; cur2.next != loop2; cur2 = cur2.next, n--) {}
            for (cur1 = head1; n > 0; cur1 = cur1.next, n--) {}  // 二者取短者，跳过多余的头部节点
            for (cur2 = head2; n < 0; cur2 = cur2.next, n++) {}  // 二者取短者，跳过多余的头部节点
            while (cur1 != cur2) {
                cur1 = cur2.next;
                cur2 = cur2.next;
            }
            return cur1;
        } else { // 判断是否入环节点是同一个环的不同节点=O
            for (Node cur = loop1.next; cur != loop1; cur = cur.next) {
                if (cur == loop2)
                    return loop1;
            }
        }
        // 两个不同的环
        return null;
    }






    // 使用容器。
    // 给定两个可能有环也可能无环的单链表头节点head1和head2，如果两个链表相交，返回相交的第一个节点。
    static public Node findIntersectNode2(Node head1, Node head2) {
        if (head1 == null || head2 == null || head1 == head2) {
            return head1;
        }
        Node loop1 = C11_FindLoopNode.findLoopNode(head1);
        Node loop2 = C11_FindLoopNode.findLoopNode(head2);
        if (loop1 == null && loop2 == null) {
            return findIntersectNodeBothNoLoop2(head1, head2);
        }
        if (loop1 !=null && loop2 != null) {
            return findIntersectNodeBothLoop2(head1, loop1, head2, loop2);
        }
        // 单链表一个有环，一个无环，肯定不相交！
        return null;
    }
    // 两个单链表都是无环的，找第一个相交节点。
    static private Node findIntersectNodeBothNoLoop2(Node head1, Node head2) {
        HashSet<Node> set = new HashSet<>();
        // 第一个单链表全部放进set
        for (Node cur = head1; cur != null; cur = cur.next) {
            set.add(cur);
        }
        // 查看set中是否已存在第二个单链表节点，如果有则该节点即是第一个相交节点。
        for (Node cur = head2; cur != null; cur = cur.next) {
            if (set.contains(cur))
                return cur;
        }
        return null;
    }
    // 两个单链表都是有环的，找第一个相交节点。
    static private Node findIntersectNodeBothLoop2(Node head1, Node loop1, Node head2, Node loop2) {
        // 有3中情况：两个不同的环、入环节点是同一个环的同一节点>O、入环节点是同一个环的不同节点=O
        // 入环节点是同一个环的同一节点>O
        if (loop1 == loop2) {
            HashSet<Node> set = new HashSet<>();
            for (Node cur = head1; cur != loop1.next; cur = cur.next) {
                set.add(cur);
            }
            for (Node cur = head2; cur != loop2.next; cur = cur.next) {
                if (set.contains(cur))
                    return cur;
            }
        } else { // 判断是否入环节点是同一个环的不同节点=O
            for (Node cur = loop1.next; cur != loop1; cur = cur.next) {
                if (cur == loop2)
                    return loop1;
            }
        }
        // 两个不同的环
        return null;
    }





    public static void main(String[] args) {
        // test1 无环、无相交
        Node head1 = new Node(0);
        head1.next = new Node(1);
        Node head2 = new Node(0);
        head2.next = new Node(1);
        System.out.printf("test1 无环、无相交: %s\n", findIntersectNode(head1, head2));
        System.out.printf("test1 无环、无相交: %s\n\n", findIntersectNode2(head1, head2));
        // test2 无环、相交
        head1.next.next = new Node(2);
        head2.next.next = head1.next.next;
        System.out.printf("test2 无环、相交: %s\n", findIntersectNode(head1, head2).value);
        System.out.printf("test2 无环、相交: %s\n\n", findIntersectNode2(head1, head2).value);
        // test3 同一个环、入环节点是同一个
        head1.next.next.next = head1.next;
        head2.next = head1.next;
        System.out.printf("test3 同一个环、入环节点是同一个: %s\n", findIntersectNode(head1, head2).value);
        System.out.printf("test3 同一个环、入环节点是同一个: %s\n\n", findIntersectNode2(head1, head2).value);
        // test4 同一个环、入环节点不是同一个
        head2.next = head1.next.next;
        System.out.printf("test4 同一个环、入环节点不是同一个: %s\n", findIntersectNode(head1, head2).value);
        System.out.printf("test4 同一个环、入环节点不是同一个: %s\n\n", findIntersectNode2(head1, head2).value);
        // test5 两个不同环
        head1.next = head1;
        head2.next = head2;
        System.out.printf("test5 两个不同环: %s\n", findIntersectNode(head1, head2));
        System.out.printf("test5 两个不同环: %s\n\n", findIntersectNode2(head1, head2));
    }
}
