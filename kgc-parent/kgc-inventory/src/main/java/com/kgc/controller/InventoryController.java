package com.kgc.controller;

import com.alibaba.fastjson.JSONObject;
import com.kgc.pojo.Order;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class InventoryController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    //3.接收对象
    @JmsListener(destination = "queue_队列模式4")
    public void getMsg3(Order order){
        System.out.println(order);
    }

    //2.接收json字符串
    @JmsListener(destination = "queue_队列模式3")
    public void getMsg2(String msg){
        System.out.println("接收到的消息："+msg);

        //json字符串解析
        HashMap hashMap = JSONObject.parseObject(msg, HashMap.class);
        Object code = hashMap.get("code");
        Object phone = hashMap.get("phone");
        System.out.println(code+"---"+phone);
    }

    //1.接收普通字符串
    @JmsListener(destination = "queue_队列模式2")
    public void getMsg1(String msg){
        System.out.println("接收到的消息："+msg);
    }

    @JmsListener(destination = "queue_队列模式")
    public void receiveQueue(String msg){
        System.out.println(msg);

        //取出消息队列中的商品ID》更新数据表库存Mysql

        //发布更新库存的消息到》消息队列服务器
        jmsMessagingTemplate.convertAndSend(new ActiveMQTopic("topic_发布订阅模式"), "发布订阅模式发布的消息:哈哈哈");
    }

}
