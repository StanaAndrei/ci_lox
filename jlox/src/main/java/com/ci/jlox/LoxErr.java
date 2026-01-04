package com.ci.jlox;

public class LoxErr {
    static boolean hadErr = false;
    static boolean hadRuntimeErr = false;


    public static void error(int line, final String msg) {
        report(line, "", msg);
    }

    private static void report(int line, final String where, final String msg) {
        System.err.println("[line " + line + "] Error" + where + ": " + msg);
        hadErr = true;
    }

    static void error(final Token token, final String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else  {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.line + "]");
        hadErr = true;
    }
}