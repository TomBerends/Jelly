package com.jelly.lexer;


import com.jelly.scanner.Scanner;
import com.jelly.util.ParsingException;

import java.io.IOException;

import static com.jelly.lexer.TokenType.*;

public class Lexer implements AutoCloseable {
    private static final char[] TRUE = {'t', 'r', 'u', 'e'};
    private static final char[] FALSE = {'f', 'a', 'l', 's', 'e'};
    private static final char[] NULL = {'n', 'u', 'l', 'l'};

    private Scanner scanner;
    private boolean closed;

    private Result buffer;
    private boolean removed = true;

    public Lexer(final Scanner scanner) {
        this.scanner = scanner;
    }

    private char nextChar() throws IOException {
        return scanner.next();
    }

    private void removeChar() {
        scanner.remove();
    }

    private char readAndRemoveChar() throws IOException {
        final char next = nextChar();
        removeChar();
        return next;
    }

    private Result processString() throws IOException {
        final char quote = readAndRemoveChar();

        final StringBuilder builder = new StringBuilder();
        char curr = readAndRemoveChar();
        while(curr != quote) {
            if(curr == '\n' || curr == '\r')
                return Result.from(new UnexpectedCharacterException(curr, quote));
            builder.append(curr);
            curr = readAndRemoveChar();
        }

        return Result.from(STRING, builder.toString());
    }

    private Result processInteger() throws IOException {
        final StringBuilder integerBuilder = new StringBuilder();

        char curr = nextChar();
        while('0' <= curr && curr <= '9') {
            removeChar();
            integerBuilder.append(curr);
            curr = nextChar();
        }

        return Result.from(INTEGER, Integer.parseInt(integerBuilder.toString()));
    }

    private Result processNumber() throws IOException {
        final Result mantissaResult = processInteger();

        if(nextChar() != '.')
            return mantissaResult;
        removeChar();

        final StringBuilder fractionBuilder = new StringBuilder();

        char curr = nextChar();
        while('0' <= curr && curr <= '9') {
            removeChar();
            fractionBuilder.append(curr);
            curr = nextChar();
        }

        if(fractionBuilder.isEmpty())
            return Result.from(new UnexpectedCharacterException(curr));

        return Result.from(FLOAT, Float.parseFloat(mantissaResult.token.value.toString() + '.' + fractionBuilder));
    }

    private Result processKeyword(final TokenType type, final char[] keyword, final Object value) throws IOException {
        for(final char expected : keyword) {
            if(nextChar() != expected)
                return Result.from(new UnexpectedCharacterException(nextChar(), expected));
            removeChar();
        }

        return Result.from(type, value);
    }

    private Result processKeyword() throws IOException {
        final char curr = nextChar();
        if(curr == 't')
            return processKeyword(TokenType.TRUE, TRUE, true);
        else if(curr == 'f')
            return processKeyword(TokenType.FALSE, FALSE, false);
        else if(curr == 'n')
            return processKeyword(TokenType.NULL, NULL, null);
        else
            return Result.from(new UnexpectedCharacterException(curr));
    }

    private Result processWhitespace() throws IOException {
        char curr = nextChar();
        while(curr == ' ' || curr == '\t') {
            removeChar();
            curr = nextChar();
        }
        return Result.from(WHITE_SPACE, " ");
    }

    private Result processNextChar() throws IOException {
        final char curr = nextChar();

        final Result result;
        switch(curr) {
            case '{' -> result = Result.from(LEFT_CURLY_BRACE, curr);
            case '}' -> result = Result.from(RIGHT_CURLY_BRACE, curr);
            case '[' -> result = Result.from(LEFT_SQUARE_BRACKET, curr);
            case ']' -> result = Result.from(RIGHT_SQUARE_BRACKET, curr);
            case ':' -> result = Result.from(COLON, curr);
            case ',' -> result = Result.from(SEPARATOR, curr);
            case '\r' -> result = Result.from(CARRIAGE_RETURN, curr);
            case '\n' -> result = Result.from(NEWLINE, curr);
            case ' ' -> {
                return processWhitespace(); //dunno
            }
            case '"' -> result = processString();
            default -> {
                if('0' <= curr && curr <= '9')
                    return processNumber(); //dunno either
                else if(curr == 't' || curr == 'f' || curr == 'n') //true false null
                    result = processKeyword();
                else
                    result = Result.from(new UnexpectedCharacterException(curr));
            }
        }

        removeChar();
        return result;
    }

    public Token next() throws IOException, LexicalException {
        if(!removed)
            return buffer.token;

        buffer = processNextChar();
        removed = false;

        if(buffer.hasError())
            throw buffer.error;

        return buffer.token;
    }

    public void remove() {
        removed = true;
    }

    public boolean hasNext() throws IOException {
        return !removed || scanner.hasNext();
    }

    @Override
    public void close() throws IOException {
        if(closed)
            return;

        scanner.close();
        scanner = null;
        closed = true;
    }

    class LexicalException extends ParsingException { //add FILE
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

        private static Result from(final TokenType type, final Object value) {
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