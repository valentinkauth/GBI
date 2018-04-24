
import java.io.*;

public class ReverseComplement {

    public static void main(String args[]) throws IOException {

        // Create classes for input and output data
        FastA myFastA = new FastA();
        FastA myFastAreversed = new FastA();

        // Specify input location
        final File inputFile = new File("src/dna_01.fasta");
        final File outputFile = new File("src/dna_01_reversed.fasta");

        Reader r = new FileReader(inputFile);

        // Read new fastA file into class
        myFastA.read(r);

        // Get size to perform iteration
        int fastAlength = myFastA.size();

        // Iterate through sequences, reverse each sequence, and add reversed sequence to FastA reversed class
        for (int i = 0; i < myFastA.size(); i++) {
            String sequenceReversed = "";
            String sequence = myFastA.getSequence(i);

            for (char c: sequence.toCharArray()) {
                switch (c) {
                    case 'A': sequenceReversed += 'T';
                        break;
                    case 'T': sequenceReversed += 'A';
                        break;
                    case 'C': sequenceReversed += 'G';
                        break;
                    case 'G': sequenceReversed += 'C';
                        break;
                    default: break;
                }

            }
            // Add header and reversed sequene to reversed FastA class
            myFastAreversed.add(myFastA.getHeader(i), sequenceReversed);


        }


        // Create the output file and use class method to write file
        Writer w = new FileWriter(outputFile);
        myFastAreversed.write(w);

    }

}
