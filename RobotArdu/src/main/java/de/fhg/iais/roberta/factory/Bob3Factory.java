package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import de.fhg.iais.roberta.components.Bob3Configuration;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.action.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.mode.action.botnroll.ActorPort;
import de.fhg.iais.roberta.mode.action.botnroll.BlinkMode;
import de.fhg.iais.roberta.mode.action.botnroll.BrickLedColor;
import de.fhg.iais.roberta.mode.sensor.bob3.TouchSensorMode;
import de.fhg.iais.roberta.mode.sensor.botnroll.BrickKey;
import de.fhg.iais.roberta.mode.sensor.botnroll.LightSensorMode;
import de.fhg.iais.roberta.mode.sensor.botnroll.SensorPort;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.RobotProgramCheckVisitor;
import de.fhg.iais.roberta.syntax.check.hardware.SimulationProgramCheckVisitor;
import de.fhg.iais.roberta.syntax.codegen.Ast2Bob3Visitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Bob3Factory extends AbstractRobotFactory {
    private Bob3CompilerWorkflow compilerWorkflow;
    private final Properties properties;
    private final String name;
    private final int robotPropertyNumber;

    public Bob3Factory(RobotCommunicator robotCommunicator) {
        String os = "linux";
        if ( SystemUtils.IS_OS_WINDOWS ) {
            os = "windows";
        }
        this.name = "bob3";
        this.robotPropertyNumber = RobertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new Bob3CompilerWorkflow(
                RobertaProperties.getTempDirForUserProjects(),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler." + os + ".dir"));
        this.properties = Util1.loadProperties("classpath:bob3.properties");
        addBlockTypesFromProperties("bob3.properties", this.properties);
    }

    @Override
    public IBlinkMode getBlinkMode(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Blink Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( BlinkMode mo : BlinkMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Blink Mode: " + mode);
    }

    @Override
    public List<IBlinkMode> getBlinkModes() {
        return null;
    }

    @Override
    public IActorPort getActorPort(String port) {
        if ( port == null || port.isEmpty() ) {
            throw new DbcException("Invalid Actor Port: " + port);
        }
        String sUpper = port.trim().toUpperCase(Locale.GERMAN);
        for ( ActorPort co : ActorPort.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return co;
            }
            for ( String value : co.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return co;
                }
            }
        }
        throw new DbcException("Invalid Actor Port: " + port);
    }

    @Override
    public List<IActorPort> getActorPorts() {
        return null;
    }

    @Override
    public IBrickLedColor getBrickLedColor(String color) {
        if ( color == null || color.isEmpty() ) {
            throw new DbcException("Invalid Brick Led Color: " + color);
        }
        String sUpper = color.trim().toUpperCase(Locale.GERMAN);
        for ( BrickLedColor co : BrickLedColor.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return co;
            }
            for ( String value : co.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return co;
                }
            }
        }
        throw new DbcException("Invalid Brick Led Color: " + color);

    }

    @Override
    public List<IBrickLedColor> getBrickLedColors() {
        return null;
    }

    @Override
    public IBrickKey getBrickKey(String brickKey) {
        if ( brickKey == null || brickKey.isEmpty() ) {
            throw new DbcException("Invalid Brick Key: " + brickKey);
        }
        String sUpper = brickKey.trim().toUpperCase(Locale.GERMAN);
        for ( BrickKey sp : BrickKey.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Brick Key: " + brickKey);
    }

    @Override
    public List<IBrickKey> getBrickKeys() {
        return null;
    }

    @Override
    public IColorSensorMode getColorSensorMode(String colorSensorMode) {
        return null;
    }

    @Override
    public List<IColorSensorMode> getColorSensorModes() {
        return null;
    }

    @Override
    public ILightSensorMode getLightSensorMode(String lightSensorMode) {
        if ( lightSensorMode == null || lightSensorMode.isEmpty() ) {
            throw new DbcException("Invalid Color Sensor Mode: " + lightSensorMode);
        }
        String sUpper = lightSensorMode.trim().toUpperCase(Locale.GERMAN);
        for ( LightSensorMode sp : LightSensorMode.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid Color Sensor Mode: " + lightSensorMode);
    }

    @Override
    public List<ILightSensorMode> getLightSensorModes() {
        return null;
    }

    @Override
    public ISoundSensorMode getSoundSensorMode(String soundSensorMode) {
        return null;
    }

    @Override
    public List<ISoundSensorMode> getSoundSensorModes() {
        return null;
    }

    @Override
    public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
        return null;
    }

    @Override
    public List<IGyroSensorMode> getGyroSensorModes() {
        return null;
    }

    @Override
    public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode) {
        return null;
    }

    @Override
    public List<IInfraredSensorMode> getInfraredSensorModes() {
        return null;
    }

    @Override
    public IMotorTachoMode getMotorTachoMode(String motorTachoMode) {
        return null;
    }

    @Override
    public List<IMotorTachoMode> getMotorTachoModes() {
        return null;
    }

    @Override
    public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode) {
        return null;
    }

    @Override
    public List<IUltrasonicSensorMode> getUltrasonicSensorModes() {
        return null;
    }

    @Override
    public ITouchSensorMode getTouchSensorMode(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Touch Sensor Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( TouchSensorMode ultra : TouchSensorMode.values() ) {
            if ( ultra.toString().equals(sUpper) ) {
                return ultra;
            }
            for ( String value : ultra.getValues() ) {
                if ( sUpper.equals(value.toUpperCase()) ) {
                    return ultra;
                }
            }
        }
        throw new DbcException("Invalid Touch Sensor Mode: " + mode);
    }

    @Override
    public List<ITouchSensorMode> getTouchSensorModes() {
        return null;
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        if ( port == null || port.isEmpty() ) {
            throw new DbcException("Invalid sensor port: " + port);
        }
        String sUpper = port.trim().toUpperCase(Locale.GERMAN);
        for ( SensorPort po : SensorPort.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid sensor port: " + port);
    }

    @Override
    public List<ISensorPort> getSensorPorts() {
        return null;
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public ILightSensorMode getLightColor(String mode) {
        return null;
    }

    @Override
    public List<ILightSensorMode> getLightColors() {
        return null;
    }

    @Override
    public ILightSensorActionMode getLightActionColor(String mode) {
        return null;
    }

    @Override
    public List<ILightSensorActionMode> getLightActionColors() {
        return null;
    }

    @Override
    public IWorkingState getWorkingState(String mode) {
        return null;
    }

    @Override
    public List<IWorkingState> getWorkingStates() {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "ino";
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.properties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.properties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.properties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.properties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.properties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.properties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.properties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.properties.getProperty("robot.info") != null ? this.properties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.properties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public SimulationProgramCheckVisitor getProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public List<IShowPicture> getShowPictures() {
        return null;
    }

    @Override
    public Boolean hasConfiguration() {
        return this.properties.getProperty("robot.configuration") != null ? false : true;
    }

    @Override
    public String getGroup() {
        return RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? RobertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return Ast2Bob3Visitor.generate((Bob3Configuration) brickConfiguration, phrasesSet, withWrapping);
    }

    @Override
    public String getConnectionType() {
        return this.properties.getProperty("robot.connection");
    }

    @Override
    public String getVendorId() {
        return this.properties.getProperty("robot.vendor");
    }

    @Override
    public IJoystickMode getJoystickMode(String joystickMode) {
        return null;
    }

    @Override
    public List<IJoystickMode> getJoystickMode() {
        return null;
    }

    @Override
    public String getCommandline() {
        return this.properties.getProperty("robot.connection.commandLine");
    }

    @Override
    public String getSignature() {
        return this.properties.getProperty("robot.connection.signature");
    }

    @Override
    public RobotProgramCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }
}
