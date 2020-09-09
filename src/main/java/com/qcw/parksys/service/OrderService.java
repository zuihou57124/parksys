package com.qcw.parksys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.vo.BackMoneyVo;
import com.qcw.parksys.vo.SysInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getUserBooks(Map<String, Object> params);

    Integer pay(Map<String, Object> params);

    /**
     * @param params
     * @return
     * 即将失效
     */
    List<SysInfoEntity> getWillValidOrdersByUserIdAndToSysInfo(Map<String, Object> params);

    PageUtils getUserOrders(Map<String, Object> params);

    /**
     * @param params
     * @return
     * 已经失效
     */
    List<SysInfoEntity> getValidOrdersByUserIdAndToSysInfo(Map<String, Object> params);

    /**
     * @param backMoneyVo
     * @return
     * 退款
     */
    Integer backMoney(BackMoneyVo backMoneyVo);

    String hasQrCode(Integer orderId);
}

