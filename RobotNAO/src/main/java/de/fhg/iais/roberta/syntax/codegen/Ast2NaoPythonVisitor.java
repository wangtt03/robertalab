package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.components.NAOConfiguration;
import de.fhg.iais.roberta.mode.action.nao.Camera;
import de.fhg.iais.roberta.mode.action.nao.TurnDirection;
import de.fhg.iais.roberta.mode.action.nao.WalkDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.nao.Animation;
import de.fhg.iais.roberta.syntax.action.nao.ApplyPosture;
import de.fhg.iais.roberta.syntax.action.nao.GetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.GetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Hand;
import de.fhg.iais.roberta.syntax.action.nao.LedOff;
import de.fhg.iais.roberta.syntax.action.nao.LedReset;
import de.fhg.iais.roberta.syntax.action.nao.MoveJoint;
import de.fhg.iais.roberta.syntax.action.nao.PlayFile;
import de.fhg.iais.roberta.syntax.action.nao.PointLookAt;
import de.fhg.iais.roberta.syntax.action.nao.RandomEyesDuration;
import de.fhg.iais.roberta.syntax.action.nao.RastaDuration;
import de.fhg.iais.roberta.syntax.action.nao.RecordVideo;
import de.fhg.iais.roberta.syntax.action.nao.SayText;
import de.fhg.iais.roberta.syntax.action.nao.SetIntensity;
import de.fhg.iais.roberta.syntax.action.nao.SetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.SetLeds;
import de.fhg.iais.roberta.syntax.action.nao.SetMode;
import de.fhg.iais.roberta.syntax.action.nao.SetStiffness;
import de.fhg.iais.roberta.syntax.action.nao.SetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Stop;
import de.fhg.iais.roberta.syntax.action.nao.TakePicture;
import de.fhg.iais.roberta.syntax.action.nao.TurnDegrees;
import de.fhg.iais.roberta.syntax.action.nao.WalkAsync;
import de.fhg.iais.roberta.syntax.action.nao.WalkDistance;
import de.fhg.iais.roberta.syntax.action.nao.WalkTo;
import de.fhg.iais.roberta.syntax.check.program.NaoCodePreprocessVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.nao.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.nao.Dialog;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrent;
import de.fhg.iais.roberta.syntax.sensor.nao.ForceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.ForgetFace;
import de.fhg.iais.roberta.syntax.sensor.nao.Gyrometer;
import de.fhg.iais.roberta.syntax.sensor.nao.LearnFace;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoGetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMark;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizedWord;
import de.fhg.iais.roberta.syntax.sensor.nao.Sonar;
import de.fhg.iais.roberta.syntax.sensor.nao.Touchsensors;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public class Ast2NaoPythonVisitor extends Ast2PythonVisitor implements NaoAstVisitor<Void> {

    private final NAOConfiguration brickConfiguration;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    private Ast2NaoPythonVisitor(NAOConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);

        this.brickConfiguration = brickConfiguration;
        NaoCodePreprocessVisitor checker = new NaoCodePreprocessVisitor(programPhrases, brickConfiguration);
        this.usedGlobalVarInFunctions = checker.getMarkedVariablesAsGlobal();
        this.isProgramEmpty = checker.isProgramEmpty();
        this.loopsLabels = checker.getloopsLabelContainer();
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(NAOConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) //
    {
        Assert.notNull(brickConfiguration);

        Ast2NaoPythonVisitor astVisitor = new Ast2NaoPythonVisitor(brickConfiguration, phrasesSet, 0);
        astVisitor.generateCode(withWrapping);

        return astVisitor.sb.toString();
    }

    //    @Override
    //    protected String getEnumCode(IMode value) {
    //        return "'" + value.toString().toLowerCase() + "'";
    //    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.sb.append("\"\"");
                break;
            case BOOLEAN:
                this.sb.append("True");
                break;
            case NUMBER_INT:
                this.sb.append("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            case COLOR:
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("h.wait(15)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("h.wait(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this);
        this.sb.append("\n").append("def run():");
        incrIndentation();
        List<Stmt<Void>> variableList = variables.get();
        if ( !variableList.isEmpty() ) {
            nlIndent();
            // insert global statement for all variables
            // TODO: there must be an easier way without the casts
            // TODO: we'd only list variables that we change, ideally we'd do this in
            // visitAssignStmt(), but we must only to this once per method and visitAssignStmt()
            // would need the list of mainTask variables (store in the class?)
            // TODO: I could store the names as a list in the instance and filter it against the parameters
            // in visitMethodVoid, visitMethodReturn
            this.sb.append("global ");
            boolean first = true;
            for ( Stmt<Void> s : variables.get() ) {
                ExprStmt<Void> es = (ExprStmt<Void>) s;
                VarDeclaration<Void> vd = (VarDeclaration<Void>) es.getExpr();
                if ( first ) {
                    first = false;
                } else {
                    this.sb.append(", ");
                }
                this.sb.append(vd.getName());
            }
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        this.sb.append("print(");
        textPrintFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        this.sb.append(getEnumCode(where1));
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        this.sb.append(getEnumCode(where2));
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                this.sb.append("BlocklyMethods.findFirst( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("BlocklyMethods.findLast( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LISTS_LENGTH:
                this.sb.append("BlocklyMethods.length( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("BlocklyMethods.isEmpty( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.sb.append("[]");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("BlocklyMethods.createListWith(");
        listCreate.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("BlocklyMethods.createListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        this.sb.append("BlocklyMethods.listsGetIndex(");
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getElementOperation()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getLocation()));
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        this.sb.append("BlocklyMethods.listsSetIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getElementOperation()));
        this.sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getLocation()));
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("BlocklyMethods.clamp(");
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
                this.sb.append("BlocklyMethods.isEven(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case ODD:
                this.sb.append("BlocklyMethods.isOdd(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case PRIME:
                this.sb.append("BlocklyMethods.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("BlocklyMethods.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("BlocklyMethods.isPositive(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case NEGATIVE:
                this.sb.append("BlocklyMethods.isNegative(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case DIVISIBLE_BY:
                this.sb.append("BlocklyMethods.isDivisibleBy(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(")");
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
                this.sb.append("BlocklyMethods.sumOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                this.sb.append("BlocklyMethods.minOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                this.sb.append("BlocklyMethods.maxOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                this.sb.append("BlocklyMethods.averageOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                this.sb.append("BlocklyMethods.medianOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                this.sb.append("BlocklyMethods.standardDeviatioin(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                this.sb.append("BlocklyMethods.randOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                this.sb.append("BlocklyMethods.modeOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("BlocklyMethods.randDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("BlocklyMethods.randInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        this.sb.append("BlocklyMethods.textJoin(");
        textJoinFunct.getParam().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetMode(SetMode<Void> setMode) {
        this.sb.append("h.mode(");
        switch ( setMode.getModus() ) {
            case ACTIVE:
                this.sb.append("1)");
                break;
            case REST:
                this.sb.append("2)");
                break;
            case SIT:
                this.sb.append("3)");
                break;
        }
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture<Void> applyPosture) {
        this.sb.append("h.applyPosture(");
        switch ( applyPosture.getPosture() ) {
            case STAND:
                this.sb.append("\"Stand\")");
                break;
            case STANDINIT:
                this.sb.append("\"StandInit\")");
                break;
            case STANDZERO:
                this.sb.append("\"StandZero\")");
                break;
            case SITRELAX:
                this.sb.append("\"SitRelax\")");
                break;
            case SIT:
                this.sb.append("\"Sit\")");
                break;
            case LYINGBELLY:
                this.sb.append("\"LyingBelly\")");
                break;
            case LYINGBACK:
                this.sb.append("\"LyingBack\")");
                break;
            case CROUCH:
                this.sb.append("\"Crouch\")");
                break;
        }
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness<Void> setStiffness) {
        this.sb.append("h.stiffness(");
        switch ( setStiffness.getBodyPart() ) {
            case BODY:
                this.sb.append("\"Body\"");
                break;
            case HEAD:
                this.sb.append("\"Head\"");
                break;
            case ARMS:
                this.sb.append("\"Arms\"");
                break;
            case LEFTARM:
                this.sb.append("\"LArm\"");
                break;
            case RIGHTARM:
                this.sb.append("\"RArm\"");
                break;
            case LEGS:
                this.sb.append("\"Legs\"");
                break;
            case LEFTLEG:
                this.sb.append("\"LLeg\"");
                break;
            case RIHTLEG:
                this.sb.append("\"RLeg\"");
                break;
        }

        switch ( setStiffness.getOnOff() ) {
            case ON:
                this.sb.append(", 1)");
                break;
            case OFF:
                this.sb.append(", 2)");
                break;
        }
        return null;
    }

    @Override
    public Void visitHand(Hand<Void> hand) {
        this.sb.append("h.hand(");
        switch ( hand.getTurnDirection() ) {
            case LEFT:
                this.sb.append("\"LHand\"");
                break;
            case RIGHT:
                this.sb.append("\"RHand\"");
                break;
        }

        switch ( hand.getModus() ) {
            case ACTIVE:
                this.sb.append(", 1)");
                break;
            case REST:
                this.sb.append(", 2)");
                break;
            case SIT:
                this.sb.append(", 3)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        this.sb.append("h.moveJoint(");
        switch ( moveJoint.getJoint() ) {
            case HEADYAW:
                this.sb.append("\"HeadYaw\"");
                break;
            case HEADPITCH:
                this.sb.append("\"HeadPitch\"");
                break;
            case LSHOULDERPITCH:
                this.sb.append("\"LShoulderPitch\"");
                break;
            case LSHOULDERROLL:
                this.sb.append("\"LShoulderRoll\"");
                break;
            case LELBOWYAW:
                this.sb.append("\"LElbowYaw\"");
                break;
            case LELBOWROLL:
                this.sb.append("\"LElbowRoll\"");
                break;
            case LWRISTYAW:
                this.sb.append("\"LWristYaw\"");
                break;
            case LHAND:
                this.sb.append("\"LHand\"");
                break;
            case LHIPYAWPITCH:
                this.sb.append("\"LHipYawPitch\"");
                break;
            case LHIPROLL:
                this.sb.append("\"LHipRoll\"");
                break;
            case LHIPPITCH:
                this.sb.append("\"LHipPitch\"");
                break;
            case LKNEEPITCH:
                this.sb.append("\"LKneePitch\"");
                break;
            case LANKLEPITCH:
                this.sb.append("\"LAnklePitch\"");
                break;
            case RANKLEROLL:
                this.sb.append("\"RAnkleRoll\"");
                break;
            case RHIPYAWPITCH:
                this.sb.append("\"RHipYawPitch\"");
                break;
            case RHIPROLL:
                this.sb.append("\"RHipRoll\"");
                break;
            case RHIPITCH:
                this.sb.append("\"RHipPitch\"");
                break;
            case RKNEEPITCH:
                this.sb.append("\"RKneePitch\"");
                break;
            case RANKLEPITCH:
                this.sb.append("\"RAnklePitch\"");
                break;
            case RSHOULDERPITCH:
                this.sb.append("\"RShoulderPitch\"");
                break;
            case RSHOULDERROLL:
                this.sb.append("\"RShoulderRoll\"");
                break;
            case RELBOWYAW:
                this.sb.append("\"RElbowYaw\"");
                break;
            case RELBOWROLL:
                this.sb.append("\"RElbowRoll\"");
                break;
            case RWRISTYAW:
                this.sb.append("\"RWristYaw\"");
                break;
            case RHAND:
                this.sb.append("\"RHand\"");
                break;
            case LANKLEROLL:
                this.sb.append("\"LAnkleRoll\"");
                break;
            default:
                break;
        }
        this.sb.append(", ");
        moveJoint.getDegrees().visit(this);
        this.sb.append(", ");
        switch ( moveJoint.getRelativeAbsolute() ) {
            case ABSOLUTE:
                this.sb.append("1)");
                break;
            case RELATIVE:
                this.sb.append("2)");
                break;
        }
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        this.sb.append("h.walk(");
        if ( walkDistance.getWalkDirection() == WalkDirection.BACKWARD ) {
            this.sb.append("-");
        }
        walkDistance.getDistanceToWalk().visit(this);
        this.sb.append(",0,0)");
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        this.sb.append("h.walk(0,0,");
        if ( turnDegrees.getTurnDirection() == TurnDirection.RIGHT ) {
            this.sb.append("-");
        }
        turnDegrees.getDegreesToTurn().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {
        this.sb.append("h.walk(");
        walkTo.getWalkToX().visit(this);
        this.sb.append(",");
        walkTo.getWalkToY().visit(this);
        this.sb.append(",");
        walkTo.getWalkToTheta().visit(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitStop(Stop<Void> stop) {
        this.sb.append("h.stop()");

        return null;
    }

    @Override
    public Void visitAnimation(Animation<Void> animation) {
        switch ( animation.getMove() ) {
            case TAICHI:
                this.sb.append("h.taiChi()");
                break;
            case BLINK:
                this.sb.append("h.blink()");
                break;
            case WAVE:
                this.sb.append("h.wave()");
                break;
            case WIPEFOREHEAD:
                this.sb.append("h.wipeForehead()");
                break;
        }
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt<Void> pointLookAt) {
        this.sb.append("h.pointLookAt(");
        pointLookAt.getpointX().visit(this);
        this.sb.append(", ");
        pointLookAt.getpointY().visit(this);
        this.sb.append(", ");
        pointLookAt.getpointZ().visit(this);
        this.sb.append(", ");
        switch ( pointLookAt.getFrame() ) {
            case TORSO:
                this.sb.append("0, ");
                break;
            case WORLD:
                this.sb.append("1, ");
                break;
            case ROBOT:
                this.sb.append("2, ");
                break;
        }
        pointLookAt.getSpeed().visit(this);
        switch ( pointLookAt.getPointLook() ) {
            case LOOK:
                this.sb.append(", 1)");
                break;
            case POINT:
                this.sb.append(", 0)");
                break;
        }
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {
        this.sb.append("h.setVolume(");
        setVolume.getVolume().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume<Void> getVolume) {
        this.sb.append("h.getVolume()");
        return null;
    }

    @Override
    public Void visitSetLanguage(SetLanguage<Void> setLanguage) {
        this.sb.append("h.setLanguage(");
        switch ( setLanguage.getLanguage() ) {
            case GERMAN:
                this.sb.append("\"German\")");
                break;
            case ENGLISH:
                this.sb.append("\"English\")");
                break;
            case FRENCH:
                this.sb.append("\"French\")");
                break;
            case JAPANESE:
                this.sb.append("\"Japanese\")");
                break;
            case CHINESE:
                this.sb.append("\"Chinese\")");
                break;
            case SPANISH:
                this.sb.append("\"Spanish\")");
                break;
            case KOREAN:
                this.sb.append("\"Korean\")");
                break;
            case ITALIAN:
                this.sb.append("\"Italian\")");
                break;
            case DUTCH:
                this.sb.append("\"Dutch\")");
                break;
            case FINNISH:
                this.sb.append("\"Finnish\")");
                break;
            case POLISH:
                this.sb.append("\"Polish\")");
                break;
            case RUSSIAN:
                this.sb.append("\"Russian\")");
                break;
            case TURKISH:
                this.sb.append("\"Turkish\")");
                break;
            case ARABIC:
                this.sb.append("\"Arabic\")");
                break;
            case CZECH:
                this.sb.append("\"Czech\")");
                break;
            case PORTUGUESE:
                this.sb.append("\"Portuguese\")");
                break;
            case BRAZILIAN:
                this.sb.append("\"Brazilian\")");
                break;
            case SWEDISH:
                this.sb.append("\"Swedish\")");
                break;
            case DANISH:
                this.sb.append("\"Danish\")");
                break;
            case NORWEGIAN:
                this.sb.append("\"Norwegian\")");
                break;
            case GREEK:
                this.sb.append("\"Greek\")");
                break;
        }
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        this.sb.append("h.getLanguage()");
        return null;
    }

    @Override
    public Void visitSayText(SayText<Void> sayText) {
        this.sb.append("h.say(");
        sayText.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile<Void> playFile) {
        this.sb.append("h.playFile(");
        playFile.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRecognizedWord(RecognizedWord<Void> recognizedWord) {
        this.sb.append("h.recognizeWord(");
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        this.sb.append("h.setLeds(");
        switch ( setLeds.getLed() ) {
            case ALL:
                this.sb.append("\"AllLeds\", ");
                break;
            case CHEST:
                this.sb.append("\"ChestLeds\", ");
                break;
            case EARS:
                this.sb.append("\"EarLeds\", ");
                break;
            case EYES:
                this.sb.append("\"FaceLeds\", ");
                break;
            case HEAD:
                this.sb.append("\"BrainLeds\", ");
                break;
            case LEFTEAR:
                this.sb.append("\"LeftEarLeds\", ");
                break;
            case LEFTEYE:
                this.sb.append("\"LeftFaceLeds\", ");
                break;
            case LEFTFOOT:
                this.sb.append("\"LeftFootLeds\", ");
                break;
            case RIGHTEAR:
                this.sb.append("\"RightEarLeds\", ");
                break;
            case RIGHTEYE:
                this.sb.append("\"RightFaceLeds\", ");
                break;
            case RIGHTFOOT:
                this.sb.append("\"RightFootLeds\", ");
                break;
        }
        setLeds.getColor().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        this.sb.append("h.setIntensity(");
        switch ( setIntensity.getLed() ) {
            case ALL:
                this.sb.append("\"AllLeds\", ");
                break;
            case CHEST:
                this.sb.append("\"ChestLeds\", ");
                break;
            case EARS:
                this.sb.append("\"EarLeds\", ");
                break;
            case EYES:
                this.sb.append("\"FaceLeds\", ");
                break;
            case HEAD:
                this.sb.append("\"BrainLeds\", ");
                break;
            case LEFTEAR:
                this.sb.append("\"LeftEarLeds\", ");
                break;
            case LEFTEYE:
                this.sb.append("\"LeftFaceLeds\", ");
                break;
            case LEFTFOOT:
                this.sb.append("\"LeftFootLeds\", ");
                break;
            case RIGHTEAR:
                this.sb.append("\"RightEarLeds\", ");
                break;
            case RIGHTEYE:
                this.sb.append("\"RightFaceLeds\", ");
                break;
            case RIGHTFOOT:
                this.sb.append("\"RightFootLeds\", ");
                break;
        }
        setIntensity.getIntensity().visit(this);
        this.sb.append(")");
        return null;
    }

    /*@Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        this.sb.append(ledColor.getRedChannel() + ", " + ledColor.getGreenChannel() + ", " + ledColor.getBlueChannel() + ", 255");
        return null;
    }*/

    @Override
    public Void visitLedOff(LedOff<Void> ledOff) {
        this.sb.append("h.ledOff(");
        switch ( ledOff.getLed() ) {
            case ALL:
                this.sb.append("\"AllLeds\"");
                break;
            case CHEST:
                this.sb.append("\"ChestLeds\"");
                break;
            case EARS:
                this.sb.append("\"EarLeds\"");
                break;
            case EYES:
                this.sb.append("\"FaceLeds\"");
                break;
            case HEAD:
                this.sb.append("\"BrainLeds\"");
                break;
            case LEFTEAR:
                this.sb.append("\"LeftEarLeds\"");
                break;
            case LEFTEYE:
                this.sb.append("\"LeftFaceLeds\"");
                break;
            case LEFTFOOT:
                this.sb.append("\"LeftFootLeds\"");
                break;
            case RIGHTEAR:
                this.sb.append("\"RightEarLeds\"");
                break;
            case RIGHTEYE:
                this.sb.append("\"RightFaceLeds\"");
                break;
            case RIGHTFOOT:
                this.sb.append("\"RightFootLeds\"");
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedReset(LedReset<Void> ledReset) {
        this.sb.append("h.ledReset(");
        switch ( ledReset.getLed() ) {
            case ALL:
                this.sb.append("\"AllLeds\"");
                break;
            case CHEST:
                this.sb.append("\"ChestLeds\"");
                break;
            case EARS:
                this.sb.append("\"EarLeds\"");
                break;
            case EYES:
                this.sb.append("\"FaceLeds\"");
                break;
            case HEAD:
                this.sb.append("\"BrainLeds\"");
                break;
            case LEFTEAR:
                this.sb.append("\"LeftEarLeds\"");
                break;
            case LEFTEYE:
                this.sb.append("\"LeftFaceLeds\"");
                break;
            case LEFTFOOT:
                this.sb.append("\"LeftFootLeds\"");
                break;
            case RIGHTEAR:
                this.sb.append("\"RightEarLeds\"");
                break;
            case RIGHTEYE:
                this.sb.append("\"RightFaceLeds\"");
                break;
            case RIGHTFOOT:
                this.sb.append("\"RightFootLeds\"");
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration<Void> randomEyesDuration) {
        this.sb.append("h.randomEyes(");
        randomEyesDuration.getDuration().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        this.sb.append("h.rasta(");
        rastaDuration.getDuration().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTouchsensors(Touchsensors<Void> touchsensors) {
        this.sb.append("h.touchsensors(");
        this.sb.append("\"" + touchsensors.getSensor().getPythonCode() + "\"");
        this.sb.append(", ");
        this.sb.append("\"" + touchsensors.getSide().getPythonCode() + "\"");
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSonar(Sonar<Void> sonar) {
        this.sb.append("h.ultrasonic()");
        return null;
    }

    @Override
    public Void visitGyrometer(Gyrometer<Void> gyrometer) {
        this.sb.append("h.gyrometer(");
        this.sb.append(gyrometer.getCoordinate().getPythonCode());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDialog(Dialog<Void> dialog) {
        this.sb.append("h.dialog(");
        this.sb.append(dialog.getPhrase().getPythonCode());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitAccelerometer(Accelerometer<Void> accelerometer) {
        this.sb.append("h.accelerometer(");
        this.sb.append(accelerometer.getCoordinate().getPythonCode());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitForceSensor(ForceSensor<Void> forceSensor) {
        this.sb.append("h.fsr(");
        this.sb.append(forceSensor.getSide().getPythonCode());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitNaoMark(NaoMark<Void> naoMark) {
        this.sb.append("h.naoMark()");
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {
        this.sb.append("h.takePicture(");
        if ( takePicture.getCamera() == Camera.TOP ) {
            this.sb.append("\"Top\", ");
        } else if ( takePicture.getCamera() == Camera.BOTTOM ) {
            this.sb.append("\"Bottom\", ");
        }
        takePicture.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        this.sb.append("h.recordVideo(");
        switch ( recordVideo.getResolution() ) {
            case LOW:
                this.sb.append("0, ");
                break;
            case MED:
                this.sb.append("1, ");
                break;
            case HIGH:
                this.sb.append("2, ");
                break;
        }
        switch ( recordVideo.getCamera() ) {
            case TOP:
                this.sb.append("\"Top\", ");
                break;
            case BOTTOM:
                this.sb.append("\"Bottom\", ");
                break;
        }
        recordVideo.getDuration().visit(this);
        this.sb.append(", ");
        recordVideo.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        this.sb.append("h.learnFace(");
        learnFace.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        this.sb.append("h.forgetFace(");
        forgetFace.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDetectFace(DetectFace<Void> detectFace) {
        this.sb.append("h.detectFace()");
        return null;
    }

    @Override
    public Void visitNaoGetSampleSensor(NaoGetSampleSensor<Void> sensorGetSample) {
        return sensorGetSample.getSensor().visit(this);
    }

    @Override
    public Void visitElectricCurrent(ElectricCurrent<Void> electricCurrent) {
        this.sb.append("h.getElectricCurrent(");
        switch ( electricCurrent.getJoint() ) {
            case HEADYAW:
                this.sb.append("\"HeadYaw\"");
                break;
            case HEADPITCH:
                this.sb.append("\"HeadPitch\"");
                break;
            case LSHOULDERPITCH:
                this.sb.append("\"LShoulderPitch\"");
                break;
            case LSHOULDERROLL:
                this.sb.append("\"LShoulderRoll\"");
                break;
            case LELBOWYAW:
                this.sb.append("\"LElbowYaw\"");
                break;
            case LELBOWROLL:
                this.sb.append("\"LElbowRoll\"");
                break;
            case LWRISTYAW:
                this.sb.append("\"LWristYaw\"");
                break;
            case LHAND:
                this.sb.append("\"LHand\"");
                break;
            case LHIPYAWPITCH:
                this.sb.append("\"LHipYawPitch\"");
                break;
            case LHIPROLL:
                this.sb.append("\"LHipRoll\"");
                break;
            case LHIPPITCH:
                this.sb.append("\"LHipPitch\"");
                break;
            case LKNEEPITCH:
                this.sb.append("\"LKneePitch\"");
                break;
            case LANKLEPITCH:
                this.sb.append("\"LAnklePitch\"");
                break;
            case RANKLEROLL:
                this.sb.append("\"RAnkleRoll\"");
                break;
            case RHIPYAWPITCH:
                this.sb.append("\"RHipYawPitch\"");
                break;
            case RHIPROLL:
                this.sb.append("\"RHipRoll\"");
                break;
            case RHIPITCH:
                this.sb.append("\"RHipPitch\"");
                break;
            case RKNEEPITCH:
                this.sb.append("\"RKneePitch\"");
                break;
            case RANKLEPITCH:
                this.sb.append("\"RAnklePitch\"");
                break;
            case RSHOULDERPITCH:
                this.sb.append("\"RShoulderPitch\"");
                break;
            case RSHOULDERROLL:
                this.sb.append("\"RShoulderRoll\"");
                break;
            case RELBOWYAW:
                this.sb.append("\"RElbowYaw\"");
                break;
            case RELBOWROLL:
                this.sb.append("\"RElbowRoll\"");
                break;
            case RWRISTYAW:
                this.sb.append("\"RWristYaw\"");
                break;
            case RHAND:
                this.sb.append("\"RHand\"");
                break;
            case LANKLEROLL:
                this.sb.append("\"LAnkleRoll\"");
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("#!/usr/bin/python\n\n");
        this.sb.append("import math\n");
        this.sb.append("import time\n");
        this.sb.append("from hal import Hal\n");
        this.sb.append("h = Hal()\n\n");

        this.sb.append("class BreakOutOfALoop(Exception): pass\n");
        this.sb.append("class ContinueLoop(Exception): pass\n\n");

    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("\n\n");
        this.sb.append("def main():\n");
        this.sb.append(INDENT).append("try:\n");
        this.sb.append(INDENT).append(INDENT).append("run()\n");
        this.sb.append(INDENT).append("except Exception as e:\n");
        this.sb.append(INDENT).append(INDENT).append("h.say(\"Error!\" + str(e))\n");
        this.sb.append(INDENT).append("finally:\n");
        this.sb.append(INDENT).append(INDENT).append("pass");

        this.sb.append("\n");
        this.sb.append("if __name__ == \"__main__\":\n");
        this.sb.append(INDENT).append("main()");
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitWalkAsync(WalkAsync<Void> walkAsync) {
        this.sb.append("h.walkAsync(");
        walkAsync.getXSpeed().visit(this);
        this.sb.append(", ");
        walkAsync.getYSpeed().visit(this);
        this.sb.append(", ");
        walkAsync.getZSpeed().visit(this);
        this.sb.append(")");
        return null;
    }

}