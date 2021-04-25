package com.jiyun.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class CatNode implements Serializable {
    @JsonProperty("n")
    private String name;
    @JsonProperty("i")
    private List<?> item;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<?> getItem() {
        return item;
    }

    public void setItem(List<?> item) {
        this.item = item;
    }
}