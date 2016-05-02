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
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Solver {

    private Node mSolution;

    // search node
    private static class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node prev;

        Node(Board board, int moves, Node prev) {
            if (board == null)
                throw new java.lang.NullPointerException();

            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        // calculate priority of this search node
        int priority() {
            return board.manhattan() + moves;
        }

        // compare node by priority (implements Comparable<Node>)
        public int compareTo(Node that) {
            return this.priority() - that.priority();
        }
    }


    //Thread for Solver
    private static class SolverTask extends Thread {

        private interface ThreadCompleteListener {
            void notifyOfThreadComplete(Node solution);
        }

        private MinPQ<Node> pq;
        private Node node;
        private List<Board> save = new LinkedList<>();
        private ThreadCompleteListener listener;

        SolverTask(MinPQ<Node> pq, Node startingNode) {
            this.pq = pq;
            this.node = startingNode;
            this.listener = null;
        }

        void setListener(ThreadCompleteListener listener) {
            this.listener = listener;
        }

        public void run() {
            Boolean running = true;
            while (running && !this.isInterrupted()) {
                if (node.board.isGoal()) {
                    listener.notifyOfThreadComplete(node);
                    running = false;
                } else {
                    node = step(pq, save);
                }
            }
        }

        private Node step(MinPQ<Node> pq, List<Board> save) {
            Node least = pq.delMin();
            for (Board neighbor : least.board.neighbors()) {
                if (least.prev == null || !neighbor.equals(least.prev.board)) {
                    if (!save.contains(neighbor)) {
                        pq.insert(new Node(neighbor, least.moves + 1, least));
                        save.add(neighbor);
                    }
                }
            }
            return least;
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.NullPointerException();

        // create initial search node (and it's twin)
        Node startingNode = new Node(initial, 0, null);
        Node twinNode = new Node(initial.twin(), 0, null);

        // priority queue for calculation
        MinPQ<Node> mainPq = new MinPQ<>();
        MinPQ<Node> twinPq = new MinPQ<>();

        // insert the initial search node into a priority queue
        mainPq.insert(startingNode);
        twinPq.insert(twinNode);

        // instantiate 2 Thread
        final SolverTask mainSolver = new SolverTask(mainPq, startingNode);
        final SolverTask twinSolver = new SolverTask(twinPq, twinNode);

        mainSolver.setListener(new SolverTask.ThreadCompleteListener() {
            @Override
            public void notifyOfThreadComplete(Node solution) {
                twinSolver.interrupt();
                mSolution = solution;
            }
        });

        twinSolver.setListener(new SolverTask.ThreadCompleteListener() {
            @Override
            public void notifyOfThreadComplete(Node solution) {
                mainSolver.interrupt();
                mSolution = solution;
            }
        });

        // solve the puzzle
        mainSolver.start();
        twinSolver.start();

        try {
            mainSolver.join();
            twinSolver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

