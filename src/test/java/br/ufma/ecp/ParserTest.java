package br.ufma.ecp;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;


public class ParserTest extends TestSupport {

    @Test
    public void testParseTermInteger () {
      var input = "10;";
      var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
      parser.parseTerm();
      var expectedResult =  """
        <term>
        <integerConstant> 10 </integerConstant>
        </term>
        """;
            
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);    

    }


    @Test
    public void testParseTermIdentifer() {
        var input = "varName;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseTerm();
      
        var expectedResult =  """
          <term>
          <identifier> varName </identifier>
          </term>
          """;
              
          var result = parser.XMLOutput();
          expectedResult = expectedResult.replaceAll("  ", "");
          result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
          assertEquals(expectedResult, result);    
  
    }



    @Test
    public void testParseTermString() {
        var input = "\"Hello World\"";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseTerm();
    
        var expectedResult =  """
          <term>
          <stringConstant> Hello World </stringConstant>
          </term>
          """;
              
          var result = parser.XMLOutput();
          expectedResult = expectedResult.replaceAll("  ", "");
          result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
          assertEquals(expectedResult, result);    
  
    }

    @Test
    public void testParseExpressionSimple() {
        var input = "10+20;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseExpression();
        
        var expectedResult =  """
          <expression>
          <term>
          <integerConstant> 10 </integerConstant>
          </term>
          <symbol> + </symbol>
          <term>
          <integerConstant> 20 </integerConstant>
          </term>
          </expression>
          """;
              
          var result = parser.XMLOutput();
          result = result.replaceAll("\r", ""); 
          expectedResult = expectedResult.replaceAll("  ", "");
          assertEquals(expectedResult, result);    

    }

    @Test
    public void testParseExpressionList() {
        // Entrada de teste contendo uma lista de expressões separadas por vírgulas
        var input = "x + y, 10, 5+3;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));

        // Chamando o método para analisar a lista de expressões
        parser.parseExpressionList();

        // Resultado esperado em formato XML
        var expectedResult = """
            <expressionList>
                <expression>
                    <term>
                        <identifier> x </identifier>
                    </term>
                    <symbol> + </symbol>
                    <term>
                        <identifier> y </identifier>
                    </term>
                </expression>
                <symbol> , </symbol>
                <expression>
                    <term>
                        <integerConstant> 10 </integerConstant>
                    </term>
                </expression>
                <symbol> , </symbol>
                <expression>
                    <term>
                        <integerConstant> 5 </integerConstant>
                    </term>
                    <symbol> + </symbol>
                    <term>
                        <integerConstant> 3 </integerConstant>
                    </term>
                </expression>
            </expressionList>
            """.replaceAll("\r", "").replaceAll("  ", "");

        var result = parser.XMLOutput();
        result = result.replaceAll("\r", "").replaceAll("  ", "");

        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseReturnStatement() {
        String input = """
            return 42;
            """;
        Parser parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseReturn();
        String expectedOutput = """
            <returnStatement>
                <keyword> return </keyword>
                <expression>
                    <term>
                        <integerConstant> 42 </integerConstant>
                    </term>
                </expression>
                <symbol> ; </symbol>
            </returnStatement>
            """;

        String result = parser.XMLOutput().replaceAll("\\s", "");
        expectedOutput = expectedOutput.replaceAll("\\s", "");

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testParseStatement() {
        //teste para LET
        String inputLet = "let x = 42;";
        Parser parserLet = new Parser(inputLet.getBytes(StandardCharsets.UTF_8));
        parserLet.parseStatement();

        //teste para RETURN
        String inputReturn = "return;";
        Parser parserReturn = new Parser(inputReturn.getBytes(StandardCharsets.UTF_8));
        parserReturn.parseStatement();
    }


    @Test
    public void testParseStatements() {
        String input = """
            let x = 42;
            return 42;
            """;

        Parser parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseStatements();
        String expectedOutput = """
            <statements>
                <letStatement>
                    <keyword> let </keyword>
                    <identifier> x </identifier>
                    <symbol> = </symbol>
                    <expression>
                      <term>
                        <integerConstant> 42 </integerConstant>
                      </term>
                    </expression>
                    <symbol> ; </symbol>
                </letStatement>
                <returnStatement>
                    <keyword> return </keyword>
                    <expression>
                        <term>
                            <integerConstant> 42 </integerConstant>
                        </term>
                    </expression>
                    <symbol> ; </symbol>
                </returnStatement>
            </statements>
            """.replaceAll("\\s", "");
          
        String result = parser.XMLOutput().replaceAll("\\s", "");
        expectedOutput = expectedOutput.replaceAll("\\s", "");
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testParseWhileStatement() {
        String input = """
            while (x < 10) {
                let x = 42;
            }
            """;
        Parser parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseWhile();

        String expectedOutput = """
            <whileStatement>
                <keyword> while </keyword>
                <symbol> ( </symbol>
                <expression>
                    <term>
                      <identifier> x </identifier>
                    </term>
                    <symbol> &lt; </symbol>
                    <term>
                        <integerConstant> 10 </integerConstant>
                    </term>
                </expression>
                <symbol> ) </symbol>
                <symbol> { </symbol>
                <statements>
                    <letStatement>
                        <keyword> let </keyword>
                        <identifier> x </identifier>
                        <symbol> = </symbol>
                        <expression>
                          <term>
                            <integerConstant> 42 </integerConstant>
                          </term>
                        </expression>
                        <symbol> ; </symbol>
                    </letStatement>
                </statements>
                <symbol> } </symbol>
            </whileStatement>
            """.replaceAll("\\s", "");

        String result = parser.XMLOutput().replaceAll("\\s", "");
        expectedOutput = expectedOutput.replaceAll("\\s", "");
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testParseIfStatement() {
        String input = """
            if (x < 10) {
                let x = 42;
            } else {
                return 42;
            }
            """;
        Parser parser = new Parser(input.getBytes(StandardCharsets.UTF_8));

        parser.parseIf();
        String expectedOutput = """
            <ifStatement>
                <keyword> if </keyword>
                <symbol> ( </symbol>
                <expression>
                    <term>
                      <identifier> x </identifier>
                    </term>
                    <symbol> &lt; </symbol>
                    <term>
                        <integerConstant> 10 </integerConstant>
                    </term>
                </expression>
                <symbol> ) </symbol>
                <symbol> { </symbol>
                <statements>
                    <letStatement>
                        <keyword> let </keyword>
                        <identifier> x </identifier>
                        <symbol> = </symbol>
                        <expression>
                          <term>
                            <integerConstant> 42 </integerConstant>
                          </term>
                        </expression>
                        <symbol> ; </symbol>
                    </letStatement>
                </statements>
                <symbol> } </symbol>
                <keyword> else </keyword>
                <symbol> { </symbol>
                <statements>
                    <returnStatement>
                        <keyword> return </keyword>
                        <expression>
                            <term>
                                <integerConstant> 42 </integerConstant>
                            </term>
                        </expression>
                        <symbol> ; </symbol>
                    </returnStatement>
                </statements>
                <symbol> } </symbol>
            </ifStatement>
            """.replaceAll("\\s", "");
        String result = parser.XMLOutput().replaceAll("\\s", "");
        expectedOutput = expectedOutput.replaceAll("\\s", "");
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testParseLetSimple1() {
        var input = "let var1 = 10+20;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseLet();
				var expectedResult =  """
	     <letStatement>
        <keyword> let </keyword>
        <identifier> var1 </identifier>
        <symbol> = </symbol>
        <expression>
          <term>
          <integerConstant> 10 </integerConstant>
          </term>
          <symbol> + </symbol>
          <term>
          <integerConstant> 20 </integerConstant>
          </term>
          </expression>
        <symbol> ; </symbol>
      </letStatement> 
				""";
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    /* Segunda Secao - veio pronto */
    @Test
    public void testParseLetSimple() {
        var input = "let string = 20;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseLet();
        System.out.println(parser.XMLOutput());
    }

    
    @Test
    public void testParseLet() {
        var input = "let square = Square.new(0, 0, 30);";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseLet();
        var expectedResult =  """
        <letStatement>
        <keyword> let </keyword>
        <identifier> square </identifier>
        <symbol> = </symbol>
        <expression>
          <term>
            <identifier> Square </identifier>
            <symbol> . </symbol>
            <identifier> new </identifier>
            <symbol> ( </symbol>
            <expressionList>
              <expression>
                <term>
                  <integerConstant> 0 </integerConstant>
                </term>
              </expression>
              <symbol> , </symbol>
              <expression>
                <term>
                  <integerConstant> 0 </integerConstant>
                </term>
              </expression>
              <symbol> , </symbol>
              <expression>
                <term>
                  <integerConstant> 30 </integerConstant>
                </term>
              </expression>
            </expressionList>
            <symbol> ) </symbol>
          </term>
        </expression>
        <symbol> ; </symbol>
      </letStatement>
      """;
        var result = parser.XMLOutput();
        System.out.println(parser.XMLOutput());
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }


    @Test
    public void testParseIf() {
        var input = "if (direction = 1) { do square.moveUp(); }";
        var expectedResult = """
            <ifStatement>
            <keyword> if </keyword>
            <symbol> ( </symbol>
            <expression>
              <term>
                <identifier> direction </identifier>
              </term>
              <symbol> = </symbol>
              <term>
                <integerConstant> 1 </integerConstant>
              </term>
            </expression>
            <symbol> ) </symbol>
            <symbol> { </symbol>
            <statements>
              <doStatement>
                <keyword> do </keyword>
                <identifier> square </identifier>
                <symbol> . </symbol>
                <identifier> moveUp </identifier>
                <symbol> ( </symbol>
                <expressionList>
                </expressionList>
                <symbol> ) </symbol>
                <symbol> ; </symbol>
              </doStatement>
            </statements>
            <symbol> } </symbol>
          </ifStatement>
                """;

        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseIf();
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseDo() {
        var input = "do Sys.wait(5);";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseDo();

        var expectedResult = """
            <doStatement>
            <keyword> do </keyword>
            <identifier> Sys </identifier>
            <symbol> . </symbol>
            <identifier> wait </identifier>
            <symbol> ( </symbol>
            <expressionList>
              <expression>
                <term>
                  <integerConstant> 5 </integerConstant>
                </term>
              </expression>
            </expressionList>
            <symbol> ) </symbol>
            <symbol> ; </symbol>
          </doStatement>
                """;
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }


    @Test
    public void testParseClassVarDec() {
        var input = "field Square square;";
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseClassVarDec();
        var expectedResult = """
            <classVarDec>
            <keyword> field </keyword>
            <identifier> Square </identifier>
            <identifier> square </identifier>
            <symbol> ; </symbol>
          </classVarDec>
                """;

        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseSubroutineDec() {
        var input = """
            constructor Square new(int Ax, int Ay, int Asize) {
                let x = Ax;
                let y = Ay;
                let size = Asize;
                do draw();
                return this;
             }
                """;;
        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parseSubroutineDec();
        var expectedResult = """
            <subroutineDec>
            <keyword> constructor </keyword>
            <identifier> Square </identifier>
            <identifier> new </identifier>
            <symbol> ( </symbol>
            <parameterList>
              <keyword> int </keyword>
              <identifier> Ax </identifier>
              <symbol> , </symbol>
              <keyword> int </keyword>
              <identifier> Ay </identifier>
              <symbol> , </symbol>
              <keyword> int </keyword>
              <identifier> Asize </identifier>
            </parameterList>
            <symbol> ) </symbol>
            <subroutineBody>
              <symbol> { </symbol>
              <statements>
                <letStatement>
                  <keyword> let </keyword>
                  <identifier> x </identifier>
                  <symbol> = </symbol>
                  <expression>
                    <term>
                      <identifier> Ax </identifier>
                    </term>
                  </expression>
                  <symbol> ; </symbol>
                </letStatement>
                <letStatement>
                  <keyword> let </keyword>
                  <identifier> y </identifier>
                  <symbol> = </symbol>
                  <expression>
                    <term>
                      <identifier> Ay </identifier>
                    </term>
                  </expression>
                  <symbol> ; </symbol>
                </letStatement>
                <letStatement>
                  <keyword> let </keyword>
                  <identifier> size </identifier>
                  <symbol> = </symbol>
                  <expression>
                    <term>
                      <identifier> Asize </identifier>
                    </term>
                  </expression>
                  <symbol> ; </symbol>
                </letStatement>
                <doStatement>
                  <keyword> do </keyword>
                  <identifier> draw </identifier>
                  <symbol> ( </symbol>
                  <expressionList>
                  </expressionList>
                  <symbol> ) </symbol>
                  <symbol> ; </symbol>
                </doStatement>
                <returnStatement>
                  <keyword> return </keyword>
                  <expression>
                    <term>
                      <keyword> this </keyword>
                    </term>
                  </expression>
                  <symbol> ; </symbol>
                </returnStatement>
              </statements>
              <symbol> } </symbol>
            </subroutineBody>
          </subroutineDec>
                """;

        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        result = result.replaceAll("\r", ""); // no codigo em linux não tem o retorno de carro
        assertEquals(expectedResult, result);
    }


    @Test
    public void testParserWithLessSquareGame() throws IOException {
        var input = fromFile("ExpressionLessSquare/SquareGame.jack");
        var expectedResult =  fromFile("ExpressionLessSquare/SquareGame.xml");

        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParserWithSquareGame() throws IOException {
        var input = fromFile("Square/SquareGame.jack");
        var expectedResult =  fromFile("Square/SquareGame.xml");

        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);
    }


    @Test
    public void testParserWithSquare() throws IOException {
        var input = fromFile("Square/Square.jack");
        var expectedResult =  fromFile("Square/Square.xml");

        var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
        parser.parse();
        var result = parser.XMLOutput();
        expectedResult = expectedResult.replaceAll("  ", "");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testVarDeclaration () {

      var input = """
        class Point {
          field int x, y;
          constructor Point new(int Ax, int Ay) {
            var int Ax;
            
            let x = Ax;
            let y = Ay;
            return this;
         }
        }
        """;;
    var parser = new Parser(input.getBytes(StandardCharsets.UTF_8));
    parser.parse();
    var result = parser.XMLOutput();
    System.out.println(result);

    }
    

}