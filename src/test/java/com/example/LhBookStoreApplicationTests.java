package com.example;

import com.example.processor.OrderProcessor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class LhBookStoreApplicationTests {
    @Resource
    private OrderProcessor orderProcessor;

    @Test
    void contextLoads() {
    }

//    @Test
//    void cancel(){
//        //准备业务数据
//        Order order = new Order(OrderStatus.ORDERED);
//        orderProcessor.process(order, OrderEvents.CANCEL);
//    }
//
//    @Test
//    void pay(){
//        //准备业务数据
//        Order order = new Order(OrderStatus.ORDERED);
//        orderProcessor.process(order, OrderEvents.PAY);
//    }
//
//    @Test
//    void invalidOrder(){
//        //准备业务数据
//        Order order = new Order(OrderStatus.ORDERED);
//        Boolean process = orderProcessor.process(order, OrderEvents.CONFIRM);
//        log.info("状态机接收结果：{}", process);
//    }

}
