package com.hzh.rabbitmq.springbootrabbitmq.controller;

import com.hzh.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author DAHUANG
 * @date 2022/7/15
 * 开始发消息
 */
@Slf4j
@Controller
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    //发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        CorrelationData correlationData=new CorrelationData("1");
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message,
                correlationData
                );
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY+"2",
                message,
                correlationData
        );
        log.info("发送消息的内容为:{}",message);
    }
}
