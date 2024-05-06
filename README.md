Description
This project implements Huffman encoding and decoding algorithms 
for file compression and decompression. It includes functionality for handling 
both individual files and archived collections of files using Huffman coding, providing 
efficient compression for text-based data.


Requirements
Maven for building and managing dependencies

Installation
Clone the repository to your local machine:

git clone https://github.com/yourusername/FileCompressionSE2H.git
cd FileCompressionSE2H

Compile the Java files (if not using Maven):
javac *.java

If using Maven, you can build the project with:

mvn compile

If using Maven, you can test the project with:

mvn test

Usage
Compressing a File
To compress a file using Huffman coding:

java SchubsH inputfile.txt 

Decompressing a File
To decompress a file that was compressed using this program:

java Deschubs <filename>
