package C08_LinkedList;

public class C02_MergeTwoSortedLinkedList {
    static public void main(String[] args) {
        ListNode n1 = new ListNode(1);
        n1.next = new ListNode(3);
        n1.next.next = new ListNode(5);

        ListNode n2 = new ListNode(2);
        n2.next = new ListNode(4);
        n2.next.next = new ListNode(6);

        ListNode m1 = mergeTwoList(n1, n2);
        while (m1 != null) {
            System.out.println(m1.value);
            m1 = m1.next;
        }
    }

    static class ListNode {
        public ListNode next = null;
        public int value = Integer.MIN_VALUE;

        public ListNode(int value) {
            this.value = value;
        }
    }

    static public ListNode mergeTwoList(ListNode head1, ListNode head2) {
        if (head1 == null || head2 == null) {
            return head1 == null ? head2 : head1;
        }

        ListNode head = head1.value <= head2.value ? head1 : head2;
        ListNode cur1 = head.next;
        ListNode cur2 = head == head1 ? head2 : head1;
        ListNode pre = head;
        while (cur1 != null && cur2 != null) {
            if (cur1.value <= cur2.value) {
                pre.next = cur1;
                cur1 = cur1.next;
            } else {
                pre.next = cur2;
                cur2 = cur2.next;
            }
            pre = pre.next;
        }
        pre.next = cur1 != null ? cur1 : cur2;

        return head;
    }
}
