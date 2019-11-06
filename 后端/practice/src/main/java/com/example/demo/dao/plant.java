package com.example.demo.dao;

import javax.persistence.*;

@Table(name="plant")
@Entity
public class plant {
    @GeneratedValue(strategy=GenerationType.IDENTITY)//自增属性
    @Id//主键
    private Integer plantid;
    private String plantpic;
    private String plantname;
    private Double waterparam;
   private  Double temperature;
    private Integer userid;
    private String plantdes;
    private String deviceid;
    private Integer roomid;
    public String getPlantdes() {
        return plantdes;
    }

    public void setPlantdes(String plantdes) {
        this.plantdes = plantdes;
    }

    public Integer getPlantid() {
        return plantid;
    }

    public void setPlantid(Integer plantid) {
        this.plantid = plantid;
    }

    public String getPlantpic() {
        return plantpic;
    }

    public void setPlantpic(String plantpic) {
        this.plantpic = plantpic;
    }

    public String getPlantname() {
        return plantname;
    }

    public void setPlantname(String plantname) {
        this.plantname = plantname;
    }

    public Double getWaterparam() {
        return waterparam;
    }

    public void setWaterparam(Double waterparam) {
        this.waterparam = waterparam;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
}
