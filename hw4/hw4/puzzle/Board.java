package hw4.puzzle;

import java.util.Arrays;
import java.util.LinkedList;

public class Board implements WorldState {
    private int[][] tiles;
    private final int size;
    private final int[][] goal;
    private final static int[] X = {-1, 1, 0, 0};
    private final static int[] Y = {0, 0, -1, 1};
    private final static int BLANK = 0;

    /**
     * Constructs a board from an N-by-N array of tiles where tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] tiles) {
        this.size = tiles.length;
        this.tiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);

        this.goal = new int[size()][size()];
        int sum = 1;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                goal[i][j] = sum;
                sum++;
            }
        }
        goal[size() - 1][size() - 1] = 0;
    }

    public Board(int[][] tiles, int[][] goal) {
        this.tiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
        this.size = tiles.length;
        this.goal = goal;
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     */
    public int tileAt(int i, int j) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
        if (j < 0 || j >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    /**
     * Returns the board size N
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns the neighbors of the current board
     */
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (goal[i][j] != tileAt(i, j) && tileAt(i, j) != BLANK) {
                    sum++;
                }
            }
        }
        return sum;
    }

    public int manhattan() {
        int sum = 0;
        // 计算tiles与goal的距离
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                // BLANK 不参与计算
                if (goal[i][j] != tileAt(i, j) && tileAt(i, j) != BLANK) {
                    int atX = (tileAt(i, j) - 1) / size();
                    int atY = (tileAt(i, j) - 1) % size();
                    sum += Math.abs(atX - i) + Math.abs(atY - j);
                }
            }
        }
        return sum;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public Iterable<WorldState> neighbors() {
        LinkedList<WorldState> list = new LinkedList<>();
        int[] blankNode = searchTheBlank();
        int xx = blankNode[0], yy = blankNode[1];

        for (int i = 0; i < X.length; i++) {
            if (!isOutOfBoard(xx + X[i], yy + Y[i])) {
                swap(xx, yy, xx + X[i], yy + Y[i]);
                list.add(new Board(tiles, goal));
                swap(xx, yy, xx + X[i], yy + Y[i]);
            }
        }
        return list;
    }

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    // 找到空点
    private int[] searchTheBlank() {
        for (int xx = 0; xx < size(); xx++) {
            for (int yy = 0; yy < size(); yy++) {
                if (tileAt(xx, yy) == BLANK) {
                    return new int[]{xx, yy};
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }

    // 判断点是否越界
    private boolean isOutOfBoard(int i, int j) {
        if (i < 0 || i >= size()) {
            return true;
        }
        if (j < 0 || j >= size()) {
            return true;
        }
        return false;
    }

    // 交换两个节点
    private void swap(int x, int y, int xx, int yy) {
        int temp = tiles[x][y];
        tiles[x][y] = tiles[xx][yy];
        tiles[xx][yy] = temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}

        Board board = (Board) o;

        return Arrays.deepEquals(tiles, board.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }
}
