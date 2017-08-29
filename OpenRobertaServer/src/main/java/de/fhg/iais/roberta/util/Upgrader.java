package de.fhg.iais.roberta.util;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

/**
 * assembles all actions to be done to upgrade the server version. This may include<br>
 * - executing SQL scripts and<br>
 * - loading, transforming and updating rows in the database<br>
 * the method {@link Upgrader#to(String)} either contains small updates or should delegate to a method for updates with many actions
 *
 * @author rbudde
 */
public class Upgrader {
    private static final Logger LOG = LoggerFactory.getLogger(Upgrader.class);

    private Upgrader() {
        // no objects
    }

    /**
     * execute the updates. Please add new updates (for server versions with higher versions) at the beginning of the if. If the upgrade contains many actions,
     * please delegate to a method whose name contains the server version, e.g. <code>private static void to_2_2_0()</code>
     *
     * @param serverVersion
     */
    public static void to(String serverVersion) {
        if ( serverVersion == null ) {
            LOG.error("Abort: serverVersion to upgrade to is null");
            System.exit(4);
        }
        LOG.info("upgrading to server version " + serverVersion);
        if ( serverVersion.equals("2.2.7") ) {
            String dbUrl = RobertaProperties.getStringProperty("hibernate.connection.url");
            SessionFactoryWrapper sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-cfg.xml", dbUrl);
            Session nativeSession = sessionFactoryWrapper.getNativeSession();
            DbSetup dbSetup = new DbSetup(nativeSession);
            nativeSession.beginTransaction();
            dbSetup.runDatabaseSetup(
                "/update-2-2-7.sql",
                "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PENDING_EMAIL_CONFIRMATIONS'",
                "select count(*) from USER where ACCOUNT = 'Gallery'");
            nativeSession.createSQLQuery("shutdown").executeUpdate();
            nativeSession.close();
        } else if ( serverVersion.equals("2.2.6") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.5") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.4") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.3") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.2") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.1") ) {
            // do nothing
        } else if ( serverVersion.equals("2.2.0") ) {
            // do nothing
        } else if ( serverVersion.equals("2.1.0") ) {
            // do nothing
        } else if ( serverVersion.equals("1.9.0") ) {
            // do nothing
        } else {
            LOG.error("Abort: serverVersion to upgrade to not valid: " + serverVersion);
            System.exit(4);
        }
    }
}
