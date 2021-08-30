package com.jelly.util;

public class ParsingException extends RuntimeException {
    private final String line;
    private final int column;
    private final int lineNumber;

    public ParsingException(final String msg, final String line, final int column, final int lineNumber) {
        super(msg + " at " + lineNumber + ":" + column + " \"" + line + "\"");
        this.line = line;
        this.column = column;
        this.lineNumber = lineNumber;
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
