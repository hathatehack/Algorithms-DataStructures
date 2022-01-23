package C08_LinkedList;

public class C13_SmallerEqualBigger {
    static public class Node {
        public int value;
        public Node next;
        public Node(int val) { value = val;}
    }

    // 将单向链表按某值划分成左边小、中间相等、右边大的形式
    // 使用指针。分成小、中、大三个链表，再把各个链表串起来，此算法稳定（面试用）
    static public Node listPartition(Node head, int pivot) {
        if (head == null) {
            return null;
        }
        Node smallerHead = null;
        Node smallerTail = null;
        Node equalHead = null;
        Node equalTail = null;
        Node biggerHead = null;
        Node biggerTail = null;
        // 分成小、中、大三个链表
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = null;
            if (head.value < pivot) {
                if (smallerHead != null) {
                    smallerTail.next = head;
                    smallerTail = head;
                } else {
                    smallerHead = smallerTail = head;
                }
            } else if (head.value == pivot) {
                if (equalHead != null) {
                    equalTail.next = head;
                    equalTail = head;
                } else {
                    equalHead = equalTail = head;
                }
            } else {
                if (biggerHead != null) {
                    biggerTail.next = head;
                    biggerTail = head;
                } else {
                    biggerHead = biggerTail = head;
                }
            }
            head = next;
        }
        // 把各个链表串起来，小于区的尾巴连等于区的头，等于区的尾巴连大于区的头。
        if (smallerTail != null) {
            smallerTail.next = equalHead;
            equalHead = equalHead == null ? smallerHead : equalHead;
        }
        if (equalTail != null) {
            equalTail.next = biggerHead;
        }
        return smallerHead != null ? smallerHead : (equalHead != null ? equalHead : biggerHead); // 妙!
    }


    // 使用容器。把链表放入数组里，在数组上做partition，此算法不稳定（笔试用）
    static public Node listPartition2(Node head, int pivot) {
        if (head == null) {
            return null;
        }
        int i = 0;
        Node cur = head;
        while (cur != null) {
            cur = cur.next;
            i++;
        }
        Node[] array = new Node[i];
        for (i = 0, cur = head; cur != null; i++, cur = cur.next) {
            array[i] = cur;
        }
        partition(array, pivot);
        for (i = 1; i < array.length; i++) {
            array[i - 1].next = array[i];
        }
        array[i - 1].next = null;
        return array[0];
    }
    static private void partition(Node[] array, int pivot) {
        int lessR = -1;
        int moreL = array.length;
        int index = 0;
        while (index < moreL) {
            if (array[index].value < pivot) {
                swap(array, ++lessR, index++);
            } else if (array[index].value == pivot) {
                index++;
            } else {
                swap(array, --moreL, index);
            }
        }
    }
    static private void swap(Node[] array, int i, int j) {
        Node tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
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
        Node head1 = new Node(7);
        head1.next = new Node(9);
        head1.next.next = new Node(1);
        head1.next.next.next = new Node(8);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(2);
        head1.next.next.next.next.next.next = new Node(5);
        Node head2 = new Node(7);
        head2.next = new Node(9);
        head2.next.next = new Node(1);
        head2.next.next.next = new Node(8);
        head2.next.next.next.next = new Node(5);
        head2.next.next.next.next.next = new Node(2);
        head2.next.next.next.next.next.next = new Node(5);
        printLinkedList(head1);
        printLinkedList(listPartition(head1, 5));
        printLinkedList(listPartition2(head2, 5));
    }
}
