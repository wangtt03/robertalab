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

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    protected Device(){

    }

    public Device(String type, String name, String code){
        if(type.equals("ev3dev")) {
            this.type = DeviceType.EV3;
        }
        else{
            this.type = DeviceType.EV3;
        }
        this.name = name;
        this.code = code;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
