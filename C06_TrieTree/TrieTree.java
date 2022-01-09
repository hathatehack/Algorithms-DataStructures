package C06_TrieTree;

import C01_random.GenerateRandomStringArray;

import java.util.Arrays;
import java.util.HashMap;

public class TrieTree {
    public static void main(String[] args) {
        int testTimes = 100000;
        int maxSize = 100;
        int maxStrLen = 5;
        int kind = 3;
        for (int i = 0; i < testTimes; i++) {
            String[] array = GenerateRandomStringArray.generateRandomStringArray(maxSize, maxStrLen, kind);
//            System.out.println(Arrays.toString(array));
            TrieTree1 trieTree1 = new TrieTree1();
            TrieTree2 trieTree2 = new TrieTree2();
            Checker checker = new Checker();
            for (String word : array) {
                double r = Math.random();
                if (r < 0.25) {
                    trieTree1.insert(word);
                    trieTree2.insert(word);
                    checker.insert(word);
                } else if (r < 0.5) {
                    trieTree1.delete(word);
                    trieTree2.delete(word);
                    checker.delete(word);
                } else if (r < 0.75) {
                    int ans1 = trieTree1.search(word);
                    int ans2 = trieTree2.search(word);
                    int ans3 = checker.search(word);
                    if (ans1 != ans2 || ans2 != ans3) {
                        System.out.println(Arrays.toString(array));
                        System.out.printf("Oops! i=%d, ans1=%d, ans2=%d, ans3=%d\n", i, ans1, ans2, ans3);
                        break;
                    }
                } else {
                    int ans1 = trieTree1.prefixNumber(word);
                    int ans2 = trieTree2.prefixNumber(word);
                    int ans3 = checker.prefixNumber(word);
                    if (ans1 != ans2 || ans2 != ans3) {
                        System.out.println(Arrays.toString(array));
                        System.out.printf("Oops! i=%d, ans1=%d, ans2=%d, ans3=%d\n", i, ans1, ans2, ans3);
                        break;
                    }
                }
            }
        }
    }

    // Array
    static public class TrieTree1 {
        static public class Node {
            public int pass = 0;
            public int end = 0;
            public Node[] next = new Node[26];  // a~z
        }

        private Node root = new Node();

        public void insert(String word) {
            if (word == null) {
                return;
            }
            char[] str = word.toCharArray();
            Node node = root;
            node.pass++;
            for (char c : str) {
                int path = c - 'a';
                if (node.next[path] == null) {
                    node.next[path] = new Node();
                }
                node = node.next[path];
                node.pass++;
            }
            node.end++;
        }

        public void delete(String word) {
            if (search(word) > 0) {
                char[] chs = word.toCharArray();
                Node node = root;
                node.pass--;
                for (char c : chs) {
                    int path = c - 'a';
                    if (--node.next[path].pass == 0) {
                        node.next[path] = null;
                        return;
                    }
                    node = node.next[path];
                }
                node.end--;
            }
        }

        // word这个单词之前加入过几次
        public int search(String word) {
            Node node = searchLastNode(word);
            return node == null ? 0 : node.end;
        }

        // 所有加入的字符串中，有几个是以pre这个字符串作为前缀的
        public int prefixNumber(String pre) {
            Node node = searchLastNode(pre);
            return node == null ? 0 : node.pass;
        }

        private Node searchLastNode(String word) {
            if (word == null) {
                return null;
            }
            char[] chs = word.toCharArray();
            Node node = root;
            for (char c : chs) {
                int path = c - 'a';
                if (node.next[path] == null) {
                    return null;
                }
                node = node.next[path];
            }
            return node;
        }
    }




    // HashMap
    static public class TrieTree2 {
        static public class Node {
            public int pass = 0;
            public int end = 0;
            public HashMap<Integer, Node> next = new HashMap<>();  // a~z
        }

        private Node root = new Node();

        public void insert(String word) {
            if (word == null) {
                return;
            }
            char[] str = word.toCharArray();
            Node node = root;
            node.pass++;
            for (char c : str) {
                int path = c - 'a';
                if (!node.next.containsKey(path)) {
                    node.next.put(path, new Node());
                }
                node = node.next.get(path);
                node.pass++;
            }
            node.end++;
        }

        public void delete(String word) {
            if (search(word) > 0) {
                char[] chs = word.toCharArray();
                Node node = root;
                node.pass--;
                for (char c : chs) {
                    int path = c - 'a';
                    if (--node.next.get(path).pass == 0) {
                        node.next.remove(path);
                        return;
                    }
                    node = node.next.get(path);
                }
                node.end--;
            }
        }

        // word这个单词之前加入过几次
        public int search(String word) {
            Node node = searchLastNode(word);
            return node == null ? 0 : node.end;
        }

        // 所有加入的字符串中，有几个是以pre这个字符串作为前缀的
        public int prefixNumber(String pre) {
            Node node = searchLastNode(pre);
            return node == null ? 0 : node.pass;
        }

        private Node searchLastNode(String word) {
            if (word == null) {
                return null;
            }
            char[] chs = word.toCharArray();
            Node node = root;
            for (char c : chs) {
                int path = c - 'a';
                if (!node.next.containsKey(path)) {
                    return null;
                }
                node = node.next.get(path);
            }
            return node;
        }
    }





    static public class Checker {
        private HashMap<String, Integer> map = new HashMap<>();

        public void insert(String word) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        public void delete(String word) {
            if (map.containsKey(word)) {
                if (map.get(word) > 1) {
                    map.put(word, map.get(word) - 1);
                } else {
                    map.remove(word);
                }
            }
        }

        public int search(String word) {
            return map.getOrDefault(word, 0);
        }

        public int prefixNumber(String pre) {
            int count = 0;
            for (String word : map.keySet()) {
                if (word.startsWith(pre)) {
                    count += map.get(word);
                }
            }
            return count;
        }
    }
}
