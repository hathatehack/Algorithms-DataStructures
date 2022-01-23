package C08_LinkedList;

import java.util.HashSet;

public class C11_FindLoopNode {
    static public class Node {
        public int value;
        public Node next;
        public Node(int val) { value = val;}
    }

    // 如果给定单链表是有环的，请找出第一个入环节点。
    // 使用指针。
    static public Node findLoopNode(Node head) {
        // 过滤掉2个节点以内的无环链表
        if (head == null || head.next == null || head.next.next == null) return null;
        // 判断是否有环，slow每次走一步，fast每次走两步
        Node slow = head.next;
        Node fast = head.next.next;
        while (slow != fast) {  // 如果有环，最终slow必定会与fast相遇！
            if (fast.next == null || fast.next.next == null) {  // fast遍历到null结尾意味着无环
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        // 搜索入环节点，fast从头节点开始每次走一步，slow继续每次走一步，相遇节点即是入环节点。
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    // 使用容器。
    static public Node findLoopNode2(Node head) {
        if (head == null || head.next == null || head.next.next == null) return null;
        HashSet<Node> set = new HashSet<>();
        Node cur = head;
        for (; cur != null; cur = cur.next) {
            if (!set.contains(cur)) {
                set.add(cur);
            } else {
                return cur;
            }
        }
        return null;
    }


    static public void main(String[] args) {
        // 0->0
        Node head1 = new Node(0);
        head1.next = head1;
        Node ans1 = findLoopNode(head1);
        Node ans2 = findLoopNode2(head1);
        if (ans1.value != ans2.value) {
            System.out.printf("Oops! ans1=%d, ans2=%d", ans1.value, ans2.value);
        }

        // 0->1->2->3->1
        head1.next = new Node(1);
        head1.next.next = new Node(2);
        head1.next.next.next = new Node(3);
        head1.next.next.next.next = head1.next;  // 3->1
        ans1 = findLoopNode(head1);
        ans2 = findLoopNode2(head1);
        if (ans1.value != ans2.value) {
            System.out.printf("Oops! ans1=%d, ans2=%d", ans1.value, ans2.value);
        }
    }
}
