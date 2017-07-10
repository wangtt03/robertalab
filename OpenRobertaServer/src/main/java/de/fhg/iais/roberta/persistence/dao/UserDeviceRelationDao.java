package de.fhg.iais.roberta.persistence.dao;

import de.fhg.iais.roberta.persistence.bo.UserDeviceRelation;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by t-zhhong on 2017/7/10.
 */
public class UserDeviceRelationDao extends AbstractDao<UserDeviceRelation> {
    public UserDeviceRelationDao(DbSession session){
        super(UserDeviceRelation.class, session);
    }

    public UserDeviceRelation persistUserDeviceRelation(String accountName, String deviceName){
        Query hql = this.session.createQuery("from UserDeviceRelation where account_name=:account_name");
        hql.setString("account_name", accountName);
        @SuppressWarnings("unchecked")
        UserDeviceRelation relation = new UserDeviceRelation(accountName, deviceName);
        List<UserDeviceRelation> il = hql.list();
        for (int i = 0; i < il.size(); i++){
            this.session.delete(il.get(i));
        }
        this.session.save(relation);
        this.session.commit();
        return relation;
    }

    public boolean deleteUserDeviceRelationByAccountName(String accountName){
        Query hql = this.session.createQuery("from UserDeviceRelation where account_name=:account_name");
        hql.setString("account_name", accountName);
        @SuppressWarnings("unchecked")
        List<UserDeviceRelation> il = hql.list();
        for (int i = 0; i < il.size(); i++){
            this.session.delete(il.get(i));
        }
        this.session.commit();
        return true;
    }

    public String getDeviceNameByAccountName(String accountName){
        Query hql = this.session.createQuery("from UserDeviceRelation where account_name=:account_name");
        hql.setString("account_name", accountName);
        @SuppressWarnings("unchecked")
        List<UserDeviceRelation> il = hql.list();
        if(il.size() <= 0){
            return null;
        }
        else{
            return il.get(0).getDeviceName();
        }
    }
}
