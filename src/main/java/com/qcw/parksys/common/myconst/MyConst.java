package com.qcw.parksys.common.myconst;


/**
 * @author qcw
 */
public class MyConst {


    /**
     * 用户登录与注册异常枚举类
     */
    public enum MemberEnum{
        HAS_USER_EXCEPTION(20001,"用户名已存在"),
        HAS_PHONE_EXCEPTION(20002,"手机号码已存在"),
        USER_LOGIN_EXCEPTION(20003,"用户名或密码错误"),
        USER_LOGIN_CODE(20004,"验证码错误");

        private  int code;
        private  String msg;

        MemberEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /**
     * 车位状态
     */
    public enum SpaceStatus{

        AVALIABLE(0,"空闲"),
        BOOKED(1,"已被预约"),
        TOKEN(2,"已被占用"),
        SERVICING(3,"维护中");

        private  int code;
        private  String msg;

        SpaceStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /**
     * 订单状态
     */
    public enum OrderStatus{

        CREATED(40001,"新建"),
        BACKED(40002,"已退款"),
        DONE(40003,"已完成"),
        TOKEN(40004,"已取消");

        private  int code;
        private  String msg;

        OrderStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /**
     * 用户付款提示信息
     */
    public enum PaySatus{

        SUCCESS(50001,"付款成功"),
        NOMONEY(50002,"余额不足,请充值"),
        VALID(50003,"预约已过期,请重新预约"),
        eeee(50004,"未知错误");

        private  int code;
        private  String msg;

        PaySatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /**
     * 退款提示
     */
    public enum BackMoneyEnum{
        SUCCESS(30001,"退款成功"),
        VALID(30002,"退款失败,车位已到期"),
        USER_LOGIN_EXCEPTION(30003,"未知异常");

        private  int code;
        private  String msg;

        BackMoneyEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }


}
