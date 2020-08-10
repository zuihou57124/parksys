package com.qcw.parksys.common.myconst;

/**
 * @author qcw
 */
public class ProductConst {

    public enum ProductStatus{

        NEW(0,"新建"),UP(1,"上架"),DOWN(2,"下架");

        private int code;
        private String msg;

        ProductStatus(int code, String msg) {
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
