package com.example.demo.dao;

import javax.persistence.*;

@Table(name="room")
@Entity
public class room {
    @GeneratedValue(strategy= GenerationType.IDENTITY)//自增属性
    @Id//主键
    private Integer Rid;
    private Integer roomid;
    private String roomname;
    private Integer userid;
    private String temperature;
    private String humidity;
    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Integer getRid() {
        return Rid;
    }

    public void setRid(Integer rid) {
        Rid = rid;
    }
}
