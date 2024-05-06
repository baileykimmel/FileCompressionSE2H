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

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SchubsL {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java SchubsL <filename> [<filename>...]");
            System.exit(1);
        }

        for (String inputPath : args) {
            Path path = Paths.get(inputPath);
            Path parentDir = path.getParent();
            String globPattern = path.getFileName().toString();

            if (parentDir == null) {
                parentDir = Paths.get(""); // Default to current directory if no parent directory is specified
            }

            final List<String> filesToCompress = new ArrayList<>();
            final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);

            try {
                Files.walkFileTree(parentDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (matcher.matches(file.getFileName())) {
                            filesToCompress.add(file.toString());
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                System.err.println("Error walking the file tree: " + e.getMessage());
            }

            for (String file : filesToCompress) {
                compressFile(file);
            }
        }
    }

    private static void compressFile(String inputFile) {
        String outputFile = inputFile + ".ll";
        try {
            LZWSE2.compress(inputFile, outputFile);
            System.out.println("Compressed: " + inputFile + " to " + outputFile);
        } catch (IOException e) {
            System.err.println("Error compressing file: " + inputFile + " - " + e.getMessage());
        }
    }
}