package com.jelly.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import static com.jelly.util.Util.LINE_SEPARATOR;

public final class Scanner implements ScannerImplementation {
    private static final int DEFAULT_LINE_BUFFER_SIZE = 256;
    private static final int LINE_BUFFER_SIZE_FACTOR = 2;

    private ScannerImplementation implementation;

    private int column;
    private int lineNumber = 1;

    private boolean scannedLine;

    private boolean removed = true;
    private boolean removable = false;

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

    private void bufferCharAt(final int idx, final char c) {
        while(idx >= lineBuffer.length)
            resizeLineBuffer();

        lineBufferSize = idx+1;

        lineBuffer[idx] = c;
    }

    private void increaseLineNumber() {
        column = 0;
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
        if(!removed)
            return lineBuffer[lineBufferIdx];

        final char next = nextChar();
        increasePosition(next);
        removed = false;
        removable = true;
        return next;
    }

    public void remove() {
        if(!removable)
            return;

        removed = true;
        removable = false;
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

        return new String(Arrays.copyOfRange(lineBuffer, 0, lineBufferSize - ((implementation.hasNext()) ? LINE_SEPARATOR.length : 0)));
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
