package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.BotNrollConfiguration;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.botnroll.ActorPort;
import de.fhg.iais.roberta.mode.action.botnroll.BlinkMode;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.botnroll.BrickKey;
import de.fhg.iais.roberta.mode.sensor.botnroll.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.botnroll.InfraredSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.makeblock.LedOffAction;
import de.fhg.iais.roberta.syntax.action.makeblock.LedOnAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.program.BotNrollCodePreprocessVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.BotnrollAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a
 * StringBuilder. <b>This representation is correct C code for Arduino.</b> <br>
 */
public class Ast2BotNrollVisitor extends Ast2ArduVisitor implements BotnrollAstVisitor<Void> {
    private final BotNrollConfiguration brickConfiguration;
    private final boolean isTimerSensorUsed;

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private Ast2BotNrollVisitor(BotNrollConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrases, int indentation) {
        super(phrases, indentation);
        this.brickConfiguration = brickConfiguration;
        BotNrollCodePreprocessVisitor codePreprocessVisitor = new BotNrollCodePreprocessVisitor(phrases, brickConfiguration);
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        this.usedSensors = codePreprocessVisitor.getUsedSensors();
        this.usedActors = codePreprocessVisitor.getUsedActors();
        this.isTimerSensorUsed = codePreprocessVisitor.isTimerSensorUsed();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
    }

