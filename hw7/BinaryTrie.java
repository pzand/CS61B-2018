import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class BinaryTrie implements Serializable {
    private Node root;

    // the constructor should build a Huffman decoding trie according to frequency Table
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> maxPQ = new MinPQ<>();
        frequencyTable.forEach((key, value) ->
                maxPQ.insert(new Node(key, value))
        );

        while (maxPQ.size() > 1) {
            Node first = maxPQ.delMin();
            Node second = maxPQ.delMin();

            Node newNode = new Node(null, first.frequent + second.frequent, first, second);
            maxPQ.insert(newNode);
        }
        root = maxPQ.delMin();
    }

    // finds the longest prefix that matches the given querySequence
    // and returns a Match object for that Match
    public Match longestPrefixMatch(BitSequence querySequence) {
        //用于记录前缀
        StringBuilder str = new StringBuilder();
        Node nodeTree = root;
        for (int i = 0;i < querySequence.length();i++) {
            if (nodeTree.isChar()) {
                break;
            }

            if (querySequence.bitAt(i) == 0) {
                str.append('0');
                nodeTree = nodeTree.getLeft();
            } else {
                str.append('1');
                nodeTree = nodeTree.getRight();
            }
        }
        return new Match(new BitSequence(str.toString()), nodeTree.getCharacter());
    }

    // returns the inverse of the coding trie
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> map = new TreeMap<>();
        lookupTableHelper(root, map, new StringBuilder());
        return map;
    }

    // 递归寻找有效节点
    private void lookupTableHelper(Node root, Map<Character, BitSequence> map, StringBuilder str) {
        if (root == null) {
            return;
        }

        if (root.isChar()) {
            map.put(root.getCharacter(), new BitSequence(str.toString()));
        }

        str.append('0');
        lookupTableHelper(root.getLeft(), map, str);
        str.deleteCharAt(str.length() - 1);

        str.append('1');
        lookupTableHelper(root.getRight(), map, str);
        str.deleteCharAt(str.length() - 1);
    }

    private static class Node implements Comparable<Node> {
        private final Character c;
        private final int frequent;
        private final Node left, right;

        public Node(Character c, int frequent, Node left, Node right) {
            this.c = c;
            this.frequent = frequent;
            this.left = left;
            this.right = right;
        }

        public Node(Character c, int frequent) {
            this(c, frequent, null, null);
        }

        public Character getCharacter() {
            return c;
        }

        public int getFrequent() {
            return frequent;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean isChar() {
            return c != null;
        }

        @Override
        public int compareTo(Node o) {
            return this.frequent - o.frequent;
        }
    }
}
