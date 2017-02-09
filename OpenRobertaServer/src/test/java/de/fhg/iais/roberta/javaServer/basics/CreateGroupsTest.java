package de.fhg.iais.roberta.javaServer.basics;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.persistence.bo.Group;
import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.GroupDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

public class CreateGroupsTest {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private String connectionUrl;
    private Session nativeSession;
    private DbSession hSession;
    private UserDao userDao;
    private GroupDao groupDao;

    private static final int TOTAL_USERS = 5;

    @Before
    public void setup() throws Exception {
        this.connectionUrl = "jdbc:hsqldb:mem:createNewGroupDb";
        this.sessionFactoryWrapper = new SessionFactoryWrapper("hibernate-test-cfg.xml", this.connectionUrl);
        this.nativeSession = this.sessionFactoryWrapper.getNativeSession();
        this.memoryDbSetup = new DbSetup(this.nativeSession);
        this.memoryDbSetup.runDefaultRobertaSetup();
        this.hSession = this.sessionFactoryWrapper.getSession();
        this.userDao = new UserDao(this.hSession);
        this.groupDao = new GroupDao(this.hSession);
    }

    @Test
    public void createGroups() throws Exception {
        //Create a list of users and groups
        for ( int number = 0; number < CreateGroupsTest.TOTAL_USERS; number++ ) {
            User user = this.userDao.loadUser("account-" + number);
            if ( user == null ) {
                User user2 = new User("account-" + number);
                user2.setEmail("stuff-" + number);
                user2.setPassword("pass-" + number);
                user2.setRole(Role.STUDENT);
                user2.setTags("rwth");
                this.hSession.save(user2);
                this.hSession.commit();
                user = user2;
            }
            Group group = this.groupDao.loadGroup("group-" + number);
            if ( group == null ) {
                Group group2 = new Group("group-" + number, user.getId());
                this.hSession.save(group2);
                this.hSession.commit();
                group = group2;
            }
            Assert.assertNotNull(group);
        }
    }

    @After
    public void tearDown() {
        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
    }

}
