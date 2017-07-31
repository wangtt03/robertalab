package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.Map.Entry;

import de.fhg.iais.roberta.components.NxtConfiguration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.nxt.ActorPort;
import de.fhg.iais.roberta.mode.general.nxt.IndexLocation;
import de.fhg.iais.roberta.mode.general.nxt.PickColor;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.nxt.BrickKey;
import de.fhg.iais.roberta.mode.sensor.nxt.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.nxt.LightSensorMode;
import de.fhg.iais.roberta.mode.sensor.nxt.MotorTachoMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.program.NxtCodePreprocessVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
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
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NxtAstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable NXC code representation of a phrase to a
 * StringBuilder. <b>This representation is correct NXC code for NXT.</b> <br>
 */
public class Ast2NxcVisitor extends Ast2CppVisitor implements NxtAstVisitor<Void>, AstSensorsVisitor<Void>, AstActorCommunicationVisitor<Void>,
    AstActorDisplayVisitor<Void>, AstActorMotorVisitor<Void>, AstActorLightVisitor<Void>, AstActorSoundVisitor<Void> {
    private final NxtConfiguration brickConfiguration;

    private boolean timeSensorUsed;
    private boolean playToneActionUsed;
    private boolean driveActionUsed;
    ArrayList<VarDeclaration<Void>> usedVars;

    /**
     * initialize the Nxc code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private Ast2NxcVisitor(NxtConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);
        this.brickConfiguration = brickConfiguration;

        NxtCodePreprocessVisitor codePreprocessVisitor = new NxtCodePreprocessVisitor(programPhrases, brickConfiguration);
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        this.timeSensorUsed = codePreprocessVisitor.isTimerSensorUsed();
        this.playToneActionUsed = codePreprocessVisitor.isPlayToneUsed();
        this.driveActionUsed = codePreprocessVisitor.isDriveUsed();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
        this.userDefinedMethods = codePreprocessVisitor.getUserDefinedMethods();
    }

    /**
     * factory method to generate NXC code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param withWrapping if false the generated code will be without the surrounding configuration code
     */
    public static String generate(NxtConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) //
    {
        Assert.notNull(brickConfiguration);

        Ast2NxcVisitor astVisitor = new Ast2NxcVisitor(brickConfiguration, programPhrases, withWrapping ? 1 : 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        String value;
        switch ( (PickColor) colorConst.getValue() ) {
            case BLACK:
                value = "INPUT_BLACKCOLOR";
                break;
            case BLUE:
                value = "INPUT_BLUECOLOR";
                break;
            case GREEN:
                value = "INPUT_GREENCOLOR";
                break;
            case YELLOW:
                value = "INPUT_YELLOWCOLOR";
                break;
            case RED:
                value = "INPUT_REDCOLOR";
                break;
            case WHITE:
                value = "INPUT_WHITECOLOR";
                break;
            case MAGENTA:
                value = "INPUT_MAGENTACOLOR";
                break;
            case ORANGE:
                value = "INPUT_ORANGECOLOR";
                break;
            case LIME:
                value = "INPUT_LIMECOLOR";
                break;
            case VIOLET:
                value = "INPUT_VIOLETCOLOR";
                break;
            case CRIMSON:
                value = "INPUT_CRIMSONCOLOR";
                break;
            case PURPLE:
                value = "INPUT_PURPLECOLOR";
                break;
            default:
                value = "NULL";
        }
        this.sb.append(value);
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("PI");
                break;
            case E:
                this.sb.append("E");
                break;
            case GOLDEN_RATIO:
                this.sb.append("GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("SQRT2");
                break;
            case SQRT1_2:
                this.sb.append("SQRT1_2");
                break;
            // IEEE 754 floating point representation
            case INFINITY:
                this.sb.append("INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    protected Void generateUsedVars() {
        for ( VarDeclaration<Void> var : this.usedVars ) {
            nlIndent();
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                if ( var.getTypeVar().isArray() ) {
                    this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(" ");
                    this.sb.append("__");
                }
                this.sb.append(var.getName()).append(var.getTypeVar().isArray() ? "[]" : "").append(" = ");
                var.getValue().visit(this);
                this.sb.append(";");
                if ( var.getTypeVar().isArray() ) {
                    nlIndent();
                    this.sb.append("for(int i = 0; i < ArrayLen(" + var.getName() + "); i++) {");
                    incrIndentation();
                    nlIndent();
                    this.sb.append(var.getName()).append("[i] = __" + var.getName() + "[i];");
                    decrIndentation();
                    nlIndent();
                    this.sb.append("}");
                }
            }
        }
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(" ");
        this.sb.append(var.getName());
        if ( var.getTypeVar().isArray() ) {
            this.sb.append("[");
            ListCreate<Void> list = var.getValue().getKind().hasName("EMPTY_EXPR") ? null : (ListCreate<Void>) var.getValue();
            this.sb.append(var.getValue().getKind().hasName("EMPTY_EXPR") ? "" : list.getValue().get().size());
            this.sb.append("]");
        }
        return null;
    }

    //TODO: fix the string concatenation
    @Override
    public Void visitBinary(Binary<Void> binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        Op op = binary.getOp();
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
        switch ( op ) {
            case TEXT_APPEND:
                if ( binary.getRight().getVarType().toString().contains("NUMBER") ) {
                    this.sb.append("NumToStr(");
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                    this.sb.append(")");
                } else {
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                }
                break;

            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        }

        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                increaseLoopCounter();
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                increaseLoopCounter();
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                increaseLoopCounter();
                String varType;
                String expression = repeatStmt.getExpr().toString();
                String segments[] = expression.split(",");
                String element = segments[2];
                String arr = null;
                if ( expression.contains("NUMBER") || expression.contains("CONNECTION") ) {
                    varType = "float";
                } else if ( expression.contains("BOOLEAN") ) {
                    varType = "bool";
                } else {
                    varType = "String";
                }
                if ( !segments[6].contains("java.util") ) {
                    arr = segments[6].substring(segments[6].indexOf("[") + 1, segments[6].indexOf("]"));
                    this.sb.append(
                        "for("
                            + varType
                            + whitespace()
                            + element
                            + " = 0;"
                            + element
                            + " < sizeof("
                            + arr
                            + ") / sizeof("
                            + arr
                            + "[0]); "
                            + element
                            + "++) {");
                } else {
                    this.sb.append("while(false){");
                }
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
        } else {
            appendBreakStmt();
        }
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("Wait(15);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("Wait(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(");");
        return null;
    }

    // TODO add uploading pictures to NXT before implementing this.
    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        String methodName;
        switch ( showTextAction.getMsg().getVarType() ) {
            case STRING:
                methodName = "TextOut";
                break;
            case BOOLEAN:
                methodName = "BoolOut";
                break;
            case COLOR:
                methodName = "ColorOut";
                break;
            case NOTHING:
                switch ( showTextAction.getMsg().getProperty().getBlockType() ) {
                    case "robSensors_key_isPressed":
                    case "robSensors_touch_isPressed":
                        methodName = "BoolOut";
                        break;
                    case "robSensors_colour_getSample":
                        if ( showTextAction.getMsg().toString().indexOf("mode=COLOUR") != -1 ) {
                            methodName = "ColorOut";
                            break;
                        }
                    default:
                        methodName = "NumOut";
                        break;
                }
                break;
            case CAPTURED_TYPE:
                if ( showTextAction.getMsg().toString().contains("Number")
                    || showTextAction.getMsg().toString().contains("ADD")
                    || showTextAction.getMsg().toString().contains("MINUS")
                    || showTextAction.getMsg().toString().contains("MULTIPLY")
                    || showTextAction.getMsg().toString().contains("DIVIDE")
                    || showTextAction.getMsg().toString().contains("MOD") ) {
                    methodName = "NumOut";
                } else if ( showTextAction.getMsg().toString().contains("String") ) {
                    methodName = "TextOut";
                } else {
                    methodName = "BoolOut";
                }
                break;
            default:
                methodName = "NumOut";
                break;
        }
        this.sb.append(methodName + "(");
        showTextAction.getX().visit(this);
        this.sb.append(", (MAXLINES - ");
        showTextAction.getY().visit(this);

        this.sb.append(") * MAXLINES, ");
        showTextAction.getMsg().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("ClearScreen();");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("volume = ");
                volumeAction.getVolume().visit(this);
                this.sb.append(" * 4 / 100.0 + 0.5;");
                break;
            case GET:
                this.sb.append("volume * 100 / 4");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("PlayFile(" + playFileAction.getFileName() + ");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("PlayToneEx(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(", volume, false);");
        nlIndent();
        this.sb.append("Wait(");
        toneAction.getDuration().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        final boolean reverse = this.brickConfiguration.getActorOnPort(motorOnAction.getPort()).getRotationDirection() == DriveDirection.BACKWARD;
        final boolean isDuration = motorOnAction.getParam().getDuration() != null;
        final boolean isRegulatedDrive = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).isRegulated();
        String sing = reverse ? "-" : "";
        String methodNamePart = reverse ? "OnRev" : "OnFwd";
        if ( isDuration ) {
            this.sb.append("RotateMotor(OUT_" + motorOnAction.getPort() + ", " + sing);
            motorOnAction.getParam().getSpeed().visit(this);
            if ( motorOnAction.getDurationMode() == MotorMoveMode.ROTATIONS ) {
                this.sb.append(", 360 * ");
            } else {
                this.sb.append(", ");
            }
            motorOnAction.getParam().getDuration().getValue().visit(this);
        } else {
            if ( isRegulatedDrive ) {
                this.sb.append(methodNamePart + "Reg(OUT_" + motorOnAction.getPort() + ", ");
                motorOnAction.getParam().getSpeed().visit(this);
                this.sb.append(", OUT_REGMODE_SPEED");
            } else {
                this.sb.append(methodNamePart + "(OUT_" + motorOnAction.getPort() + ", ");
                motorOnAction.getParam().getSpeed().visit(this);
            }
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        final boolean reverse = this.brickConfiguration.getActorOnPort(motorSetPowerAction.getPort()).getRotationDirection() == DriveDirection.BACKWARD;
        String sing = reverse ? "-" : "";
        final String methodName = "OnReg";
        //final boolean isRegulated = brickConfiguration.isMotorRegulated(motorSetPowerAction.getPort());
        this.sb.append(methodName + "(OUT_" + motorSetPowerAction.getPort() + "," + sing);
        motorSetPowerAction.getPower().visit(this);
        this.sb.append(",OUT_REGMODE_SPEED");
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        final String methodName = "MotorPower";
        this.sb.append(methodName + "(OUT_" + motorGetPowerAction.getPort());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( motorStopAction.getMode() == MotorStopMode.FLOAT ) {
            this.sb.append("Float(OUT_" + motorStopAction.getPort());
        } else {
            this.sb.append("Off(OUT_" + motorStopAction.getPort());
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        this.sb.append("__speed").append(" = ");
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append(" < 100  ? ");
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append(" : 100;  ");
        nlIndent();
        final boolean isDuration = driveAction.getParam().getDuration() != null;
        final boolean reverse =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || this.brickConfiguration.getActorOnPort(this.brickConfiguration.getRightMotorPort()).getRotationDirection() == DriveDirection.BACKWARD;
        final boolean localReverse = driveAction.getDirection() == DriveDirection.BACKWARD;
        String methodName = "";
        if ( isDuration ) {
            methodName = "RotateMotorEx";
        } else {
            methodName = "OnFwdReg";
        }
        this.sb.append(methodName + "(OUT_");
        if ( this.brickConfiguration.getLeftMotorPort().toString().charAt(0) < this.brickConfiguration.getRightMotorPort().toString().charAt(0) ) {
            this.sb.append(this.brickConfiguration.getLeftMotorPort());
            this.sb.append(this.brickConfiguration.getRightMotorPort());
        } else {
            this.sb.append(this.brickConfiguration.getRightMotorPort());
            this.sb.append(this.brickConfiguration.getLeftMotorPort());
        }
        if ( !reverse && localReverse || !localReverse && reverse ) {
            this.sb.append(", -1 * ");
        } else {
            this.sb.append(", ");
        }
        this.sb.append("__speed").append(", ");
        if ( isDuration ) {
            this.sb.append("(");
            driveAction.getParam().getDuration().getValue().visit(this);
            this.sb.append(" * 360 / (PI * WHEELDIAMETER)), 0, true, true);");
            this.nlIndent();
            this.sb.append("Wait(1");
        } else {
            this.sb.append("OUT_REGMODE_SYNC");
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        final boolean isDuration = turnAction.getParam().getDuration() != null;
        final boolean reverse =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || this.brickConfiguration.getActorOnPort(this.brickConfiguration.getRightMotorPort()).getRotationDirection() == DriveDirection.BACKWARD;

        String methodName = "";
        int turnpct = 100;
        if ( isDuration ) {
            methodName = "RotateMotorEx";
        } else {
            methodName = "OnFwdSync";
        }
        this.sb.append(methodName + "(OUT_");
        if ( this.brickConfiguration.getLeftMotorPort().toString().charAt(0) < this.brickConfiguration.getRightMotorPort().toString().charAt(0) ) {
            turnpct *= -1;
            this.sb.append(this.brickConfiguration.getLeftMotorPort());
            this.sb.append(this.brickConfiguration.getRightMotorPort());
        } else {
            this.sb.append(this.brickConfiguration.getRightMotorPort());
            this.sb.append(this.brickConfiguration.getLeftMotorPort());
        }
        if ( reverse ) {
            turnpct *= -1;
        }
        this.sb.append(", ");
        turnAction.getParam().getSpeed().visit(this);
        if ( turnAction.getDirection() == TurnDirection.LEFT ) {
            turnpct *= -1;
        }
        this.sb.append(", ");
        if ( isDuration ) {
            this.sb.append("(");
            turnAction.getParam().getDuration().getValue().visit(this);
            this.sb.append(" * TRACKWIDTH / WHEELDIAMETER), " + turnpct + ", true, true);");
            this.nlIndent();
            this.sb.append("Wait(1");
        } else {
            this.sb.append(turnpct);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {

        final boolean isDuration = curveAction.getParamLeft().getDuration() != null;
        final boolean confForward =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.FOREWARD;
        final boolean blockForward = curveAction.getDirection() == DriveDirection.FOREWARD;
        String methodName = "";
        if ( isDuration ) {
            methodName = "SteerDriveEx";
        } else {
            methodName = "SteerDrive";
        }
        this.sb.append(methodName);
        this.sb.append("(OUT_" + this.brickConfiguration.getLeftMotorPort());
        this.sb.append(", OUT_" + this.brickConfiguration.getRightMotorPort());
        this.sb.append(", ");
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append(", ");
        curveAction.getParamRight().getSpeed().visit(this);
        this.sb.append(", ");
        this.sb.append(confForward == blockForward);
        if ( isDuration ) {
            this.sb.append(", ");
            curveAction.getParamLeft().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("Off(OUT_");
        if ( this.brickConfiguration.getLeftMotorPort().toString().charAt(0) < this.brickConfiguration.getRightMotorPort().toString().charAt(0) ) {
            this.sb.append(this.brickConfiguration.getLeftMotorPort());
            this.sb.append(this.brickConfiguration.getRightMotorPort());
        } else {
            this.sb.append(this.brickConfiguration.getRightMotorPort());
            this.sb.append(this.brickConfiguration.getLeftMotorPort());
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        if ( lightSensorAction.getState().toString() == "ON" ) {
            switch ( lightSensorAction.getLight().toString() ) {
                case "RED":
                    this.sb.append("SetSensorColorRed(");
                    break;
                case "GREEN":
                    this.sb.append("SetSensorColorGreen(");
                    break;
                case "BLUE":
                    this.sb.append("SetSensorColorBlue(");
                    break;
            }
        } else {
            this.sb.append("SetSensorColorNone(");
        }
        this.sb.append(lightSensorAction.getPort());
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("SensorLight(");
        this.sb.append(lightSensor.getPort());
        this.sb.append(", ");
        switch ( (LightSensorMode) lightSensor.getMode() ) {
            case RED:
                this.sb.append("\"LIGHT\"");
                break;
            case AMBIENTLIGHT:
                this.sb.append("\"AMBIENTLIGHT\"");
                break;
            default:
                throw new DbcException("Invalide mode for Color Sensor!");
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        String button = null;
        switch ( (BrickKey) brickSensor.getKey() ) {
            case ENTER:
                button = "BTNCENTER";
                break;
            case LEFT:
                button = "BTNLEFT";
                break;
            case RIGHT:
                button = "BTNRIGHT";
                break;
        }
        this.sb.append("ButtonPressed(" + button + ", false)");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        if ( this.brickConfiguration.getSensorOnPort(colorSensor.getPort()).getType().toString().contains("HT_COLOR") ) {
            this.sb.append("SensorHtColor(");
        } else {
            this.sb.append("SensorColor(");
        }
        this.sb.append(colorSensor.getPort());
        this.sb.append(", ");
        switch ( (ColorSensorMode) colorSensor.getMode() ) {
            case COLOUR:
                this.sb.append("\"COLOR\"");
                break;
            case AMBIENTLIGHT:
                this.sb.append("\"AMBIENTLIGHT\"");
                break;
            case RED:
                this.sb.append("\"LIGHT\"");
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("Sensor(");
        this.sb.append(soundSensor.getPort());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        ActorPort encoderMotorPort = (ActorPort) encoderSensor.getMotorPort();
        switch ( (MotorTachoMode) encoderSensor.getMode() ) {
            case RESET:
                this.sb.append("ResetTachoCount(OUT_" + encoderMotorPort + ");");
                break;
            case ROTATION:
                this.sb.append("MotorTachoCount(OUT_" + encoderMotorPort + ") / 360.0");
                break;
            case DEGREE:
                this.sb.append("MotorTachoCount(OUT_" + encoderMotorPort + ")");
                break;
            case DISTANCE:
                this.sb.append("MotorTachoCount(OUT_" + encoderMotorPort + ") * PI / 360.0 * WHEELDIAMETER");
                break;
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                this.sb.append("GetTimerValue(timer" + timerSensor.getTimer() + ")");
                break;
            case RESET:
                this.sb.append("ResetTimerValue(timer" + timerSensor.getTimer() + ");");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("Sensor(" + touchSensor.getPort());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("SensorUS(" + ultrasonicSensor.getPort() + ")");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {

        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        if ( this.playToneActionUsed ) {
            this.sb.append("byte volume = 0x02;");
        }
        if ( this.timeSensorUsed ) {
            if ( this.playToneActionUsed ) {
                this.sb.append("\n");
            }
            this.sb.append("long timer1;");
        }
        if ( this.driveActionUsed ) {
            this.sb.append("float __speed;");
        }
        mainTask.getVariables().visit(this);
        incrIndentation();
        this.sb.append("\n").append("task main() {");
        this.generateUsedVars();
        this.generateSensors();
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        BlocklyType arrayType = indexOfFunct.getParam().get(0).getVarType();
        String methodName = "ArrFindFirst";
        if ( indexOfFunct.getLocation() == IndexLocation.LAST ) {
            methodName = "ArrFindLast";
        }

        switch ( arrayType ) {
            case ARRAY_NUMBER:
                methodName += "Num(";
                break;
            case ARRAY_STRING:
                methodName += "Str(";
                break;
            case ARRAY_BOOLEAN:
                methodName += "Bool(";
                break;
            default:
                throw new DbcException("Invalid array type: " + arrayType);
        }

        this.sb.append(methodName);
        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        String methodName = "ArrayLen(";
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            methodName = "ArrIsEmpty(";
        }
        this.sb.append(methodName);
        lengthOfIsEmptyFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        listGetIndex.getParam().get(1).visit(this);
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append("]");
        this.sb.append(" = ");
        listSetIndex.getParam().get(2).visit(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("Constrain(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % 2 == 0)");
                break;
            case ODD:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % 2 != 0)");
                break;
            case PRIME:
                this.sb.append("MathPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            // % in nxc doesn't leave a a fractional residual, e.g. 5.2%1 = 0, so it is not possible to cheack the wholeness by "%1", that is why
            //an additional function is used
            case WHOLE:
                this.sb.append("MathIsWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" > 0)");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" < 0)");
                break;
            //it would work only for whole numbers, however, I think that it makes sense to talk about being divisible only for the whole numbers
            case DIVISIBLE_BY:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" % ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(" == 0)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("ArrSum(");
                break;
            case MIN:
                this.sb.append("ArrMin(");
                break;
            case MAX:
                this.sb.append("ArrMax(");
                break;
            case AVERAGE:
                this.sb.append("ArrMean(");
                break;
            case MEDIAN:
                this.sb.append("ArrMedian(");
                break;
            case STD_DEV:
                this.sb.append("ArrStandardDeviatioin(");
                break;
            case RANDOM:
                this.sb.append("ArrRand(");
                break;
            case MODE:
                this.sb.append("ArrMode(");
                break;
            default:
                break;
        }
        mathOnListFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("RandomFloat()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("RandomIntegerInRange(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ROOT:
                this.sb.append("sqrt(");
                break;
            case ABS:
                this.sb.append("abs(");
                break;
            //Taylor Series converge only when value is less than one. Larger values are calculated
            //using a table.
            case LN:
                this.sb.append("MathLn(");
                break;
            case LOG10:
                this.sb.append("MathLog(");
                break;
            case EXP:
                this.sb.append("MathPow(E, ");
                break;
            case POW10:
                this.sb.append("MathPow(10, ");
                break;
            //the 3 functions below accept degrees
            case SIN:
                this.sb.append("MathSin(");
                break;
            case COS:
                this.sb.append("MathCos(");
                break;
            case TAN:
                this.sb.append("MathTan(");
                break;
            case ASIN:
                this.sb.append("MathAsin(");
                break;
            //Taylor Series converge only when value is less than one. Larger values are calculated
            //using a table.
            case ATAN:
                this.sb.append("MathAtan(");
                break;
            case ACOS:
                this.sb.append("MathAcos(");
                break;
            case ROUND:
                this.sb.append("MathRound(");
                break;
            case ROUNDUP:
                this.sb.append("MathRoundUp(");
                break;
            //check why there are double brackets
            case ROUNDDOWN:
                this.sb.append("MathFloor(");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).visit(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("MathPow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        // not supported by NXC
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReadAction) {
        String methodName;
        switch ( bluetoothReadAction.getDataType() ) {
            case "Boolean":
                methodName = "BluetoothGetBoolean(";
                break;
            case "String":
                methodName = "BluetoothGetString(";
                break;
            default:
                methodName = "BluetoothGetNumber(";
        }
        this.sb.append(methodName);
        this.sb.append(bluetoothReadAction.getChannel());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        this.sb.append("(BluetoothStatus(");
        bluetoothCheckConnectAction.getConnection().visit(this);
        this.sb.append(")==NO_ERR)");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        String methodName;

        switch ( bluetoothSendAction.getDataType() ) {
            case "Boolean":
                methodName = "SendRemoteBool(";
                break;
            case "String":
                methodName = "SendRemoteString(";
                break;
            default:
                methodName = "SendRemoteNumber(";
        }

        this.sb.append(methodName);
        bluetoothSendAction.getConnection().visit(this);
        this.sb.append(", ");
        this.sb.append(bluetoothSendAction.getChannel());
        this.sb.append(", ");
        bluetoothSendAction.getMsg().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("#define WHEELDIAMETER " + this.brickConfiguration.getWheelDiameterCM() + "\n");
        this.sb.append("#define TRACKWIDTH " + this.brickConfiguration.getTrackWidthCM() + "\n");
        this.sb.append("#define MAXLINES 8 \n");
        this.sb.append("#include \"NEPODefs.h\" // contains NEPO declarations for the NXC NXT API resources \n \n");
        decrIndentation();
        this.generateSignaturesOfUserDefinedMethods();
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            this.sb.append("\n}\n");
        }
        generateUserDefinedMethods();
    }

    @Override
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        switch ( type ) {
            case ANY:
            case COMPARABLE:
            case ADDABLE:
            case NULL:
            case REF:
            case PRIM:
            case NOTHING:
            case CAPTURED_TYPE:
            case R:
            case S:
            case T:
                return "";
            case ARRAY:
                return "int";
            case ARRAY_NUMBER:
                return "float";
            case ARRAY_STRING:
                return "string";
            case ARRAY_BOOLEAN:
                return "bool";
            case BOOLEAN:
                return "bool";
            case NUMBER:
                return "float";
            case NUMBER_INT:
                return "int";
            case STRING:
                return "string";
            case VOID:
                return "void";
            case COLOR:
                return "int";
            case CONNECTION:
                return "int";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    private void generateSensors() {
        for ( final Entry<ISensorPort, Sensor> entry : this.brickConfiguration.getSensors().entrySet() ) {
            nlIndent();
            this.sb.append("SetSensor(");
            switch ( entry.getValue().getType() ) {
                case COLOR:
                    this.sb.append(entry.getKey() + ", SENSOR_COLORFULL);");
                    break;
                case HT_COLOR:
                    this.sb.append(entry.getKey() + ", SENSOR_LOWSPEED);");
                    break;
                case LIGHT:
                    this.sb.append(entry.getKey() + ", SENSOR_LIGHT);");
                    break;
                case TOUCH:
                    this.sb.append(entry.getKey() + ", SENSOR_TOUCH);");
                    break;
                case ULTRASONIC:
                    this.sb.append(entry.getKey() + ", SENSOR_LOWSPEED);");
                    break;
                case SOUND:
                    this.sb.append(entry.getKey() + ", SENSOR_SOUND);");
                    break;
                default:
                    break;
            }
        }
        if ( this.timeSensorUsed ) {
            nlIndent();
            this.sb.append("SetTimerValue(timer1);");
        }
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        // TODO Auto-generated method stub
        return null;
    }
}
