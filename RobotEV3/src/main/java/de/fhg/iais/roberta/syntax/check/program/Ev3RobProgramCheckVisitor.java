package de.fhg.iais.roberta.syntax.check.program;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotProgramCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.brickpi.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.brickpi.SpeechRecognition;
import de.fhg.iais.roberta.syntax.sensor.brickpi.OCR;
import de.fhg.iais.roberta.syntax.action.brickpi.SayText;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;

import de.fhg.iais.roberta.visitor.BrickpiAstVisitor;


public class Ev3RobProgramCheckVisitor extends RobotProgramCheckVisitor implements AstActorCommunicationVisitor<Void>, BrickpiAstVisitor<Void> {

    public Ev3RobProgramCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitDetectFace(DetectFace<Void> detectFace) {
        return null;
    }

    @Override
    public Void visitSpeechRecognition(SpeechRecognition<Void> speechRecognition) {
        return null;
    }

    @Override
    public Void visitOCR(OCR<Void> ocr) {
        return null;
    }

    @Override
    public Void visitSayText(SayText<Void> sayText) {
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        bluetoothReceiveAction.getConnection().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        bluetoothConnectAction.getAddress().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        bluetoothSendAction.getConnection().visit(this);
        bluetoothSendAction.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        bluetoothCheckConnectAction.getConnection().visit(this);
        return null;
    }

}
