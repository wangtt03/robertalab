package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.RobotProgramCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Calliope2016Factory extends AbstractCalliopeFactory {

    public Calliope2016Factory(RobotCommunicator unusedForArdu) {

        this.name = "calliope2016";
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CalliopeCompilerWorkflow(
                RobertaProperties.getTempDirForUserProjects(),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.dir"));
        this.calliopeProperties = Util1.loadProperties("classpath:Calliope2016.properties");
        this.calliopeSimCompilerWorkflow = new MbedSimCompilerWorkflow();
        addBlockTypesFromProperties("Calliope2016.properties", this.calliopeProperties);
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

    @Override
    public RobotProgramCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public IJoystickMode getJoystickMode(String joystickMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IJoystickMode> getJoystickMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVendorId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCommandline() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSignature() {
        // TODO Auto-generated method stub
        return null;
    }
}
