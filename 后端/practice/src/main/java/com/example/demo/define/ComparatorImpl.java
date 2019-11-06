package com.example.demo.define;

import com.example.demo.dao.plantsoil;

import java.util.Comparator;
//根据时间大小对数据进行排序
public class ComparatorImpl implements Comparator<plantsoil> {
    public int compare(plantsoil s1,plantsoil s2){
        String h1=s1.getTime();
        String h2=s2.getTime();
        if(h1.compareTo(h2)>0) return 1;
        else if(h1.compareTo(h2)<0) return -1;
        else return 0;
    }
}
