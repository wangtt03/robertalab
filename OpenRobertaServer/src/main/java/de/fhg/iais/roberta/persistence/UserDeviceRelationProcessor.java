package de.fhg.iais.roberta.persistence.util;

import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.bo.UserDeviceRelation;
import de.fhg.iais.roberta.persistence.dao.UserDeviceRelationDao;
import de.fhg.iais.roberta.util.Key;

/**
 * Created by t-zhhong on 2017/7/10.
 */
public class UserDeviceRelationProcessor extends AbstractProcessor {
    public UserDeviceRelationProcessor(DbSession dbSession, HttpSessionState httpSessionState){
        super(dbSession, httpSessionState);
    }

    public String getDeviceNameByUserName(String accountName){
        UserDeviceRelationDao userDeviceRelationDao = new UserDeviceRelationDao(this.dbSession);
        String deviceName = userDeviceRelationDao.getDeviceNameByAccountName(accountName);
        if(deviceName == null){
            System.out.println("Error GET_DEVICE_NAME_BY_USER_NAME, UserName: " + accountName);
            return null;
        }
        else{
            System.out.println("Success GET_DEVICE_NAME_BY_USER_NAME, UserName: " + accountName);
            return deviceName;
        }
    }
}