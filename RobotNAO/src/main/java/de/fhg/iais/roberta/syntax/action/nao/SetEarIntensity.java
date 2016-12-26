package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_setEarIntensity</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the intensity of the LEDs in the ears of the robot.<br/>
 * <br/>
 * The client must provide the {@link intensity} (level of intensity).
 */
public final class SetEarIntensity<V> extends Action<V> {

    private final Expr<V> intensity;

    private SetEarIntensity(Expr<V> intensity, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SET_EAR_INTENSITY"), properties, comment);
        this.intensity = intensity;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SetEarIntensity}. This instance is read only and can not be modified.
     *
     * @param intensity {@link intensity} the LEDs will be set to,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetEarIntensity}
     */
    private static <V> SetEarIntensity<V> make(Expr<V> intensity, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetEarIntensity<V>(intensity, properties, comment);
    }

    public Expr<V> getIntensity() {
        return this.intensity;
    }

    @Override
    public String toString() {
        return "SetEarIntensity [" + this.intensity + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitSetEarIntensity(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);

        Phrase<V> intensity = helper.extractValue(values, new ExprParam(BlocklyConstants.INTENSITY, Integer.class));

        return SetEarIntensity.make(helper.convertPhraseToExpr(intensity), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.INTENSITY, this.intensity);

        return jaxbDestination;
    }
}