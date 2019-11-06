package com.example.demo.controller;

import com.example.demo.dao.userifo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface userMapper {
@Select("select * from userifo")
List<userifo> findUser();
//通过用户名和密码去数据库中查找是否有记录
@Select("select * from userifo where username=#{username} and userpwd=#{userpwd}")
List<userifo>  Login(@Param("username") String username,@Param("userpwd") String userpwd);
@Insert("insert userifo(username,userpwd) values(#{username},#{userpwd})")
void Register(@Param("username") String username,@Param("userpwd") String userpwd);
//通过用户名去查找数据是否在数据库当中，用以判断是否重名
@Select("select * from userifo where username=#{username}")
List<userifo> findUserByname(@Param("username")String username);
//根据用户id去查找用户名
@Select("select * from userifo where userid = #{userid}")
userifo finduserbyuid(@Param("userid") Integer userid);
}
