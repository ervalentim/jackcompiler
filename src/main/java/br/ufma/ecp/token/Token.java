package br.ufma.ecp.token;
public class Token {

    public final TokenType type;
    public final String lexeme;
    final int line;
  
    public Token (TokenType type, String lexeme, int line) {
           this.type = type;
           this.lexeme = lexeme;
           this.line = line;
    }

    public String toString() {
        String categoria = type.toString().toLowerCase();

        String valor = lexeme;
        if (isSymbol(lexeme)) {
            categoria = "symbol";
            //Os símbolos <, >, ", e & são impressos como &lt;  &gt;  &quot; e &amp; Para não conflitar com o significado destes símbolos no XML
            if (valor.equals(">")) {
                valor = "&gt;" ;
            } else if (valor.equals("<")) {
                valor = "&lt;" ;
            } else if (valor.equals("\"")) {
                valor = "&quot;" ;
            } else if (valor.equals("&")) {
                valor = "&amp;" ;
            }

        } else if (categoria.equals("number")) {
            categoria = "integerConstant";
        } else if (categoria.equals("ident")) {
            categoria = "identifier";
        } else if (categoria.equals("string")) {
            categoria = "stringConstant";
        } else {
          categoria = "keyword";
        }
        return "<" + categoria + "> " + valor  + " </" + categoria + ">";
  

    }
    
}
