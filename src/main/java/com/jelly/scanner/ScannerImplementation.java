package com.jelly.scanner;

import java.io.IOException;

interface ScannerImplementation extends AutoCloseable {
    char next() throws IOException;
    boolean hasNext() throws IOException;
    void close() throws IOException;
}
