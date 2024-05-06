**Description: **
This program provides a command-line interface (CLI) tool for file compression and decompression using either the LZW or Huffman compression algorithms. It also extends functionality to support multiple files compression with globbing and introduces archive creation and extraction. 
---------------------------------------------------------------------------------------------------
**Design and Tradeoffs: 
**
Huffman:

Frequency Analysis: The algorithm starts with a frequency analysis of the input symbols (characters or data blocks) to be encoded. This step determines how often each symbol occurs in the input data.

Building the Huffman Tree: It then constructs a binary tree called the Huffman tree, where each leaf node represents a symbol and the path from the root to each leaf node corresponds to its binary code. The frequency of each symbol determines its position in the tree, with more frequent symbols closer to the root.

Assigning Binary Codes: The algorithm assigns shorter binary codes to more frequent symbols and longer binary codes to less frequent symbols. This ensures that the most common symbols are represented by shorter codes, thereby achieving compression.

Encoding: With the Huffman tree constructed and binary codes assigned to each symbol, the input data can be encoded by replacing each symbol with its corresponding binary code.
--------------------------------------------------------------------------------------------------
**LZW:**

Dictionary Creation: LZW begins with an initial dictionary containing all possible symbols, typically initialized with single-character entries.

Encoding: It scans the input data and encodes sequences of symbols into variable-length codes. As it encounters new sequences, it adds them to the dictionary.

Compression: LZW replaces frequently occurring sequences with shorter codes, thus compressing the data.

Decoding: During decoding, it uses the dictionary to translate the encoded codes back into the original sequences of symbols.
-------------------------------------------------------------------------------------------------
**Tars:**

File Gathering: TAR gathers files and directories into a single archive file.

Metadata Preservation: It preserves file metadata such as permissions, timestamps, and ownership during archiving.

Compression Integration: TAR can integrate with external compression utilities like gzip or bzip2 to compress the archive, reducing its size.

-----------------------------------------------------------------------------------------------------

**Requirements:**
Maven for building and managing dependencies
-----------------------------------------------------------------------------------------------------
**Installation:**
Clone the repository to your local machine:

git clone https://github.com/baileykimmel/FileCompressionSE2H.git
cd FileCompressionSE2H
cd blk20c_finalComp

Compile the Java files (if not using Maven):
javac *.java

If using Maven, you can build the project with:

mvn compile

If using Maven, you can test the project with:

mvn test
----------------------------------------------------------------------------------------------------------------------------
**Run:**

When the Java project has been built using mvn compile, or all classes have been compiled manually individually using javac, you may run each of the following accordingly:
-------------------------------------------------------------------------------------------------------------------------------
**File compression CLI: **
Compressing a File

To compress a file using Huffman coding:

java SchubsH inputfile.txt 

To compress a file using LZW coding:

java SchubsL inputfile.txt 

To compress multiple files into an archive:

java SchubsArc archive-name filenames

--
Decompressing a File

To decompress a file that was compressed using this program:

java Deschubs filename
(not functional for decompressing archives)
-----------------------------------------------------------------------------------------------------------------------------------

**Test Instructions:**

to test, use mvn command, OR
run either SchubsH || SchubsL || SchubsArc || Deschubs on your own test files. 

-------------------------------------------------------------------------------------------------------------------------
**GLOB: **

*.txt ... will match any file with a .txt extension