    /**
     * factory method to generate C++ code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param withWrapping if false the generated code will be without the surrounding configuration code
     */
    public static String generate(BotNrollConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        Ast2BotNrollVisitor astVisitor = new Ast2BotNrollVisitor(brickConfiguration, programPhrases, withWrapping ? 1 : 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        String toChar = "";
        String varType = showTextAction.getMsg().getVarType().toString();
        boolean isVar = showTextAction.getMsg().getKind().getName().toString().equals("VAR");
        IColorSensorMode mode = null;
        Expr<Void> tt = showTextAction.getMsg();
        if ( tt.getKind().hasName("SENSOR_EXPR") ) {
            de.fhg.iais.roberta.syntax.sensor.Sensor<Void> sens = ((SensorExpr<Void>) tt).getSens();
            if ( sens.getKind().hasName("COLOR_SENSING") ) {
                mode = ((ColorSensor<Void>) sens).getMode();
            }
        }

        this.sb.append("one.lcd");
        if ( showTextAction.getY().toString().equals("NumConst [1]") || showTextAction.getY().toString().equals("NumConst [2]") ) {
            showTextAction.getY().visit(this);
        } else {
            this.sb.append("1");
        }

        this.sb.append("(");

        if ( isVar && (varType.equals("STRING") || varType.equals("COLOR"))
            || mode != null && !mode.toString().equals("RED") && !mode.toString().equals("RGB") ) {
            toChar = ".c_str()";
        }

        if ( varType.equals("BOOLEAN") ) {
            this.sb.append("bnr.boolToString(");
            showTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().visit(this);
        }

        this.sb.append(toChar + ");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("bnr.lcdClear();");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        BlinkMode blinkingMode = (BlinkMode) lightAction.getBlinkMode();
        switch ( blinkingMode ) {
            case ON:
                this.sb.append("one.led(HIGH);");
                break;
            case OFF:
                this.sb.append("one.led(LOW);");
                break;
            default:
                throw new DbcException("Invalide blinking mode: " + blinkingMode);
        }
        return null;

    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        //9 - sound port
        this.sb.append("tone(9, ");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        final boolean reverse =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || this.brickConfiguration.getActorOnPort(ActorPort.A).getRotationDirection() == DriveDirection.BACKWARD;
        String methodName;
        String port = null;
        final boolean isDuration = motorOnAction.getParam().getDuration() != null;
        final boolean isServo = motorOnAction.getPort() == ActorPort.A || motorOnAction.getPort() == ActorPort.D;
        if ( isServo ) {
            methodName = motorOnAction.getPort() == ActorPort.A ? "one.servo1(" : "one.servo2(";
        } else {
            methodName = isDuration ? "bnr.move1mTime(" : "one.move1m(";
            port = motorOnAction.getPort() == ActorPort.B ? "1" : "2";
        }
        this.sb.append(methodName);
        if ( !isServo ) {
            this.sb.append(port + ", ");
        }
        if ( reverse ) {
            this.sb.append("-");
        }
        motorOnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            motorOnAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String port = motorStopAction.getPort() == ActorPort.B ? "1" : "2";
        if ( motorStopAction.getMode() == MotorStopMode.FLOAT ) {
            this.sb.append("one.stop1m(");

        } else {
            this.sb.append("one.brake1m(");
        }
        this.sb.append(port + ")");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        //this.sb.append(this.brickConfiguration.generateText("q") + "\n");
        final boolean isRegulatedDrive =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).isRegulated()
                || this.brickConfiguration.getActorOnPort(ActorPort.A).isRegulated();
        final boolean isDuration = driveAction.getParam().getDuration() != null;
        final boolean reverse =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || this.brickConfiguration.getActorOnPort(ActorPort.A).getRotationDirection() == DriveDirection.BACKWARD;
        final boolean localReverse = driveAction.getDirection() == DriveDirection.BACKWARD;
        String methodName;
        String sign = "";
        if ( isDuration ) {
            methodName = "bnr.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.sb.append(methodName);
        if ( (!reverse && localReverse) || (reverse && !localReverse) ) {
            sign = "-";
        }
        this.sb.append(sign);
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        this.sb.append(sign);
        driveAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        final boolean isRegulatedDrive =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).isRegulated()
                || this.brickConfiguration.getActorOnPort(ActorPort.A).isRegulated();
        final boolean isDuration = curveAction.getParamLeft().getDuration() != null;
        final boolean reverse =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || this.brickConfiguration.getActorOnPort(ActorPort.A).getRotationDirection() == DriveDirection.BACKWARD;
        final boolean localReverse = curveAction.getDirection() == DriveDirection.BACKWARD;
        String methodName;
        String sign = "";
        if ( isDuration ) {
            methodName = "bnr.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.sb.append(methodName);
        if ( (!reverse && localReverse) || (reverse && !localReverse) ) {
            sign = "-";
        }
        this.sb.append(sign);
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append(", ");
        this.sb.append(sign);
        curveAction.getParamRight().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            curveAction.getParamLeft().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        Actor leftMotor = this.brickConfiguration.getLeftMotor();
        Actor rightMotor = this.brickConfiguration.getRightMotor();
        boolean isRegulatedDrive = leftMotor.isRegulated() || rightMotor.isRegulated();
        boolean isDuration = turnAction.getParam().getDuration() != null;
        boolean isReverseLeftMotor = leftMotor.getRotationDirection() == DriveDirection.BACKWARD;
        boolean isReverseRightMotor = rightMotor.getRotationDirection() == DriveDirection.BACKWARD;
        boolean isTurnRight = turnAction.getDirection() == TurnDirection.RIGHT;

        String methodName;
        String rightMotorSign = "";
        String leftMotorSign = "";

        if ( isTurnRight && !isReverseRightMotor ) {
            rightMotorSign = "-";
        }

        if ( !isTurnRight && !isReverseLeftMotor ) {
            leftMotorSign = "-";
        }

        if ( isDuration ) {
            methodName = "bnr.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.sb.append(methodName);
        this.sb.append(leftMotorSign);
        turnAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        this.sb.append(rightMotorSign);
        turnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("one.stop();");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("one.readAdc(");
        // ports from 0 to 7
        this.sb.append(lightSensor.getPort().getPortNumber()); // we could add "-1" so the number of ports would be 1-8 for users
        // botnroll's light sensor returns values from 0 to 1023, so to get a range from 0 to 100 we divide
        // the result by 10.23
        this.sb.append(") / 10.23");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        String btnNumber;
        switch ( (BrickKey) brickSensor.getKey() ) {
            case ENTER:
                btnNumber = "2";
                break;
            case LEFT:
                btnNumber = "1";
                break;
            case RIGHT:
                btnNumber = "3";
                break;
            default:
                btnNumber = "123";
                break;
        }
        this.sb.append("bnr.buttonIsPressed(" + btnNumber + ")");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String port = colorSensor.getPort().getPortNumber();
        String colors;
        if ( port == "1" ) {
            colors = "colorsLeft, ";
        } else {
            colors = "colorsRight, ";
        }
        switch ( (ColorSensorMode) colorSensor.getMode() ) {
            case COLOUR:
                this.sb.append("bnr.colorSensorColor(");
                this.sb.append(colors);
                this.sb.append(colorSensor.getPort().getPortNumber());
                this.sb.append(")");
                break;
            case RGB:
                this.sb.append("bnr.colorSensorRGB(" + colors + port);
                this.sb.append(")[0], bnr.colorSensorRGB(" + colors + port);
                this.sb.append(")[1], bnr.colorSensorRGB(" + colors + port);
                this.sb.append(")[2]");
                break;
            case RED:
                this.sb.append("bnr.colorSensorLight(" + colors + port);
                this.sb.append(")");
                break;
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.sb.append("bnr.readBearing()");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        this.sb.append("one.readBattery()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String port = infraredSensor.getPort().getPortNumber();
        switch ( (InfraredSensorMode) infraredSensor.getMode() ) {
            case OBSTACLE:
                this.sb.append("bnr.infraredSensorObstacle(");
                break;
            case SEEK:
                this.sb.append("bnr.infraredSensorPresence(");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode!");
        }
        this.sb.append(port + ")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                this.sb.append("T.ShowSeconds()");
                break;
            case RESET:
                this.sb.append("T.ResetTimer();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String port = ultrasonicSensor.getPort().getPortNumber();
        if ( ultrasonicSensor.getPort().getPortNumber().equals("3") ) {
            this.sb.append("bnr.sonar()");
        } else {
            this.sb.append("bnr.ultrasonicDistance(" + port + ")");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().visit(this);
        incrIndentation();
        generateUserDefinedMethods();
        this.sb.append("\n \n void setup() \n");
        this.sb.append("{");
        nlIndent();
        this.sb.append("Wire.begin();");
        nlIndent();
        //set baud rate to 9600 for printing values at serial monitor:
        this.sb.append("Serial.begin(9600);   // sets baud rate to 9600bps for printing values at serial monitor.");
        nlIndent();
        // start the communication module:
        this.sb.append("one.spiConnect(SSPIN);   // starts the SPI communication module");
        nlIndent();
        this.sb.append("brm.i2cConnect(MODULE_ADDRESS);   // starts I2C communication");
        nlIndent();
        this.sb.append("brm.setModuleAddress(0x2C);");
        nlIndent();
        // stop motors:
        this.sb.append("one.stop();");
        nlIndent();
        this.sb.append("bnr.setOne(one);");
        nlIndent();
        this.sb.append("bnr.setBrm(brm);");
        nlIndent();
        this.generateSensors();
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("T.StartTimer();");
        }
        generateUsedVars();
        this.sb.append("\n}\n");
        this.sb.append("\n").append("void loop() \n");
        this.sb.append("{");
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("T.Timer();");
        }
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        this.sb.append("#include <math.h> \n");
        this.sb.append("#include <CountUpDownTimer.h> \n");
        // Bot'n Roll ONE A library:
        this.sb.append("#include <BnrOneA.h>   // Bot'n Roll ONE A library \n");
        //Bot'n Roll CoSpace Rescue Module library (for the additional sonar kit):
        this.sb.append("#include <BnrRescue.h>   // Bot'n Roll CoSpace Rescue Module library \n");
        //additional Roberta functions:
        this.sb.append("#include <RobertaFunctions.h>   // Open Roberta library \n");
        this.sb.append("#include <BnrRoberta.h>    // Open Roberta library \n");
        // SPI communication library required by BnrOne.cpp"
        this.sb.append("#include <SPI.h>   // SPI communication library required by BnrOne.cpp \n");
        // required by BnrRescue.cpp (for the additional sonar kit):
        this.sb.append("#include <Wire.h>   //a library required by BnrRescue.cpp for the additional sonar  \n");
        // declaration of object variable to control the Bot'n Roll ONE A and Rescue:
        this.sb.append("BnrOneA one; \n");
        this.sb.append("BnrRescue brm; \n");
        this.sb.append("RobertaFunctions rob;  \n");
        this.sb.append("BnrRoberta bnr(one, brm);  \n");
        if ( this.isTimerSensorUsed ) {
            this.sb.append("CountUpDownTimer T(UP, HIGH); \n");
        }
        this.sb.append("#define SSPIN  2 \n");
        this.sb.append("#define MODULE_ADDRESS 0x2C \n");
        this.sb.append("byte colorsLeft[3]={0,0,0}; \n");
        this.sb.append("byte colorsRight[3]={0,0,0};");
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            this.sb.append("\n}\n");
        }
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.usedSensors ) {
            switch ( usedSensor.getType() ) {
                case COLOR:
                    nlIndent();
                    this.sb.append("brm.setRgbStatus(ENABLE);");
                    break;
                case INFRARED:
                    nlIndent();
                    this.sb.append("one.obstacleEmitters(ON);");
                    break;
                case ULTRASONIC:
                    nlIndent();
                    this.sb.append("brm.setSonarStatus(ENABLE);");
                    break;
                case LIGHT:
                case COMPASS:
                case SOUND:
                case TOUCH:
                    break;
                default:
                    throw new DbcException("Sensor is not supported!");
            }
        }
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
