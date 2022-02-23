package C08_LinkedList;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class C15_DoublyLinkedList {
    // 双向节点
    public static class Node<K, V> {
        private Node<K, V> pre;
        private Node<K, V> next;
        public K key;
        public V value;

        public Node(K k, V val) {
            key = k;
            value = val;
        }
    }

    // 双向链表（有占位头节点）
    public static class DoublyLinkedList<K, V> {
        private Node<K, V> head;
        private Node<K, V> tail;
        private int length;
        private Collection<V> values;

        public DoublyLinkedList() {
            head = new Node<>(null, null);  // 占位头节点，增删节点操作时减少判断条件提高效率！
            tail = head;
            length = 0;
            values = new LinkedValues();
        }

        public void addLast(Node<K, V> node) {
            node.pre = tail;
            tail.next = node;
            tail = node;
            length++;
        }

        public Node<K, V> removeFirst() {
            if (head == tail) {  // 除了头节点，链表不存在任何节点，直接返回null。
                return null;
            }
            Node<K, V> first = head.next;
            if (first == tail) {  // 除了头节点，链表只有1个节点
                tail = head;
            } else {  // 除了头节点，链表存在多个节点
                first.next.pre = head;  // 更新第2个节点的pre指向head
            }
            head.next = first.next;  // 更新头节点的next
            // 释放被删除节点的引用
            first.next = null;
            first.pre = null;
            length--;
            return first;
        }

        // 时间复杂度O(1)
        public Node<K, V> removeNode(Node<K, V> node) {
            if (node == head) {  // 不允许删除占位头节点
                return null;
            }
            node.pre.next = node.next;
            if (node != tail) {  // 删除非tail节点得连接next和pre
                node.next.pre = node.pre;
            } else {  // 删除tail节点需要重设tail
                tail = tail.pre;
                tail.next = null;
            }
            // 释放被删除节点的引用
            node.pre = null;
            node.next = null;
            length--;
            return node;
        }

        public int size() {
            return length;
        }

        public Collection<V> values() {
            return values;
        }

        private final class LinkedValues extends AbstractCollection<V> {
            @Override
            public Iterator<V> iterator() {
                return new LinkedValueIterator();
            }

            @Override
            public int size() {
                return length;
            }
        }

        private final class LinkedValueIterator implements Iterator<V> {
            private Node<K, V> next = head.next;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public V next() {
                Node<K, V> current = next;
                next = next.next;
                return current.value;
            }
        }
    }



    // 双向链表（无占位头节点）
    public static class DoublyLinkedList_<K, V> {
        private Node<K, V> head;
        private Node<K, V> tail;
        private int length;
        private Collection<V> values;

        public DoublyLinkedList_() {
            head = null;
            tail = null;
            length = 0;
            values = new LinkedValues();
        }

        public void addLast(Node<K, V> node) {
            if (head == null) {
                head = node;
            } else {
                node.pre = tail;
                tail.next = node;
            }
            tail = node;
            length++;
        }

        public Node<K, V> removeFirst() {
            if (head == null) {  // 链表不存在任何节点，直接返回null。
                return null;
            }
            Node<K, V> first = head;
            if (head == tail) {  // 链表只有1个节点
                tail = null;
            } else {  // 链表存在多个节点
                head.next.pre = null;  // 更新第2个节点的pre指向null
            }
            head = head.next;  // 更新头节点
            // 释放被删除节点的引用
            first.next = null;
            length--;
            return first;
        }

        // 时间复杂度O(1)
        public Node<K, V> removeNode(Node<K, V> node) {
            // 链表只有1个节点时的处理
            if (head == tail) {
                return removeFirst();
            }
            // 链表存在多个节点时的处理
            if (node != head) {  // 删除非head节点得连接pre和next
                node.pre.next = node.next;
            } else {  // 删除head节点需要重设head
                head = head.next;
                head.pre = null;
            }
            if (node != tail) {  // 删除非tail节点得连接next和pre
                node.next.pre = node.pre;
            } else {  // 删除tail节点需要重设tail
                tail = tail.pre;
                tail.next = null;
            }
            // 释放被删除节点的引用
            node.pre = null;
            node.next = null;
            length--;
            return node;
        }

        public int size() {
            return length;
        }

        public Collection<V> values() {
            return values;
        }

        private final class LinkedValues extends AbstractCollection<V> {
            @Override
            public Iterator<V> iterator() {
                return new LinkedValueIterator();
            }

            @Override
            public int size() {
                return length;
            }
        }

        private final class LinkedValueIterator implements Iterator<V> {
            private Node<K, V> next = head;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public V next() {
                Node<K, V> current = next;
                next = next.next;
                return current.value;
            }
        }
    }
}
