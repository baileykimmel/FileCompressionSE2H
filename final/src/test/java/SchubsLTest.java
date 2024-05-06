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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class SchubsLTest {
    private static final String TEST_DIR = "test_files";
    private Path testDirPath;

    @Before
    public void setUp() throws IOException {
        testDirPath = Paths.get(TEST_DIR);
        Files.createDirectories(testDirPath); // This is safe, it does nothing if the directory already exists

        // Ensure files are deleted if they exist before creating new ones
        Path file1 = testDirPath.resolve("test1.txt");
        Path file2 = testDirPath.resolve("test2.txt");
        Path logFile = testDirPath.resolve("test1.log");

        Files.deleteIfExists(file1);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(logFile);

        Files.createFile(file1);
        Files.createFile(file2);
        Files.writeString(file1, "Hello World");
        Files.writeString(file2, "Hello Again");
    }

    @Test
    public void testCompressSingleFile() throws Exception {
        String[] args = { testDirPath.resolve("test1.txt").toString() };
        SchubsL.main(args);
        Path outputPath = testDirPath.resolve("test1.txt.ll");
        assertTrue("Compressed file should exist", Files.exists(outputPath));
    }

    @Test
    public void testCompressMultipleFiles() throws Exception {
        // Assume setup has already created the test files in `testDirPath`

        // List all files matching the glob pattern and prepare to pass them as
        // arguments
        List<String> filesToCompress = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(testDirPath, "*.txt")) {
            for (Path entry : stream) {
                filesToCompress.add(entry.toString());
            }
        } catch (IOException e) {
            fail("Failed to list files for compression: " + e.getMessage());
        }

        // Check if there are files to compress
        assertFalse("There should be files to compress", filesToCompress.isEmpty());

        // Convert List to array and pass to SchubsL
        SchubsL.main(filesToCompress.toArray(new String[0]));

        // Check each file to see if it was compressed
        for (String filePath : filesToCompress) {
            Path outputPath = Paths.get(filePath + ".ll");
            assertTrue("Compressed file should exist: " + outputPath, Files.exists(outputPath));
        }
    }

}
