package com.example.listener;

import com.example.entity.dto.Order;
import com.example.entity.enums.OrderEvents;
import com.example.entity.enums.OrderStatus;
import com.example.repository.order.OrderRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("orderStateListener")
@Slf4j
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {
    @Resource
    private OrderRepository orderRepository;


    @OnTransition(source = "ORDERED",target = "CANCELLED")
    public boolean cancel(Message<OrderEvents> message) {
        String orderId = modifyStatus(message, OrderStatus.CANCELLED,OrderEvents.CANCEL);

        log.info("订单取消:{}", orderId);
        return true;
    }

    @OnTransition(source = "ORDERED",target = "PAYED")
    public boolean pay(Message<OrderEvents> message) {
        String orderId = modifyStatus(message, OrderStatus.PAYED,OrderEvents.PAY);

        log.info("已支付:{}", orderId);
        return true;
    }

    @OnTransition(source = "PAYED",target = "DELIVERED")
    public boolean deliver(Message<OrderEvents> message) {
        String orderId = modifyStatus(message, OrderStatus.DELIVERED,OrderEvents.DELIVER);

        log.info("已发货:{}", orderId);
        return true;
    }

    @OnTransition(source = "DELIVERED",target = "RECEIVED")
    public boolean received(Message<OrderEvents> message) {
        String orderId = modifyStatus(message, OrderStatus.RECEIVED,OrderEvents.CONFIRM);
        log.info("已收货:{}", orderId);
        return true;
    }
    
    @OnTransition(source = "RECEIVED", target = "FINISHED")
    public boolean finished(Message<OrderEvents> message) {
        
        String orderId = modifyStatus(message, OrderStatus.FINISHED,OrderEvents.FINISH);
        
        log.info("已完成:{}", orderId);
        return true;
    }
    
    private String modifyStatus(Message<OrderEvents> message, OrderStatus status,OrderEvents events) {
        Order order = (Order) message.getHeaders().get("order");
        if (order == null) {
            throw new RuntimeException();
        }
        order.setStatus(status);

        switch (events){
            case CANCEL, FINISH -> order.setFinishTime(LocalDateTime.now());
            case PAY -> order.setPayTime(LocalDateTime.now());
            case DELIVER -> order.setDeliveryTime(LocalDateTime.now());
            case CONFIRM -> order.setReceivedTime(LocalDateTime.now());
        }

        //QxkQuestion:不能使用save,要update
        orderRepository.updateById(order);
        return order.getOrderId();
    }
}
