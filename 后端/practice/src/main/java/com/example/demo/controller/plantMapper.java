package com.example.demo.controller;

import com.example.demo.dao.plant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface plantMapper {
    //插入该植物的相关参数
    @Insert("insert plant(plantname,plantpic,waterparam,roomid,temperature,userid,plantdes,deviceid) " +
            "values(#{plantname},#{plantpic},#{waterparam},#{roomid},#{temperature},#{userid},#{plantdes},#{deviceid})")
    void setPlantPara(@Param("plantname") String plantname,@Param("plantpic") String plantpic,@Param("waterparam") Double waterparam,
                             @Param("roomid") Integer roomid,@Param("temperature") Double temperature,@Param("userid") Integer userid,
                      @Param("plantdes") String plantdes,@Param("deviceid") String deviceid);
    //根据植物名以及用户id去植物表中查找记录
    @Select("select * from plant where plantname=#{plantname} and userid = #{userid}")
    List<plant> searchplantByname(@Param("plantname") String plantnaem,@Param("userid") Integer userid);
    //根据用户id来显示所有与之相关的植物信息
    @Select("select * from plant where userid=#{userid}")
    List<plant> searchplantByuserid(@Param("userid") Integer userid);
    //根据用户id来获得与之相关的植物名字
    @Select("select plantname from plant where userid=#{userid}")
    List<String> searchplantnameByid(@Param("userid") Integer userid);
    //根据植物id来获得植物的相关信息
    @Select("select * from plant where plantid=#{plantid}")
    List<plant> getplantinfoByid(@Param("plantid") Integer plantid);
    //根据植物id更新数据库里的植物相关参数
    @Update("update plant set waterparam=#{waterparam} where plantid = #{plantid}")
    void updatePlantwp(@Param("plantid") Integer plantid,@Param("waterparam") Double waterparam);
//    @Update("update plant set lightparam=#{lightparam} where plantid = #{plantid}")
//    void updatePlantgz(@Param("plantid") Integer plantid,@Param("lightparam") Double lightparam);
    @Update("update plant set temperature=#{temperature} where plantid = #{plantid}")
    void updatePlantwd(@Param("plantid") Integer plantid,@Param("temperature") Double temperature);
    @Update("update plant set plantdes=#{plantdes} where plantid = #{plantid}")
    void updatePlantms(@Param("plantid") Integer plantid,@Param("plantdes") String plantdes);
    //根据植物id删除植物相关信息
    @Delete("delete from plant where plantid=#{plantid}")
    void deletePlant(@Param("plantid") Integer plantid);
    //根据用户id以及设备id来选信息
    @Select("select * from plant where userid=#{userid} and deviceid =#{deviceid}")
    List<plant> selectpbyud(@Param("userid") Integer userid,@Param("deviceid") Integer deviceid);
}
