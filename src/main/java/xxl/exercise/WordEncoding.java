package xxl.exercise;

import java.util.*;

/**
 * 单词编码
 */
public class WordEncoding {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            System.out.println(minimumLengthEncoding2(line.split(" +")));
        }
    }

    static int minimumLengthEncoding(String[] words) {
        //求一个没有重复的集合
        Set<String> set = new HashSet<>(Arrays.asList(words));
        //遍历每个单词的真字串，如果出现再集合则删除
        for (String word : words) {
            for (int i = 1; i < word.length(); i++) {
                set.remove(word.substring(i));
            }
        }
        //求剩余单词的数量
        int len = 0;
        for (String word : set) {
            len += word.length() + 1;
        }
        return len;
    }

    static int minimumLengthEncoding2(String[] words) {
        Node root = new Node();
        Map<Node, Integer> map = new HashMap<>();

        for (int i = 0; i < words.length; ++i) {
            String word = words[i];
            Node cur = root;
            for (int j = word.length() - 1; j >= 0; --j)
                cur = cur.get(word.charAt(j));
            map.put(cur, i);
        }

        int ans = 0;
        for (Node node : map.keySet()) {
            if (node.count == 0)
                ans += words[map.get(node)].length() + 1;
        }
        return ans;

    }

    static class Node {
        Node[] children;
        int count;

        Node() {
            children = new Node[26];
            count = 0;
        }

        public Node get(char c) {
            if (children[c - 'a'] == null) {
                children[c - 'a'] = new Node();
                count++;
            }
            return children[c - 'a'];
        }

    }
}
