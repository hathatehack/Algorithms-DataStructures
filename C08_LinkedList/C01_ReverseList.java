package C08_LinkedList;

public class C01_ReverseList {
    static public void main(String[] args) {
        testReverseList();
        testReverseDoubleList();
    }

    // 单向链表
    static class Node {
        public Node next = null;
        public Object value = null;

        public Node(Object value) {
            this.value = value;
        }
    }

    static public Node reverseList(Node head) {
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

    static public void testReverseList() {
        Node n1 = new Node(1);
        n1.next = new Node(2);
        n1.next.next = new Node(3);

        Node r1 = reverseList(n1);
        while (r1 != null) {
            System.out.println(r1.value);
            r1 = r1.next;
        }
    }






    // 双向链表
    static class DoubleNode {
        public DoubleNode pre = null;
        public DoubleNode next = null;
        public Object value = null;

        public DoubleNode(Object value) {
            this.value = value;
        }
    }

    static public DoubleNode reverseDoubleList(DoubleNode head) {
        DoubleNode pre = null;
        DoubleNode next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            head.pre = next;
            pre = head;
            head = next;
        }

        return pre;
    }

    static public void testReverseDoubleList() {
        DoubleNode n1 = new DoubleNode(1);
        DoubleNode n2 = new DoubleNode(2);
        DoubleNode n3 = new DoubleNode(3);
        n1.next = n2;
        n2.pre = n1;
        n2.next = n3;
        n3.pre = n2;

        DoubleNode r1 = reverseDoubleList(n1);
        while (r1 != null) {
            System.out.format("%s   %4s  %4s\n", r1.value, r1.pre!=null ? r1.pre.value : null, r1.next!=null ? r1.next.value : null);
            r1 = r1.next;
        }
    }
}
