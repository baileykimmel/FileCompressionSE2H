import java.io.*;

public class LZWSE2exp {
    private static final int R = 256; // Number of input chars
    private static final int L = 4096; // Maximum number of codewords in dictionary
    private static final int W = 12; // Codeword width

    public static void expand(String inputFilename, String outputFilename) throws IOException {
        String[] st = new String[L];
        int size = R; // Initially, the dictionary size is the number of input characters

        // Initialize dictionary with single-character strings
        for (int i = 0; i < R; i++) {
            st[i] = "" + (char) i;
        }

        int[] bitStream = new int[2]; // This will hold bitBuffer and nBits
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(inputFilename));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFilename))) {

            int codeword = readCode(in, W, bitStream);
            if (codeword == -1)
                return; // Handle EOF or empty file correctly

            String val = st[codeword];
            out.write(val.getBytes());
            String lastVal = val;

            while ((codeword = readCode(in, W, bitStream)) != -1) {
                String entry;
                if (codeword < size && st[codeword] != null) {
                    entry = st[codeword];
                } else if (codeword == size && !lastVal.isEmpty()) {
                    entry = lastVal + lastVal.charAt(0);
                } else {
                    System.err.println("Error: Codeword out of range or undefined.");
                    System.err.println("Codeword: " + codeword + ", Dictionary size: " + size);
                    throw new IllegalArgumentException("Invalid compressed data or codeword out of range.");
                }

                out.write(entry.getBytes());

                // Add new entry to the dictionary if there's space
                if (size < L && !lastVal.isEmpty()) {
                    st[size++] = lastVal + entry.charAt(0);
                }

                // Reset dictionary if it's full
                if (size == L) {
                    for (int i = R; i < L; i++) {
                        st[i] = null;
                    }
                    size = R; // Reset size to just include the initial character set
                }

                lastVal = entry;
            }
            out.flush();
        }
    }

    private static int readCode(InputStream in, int bitWidth, int[] bitStream) throws IOException {
        int bitBuffer = bitStream[0];
        int nBits = bitStream[1];

        while (nBits < bitWidth) {
            int nextByte = in.read();
            if (nextByte == -1) {
                return -1; // Properly handle EOF
            }
            bitBuffer |= (nextByte << nBits);
            nBits += 8;
        }

        int code = bitBuffer & ((1 << bitWidth) - 1);
        bitBuffer >>= bitWidth;
        nBits -= bitWidth;

        bitStream[0] = bitBuffer;
        bitStream[1] = nBits;
        return code;
    }
}
