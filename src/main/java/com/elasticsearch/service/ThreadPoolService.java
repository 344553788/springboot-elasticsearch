package com.elasticsearch.service;

import com.elasticsearch.common.RestResponse;
import com.elasticsearch.entity.Order;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class ThreadPoolService {


    @Autowired
    private RestTemplate restTemplate;



    @SneakyThrows
    public List<Order> query(Long id) {
        List<FutureTask<Order>> futures = Lists.newArrayList();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10, 10L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(20),
                new ThreadFactoryBuilder().setNameFormat("TEST").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 3; i++) {
            FutureTask<Order> futureTask = new FutureTask(new ApiClass(restTemplate, id));
            executor.submit(futureTask);
            futures.add(futureTask);
        }
        executor.shutdown();
        if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {

        }
        List<Order> orders = Lists.newArrayList();
        futures.stream().forEach(future -> {
            try {
                Order order = future.get();
                orders.add(order);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return orders;
    }


    class ApiClass implements Callable<Order> {
        private RestTemplate restTemplate;
        private Long id;

        public ApiClass(RestTemplate restTemplate, Long id) {
            this.restTemplate = restTemplate;
            this.id = id;
        }

        @Override
        public Order call() {
            try {
                ParameterizedTypeReference<RestResponse<Order>> parameterizedTypeReference = new ParameterizedTypeReference<RestResponse<Order>>() {
                };
                ResponseEntity<RestResponse<Order>> restResponse = restTemplate.exchange("http://localhost:8771/order/query/" + id, HttpMethod.GET, HttpEntity.EMPTY, parameterizedTypeReference);
                return restResponse.getBody().getData();
            } catch (Exception e) {
                log.info("线程失败", e);
                throw new RuntimeException(e);
            }
        }
    }

}
