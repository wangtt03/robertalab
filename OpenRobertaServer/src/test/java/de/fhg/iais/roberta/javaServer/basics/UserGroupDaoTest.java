package de.fhg.iais.roberta.javaServer.basics;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.dao.GroupDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.DbSetup;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;

public class UserGroupDaoTest {
    private SessionFactoryWrapper sessionFactoryWrapper;
    private DbSetup memoryDbSetup;
    private String connectionUrl;
    private Session nativeSession;
    private DbSession hSession;
    private UserDao userDao;
    private GroupDao groupDao;
    UserGroupDao userGroupDao;

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
        this.userGroupDao = new UserGroupDao(this.hSession);
    }

    @Test
    public void persistUserGroupNotNull() throws Exception {
        UserGroup userGroup = this.userGroupDao.persistUserGroup(1, 3);
        this.hSession.commit();
        Assert.assertNotNull(userGroup);
    }

    @Test
    public void persistUserGroupNull() throws Exception {
        UserGroup userGroup = this.userGroupDao.persistUserGroup(1, 2);
        this.hSession.commit();
        Assert.assertNull(userGroup);
    }

    @Test
    public void deleteByIds() throws Exception {
        int userId = 1;
        int groupId = 1;
        int deleted = this.userGroupDao.deleteByIds(userId, groupId);
        this.hSession.commit();
        Assert.assertTrue(deleted == 1);
    }

    @Test
    public void deleteByIdsNotDeleted() throws Exception {
        int userId = 1;
        int groupId = 88;
        int deleted = this.userGroupDao.deleteByIds(userId, groupId);
        this.hSession.commit();
        Assert.assertTrue(deleted == 0);
    }

}
