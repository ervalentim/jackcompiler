package br.ufma.ecp;

import static org.junit.Assert.assertEquals;

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
}