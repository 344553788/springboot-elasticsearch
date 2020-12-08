package com.elasticsearch.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Maps;

import java.util.Map;

public enum ProductCodeEnums {

    BOOK(10001, "图书"),
    IT(10001001, "科技"),
    TV(10001002, "电视");

    private Integer projectCode;

    private String projectName;

    private static Map<Integer, ProductCodeEnums> maps = Maps.newHashMap();

    static {
        for (ProductCodeEnums productCodeEnums : ProductCodeEnums.values()) {
            maps.put(productCodeEnums.projectCode, productCodeEnums);
        }
    }


    ProductCodeEnums(Integer projectCode, String projectName) {
        this.projectCode = projectCode;
        this.projectName = projectName;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    ProductCodeEnums setCode(Integer code) {
        return maps.get(code);
    }

    @JsonValue
    public String getProjectName() {
        return projectName;
    }


}
