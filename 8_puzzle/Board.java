/**
 * Homework Assignment #6: "8-Puzzle"
 *
 *  - Board class for solving "8-Puzzle" Programming Assignment
 *
 *  Compilation:  javac Board.java Queue.java
 *
 * @ Student ID : 50151252
 * @ Name       : Faisant Florian
 **/

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Board {
    private int[][] tiles;
    private int N;
    private int mManhattan = -1;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new java.lang.NullPointerException();

        N = blocks.length;
        if (N < 2 || N > 128)
            throw new IllegalArgumentException("N must be <= 128");

        tiles = new int[N][N];
        for (int i = 0; i < N; i++)
            System.arraycopy(blocks[i], 0, tiles[i], 0, blocks[i].length);
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (mManhattan != -1) {
            return mManhattan;
        }
        mManhattan = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int currentTile = tiles[i][j];
                if (currentTile > 0) {
                    int xdest = (currentTile - 1) / N;
                    int ydest = (currentTile - 1) % N;
                    int x = i - xdest;
                    int y = j - ydest;

                    mManhattan += Math.abs(x) + Math.abs(y);
                }
            }

        }
        return mManhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                int expect = i * N + j + 1;
                if (expect != (N * N)) {
                    if (tiles[i][j] != expect) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void swap(int[][] blocks, int r1, int c1, int r2, int c2) {
        if (r1 < 0 || c1 < 0 || r2 < 0 || c2 < 0)
            throw new IndexOutOfBoundsException("row/col index < 0");
        if (r1 >= N || c1 >= N || r2 >= N || c2 >= N)
            throw new IndexOutOfBoundsException("row/col index >= N");

        // swap blocks
        int tmp = blocks[r1][c1];
        blocks[r1][c1] = blocks[r2][c2];
        blocks[r2][c2] = tmp;
    }

    // a board that is obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            System.arraycopy(tiles[i], 0, blocks[i], 0, tiles[i].length);

        //we loop over the tiles until we can swap blocks[i][j - 1] with block[i][j-1]
        for (int i = 0; i < N; i++) {
            for (int j = 1; j < N; j++) {
                if (blocks[i][j - 1] != 0 && blocks[i][j] != 0) {
                    swap(blocks, i, j, i, j - 1);
                }
            }
        }
        return new Board(blocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !(y instanceof Board)) {
            return false;
        }

        if (y == this) {
            return true;
        }

        Board tmp = (Board) y;
        return Arrays.deepEquals(this.tiles, tmp.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        Queue<Board> nbrs = new Queue<Board>();

        // put all neighbor boards into the queue

        //we search for empty tile
        int zeroRow = 0;
        int zeroCol = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (tiles[row][col] == 0) {
                    zeroRow = row;
                    zeroCol = col;
                    break;
                }
            }
        }
        // swap with above if not on the top row
        if (zeroRow > 0) {
            int[][] blocks = new int[N][N];
            for (int i = 0; i < N; i++) {
                System.arraycopy(tiles[i], 0, blocks[i], 0, tiles[i].length);
            }
            swap(blocks, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            nbrs.enqueue(new Board(blocks));
        }

        // swap with bottom if not on the bottom row
        if (zeroRow < N - 1) {
            int[][] blocks = new int[N][N];
            for (int i = 0; i < N; i++) {
                System.arraycopy(tiles[i], 0, blocks[i], 0, tiles[i].length);
            }
            swap(blocks, zeroRow, zeroCol, zeroRow + 1, zeroCol);
            nbrs.enqueue(new Board(blocks));
        }

        //swap with left if not on the first col
        if (zeroCol > 0) {
            int[][] blocks = new int[N][N];
            for (int i = 0; i < N; i++) {
                System.arraycopy(tiles[i], 0, blocks[i], 0, tiles[i].length);
            }
            swap(blocks, zeroRow, zeroCol, zeroRow, zeroCol - 1);
            nbrs.enqueue(new Board(blocks));
        }

        //swap with right if not on the last col
        if (zeroCol < N - 1) {
            int[][] blocks = new int[N][N];
            for (int i = 0; i < N; i++) {
                System.arraycopy(tiles[i], 0, blocks[i], 0, tiles[i].length);
            }
            swap(blocks, zeroRow, zeroCol, zeroRow, zeroCol + 1);
            nbrs.enqueue(new Board(blocks));
        }

        return nbrs;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (optional)
    public static void main(String[] args) {
        // read the input file
        Scanner in;
        try {
            in = new Scanner(new File(args[0]), "UTF-8");
        } catch (Exception e) {
            System.out.println("invalid or no input file ");
            return;
        }

        // create initial board from file
        int N = in.nextInt();
        int[][] blocks = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = (int) in.nextInt();
                if (blocks[i][j] >= N * N)
                    throw new IllegalArgumentException("value must be < N^2");
                if (blocks[i][j] < 0)
                    throw new IllegalArgumentException("value must be >= 0");
            }
        }

        Board initial = new Board(blocks);

        System.out.println("\n=== Initial board ===");
        System.out.println(" - manhattan = " + initial.manhattan());
        System.out.println(initial.toString());

        Board twin = initial.twin();

        System.out.println("\n=== Twin board ===");
        System.out.println(" - manhattan = " + twin.manhattan());
        System.out.println(" - equals = " + initial.equals(twin));
        System.out.println(twin.toString());

        System.out.println("\n=== Neighbors ===");
        for (Board board : initial.neighbors())
            System.out.println(board);
    }
}

