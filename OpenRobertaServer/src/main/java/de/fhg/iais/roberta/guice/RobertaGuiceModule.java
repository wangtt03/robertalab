package de.fhg.iais.roberta.guice;

import java.util.Map;
import java.util.Properties;

import de.fhg.iais.roberta.javaServer.restServices.all.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotCommand;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotDownloadProgram;
import de.fhg.iais.roberta.javaServer.restServices.robot.RobotSensorLogging;
import de.fhg.iais.roberta.main.MailManagement;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;

public class RobertaGuiceModule extends AbstractModule {
    private static final Logger LOG = LoggerFactory.getLogger(RobertaGuiceModule.class);
    private final Properties openRobertaProperties;
    private final Map<String, IRobotFactory> robotPluginMap;
    private final RobotCommunicator robotCommunicator;

    public RobertaGuiceModule(Properties openRobertaProperties, Map<String, IRobotFactory> robotPluginMap, RobotCommunicator robotCommunicator) {
        this.openRobertaProperties = openRobertaProperties;
        this.robotPluginMap = robotPluginMap;
        this.robotCommunicator = robotCommunicator;
    }

    @Override
    protected void configure() {
        // configure at least one JAX-RS resource or the server won't start.
        bind(ClientAdmin.class);
        bind(ClientConfiguration.class);
        bind(ClientToolbox.class);
        bind(ClientProgram.class);
        bind(ClientLesson.class);
        bind(ClientUser.class);
        bind(RobotDownloadProgram.class);
        bind(RobotCommand.class);
        bind(RobotSensorLogging.class);
        bind(RestExample.class);
        bind(ClientPing.class);

        bind(SessionFactoryWrapper.class).in(Singleton.class);
        bind(RobotCommunicator.class).toInstance(this.robotCommunicator);
        bind(MailManagement.class).in(Singleton.class);

        bind(new TypeLiteral<Map<String, IRobotFactory>>() {
        }).annotatedWith(Names.named("robotPluginMap")).toInstance(robotPluginMap);
        bind(String.class).annotatedWith(Names.named("hibernate.config.xml")).toInstance("hibernate-cfg.xml");

        try {
            Names.bindProperties(binder(), this.openRobertaProperties);
        } catch ( Exception e ) {
            LOG.error("Could not bind global properties to guice", e);
        }
    }
}
