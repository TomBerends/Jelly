package com.jelly.lexer;


import com.jelly.scanner.Scanner;
import com.jelly.util.ParsingException;

import java.io.IOException;

import static com.jelly.lexer.TokenType.*;

public class Lexer implements AutoCloseable {
    private Scanner scanner;
    private boolean closed;

    public Lexer(final Scanner scanner) {
        this.scanner = scanner;
    }

    private char nextChar() throws IOException {
        return scanner.next();
    }

    private void removeChar() {
        scanner.remove();
    }

    private Result processNextChar() throws IOException {
        final char curr = nextChar();

        final Result result;
        switch(curr) {
            case '{' -> result = Result.from(LEFT_CURLY_BRACE, curr);
            case '}' -> result = Result.from(RIGHT_CURLY_BRACE, curr);
            case '\r' -> {
                removeChar();
                final char next = nextChar();
                if (next == '\n')
                    result = Result.from(NEWLINE, String.valueOf(curr) + next);
                else
                    result = Result.from(CARRIAGE_RETURN, curr);
            }
            default -> result = Result.from(new UnexpectedCharacterException(curr));
        }

        removeChar();
        return result;
    }

    public Token next() throws IOException, LexicalException {
        final Result result = processNextChar();

        if(result.hasError())
            throw result.error;

        return result.token;
    }

    public boolean hasNext() throws IOException {
        return scanner.hasNext();
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
            super("Unexpected " + stringify(unexpected));
        }

        UnexpectedCharacterException(final char unexpected, final char expected) throws IOException {
            super("Unexpected " + stringify(unexpected) + " expected " + stringify(expected));
        }

        private static String stringify(final char ch) {
            if(ch == '\n')
                return "newline";
            else if(ch == '\r')
                return "carriage return";
            else
                return "'" + ch + "'";
        }
    }

    private record Result(Token token, LexicalException error) {
        private static Result from(final Token token) {
            return new Result(token, null);
        }

        private static Result from(final TokenType type, final char value) {
            return new Result(new Token(type, value), null);
        }

        private static Result from(final TokenType type, final String value) {
            return new Result(new Token(type, value), null);
        }

        private static Result from(final LexicalException error) {
            return new Result(null, error);
        }

        private boolean hasToken() {
            return token != null;
        }

        private boolean hasError() {
            return error != null;
        }
    }
}