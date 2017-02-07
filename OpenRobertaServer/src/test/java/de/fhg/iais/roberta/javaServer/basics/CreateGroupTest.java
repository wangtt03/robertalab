package de.fhg.iais.roberta.javaServer.basics;

import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.persistence.bo.Groups;
import de.fhg.iais.roberta.persistence.bo.Role;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.GroupDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

public class CreateGroupTest {
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
    public void createGroup() throws Exception {
        //Create a list of users
        for ( int userNumber = 0; userNumber < CreateGroupTest.TOTAL_USERS; userNumber++ ) {
            User user = this.userDao.loadUser("account-" + userNumber);
            if ( user == null ) {
                User user2 = new User("account-" + userNumber);
                user2.setEmail("stuff-" + userNumber);
                user2.setPassword("pass-" + userNumber);
                user2.setRole(Role.STUDENT);
                user2.setTags("rwth");
                this.hSession.save(user2);
                this.hSession.commit();
            }
        }
        List<User> userList = this.userDao.loadUserList("created", 0, "rwth");
        Assert.assertTrue(userList.size() == 5);

        //Create a group
        User owner = this.userDao.loadUser("account-" + 0);
        Groups group = this.groupDao.loadGroup("TestGroup", owner.getId());
        this.hSession.save(group);
        this.hSession.commit();

        //Show list of groups
        List<Groups> userGroupList = this.groupDao.loadAll(owner);
        Assert.assertTrue(userGroupList.size() == 1);
    }

    @After
    public void tearDown() {
        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
    }

}
