package org.jlox;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Token {}
class Scanner {
    public Scanner(final String src) {}

    List<Token> scanTokens() {
        return null;
    }
}

class ErrHandler {
    static boolean hadErr = false;
    public static void error(int line, final String msg) {
        report(line, "", msg);
    }

    private static void report(int line, final String where, final String msg) {
        System.err.println("[line " + line + "] Error" + where + ": " + msg);
        hadErr = true;
    }
}

public class Main {
    private static void run(final String src) {
        System.out.println(src);
        final var scanner = new Scanner(src);
        final var tokens = scanner.scanTokens();
        for (final var token : tokens) {
            IO.println(token);
        }
    }

    private static void runFile(final String filePathStr) throws IOException {
        final var path = Paths.get(filePathStr);
        final byte[] bytes = Files.readAllBytes(path);
        final var src = new String(bytes, Charset.defaultCharset());
        run(src);
        if (ErrHandler.hadErr) {
            System.exit(65);
        }
    }

    private static void runPrompt() throws IOException {
        var inputStreamReader = new InputStreamReader(System.in);
        var bufferedReader = new BufferedReader(inputStreamReader);
        while (true) {
            IO.print("> ");
            final var line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            ErrHandler.hadErr = false;
         }
    }

    static void main(final String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage: jlox [script.jlox]");
            System.exit(64);
        } else if (args.length == 1) {
            final String fileName = args[0];
            runFile(fileName);
        } else {
            runPrompt();
        }
    }
}
