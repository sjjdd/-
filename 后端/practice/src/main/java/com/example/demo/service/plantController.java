package com.example.demo.service;

import com.example.demo.controller.plantMapper;
import com.example.demo.controller.plantsoilMapper;
import com.example.demo.controller.roomMapper;
import com.example.demo.dao.notice;
import com.example.demo.dao.plant;
import com.example.demo.dao.plantsoil;
import com.example.demo.define.ComparatorImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@RestController
@RequestMapping({"/plant"})
public class plantController {
    @Autowired
    plantMapper plantMapper;
    @Autowired
    plantsoilMapper plantsoilMapper;
    @Autowired
    roomMapper roommapper;
    //设置植物的参数信息，保证同一用户的植物名称不能重复
    @RequestMapping(value = "/setPlant",method = RequestMethod.POST)
    public Map<String,String> setPlantParam(@RequestParam(value="plantname") String plantname,@RequestParam(value="plantpic") String plantpic,
                                            @RequestParam(value="waterparam") Double waterparam,@RequestParam(value="roomid") Integer roomid,
                                            @RequestParam(value="temperature") Double temperature,@RequestParam(value="userid") Integer userid,
                                            @RequestParam(value = "plantdes") String plantdes,@RequestParam(value="deviceid") String deviceid){

        List<plant> plantList=plantMapper.searchplantByname(plantname,userid);
        List<plant> plantList1=plantMapper.selectpbyud(userid,Integer.parseInt(deviceid));
        //如果该用户有该名字的植物那么则说明，此时不可以进行植物信息的创建，因为该用户下这植物可能重名了
        Map<String,String> a=new HashMap<String, String>();
        if(plantpic==""){
            a.put("setPlantParam","null");
            System.out.println("setPlantParam:null");
            return a;
        }
        if(plantList.size()>0||plantList1.size()>0){
            a.put("setPlantParam","no");
            System.out.println("setPlantParam:no");
            return a;
        }else{
            plantMapper.setPlantPara(plantname,plantpic,waterparam,roomid,temperature,userid,plantdes,deviceid);
            a.put("setPlantParam","yes");
            System.out.println("setPlantParam:yes");
            return a;
        }
    }
    //根据用户id去获得植物信息
    @RequestMapping(value="/getPlantByuid",method = RequestMethod.GET)
    public List<plant> searchPlantByuid(@RequestParam(value="userid") Integer userid){
        List<plant> plantList=plantMapper.searchplantByuserid(userid);
        System.out.println(plantList);
        return plantList;
    }
    @RequestMapping(value="/getPlantnameByuid",method = RequestMethod.GET)
    public List<String> searchPlantnameByuid(@RequestParam(value = "userid") Integer userid){
        List<String> plantname=plantMapper.searchplantnameByid(userid);
        System.out.println(plantname);
        return plantname;
    }
    @RequestMapping(value="/getplantInfoById",method = RequestMethod.GET)
    public List<plant> getplantInfoByid(@RequestParam(value="plantid") Integer plantid){
        List<plant> plantList=plantMapper.getplantinfoByid(plantid);
        return plantList;
    }
    @RequestMapping(value="/setplantWP",method = RequestMethod.GET)
    public String setplantWp(@RequestParam(value="plantid") Integer plantid,@RequestParam(value="waterparam") Double waterparam){
        plantMapper.updatePlantwp(plantid,waterparam);
        return "更新浇水阈值成功";
    }
//    @RequestMapping(value="/setplantGZ",method = RequestMethod.GET)
//    public String setplantGZ(@RequestParam(value="plantid") Integer plantid,@RequestParam(value="lightparam") Double lightparam){
//        plantMapper.updatePlantgz(plantid,lightparam);
//        return "更新光照阈值成功";
//    }
    @RequestMapping(value="/setplantWD",method = RequestMethod.GET)
    public String setplantWD(@RequestParam(value="plantid") Integer plantid,@RequestParam(value="temperature") Double temperature){
        plantMapper.updatePlantwd(plantid,temperature);
        return "更新温度阈值成功";
    }
    @RequestMapping(value="/setplantMS",method = RequestMethod.GET)
    public String setplantWp(@RequestParam(value="plantid") Integer plantid,@RequestParam(value="plantdes") String plantdes){
        plantMapper.updatePlantms(plantid,plantdes);
        return "更新职务描述成功";
    }
    //根据植物id去删除植物信息
    @RequestMapping(value="/deleteplant",method = RequestMethod.POST)
    public Map<String,String> deletePlant(@RequestParam(value="plantid") Integer plantid){
        List<plant> plants=plantMapper.getplantinfoByid(plantid);
        Map<String,String> a=new HashMap<>();
        if(plants.size()<=0) {
            a.put("delete","no");
            return a;
        }else{
            plantMapper.deletePlant(plantid);
            a.put("delete","yes");
            return a;
        }

    }
    //根据设备id去查找植物土壤湿度的信息
    @RequestMapping(value="/listsoilhum",method = RequestMethod.GET)
    public List<plantsoil> listsoilhum(@Param("deviceid") Integer deviceid){
        List<plantsoil> plantsoils=plantsoilMapper.listplantsoil(deviceid);
        Comparator comparator=new ComparatorImpl();
        Collections.sort(plantsoils,comparator);
        return plantsoils;
    }
    //根据植物名和用户id去得到植物的设备id
    @RequestMapping(value="/getdeviceid",method = RequestMethod.GET)
    public List<plant> getdeviceid(@Param("plantname") String plantname,@Param("userid") Integer userid){
        List<plant> plants=plantMapper.searchplantByname(plantname,userid);
        return plants;
    }
    //去查找plantsoil表中不同的deviceid的值
    @RequestMapping(value ="/getDdid",method = RequestMethod.GET)
    public  Map<String,Integer> getDdid(){
        return plantsoilMapper.sekectddvid();
    }
    //通过userid来找到对应所有植物的温度阈值，然后再分别与该植物对应的房间号的房间的实时温度比较
    //若是房间温度低于植物温度阈值返回植物名字+1，若是高于植物温度阈值返回植物名字+2,若是在正常范围内则为返回植物名字+0
    @RequestMapping(value="/noticeTem",method = RequestMethod.GET)
    public List<notice> noticeTem(@Param("userid") Integer userid){
        List<plant> plants=plantMapper.searchplantByuserid(userid);
        List<notice> notices=new ArrayList<>();
        System.out.println("筛选出的植物数目"+plants.size());
        if(plants.size()>0){
            for(int i=0;i<plants.size();i++){
                Double planttemparam=plants.get(i).getTemperature();
                String plantname=plants.get(i).getPlantname();
                Integer roomid=plants.get(i).getRoomid();
                Double roomtemperature=Double.parseDouble(roommapper.getTempAndHumi(userid,roomid).get(0).getTemperature());
                if(roomtemperature<planttemparam-3)
                {
                  notice notice=new notice();
                  notice.setPlantname(plantname);
                  notice.setStatus(1);
                  notices.add(notice);
                }else if(roomtemperature>planttemparam+3){
                    notice notice=new notice();
                    notice.setPlantname(plantname);
                    notice.setStatus(2);
                    notices.add(notice);
                }else{
                    notice notice=new notice();
                    notice.setPlantname(plantname);
                    notice.setStatus(0);
                    notices.add(notice);
                }
            }
            return notices;
        }else{
            notice notice=new notice();
            notice.setPlantname("no");
            notice.setStatus(-1);
            notices.add(notice);
            return notices;
        }
    }
}
