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
}