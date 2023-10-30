package br.ufma.ecp;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import br.ufma.ecp.SymbolTable.Kind;

public class CodeGeneratorTest {

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

    @Test
    public void testLet () {
        var input = """
            class Main {
            
              function void main () {
                  var int x;
                  let x = 42;
                  return;
              }
            }
            """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        String actual = parser.VMOutput();
        String expected = """
            function Main.main 1
            push constant 42
            pop local 0
            push constant 0
            return
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void arrayTest () {
        var input = """
            class Main {
                function void main () {
                    var Array v;
                    let v[2] = v[3] + 42;
                    return;
                }
            }
            """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        String actual = parser.VMOutput();
        String expected = """
            function Main.main 1
            push constant 2
            push local 0
            add
            push constant 3
            push local 0
            add
            pop pointer 1
            push that 0
            push constant 42
            add
            pop temp 0
            pop pointer 1
            push temp 0
            pop that 0
            push constant 0
            return        
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void callFunctionTest() {

        var input = """
            class Main {
                function int soma (int x, int y) {
                       return  x + y;
                }
               
                function void main () {
                       var int d;
                       let d = Main.soma(4,5);
                       return;
                 }
               
               }
            """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();


        String actual = parser.VMOutput();
        String expected = """
            function Main.soma 0
            push argument 0
            push argument 1
            add
            return
            function Main.main 1
            push constant 4
            push constant 5
            call Main.soma 2
            pop local 0
            push constant 0
            return
                """;
        assertEquals(expected, actual);
 
 
    }

    @Test
    public void methodTest () {
        var input = """
            class Main {
                function void main () {
                    var Point p;
                    var int x;
                    let p = Point.new (10, 20);
                    let x = p.getX();
                    return;
                }
            }
            """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        String actual = parser.VMOutput();
        String expected = """
            function Main.main 2
            push constant 10
            push constant 20
            call Point.new 2
            pop local 0
            push local 0
            call Point.getX 1
            pop local 1
            push constant 0
            return
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void doStatement () {
        var input = """
            class Main {
                function void main () {
                    var int x;
                    let x = 10;
                    do Output.printInt(x);
                    return;
                }
            }
            """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        String actual = parser.VMOutput();
        String expected = """
            function Main.main 1
            push constant 10
            pop local 0
            push local 0
            call Output.printInt 1
            pop temp 0
            push constant 0
            return
                """;
        assertEquals(expected, actual);
    }

    @Test
    public void methodsConstructorTest () {
        var input = """
            class Point {
                field int x, y;
            
                method int getX () {
                    return x;
                }
            
                method int getY () {
                    return y;
                }
            
                method void print () {
                    do Output.printInt(getX());
                    do Output.printInt(getY());
                    return;
                }
            
                constructor Point new(int Ax, int Ay) { 
                  var int w;             
                  let x = Ax;
                  let y = Ay;
                  let w = 42;
                  let x = w;
                  return this;
               }
              }
            """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        String actual = parser.VMOutput();
        String expected = """
            function Point.getX 0
            push argument 0
            pop pointer 0
            push this 0
            return
            function Point.getY 0
            push argument 0
            pop pointer 0
            push this 1
            return
            function Point.print 0
            push argument 0
            pop pointer 0
            push pointer 0
            call Point.getX 1
            call Output.printInt 1
            pop temp 0
            push pointer 0
            call Point.getY 1
            call Output.printInt 1
            pop temp 0
            push constant 0
            return
            function Point.new 1
            push constant 2
            call Memory.alloc 1
            pop pointer 0
            push argument 0
            pop this 0
            push argument 1
            pop this 1
            push constant 42
            pop local 0
            push local 0
            pop this 0
            push pointer 0
            return            
                """;
        assertEquals(expected, actual);
    }
    
    
}