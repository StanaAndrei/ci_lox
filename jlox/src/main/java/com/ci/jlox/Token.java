package com.ci.jlox;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Token {
    final TokenType tokenType;
    final String lexeme;
    final Object literal;
    final int line;

    @Override
    public String toString() {
        return tokenType + " " + lexeme + " " + literal;
    }
}
