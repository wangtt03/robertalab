package de.fhg.iais.roberta.persistence.bo;

import javax.persistence.*;

enum DeviceType {
    EV3,
    MAKEBLOCK,
    VEX,
    UBITCH
}
/**
 * Created by tiantianwang on 2017/7/1.
 */
@Entity
@Table(name = "DEVICE")
public class Device implements WithSurrogateId{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private DeviceType type;

    @Column(name = "DEVICE_NAME")
    private String deviceName;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "FIRMWARE_NAME")
    private String firmwareName;

    @Column(name = "MENU_VERSION")
    private String menuVersion;

    @Column(name = "BATTERY")
    private String battery;

    @Column(name = "FIRMWARE_VERSION")
    private String firmwareVersion;

    @Column(name = "BRICK_NAME")
    private String brickName;

    @Column(name = "MACADDR")
    private String macAddr;


    protected Device(){

    }

    public Device(String deviceName, String token, String firmwareName, String menuVersion, String battery,
                  String firmwareVersion, String brickName, String macAddr){
        this.deviceName = deviceName;
        this.token = token;
        this.firmwareName = firmwareName;
        this.menuVersion = menuVersion;
        this.battery = battery;
        this.firmwareVersion = firmwareVersion;
        this.brickName = brickName;
        this.macAddr = macAddr;

        if(brickName.equals("ev3dev")) {
            this.type = DeviceType.EV3;
        }
        else{
            this.type = DeviceType.EV3;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirmwareName(){
        return this.firmwareName;
    }

    public void setFirmwareName(String firmwareName){
        this.firmwareName = firmwareName;
    }

    public String getMenuVersion(){
        return this.menuVersion;
    }

    public void setMenuVersion(String menuVersion){
        this.menuVersion = menuVersion;
    }

    public String getBattery(){
        return this.battery;
    }

    public void setBattery(String battery){
        this.battery = battery;
    }

    public String getFirmwareVersion(){
        return this.firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion){
        this.firmwareVersion = firmwareVersion;
    }

    public String getBrickName(){
        return this.brickName;
    }

    public void setBrickName(String brickName){
        this.brickName = brickName;
    }

    public String getMacAddr(){
        return this.macAddr;
    }

    public void setMacAddr(String macAddr){
        this.macAddr = macAddr;
    }
}
