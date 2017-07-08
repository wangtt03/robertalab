package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.ArduFactory;
import de.fhg.iais.roberta.mode.action.botnroll.ActorPort;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class MotorSetPowerActionTest {
	HelperBotNroll h = new HelperBotNroll();
    ArduFactory robotFactory = new ArduFactory(null);

    @Before
    public void setUp() throws Exception {
        this.h.setRobotFactory(this.robotFactory);
    }

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-98, y=182], MotorSetPowerAction [port=B, power=NumConst [30]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorSetPower.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_MotorSetPower.xml");
        MotorSetPowerAction<Void> mgp = (MotorSetPowerAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(ActorPort.B, mgp.getPort());
    }

    @Test
    public void getPower() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_MotorSetPower.xml");
        MotorSetPowerAction<Void> mgp = (MotorSetPowerAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("NumConst [30]", mgp.getPower().toString());
    }

    @Test
    public void powerMissing() throws Exception {
        String a = "BlockAST [project=[[Location [x=22, y=145], MotorSetPowerAction [port=B, power=EmptyExpr [defVal=NUMBER_INT]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorSetPowerMissing.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorSetPower.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorSetPower1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorSetPower2.xml");
    }

    @Test
    public void reverseTransformatinMissing() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorSetPowerMissing.xml");
    }

}
