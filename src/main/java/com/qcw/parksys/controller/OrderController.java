package com.qcw.parksys.controller;

import java.util.Arrays;
import java.util.Map;

import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.vo.BackMoneyVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.service.OrderService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.R;


/**
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
@RestController
@RequestMapping("parksys/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("parksys:order:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询用户预约情况
     */
    @PostMapping("getUserBooks")
    public R getUserBooks(@RequestBody Map<String, Object> params) {
        PageUtils page = orderService.getUserBooks(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询用户订单
     */
    @PostMapping("getUserOrders")
    public R getUserOrders(@RequestBody Map<String, Object> params) {
        PageUtils page = orderService.getUserOrders(params);

        return R.ok().put("page", page);
    }

    /**
     * 退款
     */
    @PostMapping("/backMoney")
    public R backMoney(@RequestBody BackMoneyVo backMoneyVo) {
        Integer code = orderService.backMoney(backMoneyVo);

        return R.ok();
    }

    /**
     * 付款
     */
    @PostMapping("/pay")
    public R pay(@RequestBody Map<String, Object> params) {
        Integer info = orderService.pay(params);

        if (info.equals(MyConst.PaySatus.VALID.getCode())) {
            return R.error(info, MyConst.PaySatus.VALID.getMsg());
        }
        if (info.equals(MyConst.PaySatus.NOMONEY.getCode())) {
            return R.error(info, MyConst.PaySatus.NOMONEY.getMsg());
        }

        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("parksys:order:info")
    public R info(@PathVariable("id") Integer id) {
        OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("parksys:order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("parksys:order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("parksys:order:delete")
    public R delete(@RequestBody Integer[] ids) {
        orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
