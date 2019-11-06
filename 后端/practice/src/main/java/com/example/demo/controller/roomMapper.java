package com.example.demo.controller;

import com.example.demo.dao.plantsoil;
import com.example.demo.dao.room;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface roomMapper {
    @Insert("insert room(roomid,roomname,userid) values(#{roomid},#{roomname},#{userid})")
    void setRoomInfo(@Param("roomid")Integer roomid,@Param("roomname")String roomname,@Param("userid")Integer userid);
    //根据用户id以及房间名称去查找房间信息
    @Select("select * from room where roomname=#{roomname}and userid=#{userid}")
    List<room> getRByid(@Param("roomname") String roomname,@Param("userid") Integer userid);
    //根据用户id去查找房间消息
    @Select("select * from room where userid = #{userid}")
    List<room> getRoomBRid(@Param("userid") Integer userid);
    //根据用户id和房间号去请求温度和湿度
    @Select("select * from room where roomid=#{roomid} and userid=#{userid}")
    List<room> getTempAndHumi(@Param("userid") Integer userid,@Param("roomid") Integer roomid);
    //根据用户id和房间id去数据库更新温度
    @Update("update room set temperature = #{temperature} where roomid=#{roomid}")
    void setTemperature(@Param("temperature") String temperature,@Param("roomid") Integer roomid);
    //根据用户id和房间id去更新湿度
    @Update("update room set humidity=#{humidity} where roomid=#{roomid}")
    void setHumidity(@Param("humidity") String humidity,@Param("roomid") Integer roomid);
    //根据设备id去查找设备的所有土壤湿度值的记录
    @Select("select * from plantsoil where deviceid=#{deviceid}")
    List<plantsoil> getplantsoilByid(@Param("deviceid") Integer deviceid);
}
