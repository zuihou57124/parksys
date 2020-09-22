package com.qcw.parksys.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class MyQueueConfig {


    /**
     * @return
     * 订单延时队列(过期后投放到 "release" 队列)
     */
    @Bean
    public Queue orderDelayQueue(){
        HashMap<String, Object> arg = new HashMap<>();
        arg.put("x-dead-letter-exchange","order-event-exchange");
        arg.put("x-dead-letter-routing-key","order.release.event");
        //arg.put("x-message-ttl",60000*5);

        return new Queue("order.delay.queue",true,false,false,arg);
    }

    /**
     * @return
     * 订单延时队列(过期后投放到 "willvalid" 队列)
     */
    @Bean
    public Queue orderWillValidDelayQueue(){
        HashMap<String, Object> arg = new HashMap<>();
        arg.put("x-dead-letter-exchange","order-event-exchange");
        arg.put("x-dead-letter-routing-key","order.willvalid.event");
        //arg.put("x-message-ttl",60000*3);

        return new Queue("order.willvaliddelay.queue",true,false,false,arg);
    }

    /**
     * @return
     * 订单延时队列(过期后投放到 "valid" 队列)
     */
    @Bean
    public Queue orderValidDelayQueue(){
        HashMap<String, Object> arg = new HashMap<>();
        arg.put("x-dead-letter-exchange","order-event-exchange");
        arg.put("x-dead-letter-routing-key","order.valid.event");
        //arg.put("x-message-ttl",null);

        return new Queue("order.validdelay.queue",true,false,false,arg);
    }

    /**
     * @return
     * 订单关闭队列(用于关闭订单)
     */
    @Bean
    public Queue orderReleaseQueue(){

        return new Queue("order.release.queue",true,false,false);
    }

    /**
     * @return
     * 订单即将到期队列
     */
    @Bean
    public Queue orderWillValidQueue(){

        return new Queue("order.willvalid.queue",true,false,false);
    }

    /**
     * @return
     * 订单到期队列
     */
    @Bean
    public Queue orderValidQueue(){

        return new Queue("order.valid.queue",true,false,false);
    }

    /**
     * @return
     * 订单 事件 交换机
     */
    @Bean
    public Exchange orderEventExchange(){

        return new TopicExchange("order-event-exchange",true,false);
    }


    @Bean
    public Binding orderCreateBinding(){
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.event",null);
    }

    @Bean
    public Binding orderCreate2Binding(){
        return new Binding("order.willvaliddelay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.createwillvaliddelay.event",null);
    }

    @Bean
    public Binding orderCreate3Binding(){
        return new Binding("order.validdelay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.createvaliddelay.event",null);
    }

    /**
     * @return
     * 释放订单 绑定
     */
    @Bean
    public Binding orderReleaseBinding(){
        return new Binding("order.release.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.event",null);
    }

    /**
     * @return
     * 即将到期 绑定
     */
    @Bean
    public Binding orderWillValidBinding(){
        return new Binding("order.willvalid.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.willvalid.event",null);
    }

    /**
     * @return
     * 已经到期 绑定
     */
    @Bean
    public Binding orderValidBinding(){
        return new Binding("order.valid.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.valid.event",null);
    }

}
