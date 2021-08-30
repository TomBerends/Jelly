package com.jelly.scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class FileScanner implements ScannerImplementation {
    FileInputStream inputStream;
    boolean closed;

    FileScanner(final String path) throws FileNotFoundException {
        this(new FileInputStream(path));
    }

    FileScanner(final File path) throws FileNotFoundException {
        this(new FileInputStream(path));
    }

    FileScanner(final FileInputStream fileInputStream) {
        inputStream = fileInputStream;
    }

    @Override
    public char next() throws IOException {
        return (char) inputStream.read();
    }

    @Override
    public boolean hasNext() throws IOException {
        return inputStream.available() != 0;
    }

    @Override
    public void close() throws IOException {
        if(closed)
            return;

        inputStream.close();
        inputStream = null;
        closed = true;
    }
}
