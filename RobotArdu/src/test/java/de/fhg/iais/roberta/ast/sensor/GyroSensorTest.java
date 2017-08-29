package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.botnroll.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.botnroll.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class GyroSensorTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void sensorSetGyro() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-30, y=210], GyroSensor [mode=ANGLE, port=S2]], [Location [x=-26, y=250], GyroSensor [mode=RATE, port=S4]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_setGyro.xml"));
    }

    @Test
    public void getMode() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setGyro.xml");

        GyroSensor<Void> cs = (GyroSensor<Void>) transformer.getTree().get(0).get(1);
        GyroSensor<Void> cs1 = (GyroSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(GyroSensorMode.ANGLE, cs.getMode());
        Assert.assertEquals(GyroSensorMode.RATE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setGyro.xml");

        GyroSensor<Void> cs = (GyroSensor<Void>) transformer.getTree().get(0).get(1);
        GyroSensor<Void> cs1 = (GyroSensor<Void>) transformer.getTree().get(1).get(1);

        Assert.assertEquals(SensorPort.S2, cs.getPort());
        Assert.assertEquals(SensorPort.S4, cs1.getPort());
    }

    @Test
    public void sensorResetGyro() throws Exception {
        String a = "BlockAST [project=[[Location [x=-13, y=105], GyroSensor [mode=RESET, port=S2]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_resetGyro.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_setGyro.xml");
    }

    @Test
    public void reverseTransformation2() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_resetGyro.xml");
    }

}
