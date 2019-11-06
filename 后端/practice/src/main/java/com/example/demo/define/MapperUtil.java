package com.example.demo.define;

import com.example.demo.controller.plantsoilMapper;
import com.example.demo.controller.roomMapper;
import com.example.demo.dao.plantsoil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Component
public class MapperUtil {
    @Autowired
  //  private paramsMapper paramsMapper;
    private roomMapper roomMapper;
    @Autowired
    private plantsoilMapper plantsoilMapper;
    public static MapperUtil mapperUtil;
    @PostConstruct
    public void init(){
        mapperUtil=this;
       // mapperUtil.paramsMapper=this.paramsMapper;
        mapperUtil.roomMapper=this.roomMapper;
      //  mapperUtil.plantsoilMapper=this.plantsoilMapper;
    }
   public void setTemperature(String temperature,Integer roomid){
       mapperUtil.roomMapper.setTemperature(temperature,roomid);
   }
   public  void setHumidity(String humidity,Integer roomid){
       mapperUtil.roomMapper.setHumidity(humidity,roomid);
   }
   public void setSoilHum(Integer soilid,String soil, Integer deviceid, String time){
       mapperUtil.plantsoilMapper.setplantsoil(soilid,soil, deviceid, time);
   }
   public List<plantsoil> getplantsoilByid(Integer deviceid){
       List<plantsoil> a=mapperUtil.plantsoilMapper.getplantsoilByid(deviceid);
       return a;
   }
   public void updatesoil(String soil, Integer deviceid, String time,Integer soilid){
       mapperUtil.plantsoilMapper.updateSoil(soil,deviceid,time,soilid);
   }
}
