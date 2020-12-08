package com.elasticsearch.convert;

import com.elasticsearch.dto.OrderDTO;
import com.elasticsearch.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderConvert extends BasicObjectConvert<Order, OrderDTO> {

//    @Mappings({
//            @Mapping(source = "productCode", target = "productCode.name")
//    })
    OrderDTO to(Order source);
}
