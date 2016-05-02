/**
 * Homework Assignment #6: "8-Puzzle"
 * <p>
 * - Board class for solving "8-Puzzle" Programming Assignment
 * <p>
 * Compilation:  javac Board.java Queue.java
 *
 * @ Student ID : 50151252
 * @ Name       : Faisant Florian
 **/

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    final int h;                    // heuristic value
    private final int N;            // tiles length
    private final int[] spaceIndex; // Space position [0]=row, [1]=col
    private int[][] tiles;          // tiles array

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

        h = manhattanDistance(tiles);
        spaceIndex = indexOf(0, tiles);
    }

    // Build a new Board by sliding tile from given index.
    public Board(Board prev, int[] slidePosition) {
        N = prev.tiles.length;

        tiles = new int[N][N];
        for (int i = 0; i < N; i++)
            System.arraycopy(prev.tiles[i], 0, tiles[i], 0, prev.tiles[i].length);

        swap(prev.tiles, prev.spaceIndex[0], prev.spaceIndex[1], slidePosition[0], slidePosition[1]);
        h = manhattanDistance(tiles);
        spaceIndex = indexOf(0, tiles);
    }

    private static int manhattanDistance(int[][] tiles) {
        int h = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                int currentTile = tiles[i][j];
                if (currentTile > 0) {
                    int xdest = (currentTile - 1) / tiles.length;
                    int ydest = (currentTile - 1) % tiles.length;
                    int x = i - xdest;
                    int y = j - ydest;

                    h += Math.abs(x) + Math.abs(y);
                }
            }
        }
        return h;
    }

    // Return the index of val in given byte array or {-1, -1} if none found.
    private static int[] indexOf(int value, int [][] tiles) {
        //we loop over the tiles until we found the value
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 1; j < tiles.length; j++) {
                if (tiles[i][j] == value) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        return y instanceof Board && Arrays.deepEquals(this.tiles, ((Board) y).tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tiles);
    }

    // is this board the goal board?
    boolean isGoal() {
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
    Board twin() {
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

    // all neighboring boards
    ArrayList<Board> neighbors() {

        ArrayList<Board> neighbors = new ArrayList<>(4);

        // put all neighbor boards into the list

        // swap with above if not on the top row
        if (spaceIndex[0] > 0) {
            neighbors.add(new Board(this, new int[]{spaceIndex[0] - 1, spaceIndex[1]}));
        }

        // swap with bottom if not on the bottom row
        if (spaceIndex[0] < N - 1) {
            neighbors.add(new Board(this, new int[]{spaceIndex[0] + 1, spaceIndex[1]}));
        }

        //swap with left if not on the first col
        if (spaceIndex[1] > 0) {
            neighbors.add(new Board(this, new int[]{spaceIndex[0], spaceIndex[1] - 1}));
        }

        //swap with right if not on the last col
        if (spaceIndex[1] < N - 1) {
            neighbors.add(new Board(this, new int[]{spaceIndex[0], spaceIndex[1] + 1}));
        }

        return neighbors;
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
}

