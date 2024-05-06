import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.*;

public class LZWSE2Test {

    @Before
    public void setup() throws IOException {
        // Ensure directory exists in the system's temporary directory
        Path testDirectory = Files.createTempDirectory("test_files");

        // Assuming the creation of LZW compressed files is part of the test setup
        createAndCompressFile(testDirectory, "longword", "a".repeat(1000));
        createAndCompressFile(testDirectory, "contains_many_things",
                "Here are many things: 1234567890, ABC abc, !@#$%^&*()");
        createAndCompressFile(testDirectory, "lower", "abcdefghijklmnopqrstuvwxyz");
        createAndCompressFile(testDirectory, "empty", "");
        createAndCompressFile(testDirectory, "upper", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    private void createAndCompressFile(Path directory, String filename, String content) throws IOException {
        Path filePath = directory.resolve(filename + ".txt");
        Files.writeString(filePath, content);
        LZWSE2.compress(filePath.toString(), filePath.toString().replace(".txt", ".lzw"));
    }

    @After
    public void cleanup() {
        // Delete output files after each test to ensure a clean state
        new File("compressed_empty.txt").delete();
        new File("compressed_nonexistent.txt").delete();
        new File("compressed_varied_contents.txt").delete();
        new File("compressed_longword.txt").delete();
        new File("compressed_lowercase.txt").delete();
        new File("compressed_uppercase.txt").delete();
        // Cleanup input files created for testing
        new File("empty.txt").delete();
        new File("varied_contents.txt").delete();
        new File("longword.txt").delete();
        new File("lowercase.txt").delete();
        new File("uppercase.txt").delete();
    }

    @Test
    public void testEmptyFileCompression() throws IOException {
        Files.createFile(Paths.get("empty.txt"));
        LZWSE2.compress("empty.txt", "compressed_empty.txt"); // Call the actual compression method
        File file = new File("compressed_empty.txt");
        assertTrue("Compressed file should be empty.", file.exists() && file.length() == 0);
    }

    @Test
    public void testFileWithManyThings() throws IOException {
        // Assuming this file has a variety of characters, symbols, etc.
        Files.writeString(Paths.get("varied_contents.txt"), "Here are many things: 1234567890, ABC abc, !@#$%^&*()");
        LZWSE2.compress("varied_contents.txt", "compressed_varied_contents.txt");
        File file = new File("compressed_varied_contents.txt");
        assertTrue("Compressed file should not be empty.", file.exists() && file.length() > 0);
    }

    @Test
    public void testOnlyLowercase() throws IOException {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        Files.writeString(Paths.get("lowercase.txt"), lowercase);
        LZWSE2.compress("lowercase.txt", "compressed_lowercase.txt");
        File file = new File("compressed_lowercase.txt");
        assertTrue("Compressed file should contain data.", file.exists() && file.length() > 0);
    }

    @Test
    public void testOnlyUppercase() throws IOException {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Files.writeString(Paths.get("uppercase.txt"), uppercase);
        LZWSE2.compress("uppercase.txt", "compressed_uppercase.txt");
        File file = new File("compressed_uppercase.txt");
        assertTrue("Compressed file should contain data.", file.exists() && file.pathSeparatorChar > 0);
    }
}