package com.hzh.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DAHUANG
 * @date 2022/7/14
 * TTL队列配置文件
 */
@Configuration
public class TtlQueueConfig {

    //普通交换机名称
    public static final String X_EXCHANGE = "X";
    //死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列名称A
    public static final String QUEUE_A = "QA";
    //普通队列名称B
    public static final String QUEUE_B = "QB";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";
    //普通队列名称C
    public static final String QUEUE_C = "QC";

    //声明xExchange  别名
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    //声明xExchange  别名 死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明普通队列，ttl为10s
    @Bean("queueA")
    public Queue queueA(){
        Map<String,Object> arguments=new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-routing-key","YD");
        //设置过期时间ttl
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //声明普通队列，ttl为10s
    @Bean("queueB")
    public Queue queueB(){
        Map<String,Object> arguments=new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-routing-key","YD");
        //设置过期时间ttl
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    @Bean("queueC")
    public Queue queueC(){
        Map<String,Object> arguments=new HashMap<>();
        //设置死信交换机

        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-routing-key","YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //死信队列
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //绑定
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange){

        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //绑定
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    //绑定
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }






}
