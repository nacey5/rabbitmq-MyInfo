package com.hzh.rabbitmq.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author DAHUANG
 * @date 2022/7/14
 * 发送延迟消息
 */
@RestController
@RequestMapping("/ttl")
@Slf4j
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @GetMapping("sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10S 的队列: "+message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 ttl 为 40S 的队列: "+message);
    }

    @GetMapping("sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttlTime){
        log.info("当前时间：{},发送一条时长{}ms的信息给两个 TTL 队列:{}", new Date(),ttlTime, message);
        rabbitTemplate.convertAndSend("X","XC",message,msg -> {
            //发送消息的时候 延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";
    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message,@PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, message,
                correlationData ->{
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
        log.info(" 当 前 时 间 ： {}, 发送一条延迟 {} 毫秒的信息给队列 delayed.queue:{}", new
                Date(),delayTime, message);
    }




}
