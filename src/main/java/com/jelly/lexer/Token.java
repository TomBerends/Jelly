package com.jelly.lexer;

import static com.jelly.lexer.TokenType.CARRIAGE_RETURN;
import static com.jelly.lexer.TokenType.NEWLINE;

public class Token {
    final TokenType type;
    final String value;

    public Token(final TokenType type, final char value) {
        this(type, String.valueOf(value));
    }

    public Token(final TokenType type, final String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        if(type == NEWLINE || type == CARRIAGE_RETURN)
            return "[" + type + "]";

        return "[" + type + ": \"" + value + "\"]";
    }
}
