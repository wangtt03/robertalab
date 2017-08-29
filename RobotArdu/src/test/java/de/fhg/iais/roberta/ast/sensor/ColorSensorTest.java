package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.botnroll.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.botnroll.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class ColorSensorTest {
    HelperBotNroll h = new HelperBotNroll();

    @Test
    public void sensorSetColor() throws Exception {
        final String a =
            "BlockAST [project=[[Location [x=-15, y=107], ColorSensor [mode=COLOUR, port=S3]], "
                + "[Location [x=-13, y=147], ColorSensor [mode=RED, port=S1]], "
                + "[Location [x=-11, y=187], ColorSensor [mode=RGB, port=S2]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_setColor.xml"));
    }

    @Test
    public void getMode() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setColor.xml");

        final ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);

        Assert.assertEquals(ColorSensorMode.COLOUR, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_setColor.xml");

        final ColorSensor<Void> cs = (ColorSensor<Void>) transformer.getTree().get(0).get(1);
        final ColorSensor<Void> cs1 = (ColorSensor<Void>) transformer.getTree().get(1).get(1);
        final ColorSensor<Void> cs2 = (ColorSensor<Void>) transformer.getTree().get(2).get(1);

        Assert.assertEquals(SensorPort.S3, cs.getPort());
        Assert.assertEquals(SensorPort.S1, cs1.getPort());
        Assert.assertEquals(SensorPort.S2, cs2.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_setColor.xml");
    }
}
