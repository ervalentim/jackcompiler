package br.ufma.ecp;

import static br.ufma.ecp.token.TokenType.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import br.ufma.ecp.token.Token;
import br.ufma.ecp.token.TokenType;

public class Scanner {

    private byte[] input;
    private int current;
    private int start;

    private static final Map<String, TokenType> keywords;
 

    static {
        keywords = new HashMap<>();
        keywords.put("while", TokenType.WHILE);
        keywords.put("int", TokenType.INT);
        keywords.put("class", TokenType.CLASS);
        keywords.put("constructor", TokenType.CONSTRUCTOR);
        keywords.put("function", TokenType.FUNCTION);
        keywords.put("method", TokenType.METHOD);
        keywords.put("field", TokenType.FIELD);
        keywords.put("static", TokenType.STATIC);
        keywords.put("var", TokenType.VAR);
        keywords.put("char", TokenType.CHAR);
        keywords.put("boolean", TokenType.BOOLEAN);
        keywords.put("void", TokenType.VOID);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("null", TokenType.NULL);
        keywords.put("this", TokenType.THIS);
        keywords.put("let", TokenType.LET);
        keywords.put("do", TokenType.DO);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("return", TokenType.RETURN);
  }

    
    public Scanner (byte[] input) {
        this.input = input;
        current = 0;
        start = 0;
    }

    private void skipLineComments() {
        for (char ch = peek(); ch != '\n' && ch != 0;  advance(), ch = peek()) ;
    }

    private void skipBlockComments() {
        boolean endComment = false;
        advance();
        while (!endComment) {
            advance();
            char ch = peek();
            if ( ch == 0) { // eof, lexical error
                System.exit(1);
            }
         
            if (ch == '*') {
               for (ch = peek(); ch == '*';  advance(), ch = peek()) ;
                if (ch == '/') {
                    endComment = true;
                    advance();
                }
            }

        }
    }

    private void skipWhitespace() {
        char ch = peek();
        while (ch == ' ' || ch == '\r' || ch == '\t' || ch == '\n') {
            advance();
            ch = peek();
        }
    }
    

    public Token nextToken () {

        skipWhitespace();

        start = current;
        char ch = peek();

        if (Character.isDigit(ch)) {
            return number();
        }

        if (isAlpha(ch)) {
            return identifier();
        }

        switch (ch) {
            case '/':
                if (peekNext() == '/') {
                    skipLineComments();
                    return nextToken();
                } else if (peekNext() == '*') {
                    skipBlockComments();
                    return nextToken();
                }
                else {
                    advance();
                    return new Token (TokenType.SLASH,"/");
                }
            case '+':
                advance();
                return new Token (PLUS,"+");
            case '-':
                advance();
                return new Token (MINUS,"-");
            case '"':
                return string();
            case 0:
                return new Token (EOF,"EOF");
            default:
                advance();
                return new Token(ILLEGAL, Character.toString(ch));
        }
    }

    private Token identifier() {
        while (isAlphaNumeric(peek())) advance();

        String id = new String(input, start, current-start, StandardCharsets.UTF_8)  ;
        TokenType type = keywords.get(id);
        if (type == null) type = IDENT;
        return new Token(type, id);
    }

    private Token number() {
        while (Character.isDigit(peek())) {
            advance();
        }
        
            String num = new String(input, start, current-start, StandardCharsets.UTF_8)  ;
            return new Token(NUMBER, num);
    }

    private Token string () {
        advance();
        start = current;
        while (peek() != '"' && peek() != 0) {
            advance();
        }
        String s = new String(input, start, current-start, StandardCharsets.UTF_8);
        Token token = new Token (TokenType.STRING,s);
        advance();
        return token;
    }

    private void advance()  {
        char ch = peek();
        if (ch != 0) {
            current++;
        }
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
      }
    
      private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || Character.isDigit((c));
      }
    

    private char peek () {
        if (current < input.length)
           return (char)input[current];
       return 0;
    }

    private char peekNext () {
        int next = current + 1;
        if ( next  < input.length) {
            return (char)input[next];
        } else {
            return 0;
        }
   }


    
}
