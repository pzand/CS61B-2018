package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int T; // perform T independent experiments
    private final int N; // N-by-N grid
    private final PercolationFactory pf;
    private double[] nSitesToPercolation;

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
        return StdStats.mean(nSitesToPercolation);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(nSitesToPercolation);
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
        nSitesToPercolation = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation per = pf.make(N);
            nSitesToPercolation[i] = (double) completeAPercolation(per) / (N * N);
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
