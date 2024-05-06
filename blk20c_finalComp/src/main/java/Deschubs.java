/*
Bailey Kimmel
SE 2 Project File Compression: B Reeves 2024

TESTS: run MVN TEST
INSTALLATION: to install:
    -run mvn compile OR
    -clone via Github 

TEST INSTRUCTIONS: to test, please put a test folder in this dirertory with files to be compressed.
also add a folder in this directory with compressed files to be tested. 

RUN EXAMPLE:
- to run compression via HUFFMAN: java SchubsH <filename...>
to decompress: java Deschubs <filename...>
 */

import java.io.File;
import java.io.IOException;

public class Deschubs {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Deschubs <filename>");
            System.exit(1);
        }

        String compressedFile = args[0];
        String outputFilename = compressedFile.substring(0, compressedFile.lastIndexOf('.'));

        File outputFile = new File(outputFilename);

        // Check if the output file already exists
        if (outputFile.exists()) {
            System.err.println("Error: The output file " + outputFilename
                    + " already exists. Operation aborted to prevent data loss.");
            System.exit(2); // Exit to prevent overwriting the existing file
        }

        if (compressedFile.endsWith(".hh") || compressedFile.endsWith(".zh")) {
            HuffmanSE2.decompress(compressedFile, outputFilename);
        } else {
            System.err.println("Unsupported file type.");
            System.exit(3);
        }
    }
}
