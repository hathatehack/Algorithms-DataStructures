package C07_LinkedList;

import C01_random.GenerateRandomArray;

import java.util.ArrayList;

public class C03_DeleteGivenValue {
    static public void main(String[] args) {
        int testTimes = 1000000;
        for (int i = 0; i < testTimes; i++) {
            Node head1 = generateRandomNode();
            Node head2 = copy(head1);
            int value = head1 != null ? head1.value : 0;
            Node ans1 = deleteGivenValue(head1, value);
            Node ans2 = deleteGivenValue2(head2, value);
            boolean ok = compare(ans1, ans2);
            if (!ok) {
                System.out.println("出错了！");
            }
        }
    }

    static class Node {
        public Node next = null;
        public int value;

        public Node(int value) {
            this.value = value;
        }
    }

    static public Node deleteGivenValue(Node head, int value) {
        // 过滤掉开头连续等于value的节点
        while (head != null) {
            if (head.value != value) {
                break;
            }
            head = head.next;
        }
        // 剩下的节点依次判断，存在节点等于value则剔除并对其前后节点建立连接
        Node pre = null;
        Node cur = head;
        while (cur != null) {
            if (cur.value == value) {
                pre.next = cur.next;
            } else {
                pre = cur;
            }
            cur = cur.next;
        }
        return head;
    }



    // checker
    static public Node deleteGivenValue2(Node head, int value) {
         ArrayList<Integer> list = new ArrayList<>();
         for (; head != null; head = head.next) {
             if (head.value != value) {
                 list.add(head.value);
             }
         }
         Node h = null;
         if (!list.isEmpty()) {
             h = new Node(list.get(0));
         }
         Node pre = h;
         for (int i = 1; i < list.size(); i++) {
             Node n = new Node(list.get(i));
             pre.next = n;
             pre = n;
         }
         return h;
    }





    static public Node generateRandomNode() {
        int[] array = GenerateRandomArray.generateRandomArray(10, 1);
        Node head = null;
        if (array.length == 0) {
            return head;
        } else {
            head = new Node(array[0]);
        }
        Node pre = head;
        for (int i = 1; i < array.length; i++) {
            Node node = new Node(array[i]);;
            pre.next = node;
            pre = node;
        }

        return head;
    }

    static public Node copy(Node head) {
        Node old = head;
        Node h = null;
        if (head != null) {
            h = new Node(head.value);
            head = head.next;
        }
        Node pre = h;
        while (head != null) {
            Node n = new Node(head.value);
            pre.next = n;
            pre = n;
            head = head.next;
        }
        return h;
    }

    static public boolean compare(Node head1, Node head2) {
        while (head1 != null) {
            if (head1.value == head2.value) {
                head1 = head1.next;
                head2 = head2.next;
            } else {
                return false;
            }
        }
        if (head1 != head2) {
            return false;
        }
        return true;
    }
}
