package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position;
    private int line;
    private int column;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
            while(currentChar() != '\0'){
                SkipWhitSpace();
                if(currentChar() == '\0'){
                    break;
                }
                if (Character.isLetter(currentChar()) || currentChar() == '_') {
                    tokens.add(readIdentifierOrKeyword());
                } else if(Character.isDigit(currentChar()) || currentChar() == '_') {
                    tokens.add(readNumber());
                }else{
                    tokens.add(readOperatorOrSeparator());
                }
            }
        tokens.add(new Token(TokenType.EOF, "", line, column));
        return tokens;
    }

    private char currentChar() {
        if (position >= input.length()) {
            return '\0';
        }
        return input.charAt(position);
    }

    private void advance() {
        if (currentChar() == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        position++;
    }

    private void SkipWhitSpace(){
        while(Character.isWhitespace(currentChar())){
            advance();
        }

    }
    private Token readIdentifierOrKeyword() {
        int startColumn = column;
        StringBuilder sb = new StringBuilder();

        while (Character.isLetterOrDigit(currentChar()) || currentChar() == '_') {
            sb.append(currentChar());
            advance();
        }

        String text = sb.toString();

        return switch (text) {
            case "int" -> new Token(TokenType.INT, text, line, startColumn);
            case "if" -> new Token(TokenType.IF, text, line, startColumn);
            case "else" -> new Token(TokenType.ELSE, text, line, startColumn);
            case "while" -> new Token(TokenType.WHILE, text, line, startColumn);
            case "for" -> new Token(TokenType.FOR, text, line, startColumn);
            case "do" -> new Token(TokenType.DO, text, line, startColumn);
            case "switch" -> new Token(TokenType.SWITCH, text, line, startColumn);
            case "case" -> new Token(TokenType.CASE, text, line, startColumn);
            case "default" -> new Token(TokenType.DEFAULT, text, line, startColumn);
            case "break" -> new Token(TokenType.BREAK, text, line, startColumn);
            default -> new Token(TokenType.IDENT, text, line, startColumn);
        };
    }
    private Token readNumber() {
        int startColumn = column;
        StringBuilder sb = new StringBuilder();

        while (Character.isDigit(currentChar())) {
            sb.append(currentChar());
            advance();
        }

        return new Token(TokenType.NUMBER, sb.toString(), line, startColumn);
    }
    private Token readOperatorOrSeparator() {
        int startColumn = column;
        char c = currentChar();

        switch (c) {
            case '+' -> {
                advance();
                if (currentChar() == '+') {
                    advance();
                    return new Token(TokenType.INC, "++", line, startColumn);
                }
                return new Token(TokenType.PLUS, "+", line, startColumn);
            }
            case '-' -> {
                advance();
                if (currentChar() == '-') {
                    advance();
                    return new Token(TokenType.DEC, "--", line, startColumn);
                }
                return new Token(TokenType.MINUS, "-", line, startColumn);
            }
            case '*' -> {
                advance();
                return new Token(TokenType.MUL, "*", line, startColumn);
            }
            case '/' -> {
                advance();
                return new Token(TokenType.DIV, "/", line, startColumn);
            }
            case '=' -> {
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.EQ, "==", line, startColumn);
                }
                return new Token(TokenType.ASSIGN, "=", line, startColumn);
            }
            case '!' -> {
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.NEQ, "!=", line, startColumn);
                }
                return new Token(TokenType.UNKNOWN, "!", line, startColumn);
            }
            case '>' -> {
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.GTE, ">=", line, startColumn);
                }
                return new Token(TokenType.GT, ">", line, startColumn);
            }
            case '<' -> {
                advance();
                if (currentChar() == '=') {
                    advance();
                    return new Token(TokenType.LTE, "<=", line, startColumn);
                }
                return new Token(TokenType.LT, "<", line, startColumn);
            }
            case ';' -> {
                advance();
                return new Token(TokenType.SEMICOLON, ";", line, startColumn);
            }
            case ',' -> {
                advance();
                return new Token(TokenType.COMMA, ",", line, startColumn);
            }
            case '(' -> {
                advance();
                return new Token(TokenType.LPAREN, "(", line, startColumn);
            }
            case ')' -> {
                advance();
                return new Token(TokenType.RPAREN, ")", line, startColumn);
            }
            case '{' -> {
                advance();
                return new Token(TokenType.LBRACE, "{", line, startColumn);
            }
            case '}' -> {
                advance();
                return new Token(TokenType.RBRACE, "}", line, startColumn);
            }
            case ':' -> {
                advance();
                return new Token(TokenType.COLON, ":", line, startColumn);
            }
            default -> {
                advance();
                return new Token(TokenType.UNKNOWN, String.valueOf(c), line, startColumn);
            }
        }
    }
}