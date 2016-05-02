/**
 * Homework Assignment #6: "8-Puzzle"
 * <p>
 * - Solver class for solving "8-Puzzle" Programming Assignment
 * <p>
 * Compilation:  javac Solver.java Board.java
 * Execution:    java Solver inputfile.txt
 * Dependencies: MinPQ
 *
 * @ Student ID : 50151252
 * @ Name       : Faisant Florian
 **/

import java.io.File;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Solver {

    private boolean mSolved;
    private Node mSolution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.NullPointerException();

        mSolved = false;
        mSolution = null;

        solve(initial);
    }

    //Solve the puzzle
    private void solve(Board initial) {
        // create initial search node (and it's twin)
        Node startingNode = new Node(initial, 0, null);

        // priority queue for calculation
        PriorityQueue<Node> mainPq = new PriorityQueue<>(100);

        // store visited node
        HashSet<Board> mainClosed = new HashSet<>();

        // insert the initial search node into a priority queue
        mainPq.add(startingNode);


        System.out.println(startingNode.board.toString());
        System.out.println();

        for (Board board :startingNode.board.neighbors()) {
            System.out.println(board.toString());
            System.out.println();
        }

        // solve the puzzle
        while (!mainPq.isEmpty() && !mSolved) {
            //get the lowest priority state
            startingNode = mainPq.poll();

            // If it's the goal, we're done.
            if (startingNode.board.isGoal()) {
                mSolution = startingNode;
                mSolved = true;
            }

            // Make sure we don't revisit this node.
            mainClosed.add(startingNode.board);

            for (Board neighbor : startingNode.board.neighbors()) {
                if (neighbor != null && !mainClosed.contains(neighbor)) {
                    mainPq.add(new Node(neighbor, startingNode.moves + 1, startingNode.prev));
                }
            }
        }
    }

    // is the initial board solvable?
    private boolean isSolvable() {
        return mSolution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    private int moves() {
        return mSolution == null ? -1 : mSolution.moves;
    }

    // sequence of boards in a intest solution; null if unsolvable
    private Iterable<Board> solution() {
        if (mSolution == null)
            return null;
        Stack<Board> sol = new Stack<>();
        Node searchNode = mSolution;
        while (searchNode != null) {
            sol.push(searchNode.board);
            searchNode = searchNode.prev;
        }
        return sol;
    }

    // search node
    private class Node implements Comparable<Node> {
        private Board board;
        private int moves; //number of moves from start
        private Node prev; //previous status

        Node(Board board, int moves, Node prev) {
            if (board == null)
                throw new java.lang.NullPointerException();

            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        // calculate priority of this search node for A*
        int priority() {
            return board.h + moves;
        }

        @Override
        public int compareTo(Node o) {
            return this.priority() - o.priority();
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // to calculate running time
        long start = System.currentTimeMillis();
        double time;

        // read the input file
        Scanner in;
        String filename = args[0];
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
                blocks[i][j] = in.nextInt();
                if (blocks[i][j] >= N * N)
                    throw new IllegalArgumentException("value must be < N^2");
                if (blocks[i][j] < 0)
                    throw new IllegalArgumentException("value must be >= 0");
            }
        }

        // initial board
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves() + "\n");
            for (Board board : solver.solution())
                System.out.println(board);
        }

        // calculate running time
        time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.println("time = " + time + "sec");
        System.out.println("Minimum number of moves = " + solver.moves() + "\n");
    }
}