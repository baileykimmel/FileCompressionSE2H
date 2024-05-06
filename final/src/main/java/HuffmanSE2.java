
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
to decompress: java Deschubs <filename...>.zh
 */
import java.util.PriorityQueue;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HuffmanSE2 {
    private static final int R = 256;

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            return (left == null && right == null);
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    private static String readFile(String path) {
        File file = new File(path);
        try (Scanner scanner = new Scanner(file)) {
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + path);
            return null;
        }
    }

    public static void compress(String inputFile, String outputFile, boolean log) {
        String s = readFile(inputFile);
        if (s == null) {
            System.out.println("Error reading the file, compression aborted.");
            return; // Early return if file reading failed
        }
        if (s.isEmpty()) {
            System.out.println("Empty file, no content to compress.");
            return; // Early return if there's nothing to compress
        }

        char[] input = s.toCharArray();
        int[] freq = new int[R];
        for (char c : input) {
            freq[c]++;
        }

        Node root = buildTrie(freq);
        String[] st = new String[R];
        buildCode(st, root, "");

        try (PrintWriter pw = new PrintWriter(outputFile)) {
            for (int i = 0; i < R; i++) {
                if (freq[i] > 0)
                    pw.println(i + "\t" + freq[i]);
            }
            pw.println("****"); // Delimiter

            for (char c : input) {
                String code = st[c];
                pw.print(code);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to compressed file: " + outputFile);
        }
    }

    private static Node buildTrie(int[] freq) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.add(new Node(i, freq[i], null, null));

        while (pq.size() > 1) {
            Node left = pq.poll(), right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            System.out.println("buildTrie: new parent, frequencies: " + left.freq + " and " + right.freq);
            pq.add(parent);
        }
        return pq.poll();
    }

    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
            System.out.println("buildCode (" + x.freq + ") " + x.ch + " " + s);
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length < 2 || args.length > 3) {
                throw new IllegalArgumentException("Usage: java HuffmanSE2 <input file> <output file> [l]");
            }
            boolean log = args.length == 3 && "l".equals(args[2]);
            compress(args[0], args[1], log);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            // If you want to indicate an error state when exiting:
            // System.exit(1);
        }
    }

    public static void decompress(String compressedFile, String decompressedFile) {
        try {
            File file = new File(compressedFile);
            Scanner scanner = new Scanner(file);
            int[] freq = new int[R];

            // Improved diagnostics: Print lines to understand what's being read
            System.out.println("Reading frequency table:");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line); // Debug output to see what's being read
                if (line.equals("****")) {
                    System.out.println("End of frequency table.");
                    break; // End of the frequency table
                }
                try {
                    String[] parts = line.split("\t");
                    char index = (char) Integer.parseInt(parts[0]);
                    int frequency = Integer.parseInt(parts[1]);
                    freq[index] = frequency;
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing line: " + line);
                    throw e;
                }
            }

            // Continue with Huffman decoding...
            Node root = buildTrie(freq);
            StringBuilder decoded = new StringBuilder();
            Node x = root;
            while (scanner.hasNextLine()) {
                String encodedLine = scanner.nextLine();
                for (int i = 0; i < encodedLine.length(); i++) {
                    x = encodedLine.charAt(i) == '0' ? x.left : x.right;
                    if (x.isLeaf()) {
                        decoded.append(x.ch);
                        x = root; // reset to the root of the tree for the next character
                    }
                }
            }

            try (PrintWriter out = new PrintWriter(decompressedFile)) {
                out.print(decoded.toString());
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + compressedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decompressionArc(byte[] compressedData, String decompressedFilePath) {
        // Create a file output stream to write the decompressed data
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(compressedData));
                PrintWriter out = new PrintWriter(new FileOutputStream(decompressedFilePath))) {
            int[] freq = new int[R];
            boolean readingFreq = true;

            // Read the frequency table
            while (scanner.hasNextLine() && readingFreq) {
                String line = scanner.nextLine();
                if (line.equals("****")) {
                    readingFreq = false; // End of frequency table
                    continue;
                }
                String[] parts = line.split("\t");
                int index = Integer.parseInt(parts[0]);
                int frequency = Integer.parseInt(parts[1]);
                freq[index] = frequency;
            }

            // Build Huffman tree from frequency table
            Node root = buildTrie(freq);
            StringBuilder decoded = new StringBuilder();
            Node x = root;

            // Decode the rest of the file
            while (scanner.hasNextLine()) {
                String encodedLine = scanner.nextLine();
                for (int i = 0; i < encodedLine.length(); i++) {
                    x = encodedLine.charAt(i) == '0' ? x.left : x.right;
                    if (x.isLeaf()) {
                        decoded.append(x.ch);
                        x = root; // Reset to root for next character
                    }
                }
            }
            out.print(decoded.toString()); // Write the decoded string to file
        } catch (FileNotFoundException e) {
            System.err.println("File not found during decompression: " + decompressedFilePath);
            e.printStackTrace();
        }
    }

    public static void decompressArchive(String archiveFile) {
        try (FileInputStream fis = new FileInputStream(archiveFile);
                BufferedInputStream bis = new BufferedInputStream(fis)) {

            ByteArrayOutputStream metadataBuffer = new ByteArrayOutputStream();
            boolean inMetadata = true;

            // Read until metadata ends
            while (inMetadata) {
                int b = bis.read();
                if (b == -1)
                    break; // End of file check
                metadataBuffer.write(b);
                String metadataContent = metadataBuffer.toString("UTF-8");
                if (metadataContent.endsWith("****")) {
                    inMetadata = false; // stop reading metadata
                }
            }

            // Process each file entry in the metadata
            String[] metadataEntries = metadataBuffer.toString("UTF-8").split("\n");
            for (String entry : metadataEntries) {
                if (entry.trim().isEmpty() || entry.equals("****"))
                    continue; // Skip empty lines and delimiter
                int indexOfBracket = entry.lastIndexOf('[');
                if (indexOfBracket != -1) {
                    String fileName = entry.substring(0, indexOfBracket);
                    String sizeStr = entry.substring(indexOfBracket + 1, entry.length() - 1);
                    int size = Integer.parseInt(sizeStr);

                    byte[] fileData = new byte[size];
                    int readBytes = bis.read(fileData, 0, size);
                    if (readBytes != size) {
                        throw new IOException("File read error: Expected " + size + " bytes, got " + readBytes);
                    }

                    // Decompress and write the file
                    decompressionArc(fileData, fileName);
                }
            }
        } catch (IOException e) {
            System.err.println("An IO error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
