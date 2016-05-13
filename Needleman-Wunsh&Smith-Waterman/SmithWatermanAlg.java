/**
 * SmithWatermaAlg
 * - Smith-Waterman Algorithm
 *
 * @author      Jeongyeup Paek (jpaek@usc.edu)
 * @modified    Nov. 15, 2010
 *
 * @ Student ID : 50151252
 * @ Name       : Faisant Florian
 *
 **/

import java.util.Vector;

class SmithWatermanAlg {

    // scoring scheme
    private double MATCH     =  1.0; // +1 for letters that match
    private double MISMATCH  = -1.0; // -1 for letters that mismatch
    private double GAP       = -1.0; // -1 for any gap

    private enum PointerPosition {
        NONE(""),
        LEFT("--"),
        UP("|"),
        DIAGONAL("\\");

        public final String value;
        PointerPosition(String value) {
            this.value = value;
        }
    }

    private double[][] matrix_score;
    private PointerPosition[][] matrix_pointer;

    // return the match results!!!
    private int max_i;
    private int max_j;
    private double max_score;

    private String matchedString1;
    private String matchedString2;

    private String[] seqarr1;
    private String[] seqarr2;

    /*
    ** Constructor
     */
    public SmithWatermanAlg() {
        this(1.0, -1.0, -1.0);
    }

    public SmithWatermanAlg(int _mismatch, int _gap) {
        this(1.0, _mismatch, _gap);
    }

    public SmithWatermanAlg(double match, double mismatch, double gap) {
        this.MATCH = match;
        this.MISMATCH = mismatch;
        this.GAP = gap;

        this.reset();
    }

    /*
    ** Public method
     */
    public double run(String seq1, String seq2) {
        String[] seqarr1 = seq1.split("\\s+");
        String[] seqarr2 = seq2.split("\\s+");
        return run(seqarr1, seqarr2);
    }

    public double run(String[] seqarr1, String[] seqarr2) {
        this.seqarr1 = seqarr1;
        this.seqarr2 = seqarr2;
        return (run());
    }

    public void print_score_matrix() {
        int length1 = seqarr1.length;
        int length2 = seqarr2.length;

        for (int i = -1; i < length1; i++) {
            System.out.printf("%-5s", i < 0 ? " " : seqarr1[i]);
        }
        System.out.println();

        for (int i = 1; i <= length2; i++) {
            System.out.printf("%-2s", i == 0 ? " " : seqarr2[i - 1]);
            for (int j = 1; j <= length1; j++) {
                System.out.printf("%4.0f ", matrix_score[i][j]);
            }
            System.out.println();
        }
    }

    public void print_pointer_matrix() {
        int length1 = seqarr1.length;
        int length2 = seqarr2.length;

        for (int i = -1; i < length1; i++) {
            System.out.printf("%-5s", i < 0 ? " " : seqarr1[i]);
        }
        System.out.println();

        for (int i = 1; i <= length2; i++) {
            System.out.printf("%-2s", i == 0 ? "" : seqarr2[i - 1]);
            for (int j = 1; j <= length1; j++) {
                System.out.printf("%5s", matrix_pointer[i][j].value);
            }
            System.out.println();
        }
    }

    public void print_match() {
        System.out.println(matchedString1);
        System.out.println(matchedString2);
    }

    /*
    ** Private method
     */
    private void reset() {
        this.max_i = 0;
        this.max_j = 0;
        this.max_score = 0;
        this.matchedString1 = "";
        this.matchedString2 = "";
    }


