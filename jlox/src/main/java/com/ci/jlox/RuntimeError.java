package com.ci.jlox;

public class RuntimeError extends RuntimeException {
    final Token token;

    public RuntimeError(final Token token, final String message) {
        super(message);
        this.token = token;
    }
}
