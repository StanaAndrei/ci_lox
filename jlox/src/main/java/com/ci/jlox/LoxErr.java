package com.ci.jlox;

public class LoxErr {
    static boolean hadErr = false;
    public static void error(int line, final String msg) {
        report(line, "", msg);
    }

    private static void report(int line, final String where, final String msg) {
        System.err.println("[line " + line + "] Error" + where + ": " + msg);
        hadErr = true;
    }
}