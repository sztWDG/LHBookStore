package com.example.config;

import com.example.entity.dto.Order;
import com.example.entity.enums.OrderEvents;
import com.example.entity.enums.OrderStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvents> {

    /**
     * 配置状态
     * @param states
     * @throws Exception
     */
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.ORDERED) //订单状态赋初值,起点
                .states(EnumSet.allOf(OrderStatus.class));//列举订单状态所有内容，限定
    }

    /**
     * 配置状态转换事件关系
     * @param transitions
     * @throws Exception
     */
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStatus.ORDERED).target(OrderStatus.CANCELLED)
                .event(OrderEvents.CANCEL)
                .and()
                .withExternal().source(OrderStatus.ORDERED).target(OrderStatus.PAYED)
                .event(OrderEvents.PAY)
                .and()
                .withExternal().source(OrderStatus.PAYED).target(OrderStatus.DELIVERED)
                .event(OrderEvents.DELIVER)
                .and()
                .withExternal().source(OrderStatus.DELIVERED).target(OrderStatus.RECEIVED)
                .event(OrderEvents.CONFIRM)
                .and()
                .withExternal().source(OrderStatus.RECEIVED).target(OrderStatus.FINISHED)
                .event(OrderEvents.FINISH);
    }
    /**
     * 持久化配置
     * 在实际使用中，可以配合Redis等进行持久化操作
     * @return
     */
    @Bean
    public DefaultStateMachinePersister persister() {
        return new DefaultStateMachinePersister<>(
                new StateMachinePersist<Object, Object, Order>() {

            @Override
            public void write(StateMachineContext<Object, Object> context, Order order)
                    throws Exception {
                //此处没有进行持久化操作
            }

            @Override
            public StateMachineContext<Object, Object> read(Order order) throws Exception {
                //此处直接获取Order中的状态，其实并没有进行持久化读取操作
                return new DefaultStateMachineContext(order.getStatus(),null,null,null);
            }
        });
    }


}
