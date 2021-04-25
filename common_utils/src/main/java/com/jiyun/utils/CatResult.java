package com.jiyun.utils;

import java.io.Serializable;
import java.util.List;

public class CatResult implements Serializable {
    private List<?> data;

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}