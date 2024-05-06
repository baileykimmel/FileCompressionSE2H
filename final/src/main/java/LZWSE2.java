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

import java.io.*;
import java.util.*;

public class LZWSE2 {
    private static final int R = 256; // Number of input chars
    private static final int L = 4096; // Maximum number of codewords in dictionary
    private static final int W = 12; // Codeword width
    private static int bitBuffer = 0; // Buffer to hold the bits
    private static int nBits = 0; // Number of bits currently in the buffer

    public static void compress(String inputFilename, String outputFilename) throws IOException {
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < R; i++) {
            dictionary.put("" + (char) i, i);
        }

        int dictSize = R; // next available codeword value
        String w = "";
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(inputFilename));
                FileOutputStream out = new FileOutputStream(outputFilename);
                BufferedOutputStream bufferOut = new BufferedOutputStream(out)) {
            for (int c = in.read(); c != -1; c = in.read()) {
                String wc = w + (char) c;
                if (dictionary.containsKey(wc)) {
                    w = wc;
                } else {
                    writeCode(bufferOut, dictionary.get(w));
                    if (dictSize < L) {
                        dictionary.put(wc, dictSize++);
                    }
                    w = "" + (char) c;
                }
            }
            // Write the last code.
            if (!w.equals("")) {
                writeCode(bufferOut, dictionary.get(w));
            }
            flushBits(bufferOut); // Ensure all bits are written out
        }
    }

    private static void writeCode(BufferedOutputStream out, int code) throws IOException {
        System.out.println("Writing code: " + code + " Current bitBuffer: " + Integer.toBinaryString(bitBuffer));
        bitBuffer |= (code << nBits);
        nBits += W;
        while (nBits >= 8) {
            int byteToWrite = bitBuffer & 255;
            System.out.println("Output byte: " + Integer.toBinaryString(byteToWrite));
            out.write(byteToWrite);
            bitBuffer >>= 8;
            nBits -= 8;
        }
    }

    private static void flushBits(BufferedOutputStream out) throws IOException {
        if (nBits > 0) {
            out.write(bitBuffer & ((1 << nBits) - 1));
            bitBuffer = 0;
            nBits = 0;
        }
    }

}
