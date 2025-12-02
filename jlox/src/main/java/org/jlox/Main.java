package org.jlox;

public class Main {
    static void main(final String[] args) {
        if (args.length > 1) {
            System.err.println("Usage: jlox [script.jlox]");
            System.exit(64);
        } else if (args.length == 1) {
            final String fileName = args[0];
            IO.println("Run file: " + fileName);
        } else {
            IO.println("Run inline");
        }
    }
}
