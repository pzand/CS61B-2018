import java.util.LinkedList;
import java.util.List;

public class HuffmanDecoder {
    public static void main(String[] args) {
        // 判断输入参数是否够
        if (args.length < 2) {
            throw new IllegalArgumentException("you must input 2 arguments");
        }

        String fileName = args[0];
        String decodingFileName = args[1];
        ObjectReader objectReader = new ObjectReader(fileName);

        // 读取BinaryTrie对象
        Object object = objectReader.readObject();
        if (!object.getClass().equals(BinaryTrie.class)) {
            throw new IllegalArgumentException("input file is not needing .huf file(not have BinaryTrie class)");
        }
        BinaryTrie binaryTrie = (BinaryTrie) object;

        // 读取BitSequence对象
        object = objectReader.readObject();
        if (!object.getClass().equals(BitSequence.class)) {
            throw new IllegalArgumentException("input file is not needing .huf file(not have BitSequence class)");
        }
        BitSequence bitSequence = (BitSequence) object;

        // 解码
        List<Character> decodingList = new LinkedList<>();
        while (bitSequence.length() != 0) {
            Match match = binaryTrie.longestPrefixMatch(bitSequence);
            decodingList.add(match.getSymbol());

            bitSequence = bitSequence.allButFirstNBits(match.getSequence().length());
        }

        // 输出
        char[] chars = new char[decodingList.size()];
        int i = 0;
        for (char c : decodingList) {
            chars[i++] = c;
        }
        FileUtils.writeCharArray(decodingFileName, chars);
    }
}
