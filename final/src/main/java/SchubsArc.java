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

public class SchubsArc {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: java SchubsArc <archive-name.zl|.zh> <file1> [<file2>...]");
            System.exit(1);
        }

        String archiveName = args[0];
        if (!archiveName.endsWith(".zh")) {
            archiveName += ".zh"; // Ensure the archive has the correct extension
        }

        try (FileOutputStream fos = new FileOutputStream(archiveName);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            for (int i = 1; i < args.length; i++) {
                File file = new File(args[i]);
                if (!file.exists()) {
                    System.err.println("The file " + args[i] + " does not exist.");
                    continue;
                }

                // Compress the file using Huffman
                File tempFile = File.createTempFile("temp", ".hh");
                HuffmanSE2.compress(file.getAbsolutePath(), tempFile.getAbsolutePath(), true);

                // Write file header
                String fileName = file.getName();
                bos.write(fileName.length()); // Write the length of the file name
                bos.write(fileName.getBytes()); // Write the file name

                // Write the compressed file content
                try (FileInputStream fis = new FileInputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                }
                tempFile.delete(); // Delete the temporary file
            }
            bos.flush();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
