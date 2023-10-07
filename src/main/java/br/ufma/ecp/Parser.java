package br.ufma.ecp;

import br.ufma.ecp.token.Token;
import br.ufma.ecp.token.TokenType;

public class Parser {

   private static class ParseError extends RuntimeException {}

    private Scanner scan;
    private Token currentToken;
    private Token peekToken;
    private StringBuilder xmlOutput = new StringBuilder();

    public Parser(byte[] input) {
        scan = new Scanner(input);
        nextToken();
    }

    private void nextToken() {
        currentToken = peekToken;
        peekToken = scan.nextToken();
    }


   public void parse () {
        
    }

    void parseTerm() {
        printNonTerminal("term");
        switch (peekToken.type) {
          case NUMBER:
            expectPeek(TokenType.NUMBER);
            break;
          case STRING:
            expectPeek(TokenType.STRING);
            break;
          case FALSE:
          case NULL:
          case TRUE:
            expectPeek(TokenType.FALSE, TokenType.NULL, TokenType.TRUE);
            break;
          case THIS:
            expectPeek(TokenType.THIS);
            break;
          case IDENT:
            expectPeek(TokenType.IDENT);
            break;
          default:
            throw error(peekToken, "term expected");
        }
    
        printNonTerminal("/term");
      }

    static public boolean isOperator(String op) {
        return "+-*/<>=~&|".contains(op);
   }

   void parseExpression() {
        printNonTerminal("expression");
        parseTerm ();
        while (isOperator(peekToken.lexeme)) {
            expectPeek(peekToken.type);
            parseTerm();
        }
        printNonTerminal("/expression");
    }

    // funções auxiliares
    public String XMLOutput() {
        return xmlOutput.toString();
    }

    private void printNonTerminal(String nterminal) {
        xmlOutput.append(String.format("<%s>\r\n", nterminal));
    }


    boolean peekTokenIs(TokenType type) {
        return peekToken.type == type;
    }

    boolean currentTokenIs(TokenType type) {
        return currentToken.type == type;
    }

    private void expectPeek(TokenType... types) {
        for (TokenType type : types) {
            if (peekToken.type == type) {
                expectPeek(type);
                return;
            }
        }

       throw error(peekToken, "Expected a statement");

    }

    private void expectPeek(TokenType type) {
        if (peekToken.type == type) {
            nextToken();
            xmlOutput.append(String.format("%s\r\n", currentToken.toString()));
        } else {
            throw error(peekToken, "Expected "+type.name());
        }
    }


    private static void report(int line, String where,
        String message) {
            System.err.println(
            "[line " + line + "] Error" + where + ": " + message);
    }


    private ParseError error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
        return new ParseError();
    }


}