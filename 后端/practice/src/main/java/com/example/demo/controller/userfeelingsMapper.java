package com.example.demo.controller;

import com.example.demo.dao.userfeelings;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface userfeelingsMapper {
    //将用户发表的心得以及图片内容存入数据库
    @Insert("insert userfeelings(userid,content,plantpics,username,picnum) values(#{userid},#{content},#{plantpics},#{username},#{picnum})")
    void publishfeelings(@Param("userid") Integer userid,@Param("content") String content,@Param("plantpics") String plantpics,@Param("username") String username,@Param("picnum")Integer picnum);
    //筛选出所有的用户发布的信息
    @Select("select * from userfeelings")
    List<userfeelings> selectAllFeeling();
}
