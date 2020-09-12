package com.qcw.parksys.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitmqConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //rabbitmq发送端确认
    @PostConstruct
    public void initRabbitTemplate(){

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

            }
        });
    }

/*  消费端默认自动确认，收到消息后，发送端会删除消息,
    如果宕机，可能会造成消息丢失
    手动模式下，没有确认，消息不会丢失，会重置为 ready 状态
    chanle.basicAck() 手动确认
*/



}
