package com.jelly.util;

public class ParsingException extends RuntimeException {
    private final String line;
    private final int lineNumber;
    private final int column;

    public ParsingException(final String msg, final String line, final int lineNumber, final int column) {
        super(msg + " at " + lineNumber + ":" + column + " \"" + line + "\"");
        this.line = line;
        this.lineNumber = lineNumber;
        this.column = column;
    }

    public String getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
