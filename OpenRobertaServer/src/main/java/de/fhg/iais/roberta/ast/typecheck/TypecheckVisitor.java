package de.fhg.iais.roberta.ast.typecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.NepoBlocklyId;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.functions.Func;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GetSampleSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.syntax.tasks.ActivityTask;
import de.fhg.iais.roberta.ast.syntax.tasks.MainTask;
import de.fhg.iais.roberta.ast.syntax.tasks.StartActivityTask;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class TypecheckVisitor implements AstVisitor<BlocklyType> {
    private final int ERROR_LIMIT_FOR_TYPECHECK = 10;

    private final BrickConfiguration brickConfiguration;
    private final String programName;

    private final AddErrorProvider addErrorProvider = new AddErrorProvider();

    private final List<NepoProblem> errors = new ArrayList<>();
    private BlocklyType resultType;

    /**
     * initialize the typecheck visitor.
     * 
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     */
    TypecheckVisitor(String programName, BrickConfiguration brickConfiguration) {
        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
    }

    /**
     * factory method to generate Java code from an AST.<br>
     * 
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static TypecheckVisitor makeVisitorAndTypecheck(String programName, BrickConfiguration brickConfiguration, Phrase<BlocklyType> phrase) //
    {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);

        TypecheckVisitor astVisitor = new TypecheckVisitor(programName, brickConfiguration);
        astVisitor.resultType = phrase.visit(astVisitor);
        return astVisitor;
    }

    public List<NepoProblem> getErrors() {
        return this.errors;
    }

    public BlocklyType getResultType() {
        return this.resultType;
    }

    @Override
    public BlocklyType visitNumConst(NumConst<BlocklyType> numConst) {
        Assert.isTrue(numConst.getKind().equals(Kind.NUM_CONST));
        return BlocklyType.NUMERIC;
    }

    @Override
    public BlocklyType visitMathConst(MathConst<BlocklyType> mathConst) {
        Assert.isTrue(mathConst.getKind().equals(Kind.MATH_CONST));
        String name = mathConst.getMathConst().name();
        BlocklyType type = TypeTransformations.getConstantSignature(name);
        checkLookupNotNull(mathConst, name, type, "invalid mathematical constant");
        return type;
    }

    @Override
    public BlocklyType visitBoolConst(BoolConst<BlocklyType> boolConst) {
        Assert.isTrue(boolConst.getKind().equals(Kind.BOOL_CONST));
        return BlocklyType.BOOL;
    }

    @Override
    public BlocklyType visitStringConst(StringConst<BlocklyType> stringConst) {
        Assert.isTrue(stringConst.getKind().equals(Kind.STRING_CONST));
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitNullConst(NullConst<BlocklyType> nullConst) {
        Assert.isTrue(nullConst.getKind().equals(Kind.NULL_CONST));
        return BlocklyType.NULL;
    }

    @Override
    public BlocklyType visitColorConst(ColorConst<BlocklyType> colorConst) {
        Assert.isTrue(colorConst.getKind().equals(Kind.PICK_COLOR_CONST));
        return BlocklyType.COLOR;
    }

    @Override
    public BlocklyType visitVar(Var<BlocklyType> var) {
        return null;
    }

    @Override
    public BlocklyType visitUnary(Unary<BlocklyType> unary) {
        return null;
    }

    @Override
    public BlocklyType visitBinary(Binary<BlocklyType> binary) {
        BlocklyType left = binary.getLeft().visit(this);
        BlocklyType right = binary.getRight().visit(this);
        TypeSignature signature = TypeTransformations.getBinarySignature(binary.getOp().getOpSymbol());
        return signature.typeCheck(binary, Arrays.asList(left, right), this.addErrorProvider);
    }

    @Override
    public BlocklyType visitFunc(Func<BlocklyType> func) {
        List<BlocklyType> paramTypes = typecheckList(func.getParam());
        TypeSignature signature = TypeTransformations.getFunctionSignature(func.getFunctName().name());
        BlocklyType resultType = signature.typeCheck(func, paramTypes, this.addErrorProvider);
        return resultType;
    }

    @Override
    public BlocklyType visitActionExpr(ActionExpr<BlocklyType> actionExpr) {
        return null;
    }

    @Override
    public BlocklyType visitSensorExpr(SensorExpr<BlocklyType> sensorExpr) {
        return null;
    }

    @Override
    public BlocklyType visitEmptyExpr(EmptyExpr<BlocklyType> emptyExpr) {
        return null;
    }

    @Override
    public BlocklyType visitExprList(ExprList<BlocklyType> exprList) {
        return null;
    }

    @Override
    public BlocklyType visitActionStmt(ActionStmt<BlocklyType> actionStmt) {
        return null;
    }

    @Override
    public BlocklyType visitAssignStmt(AssignStmt<BlocklyType> assignStmt) {
        return null;
    }

    @Override
    public BlocklyType visitExprStmt(ExprStmt<BlocklyType> exprStmt) {
        return null;
    }

    @Override
    public BlocklyType visitIfStmt(IfStmt<BlocklyType> ifStmt) {
        return null;
    }

    @Override
    public BlocklyType visitRepeatStmt(RepeatStmt<BlocklyType> repeatStmt) {
        return null;
    }

    @Override
    public BlocklyType visitSensorStmt(SensorStmt<BlocklyType> sensorStmt) {
        return null;
    }

    @Override
    public BlocklyType visitStmtFlowCon(StmtFlowCon<BlocklyType> stmtFlowCon) {
        return null;
    }

    @Override
    public BlocklyType visitStmtList(StmtList<BlocklyType> stmtList) {
        return null;
    }

    @Override
    public BlocklyType visitDriveAction(DriveAction<BlocklyType> driveAction) {
        return null;
    }

    @Override
    public BlocklyType visitTurnAction(TurnAction<BlocklyType> turnAction) {
        return null;
    }

    @Override
    public BlocklyType visitLightAction(LightAction<BlocklyType> lightAction) {
        return null;
    }

    @Override
    public BlocklyType visitLightStatusAction(LightStatusAction<BlocklyType> lightStatusAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorGetPowerAction(MotorGetPowerAction<BlocklyType> motorGetPowerAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorOnAction(MotorOnAction<BlocklyType> motorOnAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorSetPowerAction(MotorSetPowerAction<BlocklyType> motorSetPowerAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorStopAction(MotorStopAction<BlocklyType> motorStopAction) {
        return null;
    }

    @Override
    public BlocklyType visitClearDisplayAction(ClearDisplayAction<BlocklyType> clearDisplayAction) {
        return null;
    }

    @Override
    public BlocklyType visitVolumeAction(VolumeAction<BlocklyType> volumeAction) {
        return null;
    }

    @Override
    public BlocklyType visitPlayFileAction(PlayFileAction<BlocklyType> playFileAction) {
        return null;
    }

    @Override
    public BlocklyType visitShowPictureAction(ShowPictureAction<BlocklyType> showPictureAction) {
        return null;
    }

    @Override
    public BlocklyType visitShowTextAction(ShowTextAction<BlocklyType> showTextAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorDriveStopAction(MotorDriveStopAction<BlocklyType> stopAction) {
        return null;
    }

    @Override
    public BlocklyType visitToneAction(ToneAction<BlocklyType> toneAction) {
        return null;
    }

    @Override
    public BlocklyType visitBrickSensor(BrickSensor<BlocklyType> brickSensor) {
        return null;
    }

    @Override
    public BlocklyType visitColorSensor(ColorSensor<BlocklyType> colorSensor) {
        return null;
    }

    @Override
    public BlocklyType visitEncoderSensor(EncoderSensor<BlocklyType> encoderSensor) {
        return null;
    }

    @Override
    public BlocklyType visitGyroSensor(GyroSensor<BlocklyType> gyroSensor) {
        return null;
    }

    @Override
    public BlocklyType visitInfraredSensor(InfraredSensor<BlocklyType> infraredSensor) {
        return null;
    }

    @Override
    public BlocklyType visitTimerSensor(TimerSensor<BlocklyType> timerSensor) {
        return null;
    }

    @Override
    public BlocklyType visitTouchSensor(TouchSensor<BlocklyType> touchSensor) {
        return null;
    }

    @Override
    public BlocklyType visitUltrasonicSensor(UltrasonicSensor<BlocklyType> ultrasonicSensor) {
        return null;
    }

    @Override
    public BlocklyType visitGetSampleSensor(GetSampleSensor<BlocklyType> getSampleSensor) {
        return null;
    }

    @Override
    public BlocklyType visitMainTask(MainTask<BlocklyType> mainTask) {
        return null;
    }

    @Override
    public BlocklyType visitActivityTask(ActivityTask<BlocklyType> activityTask) {
        return null;
    }

    @Override
    public BlocklyType visitStartActivityTask(StartActivityTask<BlocklyType> startActivityTask) {
        return null;
    }

    private List<BlocklyType> typecheckList(List<Expr<BlocklyType>> params) {
        List<BlocklyType> paramTypes = new ArrayList<>(params.size());
        for ( Expr<BlocklyType> param : params ) {
            paramTypes.add(param.visit(this));
        }
        return paramTypes;
    }

    private void checkFor(Phrase<BlocklyType> phrase, boolean condition, String message) {
        if ( !condition ) {
            if ( this.errors.size() >= this.ERROR_LIMIT_FOR_TYPECHECK ) {
                throw new RuntimeException("aborting typecheck. More than " + this.ERROR_LIMIT_FOR_TYPECHECK + " found.");
            } else {
                NepoProblem error = NepoProblem.of(NepoBlocklyId.of(-1), 1, phrase.getKind(), message);
                this.errors.add(error);
            }
        }
    }

    private void checkLookupNotNull(Phrase<BlocklyType> phrase, String name, Object supposedToBeNotNull, String message) {
        if ( supposedToBeNotNull == null ) {
            checkFor(phrase, false, message + ": " + name);
        }
    }

    public class AddErrorProvider {
        public void addError(Phrase<BlocklyType> phrase, String message) {
            checkFor(phrase, false, message);
        }
    }
}
