package com.example.demo.controller;

import com.example.demo.dao.plantsoil;
import org.apache.ibatis.annotations.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

@Mapper
public interface plantsoilMapper {
    //插入湿度值以及设备id,当前时间
    @Insert("insert plantsoil(soilid,soil,deviceid,time) values(#{soilid},#{soil},#{deviceid},#{time})")
    void setplantsoil(@Param("soilid") Integer soilid,@Param("soil") String soil, @Param("deviceid") Integer deviceid, @Param("time") String time);
    //根据设备id去查找设备的所有土壤湿度值的记录
    @Select("select * from plantsoil where deviceid=#{deviceid}")
    List<plantsoil> getplantsoilByid(@Param("deviceid") Integer deviceid);
    //根据soilid更新数据库
    @Update("update plantsoil set soil=#{soil},deviceid=#{deviceid},time=#{time} where soilid =#{soilid}")
    void updateSoil(@Param("soil") String soil,@Param("deviceid") Integer deviceid,@Param("time") String time,@Param("soilid") Integer soilid);
    //根据设备id取出土壤湿度的值
    @Select("select * from plantsoil where deviceid=#{deviceid}")
    List<plantsoil> listplantsoil(@Param("deviceid") Integer deviceid);
    //统计表中不同的deviceid的个数
    @Select("select count(distinct deviceid) from plantsoil")
    int selectdeviceidcount();
    //得到deviceid的不同的值
    @Select("select distinct deviceid from plantsoil")
    Map<String,Integer> sekectddvid();
}