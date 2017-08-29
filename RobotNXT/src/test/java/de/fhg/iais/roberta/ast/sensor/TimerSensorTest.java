package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.nxt.Helper;

public class TimerSensorTest {
    Helper h = new Helper();

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_resetTimer.xml");

        TimerSensor<Void> cs = (TimerSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(TimerSensorMode.RESET, cs.getMode());
    }

    @Test
    public void getTimer() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_resetTimer.xml");

        TimerSensor<Void> cs = (TimerSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(1, cs.getTimer());
    }

    @Test
    public void sensorResetTimer() throws Exception {
        String a = "BlockAST [project=[[Location [x=-96, y=73], TimerSensor [mode=RESET, timer=1]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_resetTimer.xml"));
    }

    @Test
    public void sensorGetSampleTimer() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=1], TimerSensor [mode=GET_SAMPLE, timer=1]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_getSampleTimer.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_resetTimer.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_getSampleTimer.xml");
    }
}
