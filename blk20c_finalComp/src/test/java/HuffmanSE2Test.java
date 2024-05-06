
/*BAILEY KIMMEL
 * FINAL COMPRESSION
 * HUFFMAN ENCODING
 * MARCH 8 2024
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuffmanSE2Test {

    private Path tempDir;
    private Path inputFile;
    private Path outputFile;

    @Before
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("testDirForHuffman");
        inputFile = tempDir.resolve("input.txt");
        outputFile = tempDir.resolve("output.compressed");
        Files.createFile(inputFile); // Ensure inputFile exists for tests that require it
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(inputFile);
        Files.deleteIfExists(outputFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testContainsManyThings() throws Exception {
        Files.write(inputFile,
                "This file contains a variety of characters, numbers 123456, and symbols !@#$$%".getBytes());
        HuffmanSE2.main(new String[] { inputFile.toString(), outputFile.toString() });
        assertTrue("Output file should exist", Files.exists(outputFile));
        assertTrue("The compressed file should contain data.", Files.size(outputFile) > 0);
    }

    @Test
    public void testOneReallyLongWordWithNoSpaces() throws Exception {
        Files.write(inputFile, "longwordwithoutanyspacesjusttocheckthehuffmanencoding".getBytes());
        HuffmanSE2.main(new String[] { inputFile.toString(), outputFile.toString() });
        assertTrue("Output file should exist", Files.exists(outputFile));
    }

    @Test
    public void testOnlyLowercase() throws Exception {
        Files.write(inputFile, "onlylowercaselettersinthisfile".getBytes());
        HuffmanSE2.main(new String[] { inputFile.toString(), outputFile.toString() });
        assertTrue("Output file should exist", Files.exists(outputFile));
    }

    @Test
    public void testOnlyUppercase() throws Exception {
        Files.write(inputFile, "ONLYUPPERCASELETTERSINTHISFILE".getBytes());
        HuffmanSE2.main(new String[] { inputFile.toString(), outputFile.toString() });
        assertTrue("Output file should exist", Files.exists(outputFile));
    }

}
