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

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class LZWSE2expTest {
    private static final String TEST_DIR = "test_files";

    @Test
    public void testEmptyFile() throws IOException {
        String inputFile = "test_files/empty.lzw";
        String outputFile = "test_files/empty_expanded.txt";

        // Create an empty .lzw file
        new File(TEST_DIR).mkdirs();
        new File(inputFile).createNewFile();

        // Run expand
        LZWSE2exp.expand(inputFile, outputFile);

        // Check if the output file is empty as expected
        File outFile = new File(outputFile);
        assertTrue("Output file does not exist.", outFile.exists());
        assertEquals("Output file should be empty.", 0, outFile.length());
    }

    @Test
    public void testFileHandling() throws IOException {
        String inputFile = "test_files/dummy.lzw";
        String outputFile = "test_files/dummy_expanded.txt";

        // Create a dummy .lzw file
        new File(TEST_DIR).mkdirs();
        try (OutputStream os = new FileOutputStream(inputFile)) {
            os.write(0); // Write a single byte to simulate non-empty file
        }

        // Run expand
        LZWSE2exp.expand(inputFile, outputFile);

        // Check if the output file exists
        File outFile = new File(outputFile);
        assertTrue("Output file does not exist.", outFile.exists());
    }
}
