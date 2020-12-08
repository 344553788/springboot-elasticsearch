package com.elasticsearch.controller;

import com.elasticsearch.common.RestResponse;
import com.elasticsearch.config.HandlingAsyncTaskExecutor;
import com.elasticsearch.convert.OrderConvert;
import com.elasticsearch.dao.OrderDao;
import com.elasticsearch.dto.OrderDTO;
import com.elasticsearch.entity.ESUser;
import com.elasticsearch.entity.Order;
import com.elasticsearch.service.ElasticsearchService;
import com.elasticsearch.service.ThreadPoolService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private HandlingAsyncTaskExecutor handlingAsyncTaskExecutor;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private OrderConvert orderConvert;


    //增加
    @PostMapping("/add/{id}")
    public RestResponse<Void> add(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO) {
        Order order = orderConvert.from(orderDTO);
        order.setId(id);
        try {
            Future<Order> future = handlingAsyncTaskExecutor.submit(() -> orderDao.save(order));
            Order order1 = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
    public RestResponse<Order> query(@PathVariable("id") Long id) throws InterruptedException {
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
    @SneakyThrows
    @GetMapping("/pageQuery")
    public RestResponse<Page<Order>> page(@RequestParam("categoryCode") String categoryCode,
                                          @RequestParam("storeName") String storeName, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Future<Page<Order>> orderPageFuture = handlingAsyncTaskExecutor.submit(() -> elasticsearchService.orderPages(categoryCode, storeName, page, pageSize));
        return RestResponse.success(orderPageFuture.get());
    }

    //查询
    @RequestMapping("/page/{pageNum}/{pageSize}")
    public RestResponse<Page<ESUser>> esUserPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                 @RequestParam("desc") String desc, @RequestParam("tags") List<String> tags, @RequestParam("name") String name) {
        Page<ESUser> esUserPage = elasticsearchService.esUserPages(pageNum, pageSize, desc, tags, name);
        // 组装分页对象
        return RestResponse.success(esUserPage);
    }

    @Autowired
    private ThreadPoolService threadPoolService;

    @GetMapping("/test/order/{id}")
    public RestResponse<List<OrderDTO>> testOrder(@PathVariable("id") Long id) {
        List<Order> list = threadPoolService.query(id);
        List<OrderDTO> orderDTOS = orderConvert.to(list);
        return RestResponse.success(orderDTOS);
    }

}
