package de.fhg.iais.roberta.syntax.sensor.brickpi;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.BrickpiAstVisitor;

/**
 * This class represents the <b>brickpiSensors_detectFace</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for detecting a face previously saved in BrickPis database.<br/>
 * <br/>
 */
public final class DescribeImage<V> extends de.fhg.iais.roberta.syntax.sensor.Sensor<V> {

    private DescribeImage(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("COGNITIVE_DESCRIBE_IMAGE"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link DETECTFACE}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     */
    static <V> DescribeImage<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DescribeImage<V>(properties, comment);
    }

    @Override
    public String toString() {
        return "DescribeImage []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((BrickpiAstVisitor<V>) visitor).visitDescribeImage(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        return DescribeImage.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        return jaxbDestination;
    }
}
