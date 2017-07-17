package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.makeblock.Joystick;
import de.fhg.iais.roberta.syntax.sensor.makeblock.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.makeblock.FlameSensor;

public interface MakeblockAstVisitor<V> extends ArduAstVisitor<V> {
    /**
     * visit a {@link VoltageSensor}.
     *
     * @param temperatureSensor to be visited
     */
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitJoystick(Joystick<V> joystick);

    V visitAccelerometer(Accelerometer<V> accelerometer);

    V visitFlameSensor(FlameSensor<V> flameSensor);

}
