package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.Group;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to load and store programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 */
public class GroupDao extends AbstractDao<Group> {
    private static final Logger LOG = LoggerFactory.getLogger(GroupDao.class);

    /**
     * create a new DAO for groups. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public GroupDao(DbSession session) {
        super(Group.class, session);
    }

    public Group persistGroup(String name, int owner) throws Exception {
        Assert.notNull(name);
        Group group = loadGroup(name);
        UserGroupDao userGroupDao = new UserGroupDao(this.session);
        if ( group == null ) {
            group = new Group(name, owner);
            //group.setPassword(password);
            this.session.save(group);
            this.session.commit();
            UserGroup userGroup = userGroupDao.persistUserGroup(owner, group.getId());
            return group;
        } else {
            return null;
        }
    }

    /**
     * load all groups persisted in the database which are owned by a user given
     *
     * @return the list of all groups, may be an empty list, but never null
     */

    public Group loadGroup(String name) {
        Assert.notNull(name);
        Query hql = this.session.createQuery("from Group where name=:name");
        hql.setString("name", name);
        return checkGroupExistance(hql);
    }

    public List<Group> loadAll(User owner) {
        Query hql = this.session.createQuery("from Group where owner=:owner");
        hql.setEntity("owner", owner);
        @SuppressWarnings("unchecked")
        List<Group> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    //load all members of the group
    public List<User> loadMembersByGroup(String name) {
        Group group = this.loadGroup(name);
        Query hql = this.session.createQuery("from UserGroup where group=:group");
        hql.setEntity("group", group);
        @SuppressWarnings("unchecked")
        List<User> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    public List<User> loadGroupsByMember(String name) {
        UserDao userDao = new UserDao(this.session);
        User user = userDao.loadUser(name);
        Query hql = this.session.createQuery("from UserGroup where user=:user");
        hql.setEntity("user", user.getId());
        @SuppressWarnings("unchecked")
        List<User> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    private Group checkGroupExistance(Query hql) {
        @SuppressWarnings("unchecked")
        final List<Group> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

    public int deleteByName(String name) {
        final Group toBeDeleted = loadGroup(name);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }
}
