package C08_BTree;

import java.util.LinkedList;
import java.util.Queue;

import C01_random.GenerateRandomBST;
import C01_random.GenerateRandomBST.TreeNode;

public class C07_SerializeAndDeserializeBT {
    /*
     * 二叉树可以通过先序、后序或者按层遍历的方式序列化和反序列化。
     * 但是，二叉树无法通过中序遍历的方式实现序列化和反序列化
     * 因为不同的两棵树，可能得到同样的中序序列，即便补了空位置也可能一样。
     * 比如如下两棵树
     *         __2
     *        /
     *       1
     *       和
     *       1__
     *          \
     *           2
     * 补足空位置的中序遍历结果都是{ null, 1, null, 2, null}
     *
     * */
    static public class PreOrder {
        static public Queue<String> serialize(TreeNode head) {
            Queue<String> queue = new LinkedList<>();
            s(head, queue);
            return queue;
        }

        static private void s(TreeNode node, Queue<String> queue) {
            if (node == null) {
                queue.add(null);
            } else {
                queue.add(String.valueOf(node.value));
                s(node.left, queue);
                s(node.right, queue);
            }
        }

        static public TreeNode deserialize(Queue<String> queue) {
            if (queue == null || queue.size() == 0)
                return null;
            return d(queue);
        }

        static private TreeNode d(Queue<String> queue) {
            String val = queue.poll();
            if (val == null) {
                return null;
            }
            TreeNode head = new TreeNode(Integer.parseInt(val));
            head.left = d(queue);
            head.right = d(queue);
            return head;
        }
    }

    static public class PostOrder {
    }

    static public class LevelOrder {
        static public Queue<String> serialize(TreeNode head) {
            Queue<String> serial = new LinkedList<>();
            if (head == null) {
                serial.add(null);
            } else {
                serial.add(String.valueOf(head.value));  // 序列化头
                Queue<TreeNode> levelQ = new LinkedList<>();
                levelQ.add(head);
                while (!levelQ.isEmpty()) {
                    head = levelQ.poll();
                    if (head.left != null) {
                        serial.add(String.valueOf(head.left.value)); // 序列化左节点
                        levelQ.add(head.left);  // 记录当前层节点作为下次的头
                    } else {
                        serial.add(null);
                    }
                    if (head.right != null) {
                        serial.add(String.valueOf(head.right.value)); // 序列化右节点
                        levelQ.add(head.right);  // 记录当前层节点作为下次的头
                    } else {
                        serial.add(null);
                    }
                }
            }
            return serial;
        }

        static public TreeNode deserialize(Queue<String> serial) {
            TreeNode head = null;
            if (serial == null || serial.size() == 0 || (head = generateNode(serial.poll())) == null) {
                return null;
            }
            Queue<TreeNode> levelQ = new LinkedList<>();
            levelQ.add(head);
            while (!serial.isEmpty()) {
                TreeNode node = levelQ.poll();
                node.left = generateNode(serial.poll());
                node.right = generateNode(serial.poll());
                if (node.left != null)
                    levelQ.add(node.left);
                if (node.right != null)
                    levelQ.add(node.right);
            }
            return head;
        }

        static private TreeNode generateNode(String val) {
            if (val == null) return null;
            return new TreeNode(Integer.parseInt(val));
        }
    }




    public static void main(String[] args) {
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 1000000;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            TreeNode head = GenerateRandomBST.generateRandomBST(maxLevel, maxValue);
            Queue<String> pre = PreOrder.serialize(head);
            Queue<String> level = LevelOrder.serialize(head);
            TreeNode preBuild = PreOrder.deserialize(pre);
            TreeNode levelBuild = LevelOrder.deserialize(level);
            if (!GenerateRandomBST.isSameValueStructure(preBuild, levelBuild)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish!");

    }
}
