package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;  // the size of the percolation
    private int[][] percolationSystem;

    // create N-by-N grid, with all sited initiall
    public Percolation(int N) {
        if (N <= 0){
            throw new IndexOutOfBoundsException();
        }
        percolationSystem = new int[N][N];
    }

    // open the site (row, col) if it is not open
    public void open(int row, int col) {
        if ( isOutOfBounds(row, col) ) {
            throw new IllegalArgumentException();
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if ( isOutOfBounds(row, col) ) {
            throw new IllegalArgumentException();
        }

        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if ( isOutOfBounds(row, col) ) {
            throw new IllegalArgumentException();
        }

        return false;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return 0;
    }

    // does the system percolate?
    public boolean percolates() {
        return false;
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
