import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SchubsHTest {

    private Path tempDir;
    private Path inputFile;
    private Path outputFile;

    @Before
    public void setUp() throws IOException {
        // Create a temporary directory to hold the files
        tempDir = Files.createTempDirectory("testDirForSchubsH");
        inputFile = tempDir.resolve("input.txt");
        outputFile = tempDir.resolve("input.txt.hh");

        // Ensure inputFile exists for tests that require it
        Files.createFile(inputFile);
        Files.write(inputFile, "This is a test file with enough content to compress.".getBytes());
    }

    @After
    public void tearDown() throws IOException {
        // Cleanup the created files and directory
        Files.deleteIfExists(inputFile);
        Files.deleteIfExists(outputFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testFileCompression() throws Exception {
        // Run the main method of SchubsH with the path of the created test input file
        SchubsH.main(new String[] { inputFile.toString() });

        // Check if the output file exists and has content
        assertTrue("Output file should exist after compression", Files.exists(outputFile));
        assertTrue("The compressed file should contain data.", Files.size(outputFile) > 0);
    }
}
