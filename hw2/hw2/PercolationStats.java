package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int T; // perform T independent experiments
    private final int N; // N-by-N grid
    private final PercolationFactory pf;
    private int[] nSitesToPercolation;
    private double mean;
    private double stddev;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        this.T = T;
        this.N = N;
        this.pf = pf;
        completePercolationByT();
    }

    // sample mean of percolation threshold
    public double mean() {
        if (this.mean > 0) {
            return mean;
        }
        mean = StdStats.mean(nSitesToPercolation);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (this.stddev > 0) {
            return stddev;
        }
        stddev = StdStats.stddev(nSitesToPercolation);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    private void completePercolationByT() {
        nSitesToPercolation = new int[N];
        for (int i = 0; i < N; i++) {
            Percolation per = pf.make(N);
            nSitesToPercolation[i] = completeAPercolation(per);
        }
    }

    private int completeAPercolation(Percolation per) {
        while (!per.percolates()) {
            int row = StdRandom.uniform(N);
            int col = StdRandom.uniform(N);
            per.open(row, col);
        }
        return per.numberOfOpenSites();
    }
}
