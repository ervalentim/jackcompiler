package br.ufma.ecp;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import br.ufma.ecp.SymbolTable.Kind;

public class SymbolTableTest {

    @Test
    public void testDefine () {
        SymbolTable sb = new SymbolTable();
        sb.define("var1", "int", Kind.ARG);
        sb.define("var2", "int", Kind.ARG);
        assertEquals(2,sb.varCount(Kind.ARG));
    }
    @Test
    public void testResolve () {
        SymbolTable sb = new SymbolTable();
        sb.define("var1", "int", Kind.ARG);
        SymbolTable.Symbol s = sb.resolve("var1");
        assertEquals("var1",s.name());

        sb.define("var2", "int", Kind.FIELD);
        s = sb.resolve("var2");
        assertEquals("var2",s.name());

    }

    @Test
    public void testSimpleFunctions () {
        var input = """
            class Main {
 
                function int soma (int x, int y) {
                        return  30;
                 }
                
                 function void main () {
                        var int d;
                        return;
                  }
                
                }
            """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        String actual = parser.VMOutput();
        String expected = """
            function Main.soma 0
            push constant 30
            return
            function Main.main 1
            push constant 0
            return    
                """;
        assertEquals(expected, actual);
    }
    @Test
    public void testSimpleFunctionWithVar () {
        var input = """
            class Main {

                 function int funcao () {
                        var int d;
                        return d;
                  }
                
                }
            """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        String actual = parser.VMOutput();
        String expected = """
            function Main.funcao 1
            push local 0
            return
            """;
        assertEquals(expected, actual);
    }
}