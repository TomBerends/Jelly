package com.jelly.parser;

import com.jelly.json.JSONArray;
import com.jelly.json.JSONObject;
import com.jelly.lexer.Lexer;
import com.jelly.lexer.Token;
import com.jelly.lexer.TokenType;
import com.jelly.scanner.Scanner;
import com.jelly.util.ParsingException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.jelly.lexer.TokenType.*;

public final class Parser implements AutoCloseable {
    public static Object parse(final String source) throws IOException {
        try(final Parser parser = new Parser(source)) {
            return convertResult(parser.parseObject());
        }
    }

    public static JSONObject parseJSONObject(final String source) throws IOException {
        try(final Parser parser = new Parser(source)) {
            return (JSONObject) convertResult(parser.parseJSONObject());
        }
    }

    public static JSONArray parseJSONArray(final String source) throws IOException {
        try(final Parser parser = new Parser(source)) {
            return (JSONArray) convertResult(parser.parseJSONArray());
        }
    }

    public static Object parse(final File path) throws IOException {
        try(final Parser parser = new Parser(path)) {
            return convertResult(parser.parseObject());
        }
    }

    public static JSONObject parseJSONObject(final File path) throws IOException {
        try(final Parser parser = new Parser(path)) {
            return (JSONObject) convertResult(parser.parseJSONObject());
        }
    }

    public static JSONArray parseJSONArray(final File path) throws IOException {
        try(final Parser parser = new Parser(path)) {
            return (JSONArray) convertResult(parser.parseJSONArray());
        }
    }

    private static Object convertResult(final Result result) {
        if(result.hasError())
            throw result.error;
        else
            return result.object;
    }

    private Lexer lexer;
    private boolean closed;

    private Object parsedObject;
    private boolean parsed;

    private Parser(final String source) {
        this.lexer = new Lexer(new Scanner(source));
    }

    private Parser(final File path) throws FileNotFoundException {
        this.lexer = new Lexer(new Scanner(path));
    }

    private Token nextToken() throws IOException {
        final Token token = lexer.next();

        if(token.getType() == WHITE_SPACE || token.getType() == NEWLINE || token.getType() == CARRIAGE_RETURN) {
            removeToken();
            return nextToken();
        }

        return token;
    }

    private void removeToken() {
        lexer.remove();
    }

    private Token readAndRemoveToken() throws IOException {
        final Token token = nextToken();
        removeToken();
        return token;
    }

    private Result parseField() throws IOException {
        final Token name = nextToken();
        if(name.getType() != STRING)
            return Result.from(new UnexpectedTokenException(name.getType(), STRING));
        removeToken();

        if(nextToken().getType() != COLON)
            return Result.from(new UnexpectedTokenException(nextToken(), ":"));
        removeToken();

        final Result valueResult = parseObject();

        if(valueResult.hasError())
            return valueResult;

        return Result.from(new Field(name.getValue().toString(), valueResult.object));
    }

    private Result parseJSONObject() throws IOException {
        if(nextToken().getType() != LEFT_CURLY_BRACE)
            return Result.from(new UnexpectedTokenException(nextToken(), "{"));
        removeToken();

        final JSONObject jsonObject = new JSONObject();

        do {
            removeToken();
            final Result fieldResult = parseField();

            if(fieldResult.hasError())
                return fieldResult;

            final Field field = (Field) fieldResult.object;
            jsonObject.set(field.name, field.value);
        } while(nextToken().getType() == SEPARATOR);

        if(nextToken().getType() != RIGHT_CURLY_BRACE)
            return Result.from(new UnexpectedTokenException(nextToken(), "}"));
        removeToken();

        return Result.from(jsonObject);
    }

    private Result parseObject() throws IOException {
        final Token token = nextToken();

        return switch(token.getType()) {
            case TRUE, FALSE, NULL, INTEGER, FLOAT, STRING -> Result.from(readAndRemoveToken().getValue());
            case LEFT_CURLY_BRACE -> parseJSONObject();
            case LEFT_SQUARE_BRACKET -> parseJSONArray();
            default -> Result.from(new UnexpectedTokenException(token));
        };
    }

    private Result parseJSONArray() throws IOException {
        if(nextToken().getType() != LEFT_SQUARE_BRACKET)
            return Result.from(new UnexpectedTokenException(nextToken(), "["));
        removeToken();

        final JSONArray jsonArray = new JSONArray();

        do {
            removeToken();
            final Result objectResult = parseObject();

            if(objectResult.hasError())
                return objectResult;

            jsonArray.add(objectResult.object);
        } while(nextToken().getType() == SEPARATOR);

        if(nextToken().getType() != RIGHT_SQUARE_BRACKET)
            return Result.from(new UnexpectedTokenException(nextToken(), "]"));
        removeToken();

        return Result.from(jsonArray);
    }

    private Object parse() throws IOException {
        if(parsed)
            return parsedObject;

        final Result parseResult = parseObject();

        if(parseResult.hasError())
            throw parseResult.error;

        parsed = true;
        return parsedObject = parseResult.object;
    }

    @Override
    public void close() throws IOException {
        if(closed)
            return;

        lexer.close();
        lexer = null;
        closed = true;
    }

    class SyntacticalException extends ParsingException {
        SyntacticalException(final String msg) {
            super(msg, "TODO", -1, -1);
        }
    }

    class UnexpectedTokenException extends SyntacticalException {
        UnexpectedTokenException(final Token unexpected) {
            super("Unexpected \"" + unexpected.getValue() + "\"");
        }

        UnexpectedTokenException(final Token unexpected, final String expected) {
            super("Unexpected \"" + unexpected.getValue() + "\" expected \"" + expected + "\"");
        }

        UnexpectedTokenException(final TokenType unexpected, final TokenType expected) {
            super("Unexpected token of type " + unexpected + " expected " + expected);
        }
    }

    private record Result(Object object, SyntacticalException error) {
        private static Result from(final Object object) {
            return new Result(object, null);
        }

        private static Result from(final SyntacticalException error) {
            return new Result(null, error);
        }

        private boolean hasObject() {
            return object != null;
        }

        private boolean hasError() {
            return error != null;
        }
    }

    private record Field(String name, Object value) { }
}
