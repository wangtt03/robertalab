package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.Group;
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
        Group group = loadGroup(name);
        if ( group == null ) {
            //group = new Group(name);
            //this.session.save(user);
            return group;
        } else {
            return null;
        }
    }

    public Group loadGroup(String name) {
        Assert.notNull(name);
        Query hql = this.session.createQuery("from User where account=:account");
        hql.setString("name", name);

        return checkGroupExistance(hql);
    }

    private Group checkGroupExistance(Query hql) {
        @SuppressWarnings("unchecked")
        List<Group> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

}
