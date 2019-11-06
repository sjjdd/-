package com.example.demo.dao;

import javax.persistence.criteria.CriteriaBuilder;

public class notice {
    private String plantname;
    private Integer status;

    public String getPlantname() {
        return plantname;
    }

    public void setPlantname(String plantname) {
        this.plantname = plantname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
