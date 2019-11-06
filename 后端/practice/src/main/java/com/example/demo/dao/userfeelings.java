package com.example.demo.dao;

import javax.persistence.*;

@Table(name="userfeelings")
@Entity
public class userfeelings {
    @GeneratedValue(strategy= GenerationType.IDENTITY)//自增属性
    @Id//主键
    private Integer feelingid;
    private Integer userid;
    private String content;
    private String plantpics;
    private String username;
    private Integer picnum;
    public Integer getFeelingid() {
        return feelingid;
    }

    public void setFeelingid(Integer feelingid) {
        this.feelingid = feelingid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlantpics() {
        return plantpics;
    }

    public void setPlantpics(String plantpics) {
        this.plantpics = plantpics;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPicnum() {
        return picnum;
    }

    public void setPicnum(Integer picnum) {
        this.picnum = picnum;
    }
}
