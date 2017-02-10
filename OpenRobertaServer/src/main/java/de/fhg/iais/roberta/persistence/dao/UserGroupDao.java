package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to load and store programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 */
public class UserGroupDao extends AbstractDao<UserGroup> {
    private static final Logger LOG = LoggerFactory.getLogger(UserGroupDao.class);

    /**
     * create a new DAO for user groups. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public UserGroupDao(DbSession session) {
        super(UserGroup.class, session);
    }

    public UserGroup persistUserGroup(int userId, int groupId) throws Exception {
        Assert.notNull(groupId);
        Assert.notNull(userId);
        UserGroup userGroup = loadUserGroup(userId, groupId);
        if ( userGroup == null ) {
            userGroup = new UserGroup(userId, groupId);
            //group.setPassword(password);
            this.session.save(userGroup);
            this.session.commit();
            return userGroup;
        } else {
            return null;
        }
    }

    /**
     * load all groups persisted in the database which are owned by a user given
     *
     * @return the list of all groups, may be an empty list, but never null
     */

    public UserGroup loadUserGroup(int userId, int groupId) {
        Assert.notNull(groupId);
        Assert.notNull(userId);
        Query hql = this.session.createQuery("from Group where groupId=:groupId and userId=:userId");
        hql.setInteger("groupId", groupId);
        hql.setInteger("userId", userId);
        return checkUserGroupExistance(hql);
    }

    private UserGroup checkUserGroupExistance(Query hql) {
        @SuppressWarnings("unchecked")
        final List<UserGroup> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

}
