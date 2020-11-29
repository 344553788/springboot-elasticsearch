package com.elasticsearch.dao;

import com.elasticsearch.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderDao extends ElasticsearchRepository<Order, Long> {

    Order queryOrderById(Long id);

    List<Order> queryOrderByCategoryCode(String categoryCode);

    Page<Order> queryOrderByCategoryCode(String categoryCode, Pageable pageable);

}
