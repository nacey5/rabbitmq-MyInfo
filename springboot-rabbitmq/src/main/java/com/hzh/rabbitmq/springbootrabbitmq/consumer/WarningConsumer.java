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

@Slf4j
@Component
public class WarningConsumer {

    //接收报警消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message){
        String msg=new String(message.getBody());
        log.error("报警发现不可路由消息:{}",msg);
    }
}
