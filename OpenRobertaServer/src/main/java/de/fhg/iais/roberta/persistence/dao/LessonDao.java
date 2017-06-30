package de.fhg.iais.roberta.persistence.dao;

import de.fhg.iais.roberta.persistence.bo.*;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * DAO class to load and store programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 *
 * @author rbudde
 */
public class LessonDao extends AbstractDao<Lesson> {
    private static final Logger LOG = LoggerFactory.getLogger(LessonDao.class);

    /**
     * create a new DAO for programs. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public LessonDao(DbSession session) {
        super(Lesson.class, session);
    }

    /**
     * load all lessons persisted in the database
     *
     * @return the list of all lessons, may be an empty list, but never null
     */
    public List<Lesson> loadAll() {
        Query hql = this.session.createQuery("from Lesson");
        @SuppressWarnings("unchecked")
        List<Lesson> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    public Lesson load(String name) {
        Assert.notNull(name);
        Query hql = this.session.createQuery("from Lesson where name=:name");
        hql.setString("name", name);
        @SuppressWarnings("unchecked")
        List<Lesson> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    public int deleteByName(String name) {
        Lesson toBeDeleted = load(name);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

    public Pair<Key, Lesson> persistLesson(String name, User user, Robot robot, String programText, Timestamp timestamp) {
        Assert.notNull(name);
        Assert.notNull(user);
        Assert.notNull(robot);
        Lesson program = load(name);
        if ( program == null ) {
            if ( timestamp == null ) {
                // save as && the program doesn't exist.
                program = new Lesson();
                program.setName(name);
                this.session.save(program);
                return Pair.of(Key.PROGRAM_SAVE_SUCCESS, program); // the only legal key if success
            } else {
                return Pair.of(Key.PROGRAM_SAVE_ERROR_PROGRAM_TO_UPDATE_NOT_FOUND, null);
            }
        } else {
            if ( timestamp == null ) {
                // save as && the program exists.
                return Pair.of(Key.PROGRAM_SAVE_AS_ERROR_PROGRAM_EXISTS, null);
            } else {
                LessonDao.LOG.error("update was requested, timestamps don't match. Has another user updated the program in the meantime?");
                return Pair.of(Key.PROGRAM_SAVE_ERROR_OPTIMISTIC_TIMESTAMP_LOCKING, null);
            }
        }
    }

}
