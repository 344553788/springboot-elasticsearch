package com.elasticsearch.config;

import com.elasticsearch.dao.OrderDao;
import com.elasticsearch.entity.Order;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Configuration
public class CommandLineConfig implements CommandLineRunner {

    private Log log = LogFactory.getLog(CommandLineConfig.class);

    @Autowired
    private OrderDao orderDao;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        List<Order> orderList = new ArrayList<>();
        orderList.add(Order.builder().id(random.nextInt()).storeId(random.nextInt()).storeName("美国宪法")
                .categoryCode("美国宪法是美国人的总纲领，保护的是美国公民，针对的是违法人员").quantity(random.nextInt())
                .amount(random.nextDouble()).payDate(new Date()).build());

        orderList.add(Order.builder().id(random.nextInt()).storeId(random.nextInt()).storeName("中国宪法")
                .categoryCode("中国宪法是中国人的法律总纲领，保护的是中国公民，一切法律都不得违背宪法")
                .quantity(random.nextInt()).amount(random.nextDouble()).payDate(new Date()).build());

        orderList.add(Order.builder().id(random.nextInt()).storeId(random.nextInt()).storeName("英国宪法")
                .categoryCode("英国宪法是英国人的法律总纲领，保护的是英国公民，一切法律都不得违背宪法")
                .quantity(random.nextInt()).amount(random.nextDouble()).payDate(new Date()).build());
        orderDao.saveAll(orderList);
        log.info("插入成功");
    }
}
