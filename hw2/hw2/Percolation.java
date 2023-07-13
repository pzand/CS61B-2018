package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF percolationSystem; // the percolation blocked, the N blocked is virtual top, the N+1 blocked is virtual bottom
    private WeightedQuickUnionUF perFull; // use for whether the percolation is full, the N blocked is virtual top. Avoid backwash
    private final int N;  // the size of the percolation
    private boolean[][] isOpen; // the block is open
    private int openSites; //number of opened sites
    private final int virtualTop; // the virtual top
    private final int virtualBottom; // the virtual bottom
    private final static int[] X = {-1, 1, 0, 0};
    private final static int[] Y = {0, 0, -1, 1};

    // create N-by-N grid, with all sited initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.virtualTop = N * N;
        this.virtualBottom = N * N + 1;
        this.percolationSystem = new WeightedQuickUnionUF(N * N + 2);
        this.perFull = new WeightedQuickUnionUF(N * N + 1);
        this.openSites = 0;
        this.N = N;
        this.isOpen = new boolean[N][N];

        // union the virtual top and the actual top
        for (int i = 0; i < N; i++) {
            percolationSystem.union(virtualTop, i);
            perFull.union(virtualTop, i);
        }
        for (int i = 0; i < N; i++) {
            percolationSystem.union(virtualBottom, N * (N - 1) + i);
        }
    }

    // open the site (row, col) if it is not open
    public void open(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (isOpen(row, col)) {
            return;
        }

        isOpen[row][col] = true;
        openSites++;
        for (int i = 0; i < X.length; i++) {
            int xx = row + X[i], yy = col + Y[i];
            if (isOutOfBounds(xx, yy)) {
                continue;
            }

            if (isOpen[xx][yy]) {
                percolationSystem.union(row * N + col, xx * N + yy);
                perFull.union(row * N + col, xx * N + yy);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }

        return isOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            return false;
        }

        return perFull.connected(virtualTop, row * N + col);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolationSystem.connected(virtualTop, virtualBottom);
    }

    // is out of bounds
    private boolean isOutOfBounds(int row, int col) {
        if (row < 0 || row >= N) {
            return true;
        }
        if (col < 0 || col >= N) {
            return true;
        }
        return false;
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
    }
}
