package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.persistence.bo.Device;
import de.fhg.iais.roberta.persistence.bo.Lesson;
import de.fhg.iais.roberta.persistence.dao.DeviceDao;
import de.fhg.iais.roberta.persistence.dao.LessonDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeviceProcessor extends AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceProcessor.class);

    public DeviceProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public JSONObject getDeviceById(String deviceId){
        DeviceDao deviceDao = new DeviceDao(this.dbSession);
        Device device = deviceDao.loadById(deviceId);
        if (device == null) {
            return null;
        }

        JSONObject object = new JSONObject();
        try {
            object.put("id", device.getId());
            object.put("name", device.getName());
            object.put("code", device.getCode());
        } catch (JSONException e) {
            LOG.error("error when get device by id: " + e);
        }
        return object;
    }

    public JSONObject getDeviceByName(String deviceName){
        DeviceDao deviceDao = new DeviceDao(this.dbSession);
        Device device = deviceDao.loadByName(deviceName);
        if (device == null) {
            return null;
        }

        JSONObject object = new JSONObject();
        try {
            object.put("id", device.getId());
            object.put("name", device.getName());
            object.put("code", device.getCode());
        } catch (JSONException e) {
            LOG.error("error when get device by id: " + e);
        }
        return object;
    }
}
