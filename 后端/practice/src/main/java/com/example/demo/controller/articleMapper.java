package com.example.demo.controller;

import com.example.demo.dao.article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Mapper
public interface articleMapper {
    //获得所有有关种植的知识
    @Select("select * from article")
    public List<article> getAllArticle();
    //根据文章id去找文章
    @Select("select * from article where articleid=#{articleid}")
    public  List<article> getArticleById(@Param("articleid") Integer articleid);
}
