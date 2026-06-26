package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

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

    public Lexer(String source) {
        this.source = source;
    }

    // 1. Are we past the end of the source string?
    private boolean isAtEnd() {
        // TODO
        if(current >= source.length()) {
            return true;
        }
        return false;
    }

    // 2. Consume the current character and return it.
    private char advance() {
        // TODO
        if(!isAtEnd()) {
            return source.charAt(current++);
        }
        return '\0';
    }

    // 3. Return the current character without consuming it.
    private char peek() {
        // TODO
        if(!isAtEnd()) {
            return source.charAt(current);
        }
        return '\0';
    }

    // 4. Return the character after current without consuming it.
    private char peekNext() {
        if(current + 1 < source.length()) {
            return source.charAt(current + 1);
        }
        return '\0';
    }

    // 5. Consume current character only if it matches expected.
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    // 6a. Add a token with no literal value.
    private void addToken(TokenType type) {
        // TODO
        tokens.add(new Token(type,source.substring(start, current), null, line));
    }

    // 6b. Add a token with a literal value.
    private void addToken(TokenType type, Object literal) {
        // TODO
        tokens.add(new Token(type,source.substring(start, current), literal, line));
    }

    // 7. Scan one token starting at current position.
    private void scanToken() {
        // TODO
        char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':   
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);   
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '!' : addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;  
            case '=' : addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<' : addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>' : addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case ' ': case '\r': case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    // Handle unexpected character
                    System.err.println("Unexpected character: " + c + " at line " + line);
                }
                break;
        }

    }

    // 8. Scan a string literal (called after opening " is consumed).
    private void string() {
        // TODO
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isAtEnd()) {
            System.err.println("Unterminated string at line " + line);
            return;
        }
        // Consume the closing ".
        advance();
        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    // 9. Scan a number literal (called when current char is a digit).
    private void number() {
        // TODO
        while (isDigit(peek())) advance();

        if(peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }
        // Convert the string to a number and add the token.
        String value = source.substring(start, current);
        addToken(TokenType.NUMBER, Double.parseDouble(value));
    }

    // 10. Scan an identifier or keyword.
    private void identifier() {
        // TODO
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        addToken(keywords.getOrDefault(text, TokenType.IDENTIFIER));
    }

    // 11. Is c a digit?
    private boolean isDigit(char c) {
        // TODO
        if(c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    // 12. Is c a letter or underscore?
    private boolean isAlpha(char c) {
        // TODO
        if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
            return true;
        }
        return false;
    }

    // 13. Is c a letter, underscore, or digit?
    private boolean isAlphaNumeric(char c) {
        // TODO
        if(isAlpha(c) || isDigit(c)) {
            return true;
        }
        return false;
    }

    // 14. Public entry point — scan all tokens and return the list.
    public List<Token> scanTokens() {
        // TODO
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }
        addToken(TokenType.EOF);
        return tokens;
    }
}
