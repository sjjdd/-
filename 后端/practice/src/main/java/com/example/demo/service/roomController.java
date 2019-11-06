package com.example.demo.service;

import com.example.demo.controller.plantMapper;
import com.example.demo.controller.roomMapper;
import com.example.demo.dao.room;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/room"})
public class roomController {
    @Autowired
    roomMapper roomMapper;
    //设置植物的参数信息，保证同一用户的植物名称不能重复
    @RequestMapping(value = "/setRoom",method = RequestMethod.POST)
   public  List<room> setRoomInfo(@RequestParam("roomid") Integer roomid,@RequestParam("roomname") String roomname,@RequestParam("userid") Integer userid){
        List<room> roomList=roomMapper.getRByid(roomname,userid);
        List<room> roomList2=roomMapper.getTempAndHumi(userid, roomid);
        //如果该用户有重复命名房间返回插入失败,或者用户绑定同一房间号
        if(roomList.size()==0&&roomList2.size()==0){
            roomMapper.setRoomInfo(roomid,roomname,userid);
            room room=new room();
            room.setRoomname("success");
            List<room> roomList1=new ArrayList<>();
            roomList1.add(room);
            return roomList1;
        }else{
            room room=new room();
            room.setRoomname("fail");
            List<room> roomList1=new ArrayList<>();
            roomList1.add(room);
            return roomList1;
        }
    }
    //根据usrid去查找房间号
    @RequestMapping(value = "/findroomByuid",method = RequestMethod.GET)
    public List<String> findroom(@Param("userid") Integer userid){
        List<room> roomList=roomMapper.getRoomBRid(userid);
        List<String> roomname=new ArrayList<>();
        for(int i=0;i<roomList.size();i++){
            roomname.add(roomList.get(i).getRoomname());
        }
        return roomname;
    }
    @RequestMapping(value="/getHandT",method = RequestMethod.GET)
    public  Map<String,String>  getHumAndTem(@Param("userid") Integer userid,@Param("roomid") Integer roomid){
        List<room> roomList=roomMapper.getTempAndHumi(userid,roomid);
      Map<String,String> res=new HashMap<String, String>();
        if(roomList.size()>0) {
            res.put("Humidity" + roomid, roomList.get(0).getHumidity().toString());
            res.put("Temperature" + roomid, roomList.get(0).getTemperature().toString());
        }else{
            res.put("Humidity","no");
            res.put("Temperature","no");
        }
        return  res;
    }
    @RequestMapping(value = "/getTHByUR",method = RequestMethod.GET)
    @ResponseBody
    public List<room> getTHByUR(@RequestParam("userid") Integer userid,@RequestParam("roomname") String roomname){
        List<room> roomList=roomMapper.getRByid(roomname, userid);
        return roomList;
    }
}
