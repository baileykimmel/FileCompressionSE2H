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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Tarsn {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Tarsn archive-name file1 [file2...]");
            return;
        }

        String archiveName = args[0];
        if (!archiveName.endsWith(".zh")) {
            archiveName += ".zh"; // Appends ".zh" if it's not present
        }

        File archive = new File(archiveName);
        if (archive.exists()) {
            System.err.println("The destination archive " + archiveName + " already exists - consider renaming.");
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(archive)) {
            for (int i = 1; i < args.length; i++) {
                File file = new File(args[i]);
                if (!file.exists()) {
                    System.err.println("The file " + args[i] + " does not exist.");
                    continue;
                }
                if (file.isDirectory()) {
                    System.err.println(args[i] + " is a directory, not a file.");
                    continue;
                }

                // Writing file details to archive
                byte separator = (byte) 255; // Separator
                fos.write(file.getName().length());
                fos.write(separator);
                fos.write(file.getName().getBytes());
                fos.write(separator);
                fos.write(longToBytes(file.length()));
                fos.write(separator);

                // Writing file content
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                fos.write(separator); // End of file content
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static byte[] longToBytes(long value) {
        // Convert a long to a byte array (for file size)
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xFFL);
            value >>= 8;
        }
        return result;
    }
}
