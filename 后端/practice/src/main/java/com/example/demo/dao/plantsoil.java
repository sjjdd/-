package com.example.demo.dao;

import javax.persistence.*;

@Table(name="plantsoil")
@Entity
public class plantsoil {
    @GeneratedValue(strategy=GenerationType.IDENTITY)//自增属性
    @Id//主键
    private Integer soilid;
    private String soil;
    private Integer deviceid;
    private  String  time;
    public Integer getSoilid() {
        return soilid;
    }

    public void setSoilid(Integer soilid) {
        this.soilid = soilid;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
