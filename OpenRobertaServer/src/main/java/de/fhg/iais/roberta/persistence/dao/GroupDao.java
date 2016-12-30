package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.Group;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to load and store programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 *
 * @author Evgeniya
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

    public Group persistGroup(String name, String ID) throws Exception {
        Assert.notNull(name);
        final Group group = loadGroup(name);
        if ( group == null ) {
            //group = new Group(name);
            //this.session.save(user);
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
    public List<Group> loadAll(User owner) {
        int ownerId = owner.getId();
        Query hql = this.session.createQuery("from GROUP where OWNER_ID=:ownerId");
        hql.setEntity("ownerId", ownerId);
        @SuppressWarnings("unchecked")
        List<Group> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    //TODO: replace pairs with users
    public List<User> loadMembers(String groupName) {
        Query hql = this.session.createQuery("from USER_GROUP where NAME=:groupName");
        hql.setEntity("groupName", groupName);
        @SuppressWarnings("unchecked")
        List<User> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    public Group loadGroup(String name) {
        Assert.notNull(name);
        final Query hql = this.session.createQuery("from User where account=:account");
        hql.setString("name", name);

        return checkGroupExistance(hql);
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
