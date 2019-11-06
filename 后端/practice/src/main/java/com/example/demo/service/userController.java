package com.example.demo.service;

import com.example.demo.controller.userMapper;
import com.example.demo.dao.userifo;
import com.sun.istack.internal.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/user"})
public class userController  {
 private Logger logger= Logger.getLogger(userController.class);
 @org.springframework.beans.factory.annotation.Value("${uploadDir}")
 private String uploadDir;
 @org.springframework.beans.factory.annotation.Value("${uploadFeelings}")
 private String uploadFeelings;
 @Autowired
 userMapper userMapper;
 //获得所有用户的信息
 @RequestMapping(value="/getAlluserInf",method = RequestMethod.GET)
    public List<userifo> getAlluserIfo(){
     List<userifo> AlluserIfo=userMapper.findUser();
     return AlluserIfo;
 }
 //登录接口
 @RequestMapping(value="/login",method = RequestMethod.POST)
 public Map<String,String> Login(@RequestParam(value="username") String username,@RequestParam(value="userpwd") String userpwd){
  List<userifo> userifoList=userMapper.Login(username,userpwd);
  Map<String,String> a=new HashMap<String, String>();
  if(userifoList.size()>0)
  {
   a.put("message","ok");
   a.put("username",username);
   System.out.println(a);
  }else{
   a.put("message","no");
   System.out.println(a);
  }
  return  a;
 }
 //注册用户的接口
 @RequestMapping(value="/register",method = RequestMethod.POST)
 public Map<String,String> register(@RequestParam(value="username")String username,@RequestParam(value="userpwd")String userpwd){
  List<userifo> userifos=userMapper.findUserByname(username);
  Map<String,String> a=new HashMap<String, String>();
  if(userifos.size()>0||userpwd.equals("")|username.equals(""))
  {
   a.put("Register","no");
   a.put("username",username);
  }else{
   userMapper.Register(username,userpwd);
   a.put("Register","ok");
  }
  return a;
 }
 //根据用户名来获得用户信息的接口，主要是为了得到userid
 @RequestMapping(value="/getUidByName",method = RequestMethod.GET)
 public List<userifo> getuserifoByuName(@RequestParam(value = "username") String username){
  List<userifo> userifoList=userMapper.findUserByname(username);
  System.out.println("通过用户名找用户信息"+userifoList.get(0));
  return userifoList;
 }
 //根据用户id去找用户信息
 @RequestMapping(value="/getuserifoByuid")
 public userifo getusrifoByuid(@RequestParam(value="userid") Integer useid){
  userifo userifo=userMapper.finduserbyuid(useid);
  return userifo;
 }
 //上传图片的接口
 @RequestMapping(value="/upload")
 public String upload(HttpServletRequest request,@RequestParam(value="file",required = false) MultipartFile file) throws IOException{
  Map<String,String> a=new HashMap<String, String>();
  String sjjfilename=null;
  System.out.println("执行upload");
  request.setCharacterEncoding("UTF-8");
  String user=request.getParameter("user");
  logger.info("user:"+user);
  if(!file.isEmpty()){
   logger.info("成功获取照片");
   String fileName=file.getOriginalFilename();
   String path=null;
   String type=null;
   type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()):null;
   logger.info("图片初始名称为："+fileName+"类型为："+type);
   if(type!=null){
    if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {
     // 项目在容器中实际发布运行的根路径
     String realPath = uploadDir;
     System.out.println("真实路径"+realPath);
     // 自定义的文件名称
     String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
     sjjfilename=trueFileName;
     // 设置存放图片文件的路径
     path = realPath + trueFileName;
     logger.info("存放图片文件的路径:" + path);
     file.transferTo(new File(path));
     logger.info("文件成功上传到指定目录下");
    }else {
     logger.info("不是我们想要的文件类型,请按要求重新上传");
     sjjfilename="error";
     return sjjfilename;
    }
   }else {
    sjjfilename="error";
    return sjjfilename;
   }
  }else {
   sjjfilename="error";
   return sjjfilename;
  }
  return sjjfilename;
 }
 //上传养花心得图片的接口
 @RequestMapping(value="/uploadfeelings")
 public String uploadfeelings(HttpServletRequest request,@RequestParam(value="file",required = false) MultipartFile file) throws IOException{
  Map<String,String> a=new HashMap<String, String>();
  String sjjfilename=null;
  System.out.println("执行uploadfeelings");
  request.setCharacterEncoding("UTF-8");
  String user=request.getParameter("user");
  logger.info("user:"+user);
  if(!file.isEmpty()){
   logger.info("成功获取照片");
   String fileName=file.getOriginalFilename();
   String path=null;
   String type=null;
   type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()):null;
   logger.info("图片初始名称为："+fileName+"类型为："+type);
   if(type!=null){
    if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {
     // 项目在容器中实际发布运行的根路径
     String realPath = uploadFeelings;
     System.out.println("真实路径"+realPath);
     // 自定义的文件名称
     String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
     sjjfilename=trueFileName;
     // 设置存放图片文件的路径
     path = realPath + trueFileName;
     logger.info("存放图片文件的路径:" + path);
     file.transferTo(new File(path));
     logger.info("文件成功上传到指定目录下");
    }else {
     logger.info("不是我们想要的文件类型,请按要求重新上传");
     sjjfilename="error";
     return sjjfilename;
    }
   }else {
    sjjfilename="error";
    return sjjfilename;
   }
  }else {
   sjjfilename="error";
   return sjjfilename;
  }
  return sjjfilename;
 }
}
