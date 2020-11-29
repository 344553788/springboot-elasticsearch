package com.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by baishuai on 2018/12/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "order")
@Setting(settingPath = "elasticsearch_setting.json")
@Mapping(mappingPath = "elasticsearch_mapping.json")
public class Order implements Serializable {

    @Id
    private long id;
    private int storeId;//店铺ID
    private String storeName;//店铺名字
    private int categoryId;//类目ID
    private String categoryCode;//类目名称
    private String productCode;//货号
    private int quantity;//销售件数
    private double amount;//销售金额
    private Date payDate;

}
