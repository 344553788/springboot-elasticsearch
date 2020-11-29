package com.elasticsearch.controller;

import com.elasticsearch.common.RestResponse;
import com.elasticsearch.dao.OrderDao;
import com.elasticsearch.entity.ESUser;
import com.elasticsearch.entity.Order;
import com.elasticsearch.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ElasticsearchService elasticsearchService;

    //增加
    @PostMapping("/add/{id}")
    public RestResponse<Void> add(@PathVariable("id") Long id, @RequestBody Order order) {
        order.setId(id);
        orderDao.save(order);
        return RestResponse.success();
    }

    //删除
    @RequestMapping("/delete/{id}")
    public RestResponse<Void> delete(@PathVariable("id") Long id) {
        Order order = new Order();
        order.setId(id);
        orderDao.delete(order);
        return RestResponse.success();
    }

    //局部更新
    @RequestMapping("/update/{id}")
    public RestResponse<Void> update(@PathVariable("id") Long id) {
        Order order = orderDao.queryOrderById(id);
        order.setPayDate(new Date());
        orderDao.save(order);
        return RestResponse.success();
    }

    //查询
    @RequestMapping("/query/{id}")
    public RestResponse<Order> query(@PathVariable("id") Long id) {
        Order order = orderDao.queryOrderById(id);
        return RestResponse.success(order);
    }

    //查询
    @RequestMapping("/query")
    public RestResponse<Order> query(@RequestParam("categoryCode") String categoryCode) {
        List<Order> orders = orderDao.queryOrderByCategoryCode(categoryCode);
        return RestResponse.success(orders);
    }

    //查询
    @GetMapping("/pageQuery")
    public RestResponse<Page<Order>> page(@RequestParam("categoryCode") String categoryCode,
                                          @RequestParam("storeName") String storeName, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Page<Order> orderPage = elasticsearchService.orderPages(categoryCode, storeName, page, pageSize);
        // 组装分页对象
        return RestResponse.success(orderPage);
    }

    //查询
    @RequestMapping("/page/{pageNum}/{pageSize}")
    public RestResponse<Page<ESUser>> esUserPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                @RequestParam("desc") String desc, @RequestParam("tags") List<String> tags, @RequestParam("name") String name) throws IOException {
        Page<ESUser> esUserPage = elasticsearchService.esUserPages(pageNum, pageSize, desc, tags, name);
        // 组装分页对象
        return RestResponse.success(esUserPage);
    }

}
