import edu.princeton.cs.algs4.In;

import java.util.*;

public class Tire {
    private Map<Character, Tire> wordTire;
    private boolean isWord;
    public int longestLength;
    public Tire() {
        wordTire = new HashMap<>();
        this.isWord = false;
    }

    public Tire(In in) {
        this();
        while (in.hasNextLine()) {
            String str = in.readLine();
            insertStringToTire(str);
        }
    }

    // 插入字符串
    public void insertStringToTire(String str) {
        Tire tire = this;
        for (int i = 0;i < str.length();i++) {
            tire.longestLength = Math.max(tire.longestLength, str.length() - 1);
            tire = tire.setCharacter(str.charAt(i));
        }
        tire.isWord = true;
    }

    // 目前层次的Tire是否是word
    public boolean isWord() {
        return isWord;
    }

    // 输出包含前缀为prefix的word
    public Set<String> keysWithPrefix(String prefix) {
        Set<String> set = new HashSet<>();
        Tire tire = this;

        for(int i = 0;i < prefix.length();i++) {
            char charater = prefix.charAt(i);
            if (!haveTheNextChar(charater)) {
                return set;
            }

            tire = tire.getNextTire(charater);
        }

        allKeys(tire, new StringBuilder(prefix), set);
        return set;
    }

    // 输出所有的keys
    public Set<String> allKeys() {
        Set<String> set = new HashSet<>();
        allKeys(this, new StringBuilder(), set);
        return set;
    }

    // 输出当前tire下的所有key，并以引用数据类型set进行参数传递
    private void allKeys(Tire tire, StringBuilder strBuilder, Set<String> set) {
        if (tire == null) {
            return;
        }

        if (tire.isWord()) {
            set.add(strBuilder.toString());
        }

        tire.wordTire.forEach((key, value) -> {
            strBuilder.append(key);
            allKeys(tire.getNextTire(key), strBuilder, set);
            strBuilder.deleteCharAt(strBuilder.length() - 1);
        } );
    }

    public Set<Character> getLevelChar() {
        return wordTire.keySet();
    }

    // 是否包含子节点c
    private boolean haveTheNextChar(char c) {
        return wordTire.containsKey(c);
    }

    // 获取子节点c
    public Tire getNextTire(char c) {
        return wordTire.get(c);
    }

    private Tire setCharacter(char c) {
        if (wordTire.containsKey(c)) {
            return wordTire.get(c);
        }

        Tire newTire = new Tire();
        this.wordTire.put(c, newTire);
        return newTire;
    }
}
