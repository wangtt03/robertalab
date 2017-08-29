package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class CurveActionTest {
	HelperBotNroll h = new HelperBotNroll();

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=113, y=288], CurveAction [FOREWARD, MotionParam [speed=NumConst [20], duration=null]MotionParam [speed=NumConst [50], duration=null]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorCurve.xml"));
    }

    @Test
    public void reverseTransformatinMotorDiffOn() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorCurve.xml");
    }

    @Test
    public void motorDiffOnFor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=138, y=238], CurveAction [FOREWARD, MotionParam [speed=NumConst [20], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/actions/action_MotorCurveFor.xml"));
    }

    @Test
    public void getParam() throws Exception {
        CurveAction<?> da = (CurveAction<?>) this.h.generateAST("/ast/actions/action_MotorCurveFor.xml");
        Assert.assertEquals("MotionParam [speed=NumConst [20], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]", da.getParamLeft().toString());
    }

    @Test
    public void getDirection() throws Exception {
        CurveAction<?> da = (CurveAction<?>) this.h.generateAST("/ast/actions/action_MotorCurveFor.xml");
        Assert.assertEquals(DriveDirection.FOREWARD, da.getDirection());
    }

    @Test
    public void reverseTransformatinMotorDiffOnFor() throws Exception {
        this.h.assertTransformationIsOk("/ast/actions/action_MotorCurveFor.xml");
    }

}
