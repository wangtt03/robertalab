package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.mode.sensor.ev3.GyroSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.brickpi.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.brickpi.SpeechRecognition;
import de.fhg.iais.roberta.syntax.sensor.brickpi.EmotionRecognition;
import de.fhg.iais.roberta.syntax.sensor.brickpi.DescribeImage;
import de.fhg.iais.roberta.syntax.sensor.brickpi.OCR;
import de.fhg.iais.roberta.syntax.action.brickpi.SayText;
import de.fhg.iais.roberta.syntax.action.brickpi.SetWakeupWord;

import de.fhg.iais.roberta.visitor.BrickpiAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class Ev3CodePreprocessVisitor extends PreprocessProgramVisitor implements BrickpiAstVisitor<Void> {

    public Ev3CodePreprocessVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration brickConfiguration) {
        super(brickConfiguration);
        check(phrasesSet);
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( gyroSensor.getMode() != GyroSensorMode.RESET ) {
            super.visitGyroSensor(gyroSensor);
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDetectFace(DetectFace<Void> detectFace) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSpeechRecognition(SpeechRecognition<Void> speechRecognition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitEmotionRecognition(EmotionRecognition<Void> emotionRecognition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDescribeImage(DescribeImage<Void> describeImage) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitOCR(OCR<Void> ocr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSayText(SayText<Void> sayText) {
        return null;
    }

    @Override
    public Void visitSetWakeupWord(SetWakeupWord<Void> setWakeupWord) {
        // TODO Auto-generated method stub
        return null;
    }

}
