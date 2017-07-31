package de.fhg.iais.roberta.util.test.nxt;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.NxtConfiguration;
import de.fhg.iais.roberta.factory.NxtFactory;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.action.nxt.ActorPort;
import de.fhg.iais.roberta.syntax.codegen.Ast2NxcVisitor;
import de.fhg.iais.roberta.syntax.codegen.Ast2NxtSimVisitor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class Helper extends de.fhg.iais.roberta.util.test.Helper {

    public Helper() {
        this.robotFactory = new NxtFactory(null);
        Configuration brickConfiguration =
            new NxtConfiguration.Builder()
                .addActor(ActorPort.A, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.NONE))
                .addActor(ActorPort.B, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.C, new Actor(ActorType.LARGE, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addActor(ActorPort.D, new Actor(ActorType.MEDIUM, false, DriveDirection.FOREWARD, MotorSide.NONE))
                .build();
        setRobotConfiguration(brickConfiguration);
    }

    /**
     * Generate java script code as string from a given program .
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateJavaScript(String pathToProgramXml) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        String code = Ast2NxtSimVisitor.generate(this.robotConfiguration, transformer.getTree());
        return code;
    }

    /**
     * Generate nxc as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    private String generateNXCWithoutWrapping(String pathToProgramXml) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = Ast2NxcVisitor.generate((NxtConfiguration) this.robotConfiguration, transformer.getTree(), false);
        return code;
    }

    /**
     * Generate nxc as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateNXC(String pathToProgramXml, NxtConfiguration brickConfiguration) throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = Ast2NxcVisitor.generate(brickConfiguration, transformer.getTree(), true);
        return code;
    }

    /**
     * Assert that Java code generated from Blockly XML program is correct.<br>
     * All white space are ignored!
     *
     * @param correctJavaCode correct java code
     * @param fileName of the program we want to generate java code
     * @throws Exception
     */
    public void assertCodeIsOk(String correctJavaCode, String fileName) throws Exception {
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateNXCWithoutWrapping(fileName).replaceAll("\\s+", ""));
    }

    public void assertWrappedCodeIsOk(String correctJavaCode, String fileName) throws Exception {
        NxtConfiguration brickConfiguration =
            (NxtConfiguration) new NxtConfiguration.Builder()
                .addActor(ActorPort.A, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.NONE))
                .addActor(ActorPort.B, new Actor(ActorType.MEDIUM, true, DriveDirection.FOREWARD, MotorSide.LEFT))
                .addActor(ActorPort.C, new Actor(ActorType.LARGE, false, DriveDirection.FOREWARD, MotorSide.RIGHT))
                .addActor(ActorPort.D, new Actor(ActorType.MEDIUM, false, DriveDirection.FOREWARD, MotorSide.NONE))
                .build();
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateNXC(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }

}
