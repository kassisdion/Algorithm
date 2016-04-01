/**
 * Homework Assignment #4: Percolation
 *
 *  - Percolation data structure
 *
 * @ Student ID : 50151252
 * @ Name       : Faisant Florian
 **/

import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
public class Percolation {

    private static final boolean SITE_BLOCKED = false;
    private static final boolean SITE_OPEN = true;
    private boolean[] sites;          // sites[i] = state of site i
    private int mN;                   // remember the input N
    private int topIdx;               // idx of the special top
    private int bottomIdx;            // idx of the speical bottom
    private WeightedQuickUnionUF uf;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("N must be >0");
        mN = N;

        bottomIdx = mN * mN + 1;
        topIdx = 0;
        uf = new WeightedQuickUnionUF(mN * mN + 2);
        sites = new boolean[mN * mN];
    }

    private void checkIndex(int i, int j) {
        if (i < 1 || i > mN)
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j < 1 || j > mN)
            throw new IndexOutOfBoundsException("column index j out of bounds");
    }

    private int getQFIndex(int i, int j) {
        return (mN * (i - 1) + (j - 1));
    }

    // open site(row i, column j) if it is not open already
    public void open(int i, int j) {
        checkIndex(i, j);

        sites[getQFIndex(i, j)] = SITE_OPEN;
    
        //check if we are on the top line
        if (i == 1) {
            //Connect directly to the top node
            uf.union(getQFIndex(i, j), topIdx);
        }

        //check if we are on the bottom line
        if (i == mN) {
            //connect directly on the bottom node
            uf.union(getQFIndex(i, j), bottomIdx);
        }

        //check if the right node is open
        if (j > 1 && isOpen(i, j - 1)) {
            uf.union(getQFIndex(i, j), getQFIndex(i, j - 1));
        }
        //check if the left node is open
        if (j < mN && isOpen(i, j + 1)) {
            uf.union(getQFIndex(i, j), getQFIndex(i, j + 1));
        }
        //check if the top node is open
        if (i > 1 && isOpen(i - 1, j)) {
            uf.union(getQFIndex(i, j), getQFIndex(i - 1, j));
        }
        //check if the bottom node is open
        if (i < mN && isOpen(i + 1, j)) {
            uf.union(getQFIndex(i, j), getQFIndex(i + 1, j));
        }
    }

    // is site(row i, column j) open?
    public boolean isOpen(int i, int j) {
        checkIndex(i, j);
        return (sites[getQFIndex(i, j)] == SITE_OPEN);
    }

    // is site(row i, column j) full?
    public boolean isFull(int i, int j) {
        checkIndex(i, j);
        return uf.connected(topIdx, getQFIndex(i, j));
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(topIdx, bottomIdx);
    }

    // test client(optional)
    public static void main(String[] args)
    {
        Scanner in;
        int N = 0;
        long start = System.currentTimeMillis();

        try {
            // get input file from argument
            in = new Scanner(new File(args[0]), "UTF-8");
        } catch (Exception e) {
            System.out.println("invalid or no input file ");
            return;
        }

        N = in.nextInt();         // N-by-N percolation system
        System.out.printf("N = %d\n", N);

        // repeatedly read in sites to open and draw resulting system
        Percolation perc = new Percolation(N);

        while (in.hasNext()) {
            int i = in.nextInt();   // get i for open site (i,j)
            int j = in.nextInt();   // get j for open site (i,j)
            perc.open(i, j);        // open site (i,j)
            System.out.printf("open(%d, %d)\n", i, j);
        }
        if (perc.percolates()) {
            System.out.println("This system percolates");
        } else {
            System.out.println("This system does NOT percolate");
        }

        double time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.println("running time = "+ time + " sec");
    }
}

