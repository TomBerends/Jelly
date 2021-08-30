package com.jelly.lexer;

public enum TokenType {
    LEFT_CURLY_BRACE,
    RIGHT_CURLY_BRACE,
    LEFT_SQUARE_BRACKET,
    RIGHT_SQUARE_BRACKET,
    COLON,
    SEPARATOR,

    STRING,
    INTEGER,
    FLOAT,

    TRUE,
    FALSE,
    NULL,

    WHITE_SPACE,
    NEWLINE,
    CARRIAGE_RETURN;
}
