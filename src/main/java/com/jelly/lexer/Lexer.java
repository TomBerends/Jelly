package com.jelly.lexer;


import com.jelly.scanner.Scanner;
import com.jelly.util.ParsingException;

import java.io.IOException;

public class Lexer implements AutoCloseable {
    private Scanner scanner;
    private boolean closed;

    public Lexer(final Scanner scanner) {
        this.scanner = scanner;
    }

    public Token next() throws IOException {
        return null;
    }

    public boolean hasNext() {
        return false;
    }

    @Override
    public void close() throws IOException {
        if(closed)
            return;

        scanner.close();
        scanner = null;
        closed = true;
    }

    class LexicalException extends ParsingException {
        LexicalException(final String msg) throws IOException {
            super(msg, scanner.getLine(), scanner.getLineNumber(), scanner.getColumn());
        }
    }

    class UnexpectedCharacterException extends LexicalException {
        UnexpectedCharacterException(final char unexpected) throws IOException {
            super("Unexpected Character '" + unexpected + "'");
        }

        UnexpectedCharacterException(final char unexpected, final char expected) throws IOException {
            super("Unexpected Character '" + unexpected + "' expected '" + expected + "'");
        }
    }
}