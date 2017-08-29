package de.fhg.iais.roberta.syntax.check.hardware;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.check.CheckVisitor;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

public abstract class ProgramCheckVisitor extends CheckVisitor
    implements AstActorMotorVisitor<Void>, AstSensorsVisitor<Void>, AstActorDisplayVisitor<Void>, AstActorLightVisitor<Void>, AstActorSoundVisitor<Void> {

    protected ArrayList<ArrayList<Phrase<Void>>> checkedProgram;
    protected int errorCount = 0;
    protected int warningCount = 0;
    protected Configuration brickConfiguration;

    public ProgramCheckVisitor(Configuration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public void check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(this);
            }
        }
        this.checkedProgram = phrasesSet;
    }

    /**
     * @return the checkedProgram
     */
    public ArrayList<ArrayList<Phrase<Void>>> getCheckedProgram() {
        return this.checkedProgram;
    }

    /**
     * @return the countErrors
     */
    public int getErrorCount() {
        return this.errorCount;
    }

    /**
     * @return the warningCount
     */
    public int getWarningCount() {
        return this.warningCount;
    }

    protected abstract void checkSensorPort(BaseSensor<Void> sensor);

    @Override
    public Void visitVar(Var<Void> var) {
        String name = var.getValue();
        if ( !this.declaredVariables.contains(name) ) {
            var.addInfo(NepoInfo.error("VARIABLE_USED_BEFORE_DECLARATION"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkDiffDrive(driveAction);
        Expr<Void> speed = driveAction.getParam().getSpeed();
        speed.visit(this);
        checkForZeroSpeed(speed, driveAction);
        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        visitMotorDuration(duration);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkDiffDrive(turnAction);
        Expr<Void> speed = turnAction.getParam().getSpeed();
        speed.visit(this);
        checkForZeroSpeed(speed, turnAction);
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        visitMotorDuration(duration);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        checkMotorPort(motorGetPowerAction);
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        checkMotorPort(motorOnAction);
        checkForZeroSpeed(motorOnAction.getParam().getSpeed(), motorOnAction);
        MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        visitMotorDuration(duration);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        checkMotorPort(motorSetPowerAction);
        motorSetPowerAction.getPower().visit(this);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        checkMotorPort(motorStopAction);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        checkDiffDrive(stopAction);
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        checkSensorPort(colorSensor);
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        if ( this.brickConfiguration.getActorOnPort(encoderSensor.getMotorPort()) == null ) {
            encoderSensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> driveAction) {
        checkDiffDrive(driveAction);
        checkForZeroSpeed(driveAction.getParamLeft().getSpeed(), driveAction);
        checkForZeroSpeed(driveAction.getParamRight().getSpeed(), driveAction);
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        checkSensorPort(gyroSensor);
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        checkSensorPort(infraredSensor);
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        checkSensorPort(touchSensor);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        checkSensorPort(lightSensor);
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        checkSensorPort(soundSensor);
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return null;
    }

    private void checkDiffDrive(Phrase<Void> driveAction) {
        checkLeftRightMotorPort(driveAction);

    }

    private void checkMotorPort(MoveAction<Void> action) {
        if ( this.brickConfiguration.getActorOnPort(action.getPort()) == null ) {
            action.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
            this.errorCount++;
        }
    }

    private void checkLeftRightMotorPort(Phrase<Void> driveAction) {
        Actor leftMotor = this.brickConfiguration.getLeftMotor();
        Actor rightMotor = this.brickConfiguration.getRightMotor();
        checkLeftMotorPresenceAndRegulation(driveAction, leftMotor);
        checkRightMotorPresenceAndRegulation(driveAction, rightMotor);
        checkLeftAndRightMotorRotationDirection(driveAction, leftMotor, rightMotor);
        checkNumberOfMotors(driveAction);

    }

    private void checkRightMotorPresenceAndRegulation(Phrase<Void> driveAction, Actor rightMotor) {
        if ( rightMotor == null ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
            this.errorCount++;
        } else {
            checkIfMotorRegulated(driveAction, rightMotor, "CONFIGURATION_ERROR_MOTOR_RIGHT_UNREGULATED");
        }
    }

    private void checkLeftMotorPresenceAndRegulation(Phrase<Void> driveAction, Actor leftMotor) {
        if ( leftMotor == null ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));
            this.errorCount++;
        } else {
            checkIfMotorRegulated(driveAction, leftMotor, "CONFIGURATION_ERROR_MOTOR_LEFT_UNREGULATED");
        }
    }

    private void checkLeftAndRightMotorRotationDirection(Phrase<Void> driveAction, Actor leftMotor, Actor rightMotor) {
        if ( leftMotor != null && rightMotor != null && leftMotor.getRotationDirection() != rightMotor.getRotationDirection() ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTORS_ROTATION_DIRECTION"));
            this.errorCount++;
        }
    }

    private void checkNumberOfMotors(Phrase<Void> driveAction) {
        if ( this.brickConfiguration.getNumberOfRightMotors() > 1 ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MULTIPLE_RIGHT_MOTORS"));
            this.errorCount++;
        }
        if ( this.brickConfiguration.getNumberOfLeftMotors() > 1 ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MULTIPLE_LEFT_MOTORS"));
            this.errorCount++;
        }
    }

    private void checkIfMotorRegulated(Phrase<Void> driveAction, Actor motor, String errorMsg) {
        if ( !motor.isRegulated() ) {
            driveAction.addInfo(NepoInfo.error(errorMsg));
            this.errorCount++;
        }
    }

    private void checkForZeroSpeed(Expr<Void> speed, Action<Void> action) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            NumConst<Void> speedNumConst = (NumConst<Void>) speed;
            if ( Integer.valueOf(speedNumConst.getValue()) == 0 ) {
                action.addInfo(NepoInfo.warning("MOTOR_SPEED_0"));
                this.warningCount++;
            }
        }
    }

    private void visitMotorDuration(MotorDuration<Void> duration) {
        if ( duration != null ) {
            duration.getValue().visit(this);
        }
    }

}
