package de.fhg.iais.roberta.persistence.bo;

/**
 * Created by t-zhhong on 2017/7/10.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_DEVICE_RELATION")
public class UserDeviceRelation implements WithSurrogateId{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "DEVICE_NAME")
    private String deviceName;

    protected UserDeviceRelation(){

    }

    public UserDeviceRelation(String accountName, String deviceName){
        this.accountName = accountName;
        this.deviceName = deviceName;
    }

    @Override
    public int getId(){
        return id;
    }

    public String getAccountName(){
        return accountName;
    }

    public String getDeviceName(){
        return deviceName;
    }

    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }

    @Override
    public String toString(){
        return "UserDeviceRelation[ User: " + this.accountName + ", Device: " + this.deviceName + " ]";
    }
}