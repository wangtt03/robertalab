package de.fhg.iais.roberta.ast.usedhardwarecheck;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.EV3Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.ev3.ActorPort;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.Ev3CodePreprocessVisitor;
import de.fhg.iais.roberta.util.test.ev3.Helper;

public class EV3ProgramUsedHardwareCheckTest {

    Helper h = new Helper();

    private Configuration makeConfiguration() {
        return new EV3Configuration.Builder()
            .setTrackWidth(17)
            .setWheelDiameter(5.6)
            .addActor(ActorPort.A, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))
            .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))
            .addActor(ActorPort.D, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.NONE))
            .addSensor(SensorPort.S1, new Sensor(SensorType.TOUCH))
            .addSensor(SensorPort.S2, new Sensor(SensorType.ULTRASONIC))
            .build();
    }

    @Test
    public void test0ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test1ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check1.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test2ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check2.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH], UsedSensor [S3, COLOR, COLOUR]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test3ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check3.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH], UsedSensor [S4, ULTRASONIC, DISTANCE]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test4ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check4.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals(
            "[UsedSensor [S4, INFRARED, DISTANCE], UsedSensor [S4, ULTRASONIC, DISTANCE], UsedSensor [S1, TOUCH, TOUCH]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE], UsedActor [A, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test5ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test6ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt1.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [S1, TOUCH, TOUCH]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test7ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt2.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test8ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/control/wait_stmt3.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[UsedSensor [S4, INFRARED, DISTANCE]]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test9ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check5.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [B, LARGE]]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test10ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check6.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals(
            "[UsedSensor [S3, COLOR, COLOUR], UsedSensor [S4, INFRARED, DISTANCE], UsedSensor [S4, ULTRASONIC, DISTANCE]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test11ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/ast/methods/method_return_3.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test12ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check7.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals(
            "[UsedSensor [S3, COLOR, COLOUR], UsedSensor [S3, COLOR, AMBIENTLIGHT], UsedSensor [S4, COLOR, RED]]",
            checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[]", checkVisitor.getUsedActors().toString());
    }

    @Test
    public void test13ok() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/hardware_check8.xml");

        Ev3CodePreprocessVisitor checkVisitor = new Ev3CodePreprocessVisitor(phrases, makeConfiguration());
        Assert.assertEquals("[]", checkVisitor.getUsedSensors().toString());
        Assert.assertEquals("[UsedActor [D, MEDIUM]]", checkVisitor.getUsedActors().toString());
    }
}
