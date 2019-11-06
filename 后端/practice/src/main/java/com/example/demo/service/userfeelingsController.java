package com.example.demo.service;

import com.example.demo.controller.userMapper;
import com.example.demo.controller.userfeelingsMapper;
import com.example.demo.dao.userfeelings;
import com.example.demo.dao.userifo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/feelings"})
public class userfeelingsController {
    @Autowired
    userfeelingsMapper userfeelingsmapper;
    @Autowired
    userMapper usermapper;
    @RequestMapping(value="/publishfeelings",method = RequestMethod.POST)
    public Map<String,String> publishfeel(@RequestParam(value="userid") Integer userid, @RequestParam(value="content") String content, @RequestParam(value="plantpics") String plantpics,@RequestParam(value="picnum") Integer picnum)
    {
        if(userid!=null&&content!=null&&plantpics!=null) {
            //根据用户id马上去查找用户名字存入感想表中
            userifo userifo=usermapper.finduserbyuid(userid);
            String username=userifo.getUsername();
            userfeelingsmapper.publishfeelings(userid, content, plantpics,username,picnum);
            Map<String,String> a=new HashMap<>();
            a.put("message","ok");
            return a;
        }else{
            Map<String,String> a=new HashMap<>();
            a.put("message","no");
            return a;
        }
    }
    @RequestMapping(value = "/selectAllFeel",method = RequestMethod.GET)
    public List<userfeelings> getAllFeel(){
        List<userfeelings> userfeelings=userfeelingsmapper.selectAllFeeling();
        return userfeelings;

    }
}
