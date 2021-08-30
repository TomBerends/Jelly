package main.java.com.jelly.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public final class Scanner implements ScannerImplementation {
    private static final int DEFAULT_LINE_BUFFER_SIZE = 256;
    private static final int LINE_BUFFER_SIZE_FACTOR = 2;

    private ScannerImplementation implementation;

    private int column = 1;
    private int lineNumber = 1;

    private boolean scannedLine;

    private char[] lineBuffer = new char[DEFAULT_LINE_BUFFER_SIZE];
    private int lineBufferSize = 0;
    private int lineBufferIdx;

    private boolean closed;

    public Scanner(final String source) {
        this(new StringScanner(source));
    }

    public Scanner(final File path) throws FileNotFoundException {
        this(new FileScanner(path));
    }

    private Scanner(final ScannerImplementation implementation) {
        this.implementation = implementation;
    }

    private void resetLineBuffer() {
        lineBuffer = new char[DEFAULT_LINE_BUFFER_SIZE];
        lineBufferIdx = 0;
        lineBufferSize = 0;
        scannedLine = false;
    }

    private void resizeLineBuffer() {
        lineBuffer = Arrays.copyOf(lineBuffer, lineBuffer.length * LINE_BUFFER_SIZE_FACTOR);
    }

    public char read() {
        return lineBuffer[lineBufferIdx];
    }

    private void bufferCharAt(final int idx, final char c) {
        while(idx >= lineBuffer.length)
            resizeLineBuffer();

        lineBufferSize = idx+1;

        lineBuffer[idx] = c;
    }

    private void increaseLineNumber() {
        column = 1;
        lineNumber++;
        resetLineBuffer();
    }

    private void increasePosition(final char ch) {
        if(ch == '\n')
            increaseLineNumber();
        else
            column++;
    }

    private char nextChar() throws IOException {
        if(lineBufferIdx < lineBufferSize)
            return lineBuffer[lineBufferIdx++];
        else {
            final char next = implementation.next();
            bufferCharAt(lineBufferIdx++, next);
            return next;
        }
    }

    @Override
    public char next() throws IOException {
        final char next = nextChar();
        increasePosition(next);
        return next;
    }

    @Override
    public boolean hasNext() throws IOException {
        return implementation.hasNext() || lineBufferIdx < lineBufferSize;
    }

    public String scanLine() throws IOException {
        if(scannedLine)
            return getLine();

        while(true) {
            if(!implementation.hasNext())
                break;

            final char next = implementation.next();
            bufferCharAt(lineBufferSize, next);

            if(next == '\n')
                break;
        }

        scannedLine = true;
        return getLine();
    }

    public int getColumn() {
        return column;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLine() throws IOException {
        if(!scannedLine)
            return scanLine();

        return new String(Arrays.copyOfRange(lineBuffer, 0, lineBufferSize));
    }

    @Override
    public void close() throws IOException {
        if(closed)
            return;

        implementation.close();
        implementation = null;
        closed = true;
    }
}
