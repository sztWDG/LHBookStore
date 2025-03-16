package com.example.task;

import com.example.entity.dto.Order;
import com.example.entity.enums.OrderStatus;
import com.example.repository.order.OrderRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class OrderTask {
    @Resource
    private OrderRepository orderRepository;

    private static final int BATCH_SIZE = 5;

    //每天凌晨4点执行
    @Scheduled(cron = "0 0 4 * * ?")
    public void finishOrder() {
        log.info("定时任务:修改订单状态为完成");
        List<Order> orderList = orderRepository.list()
                .stream()
                .filter(it ->
                        it.getStatus() == OrderStatus.DELIVERED ||
                                it.getStatus() == OrderStatus.RECEIVED
                )
                .toList();

        if (orderList.isEmpty()) {
            log.info("定时任务:无数据符合条件");
            return;
        }


        List<Order> orders = new ArrayList<>();
        orderList.forEach(order -> {
            if (order.getStatus() == OrderStatus.DELIVERED) {
                Order modifiedOrder = isOverTime(order, order.getDeliveryTime(), 15);
                if (modifiedOrder != null) {
                    orders.add(modifiedOrder);
                }
            } else if (order.getStatus() == OrderStatus.RECEIVED) {
                Order modifiedOrder= isOverTime(order, order.getReceivedTime(), 7);
                if (modifiedOrder != null) {
                    orders.add(modifiedOrder);
                }
            }
        });
        if (orders.isEmpty()) {
            log.info("定时任务:没有数据被修改");
            return;
        }
        //五条五条查
        orderRepository.saveOrUpdateBatch(orders, BATCH_SIZE);
        log.info("定时任务:执行完毕");
    }


    private Order isOverTime(Order order, LocalDateTime time, int days) {
        if (time != null && java.time.LocalDateTime.now().isAfter(time.plusDays(days))) {
            order.setStatus(OrderStatus.FINISHED);
            order.setFinishTime(LocalDateTime.now());
            return order;
        }
        return null;
    }
}
