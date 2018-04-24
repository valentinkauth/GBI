

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FastA {

    private List<String> sequenceList = new ArrayList<String>();
    private List<String> headerList = new ArrayList<String>();


    // Read a file from a FileWriter, split data into header and sequences and add to corresponding list
    void read (Reader r) throws IOException {

        BufferedReader in = new BufferedReader(r);
        String line;
        int counter = 0;

        while((line=in.readLine())!=null) {
            if(line.startsWith(">")) {
                headerList.add(counter, line);
            } else {
                sequenceList.add(counter, line);
                counter++;
            }
        }

    }

    // Write to file from a FileWriter by "re-combining" headers and sequences
    void write (Writer w) throws IOException {

        String output = "";

        for (int i = 0; i < headerList.size(); i++) {
            output += (headerList.get(i) + "\n" + sequenceList.get(i) + "\n");
        }

        Writer writer = new BufferedWriter(w);
        writer.write(output);
        writer.close();
    }

    // Return size (length) of sequence list
    int size (){
        return headerList.size();
    }

    // Return header at position i
    String getHeader (int i) {

        String header = headerList.get(i);

        return header;

    }

    // Return sequence at position i
    String getSequence (int i){

        String sequence = sequenceList.get(i);

        return sequence;
    }

    // Add a new combination of header and sequence to corresponding list
    void add (String header, String sequence){

        headerList.add(header);
        sequenceList.add(sequence);

    }

}

