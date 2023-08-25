import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Boggle {

    // File path of dictionary file
    static String dictPath = "words.txt";
    // 字典前缀树
    private Tire tire;
    // boogle字符数组
    private char[][] boogleBoard;
    // boogle宽度和高度
    private int width, height;
    // 用于标记是否选过该位置
    private boolean[][] havenBeenChoose;
    // 最小堆。如果其内容超过k，则弹出一个最小的word(不符合的word)
    private MinPQ<String> heap;
    // 判断heap中是否有重复的word
    private Set<String> repeat;
    // 用于回溯法的字符串
    private StringBuilder strBuilder;
    // 需要选择的word的数量
    private int k;
    // 堆中最小的word的长度，用于优化。如果当前分支的最长word的长度都不如堆中最短的word，则跳过该分支的遍历
    // 事实证明该优化效果略胜于无
    private int minLength;
    // Direction方向对应的具体方位
    // Top, Down, Left, Right, TopLeft, TopRight, DownLeft, DownRight
    private static final int[] X = {-1, 1, 0, 0, -1, -1, 1, 1};
    private static final int[] Y = {0, 0, -1, 1, -1, 1, -1, 1};

    /**
     * Solves a Boggle puzzle.
     *
     * @param k             The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     * The Strings are sorted in descending order of length.
     * If multiple words have the same length,
     * have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        if (k < 0) {
            throw new IllegalArgumentException("k is not positive Integer");
        }

        Boggle boggle = new Boggle();
        return boggle.solveHelper(k, boardFilePath);
    }

    private List<String> solveHelper(int k, String boardFilePath) {
        // 初始化
        this.k = k;
        heap = new MinPQ<>(k, (o1, o2) -> {
            if (o1.length() != o2.length()) {
                return o1.length() - o2.length();
            } else {
                return o2.compareTo(o1);
            }
        });
        this.minLength = 0;
        repeat = new HashSet<>();
        strBuilder = new StringBuilder();

        // 加载字典和地图
        loadDictionary();
        loadBoardFile(boardFilePath);

        // 递归的第一次不用邻居，一次拎出来单独进行回溯
        Set<Character> set = tire.getLevelChar();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char c = getBoard(i, j).orElse('?');
                if (set.contains(c)) {
                    strBuilder.append(c);
                    havenBeenChoose[i][j] = true;

                    dfs(tire.getNextTire(c), new Position(i, j));

                    havenBeenChoose[i][j] = false;
                    strBuilder.deleteCharAt(0);
                }
            }
        }

        // 根据heap堆，填进list中
        LinkedList<String> list = new LinkedList<>();
        while (!heap.isEmpty()) {
            list.addFirst(heap.delMin());
        }
        return list;
    }

    // 回溯法进行遍历
    private void dfs(Tire tire, Position position) {
        // 优化剪枝，
        if (minLength > tire.longestLength + strBuilder.length()) {
            return;
        }

        // 如果当前分支是一个word，则插入heap中
        if (tire.isWord()) {
            insertWord();
        }

        // 获取当前节点的邻居
        Map<Direction, Character> neighbor = neighbor(position.x(), position.y());
        // 获取当前分支的下一分组
        Set<Character> set = tire.getLevelChar();

        // 邻居中存在 当前分组的字符，则进行下一步回溯
        neighbor.forEach((direction, character) -> {
            if (set.contains(character)) {
                // 将当前字符添加builder中
                strBuilder.append(character);
                // 标记已经选择过该位置
                havenBeenChoose[position.x()][position.y()] = true;

                dfs(tire.getNextTire(character), targetPosition(position, direction));

                havenBeenChoose[position.x()][position.y()] = false;
                strBuilder.deleteCharAt(strBuilder.length() - 1);
            }
        });
    }

    // 将builder的字符串插入到堆中
    private void insertWord() {
        String str = strBuilder.toString();
        if (repeat.contains(str)) {
            return;
        }

        repeat.add(str);
        heap.insert(str);

        if (heap.size() > k) {
            String removeString = heap.delMin();
            minLength = Math.max(minLength, removeString.length());
            repeat.remove(removeString);
        }
    }

    // 加载字典
    private void loadDictionary() {
        tire = new Tire(new In(dictPath));
    }

    // 根据.txt文件，加载相关信息。
    // boogleBoard 二维数组，存储字符
    // width，height 二位数组的宽度和高度
    // havenBeenChoose 用于标记已经选过的字符
    private void loadBoardFile(String boardFilePath) {
        In in = new In(boardFilePath);
        List<String> board = new LinkedList<>();

        while (in.hasNextLine()) {
            String boardLine = in.readLine();
            board.add(boardLine);
            if (boardLine.length() != board.get(0).length()) {
                throw new IllegalArgumentException("The input board is not rectangular");
            }
        }

        this.width = board.get(0).length();
        this.height = board.size();

        havenBeenChoose = new boolean[height][width];

        int i = 0;
        boogleBoard = new char[height][];
        for (String s : board) {
            boogleBoard[i++] = s.toCharArray();
        }
    }

    // 判断是否越界
    private boolean isBoard(int x, int y) {
        if (x < 0 || x >= height) {
            return false;
        }
        if (y < 0 || y >= width) {
            return false;
        }
        return true;
    }

    // 根据坐标选择出邻居。如果选择过 或者 越界，则不选择
    private Map<Direction, Character> neighbor(int x, int y) {
        Map<Direction, Character> map = new TreeMap<>();

        int i = 0;
        for (Direction s : Direction.values()) {
            Optional<Character> dir = getBoard(x + X[i], y + Y[i]);
            dir.ifPresent(character -> map.put(s, character));
            i += 1;
        }

        return map;
    }

    // 根据坐标选择对应的 字符，如果越界或者已经选择过 则返回空
    private Optional<Character> getBoard(int x, int y) {
        if (!isBoard(x, y) || havenBeenChoose[x][y]) {
            return Optional.empty();
        }
        return Optional.of(boogleBoard[x][y]);
    }

    // 根据 当前坐标和方向，选择出下一个位置
    private Position targetPosition(int x, int y, Direction direction) {
        switch (direction) {
            case Top : {
                x += X[0];
                y += Y[0];
                break;
            }
            case Down : {
                x += X[1];
                y += Y[1];
                break;
            }
            case Left : {
                x += X[2];
                y += Y[2];
                break;
            }
            case Right : {
                x += X[3];
                y += Y[3];
                break;
            }
            case TopLeft : {
                x += X[4];
                y += Y[4];
                break;
            }
            case TopRight : {
                x += X[5];
                y += Y[5];
                break;
            }
            case DownLeft : {
                x += X[6];
                y += Y[6];
                break;
            }
            case DownRight : {
                x += X[7];
                y += Y[7];
                break;
            }
        }
        return new Position(x, y);
    }

    // 根据 当前位置和方向，选择出下一个位置
    private Position targetPosition(Position position, Direction direction) {
        return targetPosition(position.x(), position.y(), direction);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        List<String> list = solve(7, args[0]);
        list.forEach(System.out::println);

        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }
}
