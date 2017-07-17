package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class ListsCreateWithTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void Test() throws Exception {
        String a = "1.0,3.1,2";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        String a = "\"a\",\"b\",\"c\"";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with1.xml", false);
    }

    @Test
    public void Test2() throws Exception {
        String a = "true,true,false";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with2.xml", false);
    }

    @Test
    public void Test3() throws Exception {
        String a = "true,true,true";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with3.xml", false);
    }

    @Test
    public void Test4() throws Exception {
        String a = "\"NONE\",\"RED\",\"BROWN\"";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_create_with4.xml", false);
    }
}
