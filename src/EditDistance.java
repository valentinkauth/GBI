
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Compute edit distance using dynamic programming
 * Valentin Kauth & Ryan Goga
 * GBI, Daniel Huson, 4.2018
 */
public class EditDistance {


    private int[][] scoringMatrix;
    private Predecessor[][] tracebackMatrix;
    private String xSequence;
    private String ySequence;

    // Custom enum class for predecessor declaration in traceback matrix
    public enum Predecessor {
        DIAGONAL, VERTICAL, HORIZONTAL
    }


    /**
     * computes the edit distance between two sequences using dynamic programming
     * This method sets up and fills the dynamic programming matrix
     *
     * Coding scheme inspired (not copied) by http://www.java-uni.de/index.php?Seite=87
     *
     * @param x first sequence
     * @param y second sequence
     * @return edit distance
     */

    public int align(String x, String y) {

        // Define gap and mismatch scores (normal Levenshtein distance values chosen)
        final int gapCount = 1;
        final int mismatchCount = 1;
        final int matchCount = 0;

        // Instantiate sequence strings
        xSequence = x;
        ySequence = y;

        // Instantiate scoring and traceback matrices
        scoringMatrix = new int[x.length() + 1][y.length() + 1];
        tracebackMatrix = new Predecessor[x.length() + 1][y.length() + 1];


        // Initialize first element with 0
        scoringMatrix[0][0] = 0;
        // Initialize first element with DIAGONAL
        tracebackMatrix[0][0] = Predecessor.DIAGONAL;

        // Initialize first row and column by adding up the gap score and corresponding direction in traceback matrix
        for (int i = 1; i < x.length(); i++) {
            scoringMatrix[i][0] = i * gapCount;
            tracebackMatrix[i][0] = Predecessor.VERTICAL;
        }
        for (int j = 1; j < y.length(); j++) {
            scoringMatrix[0][j] = j * gapCount;
            tracebackMatrix[0][j] = Predecessor.HORIZONTAL;
        }


        // Iterate through matrices looking for the best (minimum) possible score for each cell
        for (int i = 1; i <= x.length(); i++)
            for (int j = 1; j <= y.length(); j++) {

                // Check diagonal, vertical and horizontal neighboring cells for minimum score
                int horizontalScore = scoringMatrix[i][j - 1] + gapCount;
                int verticalScore = scoringMatrix[i - 1][j] + gapCount;
                int diagonalScore = scoringMatrix[i - 1][j - 1] + ((x.charAt(i - 1) == y.charAt(j - 1)) ? matchCount : mismatchCount);

                int min = Math.min(Math.min(horizontalScore, verticalScore), diagonalScore);

                // Write in traceback matrix where the predecessor of the current cell came/scored from
                // This way might seem unneccesarly complicated, still a seperate traceback matrix might reduce computation in the traceback process
                if (diagonalScore == min) {
                    tracebackMatrix[i][j] = Predecessor.DIAGONAL;

                } else if (verticalScore == min) {
                    tracebackMatrix[i][j] = Predecessor.VERTICAL;

                } else if (horizontalScore == min) {
                    tracebackMatrix[i][j] = Predecessor.HORIZONTAL;

                }

                // Assign best (minimum) score to scoring matrix
                scoringMatrix[i][j] = min;


            }

        // Return last element of scoring matrix
        return scoringMatrix[x.length()][y.length()];

    }

    /**
     * perform traceback and print an optimal alignment to  the console (standard output)
     * This method assumes that the method align has already been run and that the dynamic programming
     * matrix has been computed and is stored in the class
     */
    public void traceBackAndShowAlignment() {

        // Check if "global" scoring matrix, traceback matrix and sequence strings were set
        if (scoringMatrix == null || tracebackMatrix == null || xSequence == null || ySequence == null) {
            System.out.println("Scoring matrix, predecessor matrix and/or sequences not set yet, call align method first");
            return;
        }

        // Instantiate empty aligned sequences as string builder class
        StringBuilder sbX = new StringBuilder(xSequence.length());
        StringBuilder sbY = new StringBuilder(ySequence.length());

        // Set starting point for traceback matrix backwards iteration
        int i = tracebackMatrix.length - 1;
        int j = tracebackMatrix[0].length - 1;

        // Iterate backwards through traceback matrix until initial cell (0,0) is reached
        while (!(i == 0 && j == 0)) {

            // Check current cell for predecessor (where the best score came from)
            Predecessor predecessor = tracebackMatrix[i][j];

            // Add values to string builder based on the direction of predecessor cell
            switch (predecessor) {
                // Diagonal --> Match, add both letters to the aligned sequences
                case DIAGONAL:
                    sbX.append(xSequence.charAt(i - 1));
                    sbY.append(ySequence.charAt(j - 1));
                    i -= 1;
                    j -= 1;
                    break;

                // Horizontal --> Add gap to first/ aligned sequence, add letter to second aligned sequence
                case HORIZONTAL:
                    sbX.append("-");
                    sbY.append(ySequence.charAt(j - 1));
                    j -= 1;
                    break;

                // Vertical --> Add gap to second aligned sequence, add letter to first aligned sequence
                case VERTICAL:
                    sbX.append(xSequence.charAt(i - 1));
                    sbY.append("-");
                    i -= 1;
                    break;
            }
        }

        // Reverse and print aligned sequences to console
        System.out.print("Sequence 1: ");
        System.out.println(sbX.reverse().toString());
        System.out.print("Sequence 2: ");
        System.out.println(sbY.reverse().toString());

    }

    /**
     * main program: reads two sequences in fastA format and computes their optimal alignment score.
     *
     * @param args commandline arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Valentin Kauth & Ryan Goga");

        if (args.length != 1)
            throw new IOException("Usage: EditDistanceDP fileName");

        String fileName = args[0];
        FileReader reader = new FileReader(fileName);

        FastA fastA = new FastA();
        fastA.read(reader);
        reader.close();

        EditDistance editDistanceDP = new EditDistance();

        if (fastA.size() == 2) {
            int editDistance = editDistanceDP.align(fastA.getSequence(0), fastA.getSequence(1));

            System.out.println("Edit distance is=" + editDistance);

            System.out.println("An optimal alignment=");
            editDistanceDP.traceBackAndShowAlignment();
        }
    }
}
