package org.jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    TokenType.AND);
        keywords.put("class",  TokenType.CLASS);
        keywords.put("else",   TokenType.ELSE);
        keywords.put("false",  TokenType.FALSE);
        keywords.put("for",    TokenType.FOR);
        keywords.put("fun",    TokenType.FUN);
        keywords.put("if",     TokenType.IF);
        keywords.put("nil",    TokenType.NIL);
        keywords.put("or",     TokenType.OR);
        keywords.put("print",  TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super",  TokenType.SUPER);
        keywords.put("this",   TokenType.THIS);
        keywords.put("true",   TokenType.TRUE);
        keywords.put("var",    TokenType.VAR);
        keywords.put("while",  TokenType.WHILE);
    }

    private final String src;
    private final List<Token> tokens;
    private int start = 0, current = 0, line = 1;

    public Scanner(final String src) {
        this.src = src;
        tokens = new ArrayList<>();
    }

    private boolean isAtEnd() {
        return current >= src.length();
    }

    private boolean match(char expected) {
        if (isAtEnd() || src.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            // single lexemes
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;
            // possible x2 lexemes
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else if (match('*')) {
                    blockComment();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case '"': string(); break;

            case ' ':
            case '\r':
            case '\t':
                break; // Ignore whitespaces
            case '\n':
                line++;
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    LoxErr.error(line, "Unexpected character!");
                }
        }
    }

    private void blockComment() {
        int depth = 1;
        while (depth > 0 && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                advance();
            } else if (peek() == '/' && peekNext() == '*') {
                advance(); // consume /
                advance(); // consume *
                depth++;
            } else if (peek() == '*' && peekNext() == '/') {
                advance(); // consume *
                advance(); // consume /
                depth--;
            } else {
                advance();
            }
        }
        if (depth > 0) {
            LoxErr.error(line, "Unfinished block comment!");
        }
    }

    private void identifier() {
        while (isAlphaNum(peek())) {
            advance();
        }
        final var txt = src.substring(start, current);
        TokenType tokenType = keywords.get(txt);
        if (tokenType == null) {
            tokenType = TokenType.IDENTIFIER;
        }
        addToken(tokenType);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }
    private boolean isAlphaNum(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            do {
                advance();
            } while (isDigit(peek()));
        }
        double d = Double.parseDouble(src.substring(start, current));
        addToken(TokenType.NUMBER, d);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() =='\n') {
                line++;
            }
            advance();
        }
        if (isAtEnd()) {
            LoxErr.error(line, "Unterminated str!");
            return;
        }
        advance(); // closing "
        // Trim the surrounding quotes
        var val = src.substring(start + 1, current - 1);
        addToken(TokenType.STRING, val);
    }

    private char peek() {
        return isAtEnd() ? '\0' : src.charAt(current);
    }

    private char peekNext() {
        return current + 1 >= src.length() ? '\0' : src.charAt(current + 1);
    }

    private char advance() {
        return src.charAt(current++);
    }

    private void addToken(TokenType tokenType) {
        addToken(tokenType, null);
    }

    private void addToken(TokenType tokenType, Object literal) {
        var txt = src.substring(start, current);
        tokens.add(new Token(tokenType, txt, literal, line));
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }
}