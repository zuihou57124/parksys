package com.qcw.parksys.common.myconst;

/**
 * @author qcw
 * 仓库系统的常量集合
 */
public class WareConst {

    /**
     * 采购单状态
     */
    public enum  PurchaseStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配")
        ,RECEIVED(2,"已领取"),DONE(3,"已完成"),
        ERROR(4,"出错");

        PurchaseStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 采购需求状态
     */
    public enum  PurchaseDetailStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配")
        ,BUYING(2,"正在采购"),DONE(3,"已完成"),
        ERROR(4,"采购失败");

        PurchaseDetailStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }


}
