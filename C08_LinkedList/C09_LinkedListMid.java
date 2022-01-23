package C08_LinkedList;

import java.util.ArrayList;
import java.util.List;

public class C09_LinkedListMid {
    static public class Node {
        public int value;
        public Node next;
        public Node(int val) { value = val;}
    }
    // 使用快慢指针。对于面试，时间复杂度依然重要，但是一定要空间最省！
    // 快慢指针：从原点出发，慢指针走一步，快指针走两步
    // 奇数长度返回中点，偶数长度返回上中点
    static public Node midOrUpMidNode(Node head) {
        if (head == null || head.next == null) return head;
        // 至少2个节点
        return run(head, head);
    }
    static public Node midOrUpMidNode_(Node head) {
        if (head == null || head.next == null || head.next.next == null) return head;
        // 至少3个节点
        return run(head.next, head.next.next);
    }

    // 奇数长度返回中点，偶数长度返回下中点
    static public Node midOrDownMidNode(Node head) {
        if (head == null || head.next == null) return head;
        // 至少2个节点
        return run(head.next, head.next);  // 等效midOrDownMidPreNode(head).next;
    }

    // 奇数长度返回中点前一个，偶数长度返回上中点前一个
    static public Node midOrUpMidPreNode(Node head) {
        // 至少3个节点
        if (head == null || head.next == null || head.next.next == null) return null;
        return run(head, head.next.next);
    }

    // 奇数长度返回中点前一个，偶数长度返回下中点前一个
    static public Node midOrDownMidPreNode(Node head) {
        // 至少2个节点
        if (head == null || head.next == null) return null;
        return run(head, head.next);
    }

    // slow表示停止时的中点，fast表示当前遍历到的最右节点。当输入的fast不能继续遍历，slow必须是正确的中点！
    static private Node run(Node slow, Node fast) {
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }




    // 使用容器辅助。对于笔试，不太在乎空间复杂度，一切为了时间复杂度。
    static public Node midOrUpMidNode2(Node head) {
        if (head == null) return null;
        List<Node> list = toList(head);
        return list.get((list.size() - 1) / 2);
    }

    static public Node midOrDownMidNode2(Node head) {
        if (head == null) return null;
        List<Node> list = toList(head);
        return list.get(list.size() / 2);
    }

    static public Node midOrUpMidPreNode2(Node head) {
        // 至少3个节点
        if (head == null || head.next == null || head.next.next == null) return null;
        List<Node> list = toList(head);
        return list.get((list.size() - 3) / 2);  // 考虑只有1个或2个节点
    }

    static public Node midOrDownMidPreNode2(Node head) {
        // 至少2个节点
        if (head == null || head.next == null) return null;
        List<Node> list = toList(head);
        return list.get((list.size() - 2) / 2);  // 考虑只有1个节点
    }

    static private List<Node> toList(Node head) {
        List<Node> list = new ArrayList<>();
        Node cur = head;
        while (cur != null) {
            list.add(cur);
            cur = cur.next;
        }
        return list;
    }





    public static void main(String[] args) {
        Node test = null;
        test = new Node(0);
        test.next = new Node(1);
        test.next.next = new Node(2);
        test.next.next.next = new Node(3);
        test.next.next.next.next = new Node(4);
        test.next.next.next.next.next = new Node(5);
        test.next.next.next.next.next.next = new Node(6);
        test.next.next.next.next.next.next.next = new Node(7);
        test.next.next.next.next.next.next.next.next = new Node(8);

        Node ans1 = null;
        Node ans2 = null;

        ans1 = midOrUpMidNode(test);
        ans2 = midOrUpMidNode2(test);
        System.out.println(ans1 != null ? ans1.value : "无");
        System.out.println(ans2 != null ? ans2.value : "无");
        System.out.println();
        ans1 = midOrDownMidNode(test);
        ans2 = midOrDownMidNode2(test);
        System.out.println(ans1 != null ? ans1.value : "无");
        System.out.println(ans2 != null ? ans2.value : "无");
        System.out.println();
        ans1 = midOrUpMidPreNode(test);
        ans2 = midOrUpMidPreNode2(test);
        System.out.println(ans1 != null ? ans1.value : "无");
        System.out.println(ans2 != null ? ans2.value : "无");
        System.out.println();
        ans1 = midOrDownMidPreNode(test);
        ans2 = midOrDownMidPreNode2(test);
        System.out.println(ans1 != null ? ans1.value : "无");
        System.out.println(ans2 != null ? ans2.value : "无");
    }
}
