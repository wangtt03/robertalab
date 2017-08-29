package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.botnroll.SensorPort;
import de.fhg.iais.roberta.mode.sensor.botnroll.TouchSensorMode;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class SensorExprTest {
	HelperBotNroll h = new HelperBotNroll();

    @Test
    public void make() throws Exception {

        TouchSensor<Void> touchSensor =
            TouchSensor.make(TouchSensorMode.TOUCH, SensorPort.S1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        String a = "SensorExpr [TouchSensor [port=S1]]";
        Assert.assertEquals(a, sensorExpr.toString());
    }

    @Test
    public void getSensor() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(TouchSensorMode.TOUCH, SensorPort.S1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        String a = "TouchSensor [port=S1]";
        Assert.assertEquals(a, sensorExpr.getSens().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(TouchSensorMode.TOUCH, SensorPort.S1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        Assert.assertEquals(999, sensorExpr.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(TouchSensorMode.TOUCH, SensorPort.S1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        SensorExpr<Void> sensorExpr = SensorExpr.make(touchSensor);
        Assert.assertEquals(Assoc.NONE, sensorExpr.getAssoc());
    }
}
