package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.factory.ArduFactory;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.botnroll.ActorPort;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class MotorStopActionTest {
	HelperBotNroll h = new HelperBotNroll();
    ArduFactory robotFactory = new ArduFactory(null);

    @Before
    public void setUp() throws Exception {
        this.h.setRobotFactory(this.robotFactory);
    }

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-76, y=95], MotorStop [port=A, mode=FLOAT]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorStop.xml"));
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_MotorStop.xml");
        MotorStopAction<Void> mgp = (MotorStopAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(ActorPort.A, mgp.getPort());
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/actions/action_MotorStop.xml");
        MotorStopAction<Void> mgp = (MotorStopAction<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(MotorStopMode.FLOAT, mgp.getMode());
    }

    @Test
    public void motorStopSim() throws Exception {
        String a = "BlockAST [project=[[Location [x=45, y=117], MainTask [], MotorStop [port=B, mode=FLOAT]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorStopSim.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorStop.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorStop1.xml");
    }
}
