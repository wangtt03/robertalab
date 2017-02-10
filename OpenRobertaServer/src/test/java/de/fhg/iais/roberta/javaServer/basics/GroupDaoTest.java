package de.fhg.iais.roberta.javaServer.basics;

import java.util.List;

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

public class GroupDaoTest {
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
    public void persistGroupReturnsNotNull() throws Exception {
        //Create a list of users and groups
        for ( int number = 0; number < GroupDaoTest.TOTAL_USERS; number++ ) {
            User user = this.userDao.persistUser("account-" + number, "pass-" + number, Role.STUDENT.toString());
            this.hSession.save(user);
            this.hSession.commit();
            Group group = this.groupDao.persistGroup("group-" + number, user.getId());
            this.hSession.save(group);
            this.hSession.commit();
            //System.out.println(user.getId());
            Assert.assertNotNull(group);
        }
    }

    //TODO: try to reimplement the test so it would make more sense
    @Test
    public void persistGroupReturnsNull() throws Exception {
        Group group = null;
        Assert.assertNull(group);
    }

    @Test
    public void loadGroupNotNull() throws Exception {
        Group group = this.groupDao.loadGroup("TestGroup");
        Assert.assertNotNull(group);
    }

    @Test
    public void loadGroupNull() throws Exception {
        Group group = this.groupDao.loadGroup("Qwerty");
        Assert.assertNull(group);
    }

    @Test
    public void loadAllListOfGroupsLengthIsFive() throws Exception {
        User owner = this.userDao.loadUser("Roberta");
        List<Group> userGroupList = this.groupDao.loadAll(owner);
        System.out.println(userGroupList.size());
        Assert.assertTrue(userGroupList.size() == 5);
    }

    @After
    public void tearDown() {
        this.memoryDbSetup.deleteAllFromUserAndProgramTmpPasswords();
    }

}
