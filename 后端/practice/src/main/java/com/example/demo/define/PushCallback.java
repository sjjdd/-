package com.example.demo.define;

import com.example.demo.dao.plantsoil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
@Configuration
@Component

public class PushCallback implements MqttCallback {
    MapperUtil mapperUtil=new MapperUtil();
    MqttPushClient mqttPushClient=new MqttPushClient();
    MqttConfiguration mqttConfiguration;
    int i=1,j=6;
    private final static Executor executor = Executors.newCachedThreadPool();
//    private final static Executor executor = Executors.newCachedThreadPool();


    @Override

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，可以做重连"+cause);
        mqttPushClient.connect("tcp://127.0.0.1:61613","mqttjs_e8022a4d0b","sjj","123456",1000,2000);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
        if(topic.contains("Humidity")){//因为返回的字符串是类似于Humidity190418101，后面的数字表示房间号所以需要解析出来
            System.out.println("订阅的是湿度主题");
            PushCallback pushCallback=new PushCallback();
            Object[] result=pushCallback.deleteSubString(topic,"Humidity");
            mapperUtil.setHumidity(new String(message.getPayload()),Integer.parseInt(result[0].toString()));
            }else if(topic.contains("Temperature")){
            System.out.println("订阅的是温度主题");
            PushCallback pushCallback=new PushCallback();
            Object[] result=pushCallback.deleteSubString(topic,"Temperature");
            mapperUtil.setTemperature(new String(message.getPayload()),Integer.parseInt(result[0].toString()));
        }else if(topic.contains("SoilHum")) {//实时土壤湿度
//              String deviceid=topic.substring(topic.length()-2);
            //设置只能存五组数据
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss ");
            String sdfstr = sdf.format(d);
            System.out.println("格式化输出：" + sdfstr);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (topic.contains("190418201")) {
                if (i > 5) i = 1;
                PushCallback pushCallback = new PushCallback();
                Object[] result = pushCallback.deleteSubString(topic, "SoilHum");
                //先调用根据deviceid取出所有土壤湿度值的接口函数
                System.out.println("deviceid" + result[0].toString());
                List<plantsoil> plantsoils = mapperUtil.getplantsoilByid(Integer.parseInt(result[0].toString()));
                System.out.println(plantsoils);
                if (plantsoils.size() < 5)//记录数小于5那么执行插入
                {
                    mapperUtil.setSoilHum(i, new String(message.getPayload()), Integer.parseInt(result[0].toString()), sdfstr);
                    i++;
                } else {
                    mapperUtil.updatesoil(new String(message.getPayload()), Integer.parseInt(result[0].toString()), sdfstr, i);
                    i++;
                }
            }else if(topic.contains("190418202")){
                if (j > 10) j = 6;
                PushCallback pushCallback = new PushCallback();
                Object[] result = pushCallback.deleteSubString(topic, "SoilHum");
                //先调用根据deviceid取出所有土壤湿度值的接口函数
                System.out.println("deviceid" + result[0].toString());
                List<plantsoil> plantsoils1 = mapperUtil.getplantsoilByid(Integer.parseInt(result[0].toString()));
                System.out.println(plantsoils1);
                if (plantsoils1.size() < 5)//记录数小于5那么执行插入
                {
                    mapperUtil.setSoilHum(j, new String(message.getPayload()), Integer.parseInt(result[0].toString()), sdfstr);
                    j++;
                } else {
                    mapperUtil.updatesoil(new String(message.getPayload()), Integer.parseInt(result[0].toString()), sdfstr, j);
                    j++;
                }
            }
        }
    }
    //自己定义一个函数，用与删除str1中包含的str2字串，返回Object[]，该数组第一个元素为删除str2之后的最终字符串，第二个为个数
    public Object[] deleteSubString(String str1,String str2) {
        StringBuffer sb = new StringBuffer(str1);
        int delCount = 0;
        Object[] obj = new Object[2];

        while (true) {
            int index = sb.indexOf(str2);
            if(index == -1) {
                break;
            }
            sb.delete(index, index+str2.length());
            delCount++;

        }
        if(delCount!=0) {
            obj[0] = sb.toString();
            obj[1] = delCount;
        }else {
            //不存在返回-1
            obj[0] = -1;
            obj[1] = -1;
        }

        return obj;
    }



}

