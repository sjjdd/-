package com.example.demo.dao;

import javax.persistence.*;

@Table(name="userifo")
@Entity
public class userifo {
    @GeneratedValue(strategy=GenerationType.IDENTITY)//自增属性
    @Id//主键
    private Integer userid;
    private String username,userpwd;



    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }
}
