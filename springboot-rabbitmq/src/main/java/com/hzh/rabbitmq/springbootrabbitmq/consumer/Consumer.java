package com.hzh.rabbitmq.springbootrabbitmq.consumer;

import com.hzh.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author DAHUANG
 * @date 2022/7/15
 */
@Component
@Slf4j
public class Consumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message){
        String msg = new String(message.getBody());
        log.info("接收到的队列confirm.queue消息:{}",msg);
    }
}
