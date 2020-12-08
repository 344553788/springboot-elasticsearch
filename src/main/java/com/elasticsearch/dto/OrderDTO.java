package com.elasticsearch.dto;

import com.elasticsearch.enums.ProductCodeEnums;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {

    private int storeId;//店铺ID
    private String storeName;//店铺名字
    private int categoryId;//类目ID
    private String categoryCode;//类目名称
    private ProductCodeEnums productCode;//货号
    private int quantity;//销售件数
    private double amount;//销售金额
    private Date payDate;
}
