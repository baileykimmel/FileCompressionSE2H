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
to run compression via LZW: java SchubsL <filename...>'
to run an archive: java SchubsArc <archive name><filename...>
to decompress: java Deschubs <filename...>
 */

import java.io.File;

public class SchubsH {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java SchubsH <filename> [<filename>...]");
            System.exit(1);
        }

        for (String filePath : args) {
            File file = new File(filePath);
            if (file.exists()) {
                compressFile(filePath);
            } else {
                System.err.println("File not found: " + filePath);
            }
        }
    }

    private static void compressFile(String inputFile) {
        String outputFile = inputFile + ".hh";

        try {
            HuffmanSE2.compress(inputFile, outputFile, false);
            System.out.println("Compressed: " + inputFile + " -> " + outputFile);
        } catch (Exception e) {
            System.err.println("Error compressing file: " + inputFile);
            e.printStackTrace();
        }
    }
}