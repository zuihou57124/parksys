package com.qcw.parksys.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.config.AliPayConfig;
import com.qcw.parksys.entity.UserEntity;
import com.qcw.parksys.vo.BackMoneyVo;
import com.qcw.parksys.vo.PayOrderVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.service.OrderService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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

    @Autowired
    private StringRedisTemplate redisTemplate;

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
     * 消息幂等性
     */
    @PostMapping("/getSubmitToken")
    public R getSubmitToken(@RequestBody Map<String,Integer> params) {

        Integer userId = params.get("userId");

        String submitToken = UUID.randomUUID().toString().substring(0, 5);
        redisTemplate.opsForValue().set("user:"+userId.toString()+"submitToken:",submitToken);

        return R.ok().put("submitToken",submitToken);
    }

    @RequestMapping("/test/paySuccess")
    public R paySuccess(HttpServletRequest request){

        R put = R.ok().put("msg", "支付成功");
        put.put("data",request.getParameterMap());
        return put;

    }

    /**
     * @return
     * 测试生成付款码
     */
    @PostMapping("/test/qrCode")
    @Transactional
    public R testPay(@RequestBody PayOrderVo payOrderVo){

        // 1. 设置参数（全局只需设置一次）
        Config config = AliPayConfig.getConfig();
        Factory.setOptions(config);
        AlipayTradePrecreateResponse response;

        String fileName = "";

        try {
            // 2. 以创建当面付收款二维码
            //支付宝订单号
            String outTradeNo = UUID.randomUUID().toString().substring(0,10);
            //设置用户订单的支付宝订单号
            OrderEntity orderEntity = orderService.getById(payOrderVo.getOrderId());
            orderEntity.setOutTradeNo(outTradeNo);
            orderService.updateById(orderEntity);

            response = Factory.Payment.FaceToFace()
                    .preCreate(payOrderVo.getSubuject(), outTradeNo,payOrderVo.getTotal().toString());

            //pay = Factory.Payment.Page().pay("Apple iPhone11 128G", "5799.00", "5799.00", config.notifyUrl);
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                System.out.println("调用成功");
            } else {
                System.err.println("调用失败，原因：" + response.msg + "，" + response.subMsg);
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        String qrCodeUrl = response.qrCode;

        try {
            // 生成二维码配置
            Map<EncodeHintType, Object> hint = new HashMap<>();
            // 设置纠错等级
            hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            // 设置字符集
            hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 设置二维码图像
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCodeUrl, BarcodeFormat.QR_CODE, 300, 300, hint);

            // 保存二维码到本地
            fileName = UUID.randomUUID().toString().substring(0,15);
            String filePath = "D:\\environment_config\\nginx\\root\\qr_code\\" + fileName + ".png";
            File file = new File(filePath);
            OutputStream outputStream1 = new FileOutputStream(file);

            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream1);

            outputStream1.flush();
            outputStream1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.ok().put("fileName",fileName);
    }


    /**
     * @return 查询支付宝订单，如果交易成功，关闭订单
     */
    @PostMapping("/test/queryAndClose")
    public R query(@RequestBody Map<String, Object> params) throws Exception {

        // 1. 设置参数（全局只需设置一次）
        Config config = AliPayConfig.getConfig();
        Factory.setOptions(config);
        AlipayTradeQueryResponse response;

        //查询出订单的支付宝订单号,调用支付宝接口查询订单是否支付
        Integer orderId = (Integer) params.get("orderId");
        OrderEntity orderEntity = orderService.getById(orderId);
        response = Factory.Payment.Common().query(orderEntity.getOutTradeNo());
        //支付宝交易成功,订单状态改变
        R payResult = R.error().put("msg","您还未支付");
        if("10000".equals(response.code)){
            payResult = this.pay(params);
        }
        return payResult;
    }


    /**
     * 付款
     */
    @PostMapping("/pay")
    public R pay(@RequestBody Map<String, Object> params) {

        Integer userId = (Integer) params.get("userId");

        //从redis获取的token
        String submitTokenFromRedis = redisTemplate.opsForValue().get("user:" + userId.toString() + "submitToken:");
        if(StringUtils.isEmpty(submitTokenFromRedis)){
            return R.error().put("msg","订单已失效,请重新下单");
        }
        //用户下单时发送的token
        String submitTokenFromUser = (String) params.get("submitToken");

        if(!submitTokenFromRedis.equals(submitTokenFromUser)){
            return R.error().put("msg","订单已失效,请重新下单");
        }

        Integer info = orderService.pay(params);

        if (info.equals(MyConst.PaySatus.VALID.getCode())) {
            return R.error(info, MyConst.PaySatus.VALID.getMsg());
        }
        if (info.equals(MyConst.PaySatus.NOMONEY.getCode())) {
            return R.error(info, MyConst.PaySatus.NOMONEY.getMsg());
        }

        //订单成功关闭后，删除token
        redisTemplate.delete("user:" + userId.toString() + "submitToken:");

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
