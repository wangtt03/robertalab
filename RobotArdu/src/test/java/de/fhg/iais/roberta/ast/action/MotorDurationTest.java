package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class MotorDurationTest {
	HelperBotNroll helper = new HelperBotNroll();

    @Test
    public void clearDisplay() throws Exception {

        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        MotorDuration<Void> motorDuration = new MotorDuration<Void>(MotorMoveMode.DEGREE, numConst);
        String a = "MotorDuration [type=DEGREE, value=NumConst [0]]";
        Assert.assertEquals(a, motorDuration.toString());
    }

    @Test
    public void getValue() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        MotorDuration<Void> motorDuration = new MotorDuration<Void>(MotorMoveMode.DEGREE, numConst);
        Assert.assertEquals("NumConst [0]", motorDuration.getValue().toString());
    }

    @Test
    public void setValue() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        MotorDuration<Void> motorDuration = new MotorDuration<Void>(MotorMoveMode.DEGREE, numConst);
        numConst = NumConst.make("1", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        motorDuration.setValue(numConst);
        Assert.assertEquals("NumConst [1]", motorDuration.getValue().toString());
    }

    @Test
    public void getType() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        MotorDuration<Void> motorDuration = new MotorDuration<Void>(MotorMoveMode.DEGREE, numConst);
        Assert.assertEquals(MotorMoveMode.DEGREE, motorDuration.getType());
    }

    @Test
    public void setType() throws Exception {
        NumConst<Void> numConst = NumConst.make("0", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        MotorDuration<Void> motorDuration = new MotorDuration<Void>(MotorMoveMode.DEGREE, numConst);
        motorDuration.setType(MotorMoveMode.DISTANCE);
        Assert.assertEquals(MotorMoveMode.DISTANCE, motorDuration.getType());
    }

}
