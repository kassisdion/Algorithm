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
    private final int spaceIndex;   // Space position
    private int[] tiles;            // tiles array
    private final int[] goalArray;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new java.lang.NullPointerException();

        N = blocks.length;
        if (N < 2 || N > 128)
            throw new IllegalArgumentException("N must be <= 128");

        tiles = new int[N * N];
        for (int i=0; i < blocks.length; i++) {
            System.arraycopy(blocks[i], 0, tiles, i * N, blocks.length);
        }
        h = manhattanDistance(tiles, N);
        spaceIndex = indexOf(0, tiles);

        goalArray = new int[N * N];
        goalArray[N * N - 1] = 0;
        for (int i = 0; i < N * N - 1; i++) {
            goalArray[i] = i + 1;
        }
        System.out.println(Arrays.toString(goalArray));
    }

    // Build a new Board by sliding tile from given index.
    public Board(Board prev, int slidePosition) {
        N = prev.N;

        tiles =  Arrays.copyOf(prev.tiles, prev.tiles.length);

        // we swap
        tiles[prev.spaceIndex] = tiles[slidePosition];
        tiles[slidePosition] = 0;

        h = manhattanDistance(tiles, N);
        spaceIndex = indexOf(0, tiles);

        goalArray = prev.goalArray;
    }

    private int manhattanDistance(int[] tiles, int N) {
        int h = 0;

        for (int i = 0; i < tiles.length; i++) {

            int currentTile = tiles[i];

            if (currentTile != 0) {
                int xdest = (currentTile - 1)/ N - i/ N;
                int ydest = (currentTile - 1)% N - i% N;

                h += (Math.abs(xdest) + Math.abs(ydest));
            }
        }
        return h;
    }

    // Return the index of val in given byte array or -1 if none found.
    private static int indexOf(int value, int [] tiles) {
        for (int i=0; i < tiles.length; i++) {
            if (tiles[i] == value) {
                return i;
            }
        }
        return -1;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        return y instanceof Board && Arrays.equals(this.tiles, ((Board) y).tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tiles);
    }

    // is this board the goal board?
    boolean isGoal() {
        return Arrays.equals(goalArray, tiles);
    }

    // all neighboring boards
    ArrayList<Board> neighbors() {

        ArrayList<Board> neighbors = new ArrayList<>(4);

        // put all neighbor boards into the list

        // swap with above if not on the top row
        if (spaceIndex > N) {
            neighbors.add(new Board(this, spaceIndex - N));
        }

        // swap with bottom if not on the bottom row
        if (spaceIndex < (N * N - N)) {
            neighbors.add(new Board(this, spaceIndex + N));
        }

        //swap with left if not on the first col
        if (spaceIndex % N > 0) {
            neighbors.add(new Board(this, spaceIndex - 1));
        }

        //swap with right if not on the last col
        if (spaceIndex % N < N - 1) {
            neighbors.add(new Board(this, spaceIndex + 1));
        }

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        return Arrays.toString(tiles);
    }
}

