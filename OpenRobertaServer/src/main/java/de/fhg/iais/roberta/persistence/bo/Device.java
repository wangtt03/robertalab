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
public class Device {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "CODE")
    private String code;


}
