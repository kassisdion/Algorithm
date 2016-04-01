/**
 * Homework Assignment #4: Percolation
 *
 * - PercolationStats for simulation of percolation
 *
 * @ Student ID : 50151252
 * @ Name       : Faisant Florian
 **/

import java.util.Random;
import java.util.Arrays;

public class PercolationStats {

    private int mT;             // number of experiments to run
    private int mN;             // size of the grid
    private double mMean;       // mean of percolation threshold
    private double mStddev;     // standard deviation of percolation threshold

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException(
                            "N and T must be greater than 1");
        mT = T;
        mN = N;

        double[] results = run();
        mMean = calcMean(results);
        mStddev = calcStddev(results);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return mStddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mMean - 1.96 * mStddev / Math.sqrt(mT);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mMean + 1.96 * mStddev / Math.sqrt(mT);
    }

    /*
    ** Private method
    */
    private int randomInteger(int min, int max) {

        Random rand = new Random();

        // nextInt excludes the top value so we have to add 1 to include the top value
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }    

    private boolean addRandomSite(Percolation perc) {
        int i = randomInteger(1, mN);
        int j = randomInteger(1, mN);

        if (perc.isOpen(i, j) == false) {
            perc.open(i, j);
            return true;
        }
        return false;
    }

    private double[] run() {
        double fractions[] = new double[mT];

        int eC;//experiment count
        for (eC = 0; eC < mT; eC++) {
            
            Percolation perc = new Percolation(mN);
            int openedSite = 0;

            while (perc.percolates() == false) {

                if (addRandomSite(perc)) {
                    openedSite++;
                }
            }
            fractions[eC] = (double)openedSite / (double)(mN * mN);
        }

        return fractions;
    }

    private static double calcMean(double[] a) {
        if (a.length == 0) {
            return Double.NaN;
        }

        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }

        return sum / a.length;
    }

    private static double calcStddev(double[] a) {

        double var = 0.0;
        if (a.length == 0) {
            return Double.NaN;
        }
        double avg = calcMean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        var = sum / (a.length - 1);

        return Math.sqrt(var);
    }

    /*
    ** Main
    */
    public static void main(String[] args)    // test client(described below)
    {
        PercolationStats percStats;
        int N = 0;
        int T = 0;
        long start = System.currentTimeMillis();
        double time;

        // you must get two inputs N and T
        if (args.length > 0) {
            try {
                N = Integer.parseInt(args[0]);  // get N
                T = Integer.parseInt(args[1]);  // get T
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " and " 
                                            + args[1] + " must be integers.");
                return;
            }
        }

        // run experiment
        percStats = new PercolationStats(N, T);

        // print result
        System.out.println("mean                    = " + percStats.mean());
        System.out.println("stddev                  = " + percStats.stddev());
        System.out.println("95% confidence interval = "
                + percStats.confidenceLo() + ", " + percStats.confidenceHi());

        time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.printf("total time = %f sec (N = %d, T = %d)\n", time, N, T);
    }
}

