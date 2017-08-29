package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.nxt.Helper;

public class MathTrigTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        final String a = "MathSin(0)MathCos(0)MathTan(0)MathAsin(0)MathAcos(0)MathAtan(0)";

        this.h.assertCodeIsOk(a, "/syntax/math/math_trig.xml");
    }

    @Test
    public void Test1() throws Exception {
        final String a = "if(0==MathSin(0)){__speed=MathAcos(0)<100?MathAcos(0):100;OnFwdReg(OUT_BC,__speed,OUT_REGMODE_SYNC);}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_trig1.xml");
    }

}
