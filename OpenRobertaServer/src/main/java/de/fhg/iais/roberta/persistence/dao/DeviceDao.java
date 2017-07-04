package de.fhg.iais.roberta.persistence.dao;

import de.fhg.iais.roberta.persistence.bo.Device;
import de.fhg.iais.roberta.persistence.bo.Lesson;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
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
public class DeviceDao extends AbstractDao<Device> {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceDao.class);

    /**
     * create a new DAO for programs. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public DeviceDao(DbSession session) {
        super(Device.class, session);
    }

    /**
     * load all lessons persisted in the database
     *
     * @return the list of all lessons, may be an empty list, but never null
     */
    public List<Device> loadAll() {
        LOG.info("LessonDao loadAll.");
        Query hql = this.session.createQuery("from Device");
        @SuppressWarnings("unchecked")
        List<Device> il = hql.list();
        LOG.info("DeviceDao loadAll count: " + il.size());
        for (Device device : il
                ) {
            LOG.info("Device object: " + device);
        }
        return Collections.unmodifiableList(il);
    }

    public Device loadByName(String name) {
        Assert.notNull(name);
        Query hql = this.session.createQuery("from Device where name=:name");
        hql.setString("name", name);
        @SuppressWarnings("unchecked")
        List<Device> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    public Device loadById(String deviceId) {
        Assert.notNull(deviceId);
        Query hql = this.session.createQuery("from Device where id=:id");
        hql.setString("id", deviceId);
        @SuppressWarnings("unchecked")
        List<Device> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    public Device persistDevice(Device device) {
        this.session.save(device);
        return device;
    }

    public int deleteByName(String name) {
        Device toBeDeleted = loadByName(name);
        if (toBeDeleted == null) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

}
