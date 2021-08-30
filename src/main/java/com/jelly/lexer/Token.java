package com.jelly.lexer;

import static com.jelly.lexer.TokenType.CARRIAGE_RETURN;
import static com.jelly.lexer.TokenType.NEWLINE;

public class Token {
    final TokenType type;
    final Object value;

    public Token(final TokenType type, final Object value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if(type == NEWLINE || type == CARRIAGE_RETURN)
            return "[" + type + "]";

        return "[" + type + ": \"" + value + "\"]";
    }
}
