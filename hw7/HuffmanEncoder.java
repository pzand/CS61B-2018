import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> map = new TreeMap<>();
        for (char inputSymbol : inputSymbols) {
            int getDefault = map.getOrDefault(inputSymbol, 0) + 1;
            map.put(inputSymbol, getDefault);
        }
        return map;
    }

    public static void main(String[] args) {
        // 读取文件，获取字符频率表
        String fileName = args[0];
        char[] charArr = FileUtils.readFile(fileName);
        Map<Character, Integer> map = buildFrequencyTable(charArr);

        // 构造哈夫曼map
        BinaryTrie binaryTrie = new BinaryTrie(map);
        Map<Character, BitSequence> lookupTable = binaryTrie.buildLookupTable();

        List<BitSequence> list = new LinkedList<>();
        for (char c : charArr) {
            list.add(lookupTable.get(c));
        }

        // 写入BinaryTrie和 编码后的BitSequence序列
        String writeFileName = fileName + ".huf";
        ObjectWriter objectWriter = new ObjectWriter(writeFileName);
        objectWriter.writeObject(binaryTrie);
        objectWriter.writeObject(BitSequence.assemble(list));
    }
}