    public double run() {
        int len1 = seqarr1.length;
        int len2 = seqarr2.length;

        this.reset();

        // initialization
        matrix_score = new double[len2 + 1][len1 + 1];
        matrix_pointer = new PointerPosition[len2 + 1][len1 + 1];

        matrix_score[0][0] = 0;
        matrix_pointer[0][0] = PointerPosition.NONE;

        for(int j = 1; j <= len1; j++) {
            matrix_score[0][j]   = 0;
            matrix_pointer[0][j] = PointerPosition.NONE;
        }
        for (int i = 1; i <= len2; i++) {
            matrix_score[i][0]   = 0;
            matrix_pointer[i][0] = PointerPosition.NONE;
        }

        // fill
        for(int i = 1; i <= len2; i++) {
            for(int j = 1; j <= len1; j++) {
                double diagonal_score;
                double left_score;
                double up_score;

                // calculate match score
                if (seqarr1[j-1].equals(seqarr2[i-1])) {
                    diagonal_score = matrix_score[i-1][j-1] + MATCH;
                }
                else {
                    diagonal_score = matrix_score[i-1][j-1] + MISMATCH;
                }

                // calculate gap scores
                up_score   = matrix_score[i-1][j] + GAP;
                left_score = matrix_score[i][j-1] + GAP;

                if ((diagonal_score <= 0) && (up_score <= 0) && (left_score <= 0)) {
                    matrix_score[i][j]   = 0;
                    matrix_pointer[i][j] = PointerPosition.NONE;
                    continue; // terminate this iteration of the loop
                }

                // choose best score
                if (diagonal_score >= up_score) {
                    if (diagonal_score >= left_score) {
                        matrix_pointer[i][j] = PointerPosition.DIAGONAL;
                        matrix_score[i][j] = diagonal_score;
                    }
                    else {
                        matrix_pointer[i][j] = PointerPosition.LEFT;
                        matrix_score[i][j] = left_score;
                    }
                } else {
                    if (up_score >= left_score) {
                        matrix_pointer[i][j] = PointerPosition.UP;
                        matrix_score[i][j] = up_score;
                    }
                    else {
                        matrix_pointer[i][j] = PointerPosition.LEFT;
                        matrix_score[i][j] = left_score;
                    }
                }

                // set maximum score
				if (matrix_score[i][j] > max_score) {
					max_i     = i;
					max_j     = j;
					max_score = matrix_score[i][j];
				}
            }
        }

        // trace-back
        Vector<String> align1 = new Vector<>();
        Vector<String> align2 = new Vector<>();
        int j = max_j;
        int i = max_i;

        while (true) {
            if (matrix_pointer[i][j] == PointerPosition.NONE)
                break;

            if (matrix_pointer[i][j] == PointerPosition.DIAGONAL) {
                align1.add(0, seqarr1[j-1]);
                align2.add(0, seqarr2[i-1]);
                i--; j--;
            }
            else if (matrix_pointer[i][j] == PointerPosition.LEFT) {
                align1.add(0, seqarr1[j-1]);
                align2.add(0, "-"); //"-"
                j--;
            }
            else if (matrix_pointer[i][j] == PointerPosition.UP) {
                align1.add(0, "-"); //"-"
                align2.add(0, seqarr2[i-1]);
                i--;
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////

        this.matchedString1 = "";
        this.matchedString2 = "";

        for (int k = 0; k < align1.size(); k++) {
            if (!matchedString1.equals("")) matchedString1 += " ";
            matchedString1 += align1.elementAt(k);
        }
        for (int k = 0; k < align2.size(); k++) {
            if (!matchedString2.equals("")) matchedString2 += " ";
            matchedString2 += align2.elementAt(k);
        }

        this.max_j = this.max_j - 1;
        this.max_i = this.max_i - 1;

        return max_score;
    }

    /*
    ** Main
     */
	public static void main(String[] args) {
        //Rad input file
        if (args.length != 2) {
            System.out.println("invalid or no input file ");
            return;
        }

        SmithWatermanAlg smithWatermanAlg = new SmithWatermanAlg();
        smithWatermanAlg.run(args[0].replace(" ", "").split("(?!^)"), args[1].replace(" ", "").split("(?!^)"));

        System.out.println("Match found:");
        smithWatermanAlg.print_match();
        System.out.println();
        smithWatermanAlg.print_score_matrix();
        System.out.println();
        smithWatermanAlg.print_pointer_matrix();
    }

}

