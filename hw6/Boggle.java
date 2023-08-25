import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import jdk.dynalink.Operation;

import java.util.*;

public class Boggle {

    // File path of dictionary file
    static String dictPath = "words.txt";
    private Tire tire;
    private char[][] boogleBoard;
    private int width, height;
    private boolean[][] havenBeenChoose;
    private MinPQ<String> heap;
    private Set<String> repeat;
    private StringBuilder strBuilder;
    private int k;
    // Top, Down, Left, Right, TopLeft, TopRight, DownLeft, DownRight
    private final int[] X = {-1, 1, 0, 0, -1, -1, 1, 1};
    private final int[] Y = {0, 0, -1, 1, -1, 1, -1, 1};

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
        this.k = k;
        heap = new MinPQ<>(k, (o1, o2) -> {
            if (o1.length() != o2.length()) {
                return o1.length() - o2.length();
            } else {
                return o2.compareTo(o1);
            }
        });
        repeat = new HashSet<>();
        strBuilder = new StringBuilder();

        loadDictionary();
        loadBoardFile(boardFilePath);

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

        LinkedList<String> list = new LinkedList<>();
        while (!heap.isEmpty()) {
            list.addFirst(heap.delMin());
        }
        return list;
    }

    private void dfs(Tire tire, Position position) {
        if (tire.isWord()) {
            levelIsWord();
        }

        Map<Direction, Character> neighbor = neighbor(position.x(), position.y());
        Set<Character> set = tire.getLevelChar();

        neighbor.forEach((direction, character) -> {
            if (set.contains(character)) {
                strBuilder.append(character);
                havenBeenChoose[position.x()][position.y()] = true;

                dfs(tire.getNextTire(character), targetPosition(position, direction));

                havenBeenChoose[position.x()][position.y()] = false;
                strBuilder.deleteCharAt(strBuilder.length() - 1);
            }
        });
    }

    private void levelIsWord() {
        String str = strBuilder.toString();
        if (repeat.contains(str)) {
            return;
        }

        repeat.add(str);
        heap.insert(str);

        if (heap.size() > k) {
            repeat.remove(heap.delMin());
        }

    }

    private void loadDictionary() {
        tire = new Tire(new In(dictPath));
    }

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

    private boolean isBoard(int x, int y) {
        if (x < 0 || x >= width) {
            return false;
        }
        if (y < 0 || y >= height) {
            return false;
        }
        return true;
    }

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

    private Optional<Character> getBoard(int x, int y) {
        if (!isBoard(x, y) || havenBeenChoose[x][y]) {
            return Optional.empty();
        }
        return Optional.of(boogleBoard[x][y]);
    }

    private Position targetPosition(int x, int y, Direction direction) {
        switch (direction) {
            case Top -> {
                x += X[0];
                y += Y[0];
            }
            case Down -> {
                x += X[1];
                y += Y[1];
            }
            case Left -> {
                x += X[2];
                y += Y[2];
            }
            case Right -> {
                x += X[3];
                y += Y[3];
            }
            case TopLeft -> {
                x += X[4];
                y += Y[4];
            }
            case TopRight -> {
                x += X[5];
                y += Y[5];
            }
            case DownLeft -> {
                x += X[6];
                y += Y[6];
            }
            case DownRight -> {
                x += X[7];
                y += Y[7];
            }
        }
        return new Position(x, y);
    }

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
