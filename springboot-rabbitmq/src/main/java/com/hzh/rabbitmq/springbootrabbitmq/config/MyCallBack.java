package com.hzh.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author DAHUANG
 * @date 2022/7/15
 */
@Slf4j
@Configuration
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{



    //注入，将当前类注入到ConfirmCallback接口中，否则无法正确的访问到
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @PostConstruct 这个注解支持所有依赖注入的类和方法，此注解注解的方法是在初始化的时候进行调用
     */
    @PostConstruct
    public void init(){
        //注入
        rabbitTemplate.setConfirmCallback(this);
        //注入
        rabbitTemplate.setReturnsCallback(this);
    }


    /**
     * 交换机确认回调方法
     * 1，发消息，交换机接收到了 回调
     * @param correlationData 保存回调消息的id及其相关信息
     * @param b 交换机收到消息 true，没收到则为false
     * @param s 成功的话cause null，失败的话，保存失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id=correlationData!=null?correlationData.getId():"";
        if (b){
            log.info("交换机已经收到ID为:{}的消息",id);
        }else {
            log.info("交换机还未收到ID为:{}的消息，原因:{}",id,s);
        }
    }

    /**
     * 可以在当消息传递过程中不可到达目的地时将消息返回给生产者
     * 只有不可到达目的地的时候，才进行回退
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息:{}被交换机:{}退回，退回的原因：{},路由key:{}",
               new String(returned.getMessage().getBody()),
                returned.getExchange(),
                returned.getReplyText(),
                returned.getRoutingKey());
    }
}
