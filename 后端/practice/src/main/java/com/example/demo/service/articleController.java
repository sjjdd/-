package com.example.demo.service;

import com.example.demo.controller.articleMapper;
import com.example.demo.dao.article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/article"})
public class articleController {
    @Autowired
    articleMapper articleMapper;
    @RequestMapping(value = "/getAllArticle",method = RequestMethod.GET)
    public List<article> getAllArticleInfo(){
        List<article> articleList=articleMapper.getAllArticle();
        return articleList;
    }
    @RequestMapping(value = "/getArticleByid",method = RequestMethod.GET)
    public List<article> getAllArticleByid(@RequestParam(value="articleid") Integer articleid){
        List<article> articles=articleMapper.getArticleById(articleid);
        return articles;
    }
}
