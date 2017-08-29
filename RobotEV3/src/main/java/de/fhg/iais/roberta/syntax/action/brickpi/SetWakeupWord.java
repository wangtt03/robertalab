package de.fhg.iais.roberta.syntax.action.brickpi;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.brickpi.WakeupWord;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.BrickpiAstVisitor;

/**
 * This class represents the <b>brickpiActions_setWakeupWord</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the wakeupWord of the robot.<br/>
 * <br/>
 * The client must provide the {@link WakeupWord} (the wakeupWord Brickpis speech engine is set to).
 */
public final class SetWakeupWord<V> extends Action<V> {

    private final WakeupWord wakeupWord;

    private SetWakeupWord(WakeupWord wakeupWord, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("COGNITIVE_SET_WAKEUP_WORD"), properties, comment);
        Assert.notNull(wakeupWord, "Missing wakeupWord in SetWakeupWord block!");
        this.wakeupWord = wakeupWord;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "SetWakeupWord [" + this.wakeupWord + "]";
    }

    /**
     * Creates instance of {@link SetWakeupWord}. This instance is read only and can not be modified.
     *
     * @param wakeupWord {@link WakeupWord} the speech engine of the robot is set to,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetWakeupWord}
     */
    private static <V> SetWakeupWord<V> make(WakeupWord wakeupWord, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetWakeupWord<V>(wakeupWord, properties, comment);
    }

    public WakeupWord getWakeupWord() {
        return this.wakeupWord;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((BrickpiAstVisitor<V>) visitor).visitSetWakeupWord(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);

        String wakeupWord = helper.extractField(fields, BlocklyConstants.WAKEUPWORD);

        return SetWakeupWord.make(WakeupWord.get(wakeupWord), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.WAKEUPWORD, this.wakeupWord.toString());

        return jaxbDestination;
    }
}
