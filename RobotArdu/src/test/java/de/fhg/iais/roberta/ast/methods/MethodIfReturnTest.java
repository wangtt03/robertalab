package de.fhg.iais.roberta.ast.methods;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.ArduFactory;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class MethodIfReturnTest {
    HelperBotNroll h = new HelperBotNroll();
    ArduFactory robotFactory = new ArduFactory(null);

    @Before
    public void setUp() throws Exception {
        this.h.setRobotFactory(this.robotFactory);
    }

    @Test
    public void methodIfReturn1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=85], MethodReturn [test, VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], false, false], "
                + "MethodStmt [MethodIfReturn [SensorExpr [TouchSensor [port=S1]], BOOLEAN, BoolConst [false]]], BOOLEAN, EmptyExpr [defVal=NULL]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/methods/method_if_return_1.xml"));

    }

    @Test
    public void methodIfReturn2() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=44, y=104], MethodReturn [Funktionsblock,"
                + " VarDeclaration [NUMBER, x, EmptyExpr [defVal=NUMBER], true, false],"
                + " VarDeclaration [NUMBER, x2, EmptyExpr [defVal=NUMBER], true, false],"
                + " VarDeclaration [NUMBER, x3, EmptyExpr [defVal=NUMBER], false, false], "
                + "\nAktionStmt [LightAction [GREEN, ON]]MethodStmt [MethodIfReturn [Binary [EQ, NumConst [0], NumConst [0]], NUMBER, Var [x2]]], NUMBER, Var [x3]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/methods/method_if_return_2.xml"));
    }

    @Test
    public void reverseTransformation1() throws Exception {
        this.h.assertTransformationIsOk("/ast/methods/method_if_return_1.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        this.h.assertTransformationIsOk("/ast/methods/method_if_return_2.xml");
    }

}
