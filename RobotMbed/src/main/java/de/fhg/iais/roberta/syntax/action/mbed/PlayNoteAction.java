package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MbedAstVisitor;

/**
 * This class represents the <b>mbedActions_leds_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class PlayNoteAction<V> extends Action<V> {
    private final String duration;
    private final String frequency;

    private PlayNoteAction(String duration, String frequency, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PLAY_NOTE_ACTION"), properties, comment);
        Assert.isTrue(NumberUtils.isNumber(duration) && NumberUtils.isNumber(frequency));
        this.duration = duration;
        this.frequency = frequency;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PlayNoteAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PlayNoteAction}
     */
    private static <V> PlayNoteAction<V> make(String duration, String frequency, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PlayNoteAction<>(duration, frequency, properties, comment);
    }

    /**
     * @return get the duration of this action.
     */
    public String getDuration() {
        return this.duration;
    }

    /**
     * @return get the duration of this action.
     */
    public String getFrequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return "PlayNoteAction [ duration=" + this.duration + ", frequency=" + this.frequency + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitPlayNoteAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String duration = helper.extractField(fields, BlocklyConstants.DURATION);
        String frequency = helper.extractField(fields, BlocklyConstants.FREQUENCE);
        return PlayNoteAction.make(duration, frequency, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DURATION, this.duration);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.FREQUENCE, this.frequency);

        return jaxbDestination;
    }
}
