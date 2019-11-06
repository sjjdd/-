package com.example.demo.service;

import com.example.demo.define.MqttPushClient;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping({"/mqtt"})
public class MqttController {
    @Autowired
    private MqttPushClient mqttPushClient;

    private final static Executor executor = Executors.newCachedThreadPool();
    @RequestMapping("/hello")
    @ResponseBody
    public String sendHello(){
        String kdTopic = "topic1";
        mqttPushClient.publish(0, false, kdTopic, "15345715326");
        mqttPushClient.subscribe(kdTopic);
        return "123";
    }
    @RequestMapping("/Humidity")
    @ResponseBody
    public Map<String,String> getHumidity(@RequestParam(value = "roomid") String roomid){
        String Topic="Humidity"+roomid;
        mqttPushClient.subscribe(Topic);
        Map<String,String> a=new HashMap<String, String>();
        a.put("Humidity","");
        return a;
    }
    @RequestMapping(value = "/SoilHum",method = RequestMethod.GET)
    @ResponseBody
    public String getSoilHum(@Param("deviceid") Integer deviceid){
        String Topic="SoilHum"+deviceid;
        mqttPushClient.subscribe(Topic);
        return "订阅设备"+deviceid+"的土壤湿度值";
    }
    @RequestMapping("/Temperature")
    @ResponseBody
    public String getTemperature(@RequestParam(value = "roomid") String roomid){
        String Topic="Temperature"+roomid;
        mqttPushClient.subscribe(Topic);

        return "订阅设备"+roomid+"的温度";
    }
    @RequestMapping(value="/soil",method = RequestMethod.GET)
    @ResponseBody
    public String setSoilOC(@RequestParam(value="soil") Integer soil,@RequestParam(value = "deviceid") String deviceid){
        //发送打开水泵指令
        if(soil==1){
            mqttPushClient.publish("soil"+deviceid,"1");
            return "打开水泵";
        }else{
            mqttPushClient.publish("soil"+deviceid,"0");
            return "关闭水泵";
        }
    }
    //发布lighntparam的话题,目前是单击版本，没有与植物关联起来
    @RequestMapping("/lightParam")
    @ResponseBody
    public String publishLight(@RequestParam(value = "lightparam") String lightparam,@RequestParam(value = "deviceid") String deviceid){
        mqttPushClient.publish("lightparam"+deviceid,lightparam);
        return "发布了设备"+deviceid+"湿度是："+lightparam;
    }
    //发布土壤温湿度的值
    @RequestMapping("/publishsoilParam")
    @ResponseBody
    public String publishsoilParam(@RequestParam(value="deviceid")String deviceid,@RequestParam(value = "waterparam") Double setwaterparam){
        mqttPushClient.publish("setwaterparam"+deviceid,String.valueOf(setwaterparam));
       return "发布了土壤湿度的阈值";
    }
    //实现一个一同发布所有的话题的接口
    @RequestMapping(value="/publishAll",method=RequestMethod.POST)
    @ResponseBody
    public String  publishAll(@RequestParam(value="deviceid")String deviceid,@RequestParam(value = "waterparam") Double setwaterparam,
    @RequestParam(value="temperature") Double settemperatureparam){
        mqttPushClient.publish("setwaterparam"+deviceid,String.valueOf(setwaterparam));
        mqttPushClient.publish("settemperatureparam"+deviceid,String.valueOf(settemperatureparam));
        return "发布关于植物设备"+deviceid+"的相关阈值信息";
    }
//
//    @RequestMapping(value = "/updateHumidity")
//    @ResponseBody
//    public String updateHumidity(@RequestParam(value="humidity") String humidity,@RequestParam(value = "deviceid") String deviceid){
//        paramsMapper.setHumidity(humidity,Integer.parseInt(deviceid));
//        return "更新湿度";
//    }

}
