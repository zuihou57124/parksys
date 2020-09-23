package com.qcw.parksys.component;

import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.entity.SpaceEntity;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.service.OrderService;
import com.qcw.parksys.service.SpaceService;
import com.qcw.parksys.service.SysInfoService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听--消息队列
 */
@Component
public class OrderCloseListener {

    @Autowired
    OrderService orderService;

    @Autowired
    SysInfoService sysInfoService;

    @Autowired
    SpaceService spaceService;

    /**
     * @param order
     * @param message
     * @param channel
     * @throws IOException
     * 监听 关闭订单 队列
     */
    @RabbitListener(queues = "order.release.queue")
    public void releaseStockHdanler(OrderEntity order, Message message, Channel channel) throws IOException {
        System.out.println("收到关闭订单的消息...");

        try {
            orderService.closeOrder(order);
            System.out.println(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            System.out.println("关闭失败,重新入列");
        }

    }


    /**
     * @param order
     * @param message
     * @param channel
     * @throws IOException
     * 即将 到期的预约 或者 订单,封装成 消息实体
     */
    @RabbitListener(queues = "order.willvalid.queue")
    public void willValidAndToSysInfo(OrderEntity order, Message message, Channel channel) throws IOException {
        System.out.println("收到即将到期的消息...");

        try {
            SysInfoEntity sysInfo = orderService.willValidOrderToSysInfo(order);
            if(sysInfo!=null){
                sysInfoService.save(sysInfo);
            }
            System.out.println(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            System.out.println("处理失败,重新入列");
        }

    }

    /**
     * @param order
     * @param message
     * @param channel
     * @throws IOException
     * 已经 到期的预约 或者 订单,封装成 消息实体
     */
    @RabbitListener(queues = "order.valid.queue")
    public void validAndToSysInfo(OrderEntity order, Message message, Channel channel) throws IOException {
        System.out.println("收到已经到期的消息...");

        try {
            SysInfoEntity sysInfo = orderService.validOrderToSysInfo(order);
            if(sysInfo!=null){
                sysInfoService.save(sysInfo);
            }
            System.out.println(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            System.out.println("处理失败,重新入列");
        }

    }

    /**
     * @param space
     * @param message
     * @param channel
     * @throws IOException
     * 取消到期的优惠
     */
    @RabbitListener(queues = "space.canceldiscount.queue")
    public void cancelDiscount(SpaceEntity space, Message message, Channel channel) throws IOException {
        System.out.println("收到取消打折的消息...");

        try {
            spaceService.canceldiscount(space);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            System.out.println("处理失败,重新入列");
        }

    }

}
