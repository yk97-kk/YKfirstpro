package com.kgc.controller;

import com.alibaba.fastjson.JSONObject;
import com.kgc.pojo.Order;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Resource
    private ActiveMQQueue queue1;
    @Autowired
    private ActiveMQQueue queue2;

    //3.发送对象
    @GetMapping("sendMsg3")
    public Object sendMsg3() {
        Order order = new Order(10002,"欧莱雅爽肤水",465.45);
        order.merchant = "asd";

        jmsMessagingTemplate.convertAndSend(queue1, order);

        return "发送的普通字符串";
    }

    //2.发json字符串
    @GetMapping("sendMsg2")
    public Object sendMsg2() {
        Map<String,Object> map = new HashMap<>();
        map.put("phone","1898787231");
        map.put("code","6548");

        String jsonString = JSONObject.toJSONString(map);

        jmsMessagingTemplate.convertAndSend(queue2, jsonString);

        return "发送的普通字符串";
    }

    //1.发普通字符串
    @GetMapping("sendMsg1")
    public Object sendMsg1() {
        jmsMessagingTemplate.convertAndSend(new ActiveMQQueue("queue_队列模式2"), "哈哈哈哈");

        return "发送的普通字符串";
    }

    @GetMapping("addOrder")
    public Object addOrder(){
        //将订单添加到数据库Mysql...

        //将商品ID发送到消息队列中（库存更新）
        int productId = 10001;

        jmsMessagingTemplate.convertAndSend(new ActiveMQQueue("queue_队列模式"), "队列模式发送的消息:"+productId);
        return "queue模式-发送消息成功";
    }

    @JmsListener(destination = "topic_发布订阅模式",containerFactory = "topicListenerContainer")
    public void getAdvice(String msg){
        //获取库存更新的通知
        System.out.println("接收到的消息："+msg);

        //修改订单状态为已发货》Mysql
    }
}
