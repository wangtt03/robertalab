package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.MakeBlockConfiguration;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.makeblock.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.makeblock.FlameSensor;
import de.fhg.iais.roberta.syntax.sensor.makeblock.Joystick;
import de.fhg.iais.roberta.visitor.MakeblockAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class MakeBlockCodePreprocessVisitor extends ArduCodePreprocessVisitor implements MakeblockAstVisitor<Void> {
    private boolean isToneActionUsed = false;
    private boolean isTemperatureSensorUsed = false;

    public MakeBlockCodePreprocessVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, MakeBlockConfiguration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    public boolean isToneActionUsed() {
        return this.isToneActionUsed;
    }

    public boolean isTemperatureSensorUsed() {
        return this.isTemperatureSensorUsed;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.usedSensors.add(new UsedSensor(temperatureSensor.getPort(), SensorType.TEMPERATURE, null));
        this.isTemperatureSensorUsed = true;
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        return null;
    }

    @Override
    public Void visitAccelerometer(Accelerometer<Void> accelerometer) {
        this.usedSensors.add(new UsedSensor(accelerometer.getPort(), SensorType.ACCELEROMETER, accelerometer.getCoordinate()));
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.usedSensors.add(new UsedSensor(gyroSensor.getPort(), SensorType.GYROSCOPE, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> flameSensor) {
        this.usedSensors.add(new UsedSensor(flameSensor.getPort(), SensorType.FLAMESENSOR, null));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        super.visitToneAction(toneAction);
        this.isToneActionUsed = true;
        return null;
    }

}
