package com.qidian.mall.message.rocketmq.orderdemo;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Data
public class Order {

    private Long orderId;

    private String desc;

    public static List<Order> buildOrders(){
        List<Order> orderList = new ArrayList<>();
        // 1L 创建订单 付款 推送 完成
        // 2L 创建订单 付款 推送 完成
        Order order =new Order();
        order.setOrderId(1L);
        order.setDesc("创建订单");
        orderList.add(order);

        order =new Order();
        order.setOrderId(1L);
        order.setDesc("付款");
        orderList.add(order);

        order =new Order();
        order.setOrderId(1L);
        order.setDesc("推送");
        orderList.add(order);

        order =new Order();
        order.setOrderId(1L);
        order.setDesc("完成");
        orderList.add(order);

        order =new Order();
        order.setOrderId(2L);
        order.setDesc("创建订单2");
        orderList.add(order);

        order =new Order();
        order.setOrderId(2L);
        order.setDesc("付款2");
        orderList.add(order);

        order =new Order();
        order.setOrderId(2L);
        order.setDesc("推送2");
        orderList.add(order);

        order =new Order();
        order.setOrderId(2L);
        order.setDesc("完成2");
        orderList.add(order);

        return orderList;

    }
}
