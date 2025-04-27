package com.example.processor;

import com.example.entity.dto.Order;
import com.example.entity.enums.OrderEvents;
import com.example.entity.enums.OrderStatus;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderProcessor{
    @Resource
    private StateMachine<OrderStatus, OrderEvents> orderStateMachine;
    //持久化
    @Resource
    private StateMachinePersister<OrderStatus,OrderEvents, Order> persist;

    public Boolean process(Order order, OrderEvents events) {
        Message<OrderEvents> message = MessageBuilder.withPayload(events)
                .setHeader("order",order).build();
        //QxkQuestion:StateMachine0
        return sendEvent(message);
    }

    @SneakyThrows
    private boolean sendEvent(Message<OrderEvents> message) {
        Order order = (Order) message.getHeaders().get("order");

        persist.restore(orderStateMachine,order);
        boolean result = orderStateMachine.sendEvent(message);
        persist.persist(orderStateMachine,order);


        return result;
    }

}
