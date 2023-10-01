package br.ufma.ecp;

import static br.ufma.ecp.token.TokenType.*;



import br.ufma.ecp.token.Token; 

public class App {

    public static void main(String[] args) {

        String input = """
                // This file is part of www.nand2tetris.org
                // and the book "The Elements of Computing Systems"
                // by Nisan and Schocken, MIT Press.
                // File name: projects/10/Square/Main.jack
                
                // (derived from projects/09/Square/Main.jack, with testing additions)
                
                /** Initializes a new Square Dance game and starts running it. */
                class Main {
                    static boolean test;    // Added for testing -- there is no static keyword
                                            // in the Square files.
                    function void main() {
                        var SquareGame game;
                        let game = SquareGame.new();
                        do game.run();
                        do game.dispose();
                        return;
                    }
                
                    function void test() {  // Added to test Jack syntax that is not use in
                        var int i, j;       // the Square files.
                        var String s;
                        var Array a;
                        if (false) {
                            let s = "string constant";
                            let s = null;
                            let a[1] = a[2];
                        }
                        else {              // There is no else keyword in the Square files.
                            let i = i * (-j);
                            let j = j / (-2);   // note: unary negate constant 2
                            let i = i | j;
                        }
                        return;
                    }
                }
                """;
        Scanner scan = new Scanner(input.getBytes());
        System.out.println("<tokens>");
        for (Token tk = scan.nextToken(); tk.type != EOF; tk = scan.nextToken()) {
            // Ignore tokens with empty lexemes
            if (!tk.lexeme.trim().isEmpty()) {
                System.out.println(tk);
            }
        }
        System.out.println("</tokens>");

    }
}